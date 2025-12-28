-- =============================================================
-- PROGETTO: UninaFoodLab
-- =============================================================

-- PULIZIA AMBIENTE
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- Creazione dei vari enum
CREATE TYPE tipo_utente_enum AS ENUM ('chef', 'studente', 'chefStudente');
CREATE TYPE tipo_sessione_enum AS ENUM ('presenza', 'online');

-- Creazione della tabella INGREDIENTE
CREATE TABLE Ingrediente (
    nome VARCHAR(50) PRIMARY KEY, 
    unita_di_misura VARCHAR(20) NOT NULL,
    CONSTRAINT check_nome_ingrediente CHECK (nome ~ '^[A-Za-zÀ-ÿ ''-]+$'),
    CONSTRAINT check_unita_di_misura CHECK (unita_di_misura ~ '^[A-Za-z]+$')
);

-- Creazione della tabella UTENTE
CREATE TABLE Utente (
    email VARCHAR(255) PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    cognome VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    matricola VARCHAR(20), -- Obbligatoria solo se studente
    tipo_utente tipo_utente_enum NOT NULL,
    
    -- Vincoli di formato
    CONSTRAINT check_email CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    CONSTRAINT check_nome_utente CHECK (nome ~ '^[A-Za-zÀ-ÿ ''-]+$'),
    CONSTRAINT check_cognome CHECK (cognome ~ '^[A-Za-zÀ-ÿ ''-]+$'),
    -- Password complessa (min 8 char, Maiusc, Minusc, Num, Special)
    CONSTRAINT check_password CHECK (
        LENGTH(password) >= 8 AND password ~ '[A-Z]' AND password ~ '[a-z]' 
        AND password ~ '[0-9]' AND password ~ '[^a-zA-Z0-9]'
    ),
    -- Coerenza Ruolo
    CONSTRAINT check_ruolo_dati CHECK (
        (tipo_utente = 'studente' AND matricola IS NOT NULL) OR
        (tipo_utente = 'chef') OR 
        (tipo_utente = 'chefStudente' AND matricola IS NOT NULL)
    )
);

-- Creazione della tabella CORSO
CREATE TABLE Corso (
    id_corso INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, 
    data_inizio DATE NOT NULL,
    nome VARCHAR(50) NOT NULL,
    frequenza VARCHAR(50) NOT NULL,
    num_partecipanti INTEGER DEFAULT 0, -- Aggiornato da Trigger
    num_sessioni INTEGER NOT NULL CHECK (num_sessioni > 0),
    CONSTRAINT check_nome_corso CHECK (nome ~ '^[A-Za-zÀ-ÿ ''-]+$')
);

-- Creazione della tabella SESSIONE
CREATE TABLE Sessione (
    id_sessione INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ora_inizio TIMESTAMP NOT NULL, 
    num_aderenti INTEGER DEFAULT 0 CHECK (num_aderenti >= 0), -- Aggiornato da Trigger
    quantita DECIMAL(10,2),
    teoria VARCHAR(255),
    tipo_sessione tipo_sessione_enum NOT NULL,
    id_corso INTEGER NOT NULL,
    
    FOREIGN KEY (id_corso) REFERENCES Corso(id_corso) ON DELETE CASCADE,
    
    -- Vincoli:
    -- - Se ONLINE: teoria non nulla, alfanumerica, max 255; quantita nulla
    -- - Se PRESENZA: quantita > 0; teoria nulla
    CONSTRAINT check_logica_sessione CHECK (
        (tipo_sessione = 'online'
            AND teoria IS NOT NULL
            AND LENGTH(teoria) > 0
            AND LENGTH(teoria) <= 255
            AND teoria ~ '^[A-Za-zÀ-ÿ0-9 ''-]+$'
            AND quantita IS NULL
        )
        OR
        (tipo_sessione = 'presenza'
            AND quantita IS NOT NULL
            AND quantita > 0
            AND teoria IS NULL
        )
    )
);

-- Creazione della tabella RICETTA
-- La relazione Prepara tra Sessione e Ricetta è M:N.
CREATE TABLE Ricetta (
    id_ricetta INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    descrizione VARCHAR(250) NOT NULL,
    tempo TIME NOT NULL,

    CONSTRAINT check_nome_ricetta CHECK (nome ~ '^[A-Za-zÀ-ÿ ''-]+$'),
    CONSTRAINT check_descrizione_ricetta CHECK (descrizione ~ '^[A-Za-zÀ-ÿ ''-]+$')
);

---- Gestione dei multivalori ----
-- Creazione della tabella SPECIALIZZAZIONE_CHEF (1:N)
CREATE TABLE Specializzazione_Chef (
    email_chef VARCHAR(255),
    specializzazione VARCHAR(50) NOT NULL,
    PRIMARY KEY (email_chef, specializzazione),
    FOREIGN KEY (email_chef) REFERENCES Utente(email) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Creazione della tabella CATEGORIA_CORSO (1:N)
CREATE TABLE Categoria_Corso (
    id_corso INTEGER,
    categoria VARCHAR(50) NOT NULL,
    PRIMARY KEY (id_corso, categoria),
    FOREIGN KEY (id_corso) REFERENCES Corso(id_corso) ON UPDATE CASCADE ON DELETE CASCADE
);
---- Gestione delle associazioni ----
-- Creazione della tabella GESTISCE (Chef <-> Corso)
-- Regola: uno chef può gestire 0..N corsi; un corso deve essere gestito da >=1 chef.
CREATE TABLE Gestisce (
    email_chef VARCHAR(255),
    id_corso INTEGER,
    PRIMARY KEY (email_chef, id_corso),
    FOREIGN KEY (email_chef) REFERENCES Utente(email) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (id_corso) REFERENCES Corso(id_corso) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Creazione della tabella RICHIEDE (Ingredienti per Ricetta)
CREATE TABLE Richiede (
    id_ricetta INTEGER,
    nome_ingrediente VARCHAR(50),
    quantita_necessaria DECIMAL(10,2) CHECK (quantita_necessaria > 0),
    PRIMARY KEY (id_ricetta, nome_ingrediente),
    FOREIGN KEY (id_ricetta) REFERENCES Ricetta(id_ricetta) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (nome_ingrediente) REFERENCES Ingrediente(nome) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Creazione della tabella PREPARA (Sessione <-> Ricetta)
-- Regola: una sessione prepara 1..N ricette e una ricetta può essere preparata in 1..N sessioni.
CREATE TABLE Prepara (
    id_sessione INTEGER,
    id_ricetta INTEGER,
    PRIMARY KEY (id_sessione, id_ricetta),
    FOREIGN KEY (id_sessione) REFERENCES Sessione(id_sessione) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (id_ricetta) REFERENCES Ricetta(id_ricetta) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Creazione della tabella ISCRIZIONE (Utente -> Corso)
CREATE TABLE Iscrizione (
    email_utente VARCHAR(255),
    id_corso INTEGER,
    data_iscrizione DATE DEFAULT CURRENT_DATE,
    stato VARCHAR(20) DEFAULT 'Attivo',
    PRIMARY KEY (email_utente, id_corso), 
    FOREIGN KEY (email_utente) REFERENCES Utente(email) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (id_corso) REFERENCES Corso(id_corso) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Creazione della tabella ADESIONE (Utente -> Sessione Pratica)
CREATE TABLE Adesione (
    email_utente VARCHAR(255),
    id_sessione INTEGER,
    data_adesione DATE DEFAULT CURRENT_DATE,
    PRIMARY KEY (email_utente, id_sessione), 
    FOREIGN KEY (email_utente) REFERENCES Utente(email) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (id_sessione) REFERENCES Sessione(id_sessione) ON UPDATE CASCADE ON DELETE CASCADE
);


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
-- Vincoli di coerenza su relazioni esterne
-- =============================================================

-- Associazione: Iscrizione ammessa solo per studente/chefStudente
CREATE OR REPLACE FUNCTION check_iscrizione_tipo_utente() RETURNS TRIGGER AS $$
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

CREATE TRIGGER trg_check_iscrizione_tipo
BEFORE INSERT OR UPDATE ON Iscrizione
FOR EACH ROW EXECUTE FUNCTION check_iscrizione_tipo_utente();

-- Associazione: Adesione ammessa solo per studente/chefStudente e solo per sessioni di tipo presenza
CREATE OR REPLACE FUNCTION check_adesione_coerenza() RETURNS TRIGGER AS $$
DECLARE
    t tipo_utente_enum;
    ts tipo_sessione_enum;
BEGIN
    SELECT tipo_utente INTO t FROM Utente WHERE email = NEW.email_utente;
    IF t IS NULL THEN
        RAISE EXCEPTION 'Utente % inesistente', NEW.email_utente;
    END IF;
    IF t = 'chef' THEN
        RAISE EXCEPTION 'Solo studente/chefStudente possono aderire alle sessioni (%).', NEW.email_utente;
    END IF;

    SELECT tipo_sessione INTO ts FROM Sessione WHERE id_sessione = NEW.id_sessione;
    IF ts IS NULL THEN
        RAISE EXCEPTION 'Sessione % inesistente', NEW.id_sessione;
    END IF;
    IF ts <> 'presenza' THEN
        RAISE EXCEPTION 'Adesione ammessa solo per sessioni in presenza (sessione=%).', NEW.id_sessione;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_adesione_coerenza
BEFORE INSERT OR UPDATE ON Adesione
FOR EACH ROW EXECUTE FUNCTION check_adesione_coerenza();

-- Associazione: Prepara ammessa solo per sessioni di tipo presenza
CREATE OR REPLACE FUNCTION check_prepara_sessione_presenza() RETURNS TRIGGER AS $$
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

CREATE TRIGGER trg_check_prepara_sessione_presenza
BEFORE INSERT OR UPDATE ON Prepara
FOR EACH ROW EXECUTE FUNCTION check_prepara_sessione_presenza();

-- Multivalore: Specializzazione ammessa solo per chef/chefStudente
CREATE OR REPLACE FUNCTION check_specializzazione_tipo_utente() RETURNS TRIGGER AS $$
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

CREATE TRIGGER trg_check_specializzazione_tipo
BEFORE INSERT OR UPDATE ON Specializzazione_Chef
FOR EACH ROW EXECUTE FUNCTION check_specializzazione_tipo_utente();

-- Associazione: Gestisce ammessa solo per chef/chefStudente
CREATE OR REPLACE FUNCTION check_gestisce_tipo_utente() RETURNS TRIGGER AS $$
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

CREATE TRIGGER trg_check_gestisce_tipo
BEFORE INSERT OR UPDATE ON Gestisce
FOR EACH ROW EXECUTE FUNCTION check_gestisce_tipo_utente();

-- Constraint trigger DEFERRABLE per obblighi minimi:
-- - corso gestito da >=1 chef
-- - corso con >=1 iscritto
-- - corso composto da >=1 sessione
-- - chef/chefStudente con >=1 specializzazione
CREATE OR REPLACE FUNCTION assert_regole_consistenza() RETURNS TRIGGER AS $$
DECLARE
    corso_id INTEGER;
    email_u VARCHAR(255);
    session_id INTEGER;
    ricetta_id INTEGER;
    session_tipo tipo_sessione_enum;
BEGIN
    corso_id := COALESCE(NEW.id_corso, OLD.id_corso);

    session_id := COALESCE(
        (to_jsonb(NEW)->>'id_sessione')::INTEGER,
        (to_jsonb(OLD)->>'id_sessione')::INTEGER
    );
    ricetta_id := COALESCE(
        (to_jsonb(NEW)->>'id_ricetta')::INTEGER,
        (to_jsonb(OLD)->>'id_ricetta')::INTEGER
    );

    IF corso_id IS NULL AND session_id IS NOT NULL THEN
        SELECT s.id_corso INTO corso_id FROM Sessione s WHERE s.id_sessione = session_id;
    END IF;
    IF corso_id IS NOT NULL THEN
        IF NOT EXISTS (SELECT 1 FROM Gestisce g WHERE g.id_corso = corso_id) THEN
            RAISE EXCEPTION 'Vincolo di consistenza: il corso % deve essere gestito da almeno 1 chef.', corso_id;
        END IF;
        IF NOT EXISTS (SELECT 1 FROM Iscrizione i WHERE i.id_corso = corso_id) THEN
            RAISE EXCEPTION 'Vincolo di consistenza: il corso % deve avere almeno 1 iscritto.', corso_id;
        END IF;
        IF NOT EXISTS (SELECT 1 FROM Sessione s WHERE s.id_corso = corso_id) THEN
            RAISE EXCEPTION 'Vincolo di consistenza: il corso % deve avere almeno 1 sessione.', corso_id;
        END IF;

        -- Le sessioni in presenza devono preparare almeno 1 ricetta.
        IF EXISTS (
            SELECT 1
            FROM Sessione s
            WHERE s.id_corso = corso_id
              AND s.tipo_sessione = 'presenza'
              AND NOT EXISTS (
                  SELECT 1
                  FROM Prepara p
                  WHERE p.id_sessione = s.id_sessione
              )
        ) THEN
            RAISE EXCEPTION 'Vincolo di consistenza: tutte le sessioni in presenza del corso % devono preparare almeno 1 ricetta.', corso_id;
        END IF;
    END IF;

    -- Coerenza sessione: online non deve avere adesioni né ricette preparate; presenza deve avere >=1 ricetta.
    IF session_id IS NOT NULL THEN
        SELECT tipo_sessione INTO session_tipo FROM Sessione WHERE id_sessione = session_id;
        IF session_tipo = 'presenza' THEN
            IF NOT EXISTS (SELECT 1 FROM Prepara p WHERE p.id_sessione = session_id) THEN
                RAISE EXCEPTION 'Vincolo di consistenza: la sessione % (presenza) deve preparare almeno 1 ricetta.', session_id;
            END IF;
        ELSIF session_tipo = 'online' THEN
            IF EXISTS (SELECT 1 FROM Prepara p WHERE p.id_sessione = session_id) THEN
                RAISE EXCEPTION 'Vincolo di consistenza: la sessione % (online) non può preparare ricette.', session_id;
            END IF;
            IF EXISTS (SELECT 1 FROM Adesione a WHERE a.id_sessione = session_id) THEN
                RAISE EXCEPTION 'Vincolo di consistenza: la sessione % (online) non può avere adesioni.', session_id;
            END IF;
        END IF;
    END IF;

    -- Una ricetta deve essere preparata in almeno 1 sessione.
    IF ricetta_id IS NOT NULL THEN
        IF NOT EXISTS (SELECT 1 FROM Prepara p WHERE p.id_ricetta = ricetta_id) THEN
            RAISE EXCEPTION 'Vincolo di consistenza: la ricetta % deve essere preparata in almeno 1 sessione.', ricetta_id;
        END IF;
    END IF;

    email_u := COALESCE(
        to_jsonb(NEW)->>'email',
        to_jsonb(OLD)->>'email',
        to_jsonb(NEW)->>'email_chef',
        to_jsonb(OLD)->>'email_chef',
        to_jsonb(NEW)->>'email_utente',
        to_jsonb(OLD)->>'email_utente'
    );
    IF email_u IS NOT NULL THEN
        IF EXISTS (
            SELECT 1
            FROM Utente u
            WHERE u.email = email_u
              AND u.tipo_utente IN ('chef', 'chefStudente')
        ) THEN
            IF NOT EXISTS (
                SELECT 1
                FROM Specializzazione_Chef sc
                WHERE sc.email_chef = email_u
            ) THEN
                RAISE EXCEPTION 'Vincolo di consistenza: lo chef % deve avere almeno 1 specializzazione.', email_u;
            END IF;
        END IF;
    END IF;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Lega l'asserzione ai punti in cui può cambiare la verità dei vincoli minimi
CREATE CONSTRAINT TRIGGER ctr_assert_minimi_corso
AFTER INSERT OR UPDATE OR DELETE ON Corso
DEFERRABLE INITIALLY DEFERRED
FOR EACH ROW EXECUTE FUNCTION assert_regole_consistenza();

CREATE CONSTRAINT TRIGGER ctr_assert_minimi_gestisce
AFTER INSERT OR UPDATE OR DELETE ON Gestisce
DEFERRABLE INITIALLY DEFERRED
FOR EACH ROW EXECUTE FUNCTION assert_regole_consistenza();

CREATE CONSTRAINT TRIGGER ctr_assert_minimi_iscrizione
AFTER INSERT OR UPDATE OR DELETE ON Iscrizione
DEFERRABLE INITIALLY DEFERRED
FOR EACH ROW EXECUTE FUNCTION assert_regole_consistenza();

CREATE CONSTRAINT TRIGGER ctr_assert_minimi_sessione
AFTER INSERT OR UPDATE OR DELETE ON Sessione
DEFERRABLE INITIALLY DEFERRED
FOR EACH ROW EXECUTE FUNCTION assert_regole_consistenza();

CREATE CONSTRAINT TRIGGER ctr_assert_minimi_utente
AFTER INSERT OR UPDATE OR DELETE ON Utente
DEFERRABLE INITIALLY DEFERRED
FOR EACH ROW EXECUTE FUNCTION assert_regole_consistenza();

CREATE CONSTRAINT TRIGGER ctr_assert_minimi_specializzazione
AFTER INSERT OR UPDATE OR DELETE ON Specializzazione_Chef
DEFERRABLE INITIALLY DEFERRED
FOR EACH ROW EXECUTE FUNCTION assert_regole_consistenza();

CREATE CONSTRAINT TRIGGER ctr_assert_minimi_prepara
AFTER INSERT OR UPDATE OR DELETE ON Prepara
DEFERRABLE INITIALLY DEFERRED
FOR EACH ROW EXECUTE FUNCTION assert_regole_consistenza();

CREATE CONSTRAINT TRIGGER ctr_assert_minimi_ricetta
AFTER INSERT OR UPDATE OR DELETE ON Ricetta
DEFERRABLE INITIALLY DEFERRED
FOR EACH ROW EXECUTE FUNCTION assert_regole_consistenza();

CREATE CONSTRAINT TRIGGER ctr_assert_minimi_adesione
AFTER INSERT OR UPDATE OR DELETE ON Adesione
DEFERRABLE INITIALLY DEFERRED
FOR EACH ROW EXECUTE FUNCTION assert_regole_consistenza();

-- =============================================================
-- Popolamento del Database
-- =============================================================

BEGIN;

-- Ingredienti
INSERT INTO Ingrediente VALUES ('Riso Carnaroli', 'kg'), ('Zafferano', 'g'), ('Brodo', 'litri'), ('Parmigiano', 'kg');

-- Corso
INSERT INTO Corso (data_inizio, nome, frequenza, num_sessioni) VALUES ('2024-11-01', 'Risotti dal Mondo', 'Settimanale', 1);
INSERT INTO Categoria_Corso (id_corso, categoria) VALUES (1, 'Cucina Italiana'), (1, 'Primi Piatti');

-- Sessione (Vuota all'inizio)
INSERT INTO Sessione (ora_inizio, tipo_sessione, id_corso, quantita) 
VALUES ('2024-11-01 18:00:00', 'presenza', 1, 1.00);

-- Ricetta collegata alla Sessione
INSERT INTO Ricetta (nome, descrizione, tempo) 
VALUES ('Risotto alla Milanese', 'Classico giallo', '00:45:00');

-- Collegamento M:N Sessione <-> Ricetta (Prepara)
INSERT INTO Prepara (id_sessione, id_ricetta) VALUES (1, 1);

-- Dettaglio Ricetta (Dosi per 1 persona)
INSERT INTO Richiede (id_ricetta, nome_ingrediente, quantita_necessaria) VALUES 
(1, 'Riso Carnaroli', 0.08),  -- 80g a testa
(1, 'Zafferano', 0.1),        -- 0.1g a testa
(1, 'Brodo', 0.5);            -- mezzo litro a testa

-- Utenti
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) 
VALUES ('mario.std@unina.it', 'Mario', 'Rossi', 'Studente1!', 'N86001', 'studente');

INSERT INTO Utente (email, nome, cognome, password, tipo_utente) 
VALUES ('chef.cannav@email.it', 'Antonino', 'Cannav', 'ChefTop1!', 'chef');
INSERT INTO Specializzazione_Chef VALUES ('chef.cannav@email.it', 'Cucina Partenopea');

-- Gestione corso da parte dello chef
INSERT INTO Gestisce (email_chef, id_corso) VALUES ('chef.cannav@email.it', 1);

-- Prova delle operazioni

-- Mario si iscrive al corso
INSERT INTO Iscrizione (email_utente, id_corso) VALUES ('mario.std@unina.it', 1);

-- Mario aderisce alla sessione pratica
INSERT INTO Adesione (email_utente, id_sessione) VALUES ('mario.std@unina.it', 1);

COMMIT;

---- Query anti-spreco ----
SELECT 
    i.nome, i.unita_di_misura, 
    SUM(ri.quantita_necessaria * s.num_aderenti) AS totale_da_comprare
FROM Sessione s
JOIN Prepara p ON s.id_sessione = p.id_sessione
JOIN Ricetta r ON p.id_ricetta = r.id_ricetta
JOIN Richiede ri ON r.id_ricetta = ri.id_ricetta
JOIN Ingrediente i ON ri.nome_ingrediente = i.nome
WHERE s.id_sessione = 1 
GROUP BY i.nome, i.unita_di_misura;