-- =============================================================
-- VINCOLI CHECK
-- =============================================================

-- Ingrediente
-- Nome e unità di misura con caratteri validi
ALTER TABLE Ingrediente ADD CONSTRAINT check_nome_ingrediente CHECK (nome ~ '^[A-Za-zÀ-ÿ ''-]+$');
ALTER TABLE Ingrediente ADD CONSTRAINT check_unita_di_misura CHECK (unita_di_misura ~ '^[A-Za-z]+$');

--Utente
-- Email, nome, cognome, password e coerenza dati in base al ruolo
ALTER TABLE Utente ADD CONSTRAINT check_email CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$');
ALTER TABLE Utente ADD CONSTRAINT check_nome_utente CHECK (nome ~ '^[A-Za-zÀ-ÿ ''-]+$');
ALTER TABLE Utente ADD CONSTRAINT check_cognome CHECK (cognome ~ '^[A-Za-zÀ-ÿ ''-]+$');
ALTER TABLE Utente ADD CONSTRAINT check_password CHECK (
    LENGTH(password) >= 8 AND password ~ '[A-Z]' AND password ~ '[a-z]'
    AND password ~ '[0-9]' AND password ~ '[^a-zA-Z0-9]'
);
ALTER TABLE Utente ADD CONSTRAINT check_ruolo_dati CHECK (
    ((tipo_utente IN ('studente', 'chefStudente')) AND matricola IS NOT NULL)
    OR
    (tipo_utente = 'chef' AND matricola IS NULL)
);

-- Corso
--  Numero sessioni positivo, nome con caratteri validi
ALTER TABLE Corso ADD CONSTRAINT check_num_sessioni CHECK (num_sessioni > 0);
ALTER TABLE Corso ADD CONSTRAINT check_nome_corso CHECK (nome ~ '^[A-Za-zÀ-ÿ ''-]+$');

-- Sessione
-- Numero aderenti non negativo, coerenza dati in base al tipo di sessione
ALTER TABLE Sessione ADD CONSTRAINT check_num_aderenti CHECK (num_aderenti >= 0);
ALTER TABLE Sessione ADD CONSTRAINT check_logica_sessione CHECK (
    (tipo_sessione = 'online'
        AND teoria IS NOT NULL
        AND LENGTH(teoria) > 0
        AND LENGTH(teoria) <= 255
        AND teoria ~ '^[A-Za-zÀ-ÿ0-9 ''-]+$'

    )
    OR
    (tipo_sessione = 'presenza'
        AND teoria IS NULL
    )
);

-- Ricetta
-- Nome e descrizione con caratteri validi
ALTER TABLE Ricetta ADD CONSTRAINT check_nome_ricetta CHECK (nome ~ '^[A-Za-zÀ-ÿ ''-]+$');
ALTER TABLE Ricetta ADD CONSTRAINT check_descrizione CHECK (descrizione ~ '^[A-Za-zÀ-ÿ ''-]+$');

-- Richiede
-- Quantità necessaria positiva
ALTER TABLE Richiede ADD CONSTRAINT check_quantita_necessaria CHECK (quantita_necessaria > 0);