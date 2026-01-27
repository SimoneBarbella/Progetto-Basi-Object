GRANT ALL ON SCHEMA uninafoodlab TO public;
SET search_path TO uninafoodlab, public;

--- CREAZIONE TIPI ENUM ---
CREATE TYPE tipo_utente_enum AS ENUM ('chef', 'studente', 'chefStudente');
CREATE TYPE tipo_sessione_enum AS ENUM ('presenza', 'online');

--- CREAZIONE DELLE TABELLE ---

-- Creazione della tabella INGREDIENTE
CREATE TABLE Ingrediente (
    nome VARCHAR(50) PRIMARY KEY,
    unita_di_misura VARCHAR(20) NOT NULL
);

-- Creazione della tabella UTENTE
CREATE TABLE Utente (
    email VARCHAR(255) PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    cognome VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    matricola VARCHAR(20) UNIQUE, -- Obbligatoria solo se studente
    tipo_utente tipo_utente_enum NOT NULL
);

-- Creazione della tabella CORSO
CREATE TABLE Corso (
    id_corso INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    data_inizio DATE NOT NULL,
    nome VARCHAR(50) NOT NULL,
    frequenza VARCHAR(50) NOT NULL,
    num_partecipanti INTEGER DEFAULT 0, -- Aggiornato da Trigger
    num_sessioni INTEGER NOT NULL
);

-- Creazione della tabella SESSIONE
CREATE TABLE Sessione (
    id_sessione INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ora_inizio TIMESTAMP NOT NULL, 
    num_aderenti INTEGER DEFAULT 0, -- Aggiornato da Trigger
    teoria VARCHAR(255),
    tipo_sessione tipo_sessione_enum NOT NULL,
    id_corso INTEGER NOT NULL,
    FOREIGN KEY (id_corso) REFERENCES Corso(id_corso) ON DELETE CASCADE
);

-- Creazione della tabella RICETTA
CREATE TABLE Ricetta (
    id_ricetta INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    descrizione VARCHAR(250) NOT NULL,
    tempo TIME NOT NULL
);

CREATE TABLE Notifica (
    id_notifica INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    messaggio TEXT NOT NULL,
    data_invio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    email_chef VARCHAR(255) NOT NULL,
    id_corso INTEGER, -- Se NULL, si intende "A tutti i corsi"
    
    FOREIGN KEY (email_chef) REFERENCES Utente(email) ON DELETE CASCADE,
    FOREIGN KEY (id_corso) REFERENCES Corso(id_corso) ON DELETE CASCADE
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
    quantita_necessaria DECIMAL(10,2),
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
-- =============================================================
-- Creazione Viste
-- =============================================================

CREATE VIEW Vista_Fabbisogni_Sessione AS
SELECT 
    -- Chiavi di identificazione
    Prepara.id_sessione,
    Richiede.nome_ingrediente,
    
    -- Calcolo: (Dose Unitaria * Numero Studenti), sommato per ogni ricetta
    SUM(Richiede.quantita_necessaria * Sessione.num_aderenti) AS quantita_totale

FROM 
    Prepara
    
    -- 1. JOIN con la tabella RICHIEDE (per sapere le dosi unitarie)
    JOIN Richiede ON Prepara.id_ricetta = Richiede.id_ricetta

    -- 2. JOIN con SESSIONE (num_aderenti è mantenuto dal trigger)
    JOIN Sessione ON Sessione.id_sessione = Prepara.id_sessione

GROUP BY 
    Prepara.id_sessione, 
    Richiede.nome_ingrediente;