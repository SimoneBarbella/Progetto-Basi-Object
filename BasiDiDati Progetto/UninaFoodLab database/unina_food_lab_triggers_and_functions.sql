
-- =============================================================
-- TRIGGER
-- =============================================================

GRANT ALL ON SCHEMA uninafoodlab TO public;
SET search_path TO uninafoodlab, public;

-- Creazione del trigger: Aggiorna numero partecipanti Corso
CREATE OR REPLACE FUNCTION aggiorna_partecipanti() RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'INSERT') THEN
        UPDATE Corso SET num_partecipanti = num_partecipanti + 1 WHERE id_corso = NEW.id_corso;
        RETURN NEW;
    ELSIF (TG_OP = 'DELETE') THEN
        UPDATE Corso SET num_partecipanti = num_partecipanti - 1 WHERE id_corso = OLD.id_corso;
        RETURN OLD;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_partecipanti AFTER INSERT OR DELETE ON Iscrizione
FOR EACH ROW EXECUTE FUNCTION aggiorna_partecipanti();

-- Creazione del trigger: Aggiorna numero aderenti Sessione
CREATE OR REPLACE FUNCTION aggiorna_aderenti() RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'INSERT') THEN
        UPDATE Sessione SET num_aderenti = num_aderenti + 1 WHERE id_sessione = NEW.id_sessione;
        RETURN NEW;
    ELSIF (TG_OP = 'DELETE') THEN
        UPDATE Sessione SET num_aderenti = num_aderenti - 1 WHERE id_sessione = OLD.id_sessione;
        RETURN OLD;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_aderenti AFTER INSERT OR DELETE ON Adesione
FOR EACH ROW EXECUTE FUNCTION aggiorna_aderenti();


-- =============================================================
-- FUNZIONI E TRIGGER DI CONTROLLO AVANZATI
-- =============================================================

-- Impedisce di creare/spostare una Sessione su un corso che ha già raggiunto il limite.
CREATE OR REPLACE FUNCTION check_max_sessioni_per_corso() RETURNS TRIGGER AS $$
DECLARE
    limite INTEGER;
    conteggio INTEGER;
BEGIN
    SELECT num_sessioni INTO limite
    FROM Corso
    WHERE id_corso = NEW.id_corso;

    IF limite IS NULL THEN
        RAISE EXCEPTION 'Corso % inesistente', NEW.id_corso;
    END IF;

    SELECT COUNT(*) INTO conteggio
    FROM Sessione
    WHERE id_corso = NEW.id_corso;

    IF conteggio > limite THEN
        RAISE EXCEPTION 'Limite sessioni superato per corso % (sessioni=% / limite=%).', NEW.id_corso, conteggio, limite;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_max_sessioni_per_corso
AFTER INSERT OR UPDATE OF id_corso ON Sessione
FOR EACH ROW EXECUTE FUNCTION check_max_sessioni_per_corso();

-- Impedisce di ridurre num_sessioni sotto il numero di Sessioni già istanziate.
CREATE OR REPLACE FUNCTION check_num_sessioni_corso_update() RETURNS TRIGGER AS $$
DECLARE
    conteggio INTEGER;
BEGIN
    SELECT COUNT(*) INTO conteggio
    FROM Sessione
    WHERE id_corso = NEW.id_corso;

    IF NEW.num_sessioni < conteggio THEN
        RAISE EXCEPTION 'num_sessioni non può essere < delle sessioni già create (corso=%; sessioni=%; nuovo_limite=%).',
            NEW.id_corso, conteggio, NEW.num_sessioni;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_num_sessioni_corso_update
BEFORE UPDATE OF num_sessioni ON Corso
FOR EACH ROW EXECUTE FUNCTION check_num_sessioni_corso_update();

-- Associazione: Iscrizione ammessa solo per studente/chefStudente
CREATE OR REPLACE FUNCTION check_iscrizione() RETURNS TRIGGER AS $$
DECLARE
    t tipo_utente_enum;
BEGIN
    SELECT tipo_utente INTO t FROM Utente WHERE email = NEW.email_utente;
    IF t IS NULL THEN
        RAISE EXCEPTION 'Utente % inesistente', NEW.email_utente;
    END IF;
    IF t = 'chef' THEN
        RAISE EXCEPTION 'Solo studente/chefStudente possono iscriversi ai corsi (%).', NEW.email_utente;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_iscrizione
BEFORE INSERT OR UPDATE ON Iscrizione
FOR EACH ROW EXECUTE FUNCTION check_iscrizione();

-- Associazione: Adesione ammessa solo per studente/chefStudente e solo per sessioni di tipo presenza
CREATE OR REPLACE FUNCTION check_adesione() RETURNS TRIGGER AS $$
DECLARE
    t tipo_utente_enum;
    ts tipo_sessione_enum;
    corso_sessione INTEGER;
BEGIN
    SELECT tipo_utente INTO t FROM Utente WHERE email = NEW.email_utente;
    IF t IS NULL THEN
        RAISE EXCEPTION 'Utente % inesistente', NEW.email_utente;
    END IF;
    IF t = 'chef' THEN
        RAISE EXCEPTION 'Solo studente/chefStudente possono aderire alle sessioni (%).', NEW.email_utente;
    END IF;

    SELECT tipo_sessione, id_corso INTO ts, corso_sessione
    FROM Sessione
    WHERE id_sessione = NEW.id_sessione;
    IF ts IS NULL THEN
        RAISE EXCEPTION 'Sessione % inesistente', NEW.id_sessione;
    END IF;
    IF ts <> 'presenza' THEN
        RAISE EXCEPTION 'Adesione ammessa solo per sessioni in presenza (sessione=%).', NEW.id_sessione;
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM Iscrizione i
        WHERE i.email_utente = NEW.email_utente
          AND i.id_corso = corso_sessione
    ) THEN
        RAISE EXCEPTION 'Adesione non consentita: utente % non iscritto al corso % (sessione=%).', 
        NEW.email_utente, corso_sessione, NEW.id_sessione;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_adesione
BEFORE INSERT OR UPDATE ON Adesione
FOR EACH ROW EXECUTE FUNCTION check_adesione();

-- Associazione: Prepara ammessa solo per sessioni di tipo presenza
CREATE OR REPLACE FUNCTION check_prepara() RETURNS TRIGGER AS $$
DECLARE
    ts tipo_sessione_enum;
BEGIN
    SELECT tipo_sessione INTO ts FROM Sessione WHERE id_sessione = NEW.id_sessione;
    IF ts IS NULL THEN
        RAISE EXCEPTION 'Sessione % inesistente', NEW.id_sessione;
    END IF;
    IF ts <> 'presenza' THEN
        RAISE EXCEPTION 'Prepara ammessa solo per sessioni in presenza (sessione=%).', NEW.id_sessione;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_prepara
BEFORE INSERT OR UPDATE ON Prepara
FOR EACH ROW EXECUTE FUNCTION check_prepara();

-- Multivalore: Specializzazione ammessa solo per chef/chefStudente
CREATE OR REPLACE FUNCTION check_specializzazione() RETURNS TRIGGER AS $$
DECLARE
    t tipo_utente_enum;
BEGIN
    SELECT tipo_utente INTO t FROM Utente WHERE email = NEW.email_chef;
    IF t IS NULL THEN
        RAISE EXCEPTION 'Utente % inesistente', NEW.email_chef;
    END IF;
    IF t = 'studente' THEN
        RAISE EXCEPTION 'Specializzazione non ammessa per utente di tipo studente (%).', NEW.email_chef;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_specializzazione
BEFORE INSERT OR UPDATE ON Specializzazione_Chef
FOR EACH ROW EXECUTE FUNCTION check_specializzazione();

-- Associazione: Gestisce ammessa solo per chef/chefStudente
CREATE OR REPLACE FUNCTION check_gestisce() RETURNS TRIGGER AS $$
DECLARE
    t tipo_utente_enum;
BEGIN
    SELECT tipo_utente INTO t FROM Utente WHERE email = NEW.email_chef;
    IF t IS NULL THEN
        RAISE EXCEPTION 'Utente % inesistente', NEW.email_chef;
    END IF;
    IF t = 'studente' THEN
        RAISE EXCEPTION 'Solo chef/chefStudente possono gestire corsi (%).', NEW.email_chef;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_gestisce
BEFORE INSERT OR UPDATE ON Gestisce
FOR EACH ROW EXECUTE FUNCTION check_gestisce();