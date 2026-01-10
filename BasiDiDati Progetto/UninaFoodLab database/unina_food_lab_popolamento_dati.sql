-- =============================================================
-- Popolamento del Database
-- =============================================================

BEGIN;

-- Ingredienti
INSERT INTO Ingrediente VALUES ('Riso Carnaroli', 'kg'), ('Zafferano', 'g'), ('Brodo', 'litri'), ('Parmigiano', 'kg'), ('Burro', 'kg'),
('Cipolla', 'kg'), ('Vino Bianco', 'litri'), ('Olio d''Oliva', 'litri'), ('Sale', 'g'), ('Pepe', 'g'),
('Pomodori', 'kg'), ('Basilico', 'g'), ('Aglio', 'g'), ('Mozzarella', 'kg'), ('Farina', 'kg'), ('Uova', 'unita'), ('Lievito', 'g'),
 ('Zucchero', 'g'), ('Latte', 'litri'), ('Verdure', 'unita');

-- Corso
INSERT INTO Corso (data_inizio, nome, frequenza, num_sessioni) VALUES
('2024-11-01', 'Risotti dal Mondo', 'Settimanale', 1),
('2024-12-01', 'Pizze e Focacce', 'Bisettimanale', 2),
('2025-01-15', 'Dolci della Tradizione', 'Mensile', 3),
('2025-02-10', 'Cucina Vegana', 'Settimanale', 4);
INSERT INTO Categoria_Corso (id_corso, categoria) VALUES (1, 'Cucina Italiana'), (1, 'Primi Piatti'), 
(2, 'Cucina Italiana'), (2, 'Pizze e Focacce'),
(3, 'Pasticceria'), (3, 'Dolci Tradizionali'),
(4, 'Cucina Vegana'), (4, 'Cucina Salutare'), (4, 'Cucina Internazionale');


-- Sessione (Vuota all'inizio)
-- Sessioni in presenza (teoria deve essere NULL)
INSERT INTO Sessione ( ora_inizio, tipo_sessione, id_corso) 
VALUES ( '2024-11-01 18:00:00', 'presenza', 1),
( '2024-12-08 17:00:00', 'presenza', 3),
( '2025-01-15 16:00:00', 'presenza', 3),
( '2025-02-17 18:00:00', 'presenza', 4);

-- Sessioni online (teoria obbligatoria)
INSERT INTO Sessione (ora_inizio, tipo_sessione, teoria, id_corso) 
VALUES ( '2024-12-01 17:00:00', 'online', 'Teoria impasto e lievitazione', 2),
('2025-02-10 18:00:00', 'online', 'Teoria cucina vegana e nutrienti', 4);

-- Ricetta collegata alla Sessione
INSERT INTO Ricetta (nome, descrizione, tempo) 
VALUES ('Risotto alla Milanese', 'Classico giallo', '00:45:00'),
('Pizza Margherita', 'La regina delle pizze', '01:30:00'),
('Tiramisù', 'Dolce al cucchiaio con caffè e mascarpone', '00:30:00'),
('Insalata di Quinoa e Verdure', 'Piatto vegano fresco e nutriente', '00:20:00');

-- Collegamento M:N Sessione <-> Ricetta (Prepara)
INSERT INTO Prepara (id_sessione, id_ricetta) VALUES (1, 1),
(3, 2),
(4, 3),
(2, 4);

-- Dettaglio Ricetta (Dosi per 1 persona)
INSERT INTO Richiede (id_ricetta, nome_ingrediente, quantita_necessaria) VALUES 
(1, 'Riso Carnaroli', 0.08),  -- 80g a testa
(1, 'Zafferano', 0.1),        -- 0.1g a testa
(1, 'Brodo', 0.5),           -- mezzo litro a testa
(2, 'Farina', 0.2),         -- 200g a testa
(2, 'Mozzarella', 0.15),     -- 150g a testa
(2, 'Pomodori', 0.1),        -- 100g a testa
(3, 'Uova', 0.1),            -- 1 uovo ogni 10 persone
(3, 'Zucchero', 0.05),       -- 50g a testa
(3, 'Latte', 0.1),           -- 100ml a testa
(4, 'Verdure', 0.2);         -- 200g a testa

-- Utenti
-- Studenti
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) 
VALUES ('mario.std@unina.it', 'Mario', 'Rossi', 'Studente1!', 'N86001', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente)
VALUES ('giacomo.std@unina.it', 'Giacomo', 'Bianchi', 'Studente2!', 'N86002', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente)
VALUES ('sara.std@unina.it', 'Sara', 'Verdi', 'Studente3!', 'N86003', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente)
VALUES ('chiara.std@unina.it', 'Chiara', 'Neri', 'Studente4!', 'N86004', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente)
VALUES ('luca.std@unina.it', 'Luca', 'Russo', 'Studente5!', 'N86005', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente)
VALUES ('matilde.std@unina.it', 'Matilde', 'Esposito', 'Studente6!', 'N86006', 'studente');
-- Chef
INSERT INTO Utente (email, nome, cognome, password, tipo_utente) 
VALUES ('chef.cannav@email.it', 'Antonino', 'Cannav', 'ChefTop1!', 'chef');
INSERT INTO Specializzazione_Chef VALUES ('chef.cannav@email.it', 'Cucina Partenopea'), 
('chef.cannav@email.it','Cucina Italiana'), ('chef.cannav@email.it','Pizze e Focacce');
INSERT INTO Utente (email, nome, cognome, password, tipo_utente)
VALUES ('chef.barb@email.it', 'Giorgio', 'Barbieri', 'ChefTop2!', 'chef');
INSERT INTO Specializzazione_Chef VALUES ('chef.barb@email.it', 'Cucina Gourmet'),
('chef.barb@email.it','Pasticceria'), ('chef.barb@email.it','Cucina Italiana');
INSERT INTO Utente (email, nome, cognome, password, tipo_utente)
VALUES ('chef.dem@email.com', 'Luca', 'Demi', 'ChefTop3!', 'chef');
INSERT INTO Specializzazione_Chef VALUES ('chef.dem@email.com', 'Cucina Vegana'), 
('chef.dem@email.com','Cucina Salutare'), ('chef.dem@email.com','Cucina Internazionale');
-- ChefStudente
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente)
VALUES ('chef.grn@unina.it', 'Gianni', 'Rossi', 'ChefStud1!', 'N86007', 'chefStudente');
INSERT INTO Specializzazione_Chef VALUES ('chef.grn@unina.it', 'Cucina Italiana'),
('chef.grn@unina.it','Pizze e Focacce');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente)
VALUES ('chef.drg@unina.it', 'Dario', 'Grasso', 'ChefStud2!', 'N86008', 'chefStudente');
INSERT INTO Specializzazione_Chef VALUES ('chef.drg@unina.it', 'Pasticceria'), ('chef.drg@unina.it','Cucina Vegana');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente)
VALUES ('chef.apr@unina.it', 'Alessia', 'Prato', 'ChefStud3!', 'N86009', 'chefStudente');
INSERT INTO Specializzazione_Chef VALUES ('chef.apr@unina.it', 'Cucina Salutare'), ('chef.apr@unina.it','Cucina Internazionale');

-- Gestione corso da parte dello chef
INSERT INTO Gestisce (email_chef, id_corso) VALUES ('chef.cannav@email.it', 1);
INSERT INTO Gestisce (email_chef, id_corso) VALUES ('chef.barb@email.it', 2);
INSERT INTO Gestisce (email_chef, id_corso) VALUES ('chef.dem@email.com', 3);
INSERT INTO Gestisce (email_chef, id_corso) VALUES ('chef.grn@unina.it', 2);
INSERT INTO Gestisce (email_chef, id_corso) VALUES ('chef.apr@unina.it', 3);
INSERT INTO Gestisce (email_chef, id_corso) VALUES ('chef.drg@unina.it', 4);
-- Prova delle operazioni

-- Iscrizioni al corso
INSERT INTO Iscrizione (email_utente, id_corso) VALUES ('mario.std@unina.it', 1);
INSERT INTO Iscrizione (email_utente, id_corso) VALUES ('mario.std@unina.it', 2);
INSERT INTO Iscrizione (email_utente, id_corso) VALUES ('mario.std@unina.it', 3);
INSERT INTO Iscrizione (email_utente, id_corso) VALUES ('matilde.std@unina.it', 1);
INSERT INTO Iscrizione (email_utente, id_corso) VALUES ('matilde.std@unina.it', 4);
INSERT INTO Iscrizione (email_utente, id_corso) VALUES ('giacomo.std@unina.it', 1);
INSERT INTO Iscrizione (email_utente, id_corso) VALUES ('giacomo.std@unina.it', 2);
INSERT INTO Iscrizione (email_utente, id_corso) VALUES ('sara.std@unina.it', 2);
INSERT INTO Iscrizione (email_utente, id_corso) VALUES ('sara.std@unina.it', 3);
INSERT INTO Iscrizione (email_utente, id_corso) VALUES ('chiara.std@unina.it', 1);
INSERT INTO Iscrizione (email_utente, id_corso) VALUES ('luca.std@unina.it', 3);
INSERT INTO Iscrizione (email_utente, id_corso) VALUES ('luca.std@unina.it', 4);
INSERT INTO Iscrizione (email_utente, id_corso) VALUES ('chiara.std@unina.it', 2);
INSERT INTO Iscrizione (email_utente, id_corso) VALUES ('chiara.std@unina.it', 3);
INSERT INTO Iscrizione (email_utente, id_corso) VALUES ('chiara.std@unina.it', 4);
INSERT INTO Iscrizione (email_utente, id_corso) VALUES ('matilde.std@unina.it', 2);
INSERT INTO Iscrizione (email_utente, id_corso) VALUES ('matilde.std@unina.it', 3);
INSERT INTO Iscrizione (email_utente, id_corso) VALUES ('giacomo.std@unina.it', 3);
INSERT INTO Iscrizione (email_utente, id_corso) VALUES ('giacomo.std@unina.it', 4);
INSERT INTO Iscrizione (email_utente, id_corso) VALUES ('sara.std@unina.it', 1);
INSERT INTO Iscrizione (email_utente, id_corso) VALUES ('sara.std@unina.it', 4);
INSERT INTO Iscrizione (email_utente, id_corso) VALUES ('luca.std@unina.it', 2);


-- Adesioni alle sessioni pratiche (solo "presenza")
-- Nota:  la sessione 4 è volutamente vuota
INSERT INTO Adesione (email_utente, id_sessione) VALUES ('mario.std@unina.it', 1);
INSERT INTO Adesione (email_utente, id_sessione) VALUES ('matilde.std@unina.it', 1);
INSERT INTO Adesione (email_utente, id_sessione) VALUES ('giacomo.std@unina.it', 1);
INSERT INTO Adesione (email_utente, id_sessione) VALUES ('chiara.std@unina.it', 1);
INSERT INTO Adesione (email_utente, id_sessione) VALUES ('sara.std@unina.it', 1);

INSERT INTO Adesione (email_utente, id_sessione) VALUES ('mario.std@unina.it', 3);
INSERT INTO Adesione (email_utente, id_sessione) VALUES ('giacomo.std@unina.it', 3);
INSERT INTO Adesione (email_utente, id_sessione) VALUES ('sara.std@unina.it', 3);
INSERT INTO Adesione (email_utente, id_sessione) VALUES ('chiara.std@unina.it', 3);
INSERT INTO Adesione (email_utente, id_sessione) VALUES ('matilde.std@unina.it', 3);
INSERT INTO Adesione (email_utente, id_sessione) VALUES ('luca.std@unina.it', 3);

INSERT INTO Adesione (email_utente, id_sessione) VALUES ('matilde.std@unina.it', 2);
INSERT INTO Adesione (email_utente, id_sessione) VALUES ('giacomo.std@unina.it', 2);
INSERT INTO Adesione (email_utente, id_sessione) VALUES ('sara.std@unina.it', 2);
INSERT INTO Adesione (email_utente, id_sessione) VALUES ('chiara.std@unina.it', 2);
INSERT INTO Adesione (email_utente, id_sessione) VALUES ('luca.std@unina.it', 2);


COMMIT;