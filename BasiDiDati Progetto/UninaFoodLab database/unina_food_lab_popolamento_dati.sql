
-- =============================================================
-- Popolamento del Database
-- =============================================================

GRANT ALL ON SCHEMA uninafoodlab TO public;
SET search_path TO uninafoodlab, public;

BEGIN;

-- Ingredienti
INSERT INTO Ingrediente VALUES
('Riso Carnaroli', 'kg'), ('Zafferano', 'g'), ('Brodo', 'litri'), ('Parmigiano', 'kg'), ('Burro', 'kg'),
('Cipolla', 'kg'), ('Vino Bianco', 'litri'), ('Olio d''Oliva', 'litri'), ('Sale', 'g'), ('Pepe', 'g'),
('Pomodori', 'kg'), ('Basilico', 'g'), ('Aglio', 'g'), ('Mozzarella', 'kg'), ('Farina', 'kg'), ('Uova', 'unita'), ('Lievito', 'g'),
('Zucchero', 'g'), ('Latte', 'litri'), ('Verdure', 'unita'),
('Peperoncino', 'g'), ('Rosmarino', 'g'), ('Salvia', 'g'), ('Panna', 'ml'), ('Prosciutto', 'kg'),
('Funghi', 'kg'), ('Patate', 'kg'), ('Spinaci', 'kg'), ('Zucchine', 'kg'), ('Limone', 'kg'),
('Origano', 'g'), ('Peperoni', 'kg'), ('Curry', 'g'), ('Curcuma', 'g'), ('Yogurt', 'ml'),
('Ricotta', 'kg'), ('Mascarpone', 'kg'), ('Speck', 'kg'), ('Salmone', 'kg'), ('Tonno', 'kg'),
('Capperi', 'g'), ('Acciughe', 'g'), ('Noci', 'g'), ('Mandorle', 'g'), ('Pane', 'kg'),
('Semola', 'kg'), ('Bicarbonato', 'g'), ('Panna Acida', 'ml'), ('Coriandolo', 'g'), ('Menta', 'g'),
('Cilantro', 'g'), ('Cumino', 'g'), ('Zucchero di Canna', 'g'), ('Sciroppo d''Acero', 'ml'), ('Vaniglia', 'g'),
('Cannella', 'g'), ('Chiodi di Garofano', 'g'), ('Noce Moscata', 'g'),
('Zenzero', 'g'), ('Aneto', 'g'), ('Alloro', 'g'), ('Cacao', 'g'), ('Cioccolato Fondente', 'g'),
('Burro Arachidi', 'g'), ('Miele', 'g'), ('Salsa di Soia', 'ml'), ('Aceto Balsamico', 'ml'), ('Aceto di Vino', 'ml'),
('Maionese', 'ml'), ('Senape', 'g'), ('Jalapeno', 'g'), ('Pepe Nero', 'g'), ('Cocco', 'kg'),
('Latte di Cocco', 'ml'), ('Farina di Mais', 'kg'), ('Amido di Mais', 'g'), ('Semi di Sesamo', 'g'), ('Semi di Papavero', 'g'),
('Semi di Chia', 'g'), ('Marmellata', 'g'), ('Gelatina', 'g'), ('Burro Salato', 'kg'), ('Pecorino', 'kg'),
('Ciliegie', 'kg'), ('Granella di Nocciole', 'g'), ('Zucchero a Velo', 'g'), ('Polvere di Aglio', 'g'), ('Paprika', 'g'),
('Pistacchi', 'g'), ('Amaretti', 'g'), ('Feta', 'kg'), ('Olive', 'g'), ('Cetrioli', 'kg'), ('Carne macinata', 'kg'), 
('Gamberi', 'kg'), ('Pollo', 'kg'), ('Spaghetti','kg'),('Lasagne','kg'),('Ragù alla Bolognese','kg'),('Besciamella','kg'),
('Gnocchi','kg'),('Tortellini','kg'), ('Farro','kg'),('Pasta','kg'),('Pesce misto','kg'),('Pesce','kg'),('Pesce crudo','kg'),
('Frutti di Mare','kg'), ('Nero di Seppia','g'),('Couscous','kg'),('Noodles','kg'),('Spezie','g'),
('Tahina','g'),('Ceci','kg'),('Lenticchie','kg'),('Asparagi','kg'),('Barbabietola','kg'),('Fagioli','kg'),('Frutta','kg'),
('Mele','kg'),('Melanzane','kg'),('Baccalà','kg'),('Banana','kg'),('Confit dAnatra','kg'),('Rucola','kg'),('Zucca','kg'),
('Insalata mista','unita'),('Formaggio','kg'),('Filetto','kg'),('Polpo','kg'), ('Legumi','kg');

-- Corsi
INSERT INTO Corso (data_inizio, nome, frequenza, num_sessioni) VALUES
('2025-08-05', 'Risotti dal Mondo', 'Settimanale', 1),
('2025-09-01', 'Pizze e Focacce', 'Bisettimanale', 2),
('2025-10-10', 'Dolci della Tradizione', 'Mensile', 3),
('2025-11-15', 'Cucina Vegana', 'Settimanale', 4),
('2025-08-15', 'Tecniche di Sushi', 'Mensile', 3),
('2025-08-22', 'Cucina Mediterranea', 'Settimanale', 4),
('2025-09-05', 'Pasticceria Moderna', 'Bisettimanale', 5),
('2025-09-20', 'Cucina Regionale', 'Mensile', 4),
('2025-10-03', 'Panificazione', 'Bisettimanale', 6),
('2025-10-18', 'Gelato Artigianale', 'Mensile', 3),
('2025-11-02', 'Cocktail e Mixology', 'Mensile', 2),
('2025-11-20', 'Cucina Senza Glutine', 'Settimanale', 4),
('2025-12-05', 'Cucina per Bambini', 'Mensile', 2),
('2025-12-18', 'Cucina per Sportivi', 'Mensile', 3),
('2026-01-10', 'Conservazione Alimenti', 'Mensile', 2),
('2026-01-25', 'Cioccolateria', 'Bisettimanale', 4),
('2026-02-08', 'Cucina Internazionale Avanzata', 'Mensile', 5),
('2026-02-22', 'Cucina Etica e Sostenibile', 'Mensile', 3),
('2026-03-07', 'Fermentazioni', 'Mensile', 3),
('2026-03-21', 'Street Food Italiano', 'Bisettimanale', 4),
('2026-04-05', 'Cucina Francese', 'Mensile', 4),
('2026-04-20', 'Cucina Spagnola', 'Mensile', 3),
('2026-05-10', 'Cucina Giapponese', 'Mensile', 4),
('2026-06-01', 'Cucina Nordica', 'Mensile', 3),
('2026-06-15', 'Tecniche Avanzate di Lievitazione', 'Bisettimanale', 5),
('2026-06-29', 'Cucina Vegetariana Creativa', 'Settimanale', 4),
('2026-07-12', 'Cucina Senza Lattosio', 'Mensile', 2);
-- Categoria_Corso (1-3 categorie per corso)
INSERT INTO Categoria_Corso (id_corso, categoria) VALUES
(1, 'Cucina Italiana'), (1, 'Primi Piatti'),
(2, 'Cucina Italiana'), (2, 'Pizze e Focacce'),
(3, 'Pasticceria'), (3, 'Dolci Tradizionali'),
(4, 'Cucina Vegana'), (4, 'Cucina Salutare'),
(5, 'Cucina Giapponese'), (5, 'Tecniche'),
(6, 'Cucina Internazionale'), (6, 'Cucina Mediterranea'),
(7, 'Pasticceria'), (7, 'Pasticceria Moderna'),
(8, 'Cucina Italiana'), (8, 'Cucina Regionale'),
(9, 'Panificazione'), (9, 'Pane e Lievitazione'),
(10, 'Pasticceria'), (10, 'Gelateria'),
(11, 'Drink'), (11, 'Mixology'),
(12, 'Cucina Speciale'), (12, 'Senza Glutine'),
(13, 'Cucina Educativa'), (13, 'Cucina Salutare'),
(14, 'Cucina Salutare'), (14, 'Nutrizione'),
(15, 'Tecniche'), (15, 'Sicurezza Alimentare'),
(16, 'Pasticceria'), (16, 'Cioccolateria'),
(17, 'Cucina Internazionale'), (17, 'Cucina Gourmet'),
(18, 'Cucina Sostenibile'), (18, 'Etica Alimentare'),
(19, 'Tecniche'), (19, 'Fermentazione'),
(20, 'Street Food'), (20, 'Cucina Italiana'),
(21, 'Cucina Internazionale'), (21, 'Cucina Francese'),
(22, 'Cucina Internazionale'), (22, 'Cucina Spagnola'),
(23, 'Cucina Internazionale'), (23, 'Cucina Giapponese'),
(24, 'Cucina Internazionale'), (24, 'Cucina Nordica'),
(25, 'Panificazione'), (25, 'Lievitazione'), (25, 'Tecniche'),
(26, 'Cucina Vegetariana'), (26, 'Cucina Creativa'),
(27, 'Senza Lattosio'), (27, 'Cucina Salutare');

-- Sessione (una sessione per `num_sessioni` del corso; seconda sessione dei corsi 2..24 è online)
INSERT INTO Sessione (ora_inizio, tipo_sessione, teoria, id_corso) VALUES
-- Corso 1 (2025-08-05) num=1
('2025-08-05 18:00:00','presenza', NULL, 1),
-- Corso 2 (2025-09-01) num=2 (2nd online)
('2025-09-01 18:00:00','presenza', NULL, 2),('2025-09-15 18:00:00','online',' Teoria Pizze e Focacce', 2),
-- Corso 3 (2025-10-10) num=3 (2nd online)
('2025-10-10 18:00:00','presenza', NULL, 3),('2025-11-07 18:00:00','online',' Teoria Dolci della Tradizione', 3),('2025-12-05 18:00:00','presenza', NULL, 3),
-- Corso 4 (2025-11-15) num=4 (2nd online)
('2025-11-15 18:00:00','presenza', NULL, 4),('2025-11-22 18:00:00','online',' Teoria Cucina Vegana', 4),('2025-11-29 18:00:00','presenza', NULL, 4),('2025-12-06 18:00:00','presenza', NULL, 4),
-- Corso 5 (2025-08-15) num=3 (2nd online)
('2025-08-15 18:00:00','presenza', NULL, 5),('2025-09-12 18:00:00','online',' Teoria Tecniche di Sushi', 5),('2025-10-10 18:00:00','presenza', NULL, 5),
-- Corso 6 (2025-08-22) num=4 (2nd online)
('2025-08-22 18:00:00','presenza', NULL, 6),('2025-08-29 18:00:00','online',' Teoria Cucina Mediterranea', 6),('2025-09-05 18:00:00','presenza', NULL, 6),('2025-09-12 18:00:00','presenza', NULL, 6),
-- Corso 7 (2025-09-05) num=5 (2nd online)
('2025-09-05 18:00:00','presenza', NULL, 7),('2025-09-19 18:00:00','online',' Teoria Pasticceria Moderna', 7),('2025-10-03 18:00:00','presenza', NULL, 7),('2025-10-17 18:00:00','presenza', NULL, 7),('2025-10-31 18:00:00','presenza', NULL, 7),
-- Corso 8 (2025-09-20) num=4 (2nd online)
('2025-09-20 18:00:00','presenza', NULL, 8),('2025-10-18 18:00:00','online',' Teoria Cucina Regionale', 8),('2025-11-15 18:00:00','presenza', NULL, 8),('2025-12-13 18:00:00','presenza', NULL, 8),
-- Corso 9 (2025-10-03) num=6 (2nd online)
('2025-10-03 18:00:00','presenza', NULL, 9),('2025-10-17 18:00:00','online',' Teoria Panificazione', 9),('2025-10-31 18:00:00','presenza', NULL, 9),('2025-11-14 18:00:00','presenza', NULL, 9),('2025-11-28 18:00:00','presenza', NULL, 9),('2025-12-12 18:00:00','presenza', NULL, 9),
-- Corso 10 (2025-10-18) num=3 (2nd online)
('2025-10-18 18:00:00','presenza', NULL, 10),('2025-11-15 18:00:00','online',' Teoria Gelato Artigianale', 10),('2025-12-13 18:00:00','presenza', NULL, 10),
-- Corso 11 (2025-11-02) num=2 (2nd online)
('2025-11-02 18:00:00','presenza', NULL, 11),('2025-11-30 18:00:00','online',' Teoria Cocktail e Mixology', 11),
-- Corso 12 (2025-11-20) num=4 (2nd online)
('2025-11-20 18:00:00','presenza', NULL, 12),('2025-11-27 18:00:00','online',' Teoria Cucina Senza Glutine', 12),('2025-12-04 18:00:00','presenza', NULL, 12),('2025-12-11 18:00:00','presenza', NULL, 12),
-- Corso 13 (2025-12-05) num=2 (2nd online)
('2025-12-05 18:00:00','presenza', NULL, 13),('2026-01-02 18:00:00','online',' Teoria Cucina per Bambini', 13),
-- Corso 14 (2025-12-18) num=3 (2nd online)
('2025-12-18 18:00:00','presenza', NULL, 14),('2026-01-15 18:00:00','online',' Teoria Cucina per Sportivi', 14),('2026-02-12 18:00:00','presenza', NULL, 14),
-- Corso 15 (2026-01-10) num=2 (2nd online)
('2026-01-10 18:00:00','presenza', NULL, 15),('2026-02-07 18:00:00','online',' Teoria Conservazione Alimenti', 15),
-- Corso 16 (2026-01-25) num=4 (2nd online)
('2026-01-25 18:00:00','presenza', NULL, 16),('2026-02-08 18:00:00','online',' Teoria Cioccolateria', 16),('2026-02-22 18:00:00','presenza', NULL, 16),('2026-03-08 18:00:00','presenza', NULL, 16),
-- Corso 17 (2026-02-08) num=5 (2nd online)
('2026-02-08 18:00:00','presenza', NULL, 17),('2026-03-07 18:00:00','online',' Teoria Cucina Internazionale Avanzata', 17),('2026-04-04 18:00:00','presenza', NULL, 17),('2026-05-02 18:00:00','presenza', NULL, 17),('2026-05-30 18:00:00','presenza', NULL, 17),
-- Corso 18 (2026-02-22) num=3 (2nd online)
('2026-02-22 18:00:00','presenza', NULL, 18),('2026-03-22 18:00:00','online',' Teoria Cucina Etica e Sostenibile', 18),('2026-04-19 18:00:00','presenza', NULL, 18),
-- Corso 19 (2026-03-07) num=3 (2nd online)
('2026-03-07 18:00:00','presenza', NULL, 19),('2026-04-04 18:00:00','online',' Teoria Fermentazioni', 19),('2026-05-02 18:00:00','presenza', NULL, 19),
-- Corso 20 (2026-03-21) num=4 (2nd online)
('2026-03-21 18:00:00','presenza', NULL, 20),('2026-04-04 18:00:00','online',' Teoria Street Food Italiano', 20),('2026-04-18 18:00:00','presenza', NULL, 20),('2026-05-02 18:00:00','presenza', NULL, 20),
-- Corso 21 (2026-04-05) num=4 (2nd online)
('2026-04-05 18:00:00','presenza', NULL, 21),('2026-05-03 18:00:00','online',' Teoria Cucina Francese', 21),('2026-05-31 18:00:00','presenza', NULL, 21),('2026-06-28 18:00:00','presenza', NULL, 21),
-- Corso 22 (2026-04-20) num=3 (2nd online)
('2026-04-20 18:00:00','presenza', NULL, 22),('2026-05-18 18:00:00','online',' Teoria Cucina Spagnola', 22),('2026-06-15 18:00:00','presenza', NULL, 22),
-- Corso 23 (2026-05-10) num=4 (2nd online)
('2026-05-10 18:00:00','presenza', NULL, 23),('2026-06-07 18:00:00','online',' Teoria Cucina Giapponese', 23),('2026-07-05 18:00:00','presenza', NULL, 23),('2026-08-02 18:00:00','presenza', NULL, 23),
-- Corso 24 (2026-06-01) num=3 (2nd online)
('2026-06-01 18:00:00','presenza', NULL, 24),('2026-06-29 18:00:00','online',' Teoria Cucina Nordica', 24),('2026-07-27 18:00:00','presenza', NULL, 24),
-- Corso 25 (2026-06-15) num=5 (2nd online)
('2026-06-15 18:00:00','presenza', NULL, 25),('2026-06-29 18:00:00','online',' Teoria Tecniche Avanzate di Lievitazione', 25),('2026-07-13 18:00:00','presenza', NULL, 25),('2026-07-27 18:00:00','presenza', NULL, 25),('2026-08-10 18:00:00','presenza', NULL, 25),
-- Corso 26 (2026-06-29) num=4 (2nd online)
('2026-06-29 18:00:00','presenza', NULL, 26),('2026-07-06 18:00:00','online',' Teoria Cucina Vegetariana Creativa', 26),('2026-07-13 18:00:00','presenza', NULL, 26),('2026-07-20 18:00:00','presenza', NULL, 26),
-- Corso 27 (2026-07-12) num=2 (no second online because id>24)
('2026-07-12 18:00:00','presenza', NULL, 27),('2026-08-09 18:00:00','presenza', NULL, 27);


-- Ricetta collegata alla Sessione
INSERT INTO Ricetta (nome, descrizione, tempo) 
VALUES
('Risotto alla Milanese', 'Classico giallo', '00:45:00'),
('Pizza Margherita', 'La regina delle pizze', '01:30:00'),
('Tiramisù', 'Dolce al cucchiaio con caffè e mascarpone', '00:30:00'), 
('Insalata di Quinoa e Verdure', 'Piatto vegano fresco e nutriente', '00:20:00'),
('Spaghetti alla Carbonara', 'Uova, pecorino e guanciale', '00:25:00'),
('Lasagne alla Bolognese', 'Strati di pasta, ragù e besciamella', '01:20:00'),
('Gnocchi al Pesto', 'Gnocchi di patate con pesto genovese', '00:30:00'),
('Pollo al Curry', 'Curry aromatico con latte di cocco', '00:40:00'),
('Zuppa di Legumi', 'Misto di legumi stufati', '01:00:00'),
('Carbonara Vegetariana', 'Versione senza carne con funghi', '00:30:00'),
('Ravioli di Ricotta', 'Ravioli fatti in casa con ricotta e spinaci', '01:10:00'),
('Parmigiana di Melanzane', 'Melanzane, pomodoro e formaggio', '01:00:00'),
('Baccalà alla Vicentina', 'Baccalà stufato con polenta', '02:00:00'),
('Spezzatino di Vitello', 'Brasato con vino rosso e aromi', '01:40:00'),
('Caponata Siciliana', 'Melanzane agrodolci con pomodoro', '00:50:00'),
('Pasta al Pesto di Rucola', 'Salsa a base di rucola e mandorle', '00:25:00'),
('Zuppa di Pesce', 'Misto di pesce in brodetto saporito', '01:10:00'),
('Risotto ai Funghi', 'Cremoso con funghi porcini', '00:45:00'),
('Tortellini in Brodo', 'Tortellini tradizionali in brodo', '00:40:00'),
('Insalata di Farro', 'Farro con verdure e pomodorini', '00:20:00'),
('Crema Catalana', 'Dessert alla crema con caramello', '00:35:00'),
('Pancake alla Banana', 'Pancake soffici con banana', '00:20:00'),
('Brioche al Burro', 'Brioche morbide fatte in casa', '02:30:00'),
('Mousse al Cioccolato', 'Dessert al cioccolato fondente', '00:25:00'),
('Couscous alle Verdure', 'Couscous con verdure speziate', '00:35:00'),
('Pad Thai', 'Noodles thailandesi con gamberi', '00:30:00'),
('Chicken Tikka Masala', 'Pollo speziato in salsa cremosa', '00:50:00'),
('Falafel e Hummus', 'Polpette di ceci con hummus', '00:45:00'),
('Gulasch Ungherese', 'Stufato speziato di manzo', '01:50:00'),
('Risotto al Nero di Seppia', 'Risotto saporito al nero', '00:50:00'),
('Tacos di Pesce', 'Tacos con pesce fritto e insalata', '00:30:00'),
('Paella Valenciana', 'Paella con frutti di mare e pollo', '01:20:00'),
('Confit dAnatra', 'Cosce d''anatra confit croccanti', '02:30:00'),
('Ratatouille', 'Stufato di verdure alla provenzale', '00:50:00'),
('Burger Vegano', 'Burger a base di legumi e cereali', '00:35:00'),
('Sushi Misto', 'Nigiri e maki assortiti', '01:10:00'),
('Poke Bowl', 'Poke con riso e pesce crudo', '00:25:00'),
('Crepes Dolci', 'Crepes morbide con ripieno a scelta', '00:30:00'),
('Brownies al Cioccolato', 'Brownies ricchi e umidi', '00:35:00'),
('Frittata di Asparagi', 'Frittata primaverile con asparagi', '00:25:00'),
('Hummus di Barbabietola', 'Hummus colorato con barbabietola', '00:20:00'),
('Curry di Lenticchie', 'Lenticchie speziate al curry', '00:40:00'),
('Pasta alla Norma', 'Pasta con melanzane e ricotta salata', '00:30:00'),
('Filetto al Pepe Verde', 'Filetto di manzo con salsa al pepe', '00:45:00'),
('Insalata di Polpo', 'Polpo tenero con patate e prezzemolo', '00:50:00'),
('Zabaione', 'Dessert alcolico con uova e zucchero', '00:15:00'),
('Bruschette Miste', 'Bruschette con vari topping', '00:15:00'),
('Pasta e Fagioli', 'Zuppa rustica di pasta e fagioli', '01:00:00'),
('Crostata di Frutta', 'Pasta frolla con crema e frutta fresca', '01:20:00'),
('Gnocchi alla Sorrentina', 'Gnocchi al pomodoro e mozzarella', '00:40:00'),
('Arancini di Riso', 'Riso ripieno impanato e fritto', '01:00:00'),
('Polpette al Sugo', 'Polpette di carne in salsa di pomodoro', '00:50:00'),
('Sformato di Patate', 'Sformato cremoso di patate e formaggi', '01:10:00'),
('Insalata Caprese', 'Mozzarella, pomodoro e basilico', '00:15:00'),
('Coda alla Vaccinara', 'Stufato di coda di manzo', '02:30:00'),
('Focaccia Genovese', 'Focaccia alta con olio e sale grosso', '02:00:00'),
('Ragù alla Bolognese', 'Classico ragù di carne per pasta', '02:00:00'),
('Zuppa di Zucca', 'Vellutata di zucca con crostini', '00:35:00'),
('Risotto al Limone', 'Risotto cremoso con scorza di limone', '00:40:00'),
('Salmone al Forno', 'Filetto di salmone al forno con erbe', '00:35:00'),
('Torta Salata agli Spinaci', 'Pasta sfoglia ripiena di spinaci e ricotta', '01:00:00'),
('Marmellata Casalinga', 'Marmellata di frutta mista', '02:00:00'),
('Pollo Arrosto', 'Pollo intero arrosto con rosmarino', '01:30:00'),
('Ravioli di Zucca', 'Ravioli dolci-salati con zucca e amaretti', '01:20:00'),
('Insalata di Mare', 'Insalata fresca di mare con limone', '00:30:00'),
('Torta al Limone', 'Torta soffice al limone', '01:10:00'),
('Caponata Estiva', 'Verdure grigliate con pesto leggero', '00:40:00'),
('Polenta con Funghi', 'Polenta morbida con funghi trifolati', '01:00:00'),
('Crespelle ai Funghi', 'Crespelle ripiene di funghi e besciamella', '00:50:00'),
('Pasta al Pesto di Pistacchi', 'Pasta cremosa con pesto di pistacchi', '00:30:00'),
('Sformato di Zucchine', 'Zucchine gratinate con parmigiano', '00:45:00'),
('Tiramisù al Limoncello', 'Versione aromatizzata al limoncello', '00:45:00'),
('Gelato alla Stracciatella', 'Gelato artigianale con cioccolato', '02:00:00'),
('Baccala Mantecato', 'Baccalà cremoso spalmabile', '01:10:00'),
('Insalata Greca', 'Insalata con feta, olive e cetrioli', '00:20:00'),
('Riso Venere con Verdure', 'Riso venere saltato con verdure', '00:35:00'),
('Sfogliata alle Mele', 'Sfoglia ripiena di mele e cannella', '01:15:00'),
('Torta al Cioccolato', 'Torta ricca al cioccolato fondente', '01:30:00'),
('Insalata Mista', 'Insalata mista con vinaigrette', '00:15:00');

-- Collegamento M:N Sessione <-> Ricetta (Prepara)
-- Associo tutte le ricette alle prime sessioni di tipo 'presenza' (una per ricetta)
-- Collegamento M:N Sessione <-> Ricetta (Prepara)
INSERT INTO Prepara (id_sessione, id_ricetta) VALUES
(1,1),(1,69),(2,2),(2,70),(6,50),(4,3),(4,71),(6,51),(6,4),(6,72),(7,5),(7,73),(7,52),(9,6),(9,74),(10,7),
(10,75),(11,8),(11,76),(11,53),(13,9),(13,77),(14,10),(14,78),(14,54),(16,11),(16,79),(17,12),(18,13),(18,55),(20,14),(21,15),
(22,16),(23,17),(23,56),(25,18),(26,19),(27,20),(27,57),(29,21),(30,22),(31,23),(32,24),(33,25),(33,58),(35,26),(36,27),(36,59),
(38,28),(38,60),(40,29),(41,30),(42,31),(42,61),(44,32),(44,62),(46,33),(47,34),(47,63),(49,35),(49,64),(51,36),(52,37),(53,38),
(53,65),(55,39),(56,40),(57,41),(58,42),(58,66),(60,43),(61,44),(61,67),(63,45),(64,46),(64,68),(66,47),(67,48),(68,49);

-- Dettaglio Ricetta (Dosi per 1 persona)
-- Raggruppo le righe per `id_ricetta`: tutte le referenze della stessa ricetta sono su una riga
INSERT INTO Richiede (id_ricetta, nome_ingrediente, quantita_necessaria) VALUES
-- 1 Risotto alla Milanese
(1, 'Riso Carnaroli', 0.08), (1, 'Zafferano', 0.1), (1, 'Burro', 0.02),
-- 2 Pizza Margherita
(2, 'Farina', 0.25), (2, 'Mozzarella', 0.15), (2, 'Pomodori', 0.12), (2, 'Olio d''Oliva', 0.02),
-- 3 Tiramisù
(3, 'Uova', 0.12), (3, 'Zucchero', 0.08), (3, 'Mascarpone', 0.15),
-- 4 Insalata di Quinoa e Verdure
(4, 'Verdure', 0.2), (4, 'Olio d''Oliva', 0.01),
-- 5 Spaghetti alla Carbonara
(5, 'Spaghetti', 0.1), (5, 'Uova', 0.1), (5, 'Pecorino', 0.05), (5, 'Speck', 0.05),
-- 6 Lasagne alla Bolognese
(6, 'Lasagne', 0.15), (6, 'Ragù alla Bolognese', 0.2), (6, 'Besciamella', 0.1),
-- 7 Gnocchi al Pesto
(7, 'Gnocchi', 0.18), (7, 'Basilico', 0.02), (7, 'Parmigiano', 0.02),
-- 8 Pollo al Curry
(8, 'Pollo', 0.2), (8, 'Curry', 0.01), (8, 'Latte di Cocco', 0.05),
-- 9 Zuppa di Legumi
(9, 'Legumi', 0.15), (9, 'Brodo', 0.3),
-- 10 Carbonara Vegetariana
(10, 'Spaghetti', 0.1), (10, 'Uova', 0.08), (10, 'Funghi', 0.08),
-- 11 Ravioli di Ricotta
(11, 'Farina', 0.18), (11, 'Ricotta', 0.12), (11, 'Spinaci', 0.05),
-- 12 Parmigiana di Melanzane
(12, 'Melanzane', 0.3), (12, 'Pomodori', 0.15), (12, 'Parmigiano', 0.05),
-- 13 Baccalà alla Vicentina
(13, 'Baccalà', 0.25), (13, 'Brodo', 0.2),
-- 14 Spezzatino di Vitello
(14, 'Carne macinata', 0.22), (14, 'Vino Bianco', 0.02),
-- 15 Caponata Siciliana
(15, 'Melanzane', 0.25), (15, 'Pomodori', 0.1), (15, 'Aceto di Vino', 0.02),
-- 16 Pasta al Pesto di Rucola
(16, 'Pasta', 0.1), (16, 'Rucola', 0.02), (16, 'Parmigiano', 0.02),
-- 17 Zuppa di Pesce
(17, 'Pesce misto', 0.25), (17, 'Brodo', 0.3),
-- 18 Risotto ai Funghi
(18, 'Riso Carnaroli', 0.08), (18, 'Funghi', 0.12), (18, 'Burro', 0.02),
-- 19 Tortellini in Brodo
(19, 'Tortellini', 0.12), (19, 'Brodo', 0.3),
-- 20 Insalata di Farro
(20, 'Farro', 0.12), (20, 'Verdure', 0.08),
-- 21 Crema Catalana
(21, 'Latte', 0.15), (21, 'Zucchero', 0.05),
-- 22 Pancake alla Banana
(22, 'Farina', 0.12), (22, 'Banana', 0.15), (22, 'Latte', 0.05),
-- 23 Brioche al Burro
(23, 'Farina', 0.2), (23, 'Burro', 0.06), (23, 'Lievito', 0.005),
-- 24 Mousse al Cioccolato
(24, 'Cioccolato Fondente', 0.08), (24, 'Panna', 0.05),
-- 25 Couscous alle Verdure
(25, 'Couscous', 0.12), (25, 'Verdure', 0.15),
-- 26 Pad Thai
(26, 'Noodles', 0.12), (26, 'Gamberi', 0.12), (26, 'Salsa di Soia', 0.01),
-- 27 Chicken Tikka Masala
(27, 'Pollo', 0.2), (27, 'Spezie', 0.02), (27, 'Panna', 0.05),
-- 28 Falafel e Hummus
(28, 'Ceci', 0.12), (28, 'Tahina', 0.03),
-- 29 Gulasch Ungherese
(29, 'Carne macinata', 0.25), (29, 'Paprika', 0.01),
-- 30 Risotto al Nero di Seppia
(30, 'Riso Carnaroli', 0.08), (30, 'Nero di Seppia', 0.02),
-- 31 Tacos di Pesce
(31, 'Pesce', 0.15), (31, 'Pane', 0.05),
-- 32 Paella Valenciana
(32, 'Riso Carnaroli', 0.12), (32, 'Frutti di Mare', 0.2),
-- 33 Confit d'Anatra
(33, 'Confit dAnatra', 0.25), (33, 'Olio d''Oliva', 0.02),
-- 34 Ratatouille
(34, 'Verdure', 0.25), (34, 'Origano', 0.005),
-- 35 Burger Vegano
(35, 'Legumi', 0.15), (35, 'Pane', 0.05),
-- 36 Sushi Misto
(36, 'Riso Carnaroli', 0.12), (36, 'Salmone', 0.08),
-- 37 Poke Bowl
(37, 'Riso Carnaroli', 0.12), (37, 'Pesce crudo', 0.12),
-- 38 Crepes Dolci
(38, 'Farina', 0.12), (38, 'Uova', 0.05), (38, 'Latte', 0.05),
-- 39 Brownies al Cioccolato
(39, 'Cioccolato Fondente', 0.08), (39, 'Farina', 0.08), (39, 'Zucchero', 0.06),
-- 40 Frittata di Asparagi
(40, 'Uova', 0.12), (40, 'Asparagi', 0.1),
-- 41 Hummus di Barbabietola
(41, 'Ceci', 0.12), (41, 'Barbabietola', 0.08),
-- 42 Curry di Lenticchie
(42, 'Lenticchie', 0.15), (42, 'Curry', 0.01),
-- 43 Pasta alla Norma
(43, 'Pasta', 0.12), (43, 'Melanzane', 0.12), (43, 'Ricotta', 0.03),
-- 44 Filetto al Pepe Verde
(44, 'Filetto', 0.22), (44, 'Pepe Nero', 0.005),
-- 45 Insalata di Polpo
(45, 'Polpo', 0.2), (45, 'Patate', 0.12),
-- 46 Zabaione
(46, 'Uova', 0.08), (46, 'Zucchero', 0.04),
-- 47 Bruschette Miste
(47, 'Pane', 0.06), (47, 'Pomodori', 0.05),
-- 48 Pasta e Fagioli
(48, 'Pasta', 0.12), (48, 'Fagioli', 0.15),
-- 49 Crostata di Frutta
(49, 'Farina', 0.18), (49, 'Zucchero a Velo', 0.04), (49, 'Frutta', 0.2),
-- 50 Gnocchi alla Sorrentina
(50, 'Gnocchi', 0.18), (50, 'Pomodori', 0.12), (50, 'Mozzarella', 0.1),
-- 51 Arancini di Riso
(51, 'Riso Carnaroli', 0.18), (51, 'Pomodori', 0.05), (51, 'Pane', 0.05),
-- 52 Polpette al Sugo
(52, 'Carne macinata', 0.2), (52, 'Pane', 0.05), (52, 'Pomodori', 0.12),
-- 53 Sformato di Patate
(53, 'Patate', 0.25), (53, 'Formaggio', 0.05),
-- 54 Insalata Caprese
(54, 'Mozzarella', 0.12), (54, 'Pomodori', 0.12), (54, 'Basilico', 0.01),
-- 55 Coda alla Vaccinara
(55, 'Carne macinata', 0.3), (55, 'Pomodori', 0.12),
-- 56 Focaccia Genovese
(56, 'Farina', 0.25), (56, 'Olio d''Oliva', 0.03),
-- 57 Ragù alla Bolognese
(57, 'Carne macinata', 0.25), (57, 'Pomodori', 0.12), (57, 'Vino Bianco', 0.02),
-- 58 Zuppa di Zucca
(58, 'Zucca', 0.25), (58, 'Brodo', 0.3),
-- 59 Risotto al Limone
(59, 'Riso Carnaroli', 0.08), (59, 'Limone', 0.01),
-- 60 Salmone al Forno
(60, 'Salmone', 0.2), (60, 'Limone', 0.01),
-- 61 Torta Salata agli Spinaci
(61, 'Spinaci', 0.2), (61, 'Ricotta', 0.08),
-- 62 Marmellata Casalinga
(62, 'Frutta', 0.5), (62, 'Zucchero', 0.25),
-- 63 Pollo Arrosto
(63, 'Pollo', 0.6), (63, 'Rosmarino', 0.01),
-- 64 Ravioli di Zucca
(64, 'Farina', 0.18), (64, 'Zucca', 0.2), (64, 'Amaretti', 0.02),
-- 65 Insalata di Mare
(65, 'Pesce misto', 0.25), (65, 'Limone', 0.01),
-- 66 Torta al Limone
(66, 'Farina', 0.18), (66, 'Limone', 0.02), (66, 'Zucchero', 0.12),
-- 67 Caponata Estiva
(67, 'Verdure', 0.25), (67, 'Olio d''Oliva', 0.02),
-- 68 Polenta con Funghi
(68, 'Farina di Mais', 0.15), (68, 'Funghi', 0.12),
-- 69 Crespelle ai Funghi
(69, 'Farina', 0.12), (69, 'Funghi', 0.12), (69, 'Besciamella', 0.05),
-- 70 Pasta al Pesto di Pistacchi
(70, 'Pasta', 0.12), (70, 'Pistacchi', 0.03), (70, 'Parmigiano', 0.02),
-- 71 Sformato di Zucchine
(71, 'Zucchine', 0.25), (71, 'Parmigiano', 0.02),
-- 72 Tiramisù al Limoncello
(72, 'Uova', 0.1), (72, 'Zucchero', 0.06), (72, 'Mascarpone', 0.15),
-- 73 Gelato alla Stracciatella
(73, 'Latte', 0.2), (73, 'Cioccolato Fondente', 0.05),
-- 74 Baccalà Mantecato
(74, 'Baccalà', 0.18), (74, 'Olio d''Oliva', 0.02),
-- 75 Insalata Greca
(75, 'Feta', 0.08), (75, 'Olive', 0.02), (75, 'Cetrioli', 0.08),
-- 76 Riso Venere con Verdure
(76, 'Riso Carnaroli', 0.12), (76, 'Verdure', 0.12),
-- 77 Sfogliata alle Mele
(77, 'Farina', 0.18), (77, 'Mele', 0.25), (77, 'Zucchero', 0.08),
-- 78 Torta al Cioccolato (ingredienti di esempio)
(78, 'Farina', 0.18), (78, 'Zucchero', 0.12), (78, 'Cioccolato Fondente', 0.15),
-- 79 Insalata Mista (ingredienti di esempio)
(79, 'Insalata mista', 0.15), (79, 'Olio d''Oliva', 0.01), (79, 'Sale', 0.005);

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
-- Additional students (80)
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud010@unina.it', 'Studente', 'Test', 'Studente10!', 'N86010', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud011@unina.it', 'Studente', 'Test', 'Studente11!', 'N86011', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud012@unina.it', 'Studente', 'Test', 'Studente12!', 'N86012', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud013@unina.it', 'Studente', 'Test', 'Studente13!', 'N86013', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud014@unina.it', 'Studente', 'Test', 'Studente14!', 'N86014', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud015@unina.it', 'Studente', 'Test', 'Studente15!', 'N86015', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud016@unina.it', 'Studente', 'Test', 'Studente16!', 'N86016', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud017@unina.it', 'Studente', 'Test', 'Studente17!', 'N86017', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud018@unina.it', 'Studente', 'Test', 'Studente18!', 'N86018', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud019@unina.it', 'Studente', 'Test', 'Studente19!', 'N86019', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud020@unina.it', 'Studente', 'Test', 'Studente20!', 'N86020', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud021@unina.it', 'Studente', 'Test', 'Studente21!', 'N86021', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud022@unina.it', 'Studente', 'Test', 'Studente22!', 'N86022', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud023@unina.it', 'Studente', 'Test', 'Studente23!', 'N86023', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud024@unina.it', 'Studente', 'Test', 'Studente24!', 'N86024', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud025@unina.it', 'Studente', 'Test', 'Studente25!', 'N86025', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud026@unina.it', 'Studente', 'Test', 'Studente26!', 'N86026', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud027@unina.it', 'Studente', 'Test', 'Studente27!', 'N86027', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud028@unina.it', 'Studente', 'Test', 'Studente28!', 'N86028', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud029@unina.it', 'Studente', 'Test', 'Studente29!', 'N86029', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud030@unina.it', 'Studente', 'Test', 'Studente30!', 'N86030', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud031@unina.it', 'Studente', 'Test', 'Studente31!', 'N86031', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud032@unina.it', 'Studente', 'Test', 'Studente32!', 'N86032', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud033@unina.it', 'Studente', 'Test', 'Studente33!', 'N86033', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud034@unina.it', 'Studente', 'Test', 'Studente34!', 'N86034', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud035@unina.it', 'Studente', 'Test', 'Studente35!', 'N86035', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud036@unina.it', 'Studente', 'Test', 'Studente36!', 'N86036', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud037@unina.it', 'Studente', 'Test', 'Studente37!', 'N86037', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud038@unina.it', 'Studente', 'Test', 'Studente38!', 'N86038', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud039@unina.it', 'Studente', 'Test', 'Studente39!', 'N86039', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud040@unina.it', 'Studente', 'Test', 'Studente40!', 'N86040', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud041@unina.it', 'Studente', 'Test', 'Studente41!', 'N86041', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud042@unina.it', 'Studente', 'Test', 'Studente42!', 'N86042', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud043@unina.it', 'Studente', 'Test', 'Studente43!', 'N86043', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud044@unina.it', 'Studente', 'Test', 'Studente44!', 'N86044', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud045@unina.it', 'Studente', 'Test', 'Studente45!', 'N86045', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud046@unina.it', 'Studente', 'Test', 'Studente46!', 'N86046', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud047@unina.it', 'Studente', 'Test', 'Studente47!', 'N86047', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud048@unina.it', 'Studente', 'Test', 'Studente48!', 'N86048', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud049@unina.it', 'Studente', 'Test', 'Studente49!', 'N86049', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud050@unina.it', 'Studente', 'Test', 'Studente50!', 'N86050', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud051@unina.it', 'Studente', 'Test', 'Studente51!', 'N86051', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud052@unina.it', 'Studente', 'Test', 'Studente52!', 'N86052', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud053@unina.it', 'Studente', 'Test', 'Studente53!', 'N86053', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud054@unina.it', 'Studente', 'Test', 'Studente54!', 'N86054', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud055@unina.it', 'Studente', 'Test', 'Studente55!', 'N86055', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud056@unina.it', 'Studente', 'Test', 'Studente56!', 'N86056', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud057@unina.it', 'Studente', 'Test', 'Studente57!', 'N86057', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud058@unina.it', 'Studente', 'Test', 'Studente58!', 'N86058', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud059@unina.it', 'Studente', 'Test', 'Studente59!', 'N86059', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud060@unina.it', 'Studente', 'Test', 'Studente60!', 'N86060', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud061@unina.it', 'Studente', 'Test', 'Studente61!', 'N86061', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud062@unina.it', 'Studente', 'Test', 'Studente62!', 'N86062', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud063@unina.it', 'Studente', 'Test', 'Studente63!', 'N86063', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud064@unina.it', 'Studente', 'Test', 'Studente64!', 'N86064', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud065@unina.it', 'Studente', 'Test', 'Studente65!', 'N86065', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud066@unina.it', 'Studente', 'Test', 'Studente66!', 'N86066', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud067@unina.it', 'Studente', 'Test', 'Studente67!', 'N86067', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud068@unina.it', 'Studente', 'Test', 'Studente68!', 'N86068', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud069@unina.it', 'Studente', 'Test', 'Studente69!', 'N86069', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud070@unina.it', 'Studente', 'Test', 'Studente70!', 'N86070', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud071@unina.it', 'Studente', 'Test', 'Studente71!', 'N86071', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud072@unina.it', 'Studente', 'Test', 'Studente72!', 'N86072', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud073@unina.it', 'Studente', 'Test', 'Studente73!', 'N86073', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud074@unina.it', 'Studente', 'Test', 'Studente74!', 'N86074', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud075@unina.it', 'Studente', 'Test', 'Studente75!', 'N86075', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud076@unina.it', 'Studente', 'Test', 'Studente76!', 'N86076', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud077@unina.it', 'Studente', 'Test', 'Studente77!', 'N86077', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud078@unina.it', 'Studente', 'Test', 'Studente78!', 'N86078', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud079@unina.it', 'Studente', 'Test', 'Studente79!', 'N86079', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud080@unina.it', 'Studente', 'Test', 'Studente80!', 'N86080', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud081@unina.it', 'Studente', 'Test', 'Studente81!', 'N86081', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud082@unina.it', 'Studente', 'Test', 'Studente82!', 'N86082', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud083@unina.it', 'Studente', 'Test', 'Studente83!', 'N86083', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud084@unina.it', 'Studente', 'Test', 'Studente84!', 'N86084', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud085@unina.it', 'Studente', 'Test', 'Studente85!', 'N86085', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud086@unina.it', 'Studente', 'Test', 'Studente86!', 'N86086', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud087@unina.it', 'Studente', 'Test', 'Studente87!', 'N86087', 'studente');
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud088@unina.it', 'Studente', 'Test', 'Studente88!', 'N86088', 'studente');
 INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES ('stud089@unina.it', 'Studente', 'Test', 'Studente89!', 'N86089', 'studente');
-- Additional 100 students
INSERT INTO Utente (email, nome, cognome, password, matricola, tipo_utente) VALUES
('stud090@unina.it','Studente','Test','Studente90!','N86090','studente'),('stud091@unina.it','Studente','Test','Studente91!','N86091','studente'),
('stud092@unina.it','Studente','Test','Studente92!','N86092','studente'),('stud093@unina.it','Studente','Test','Studente93!','N86093','studente'),
('stud094@unina.it','Studente','Test','Studente94!','N86094','studente'),('stud095@unina.it','Studente','Test','Studente95!','N86095','studente'),
('stud096@unina.it','Studente','Test','Studente96!','N86096','studente'),('stud097@unina.it','Studente','Test','Studente97!','N86097','studente'),
('stud098@unina.it','Studente','Test','Studente98!','N86098','studente'),('stud099@unina.it','Studente','Test','Studente99!','N86099','studente'),
('stud100@unina.it','Studente','Test','Studente100!','N86100','studente'),('stud101@unina.it','Studente','Test','Studente101!','N86101','studente'),
('stud102@unina.it','Studente','Test','Studente102!','N86102','studente'),('stud103@unina.it','Studente','Test','Studente103!','N86103','studente'),
('stud104@unina.it','Studente','Test','Studente104!','N86104','studente'),('stud105@unina.it','Studente','Test','Studente105!','N86105','studente'),
('stud106@unina.it','Studente','Test','Studente106!','N86106','studente'),('stud107@unina.it','Studente','Test','Studente107!','N86107','studente'),
('stud108@unina.it','Studente','Test','Studente108!','N86108','studente'),('stud109@unina.it','Studente','Test','Studente109!','N86109','studente'),
('stud110@unina.it','Studente','Test','Studente110!','N86110','studente'),('stud111@unina.it','Studente','Test','Studente111!','N86111','studente'),
('stud112@unina.it','Studente','Test','Studente112!','N86112','studente'),('stud113@unina.it','Studente','Test','Studente113!','N86113','studente'),
('stud114@unina.it','Studente','Test','Studente114!','N86114','studente'),('stud115@unina.it','Studente','Test','Studente115!','N86115','studente'),
('stud116@unina.it','Studente','Test','Studente116!','N86116','studente'),('stud117@unina.it','Studente','Test','Studente117!','N86117','studente'),
('stud118@unina.it','Studente','Test','Studente118!','N86118','studente'),('stud119@unina.it','Studente','Test','Studente119!','N86119','studente'),
('stud120@unina.it','Studente','Test','Studente120!','N86120','studente'),('stud121@unina.it','Studente','Test','Studente121!','N86121','studente'),
('stud122@unina.it','Studente','Test','Studente122!','N86122','studente'),('stud123@unina.it','Studente','Test','Studente123!','N86123','studente'),
('stud124@unina.it','Studente','Test','Studente124!','N86124','studente'),('stud125@unina.it','Studente','Test','Studente125!','N86125','studente'),
('stud126@unina.it','Studente','Test','Studente126!','N86126','studente'),('stud127@unina.it','Studente','Test','Studente127!','N86127','studente'),
('stud128@unina.it','Studente','Test','Studente128!','N86128','studente'),('stud129@unina.it','Studente','Test','Studente129!','N86129','studente'),
('stud130@unina.it','Studente','Test','Studente130!','N86130','studente'),('stud131@unina.it','Studente','Test','Studente131!','N86131','studente'),
('stud132@unina.it','Studente','Test','Studente132!','N86132','studente'),('stud133@unina.it','Studente','Test','Studente133!','N86133','studente'),
('stud134@unina.it','Studente','Test','Studente134!','N86134','studente'),('stud135@unina.it','Studente','Test','Studente135!','N86135','studente'),
('stud136@unina.it','Studente','Test','Studente136!','N86136','studente'),('stud137@unina.it','Studente','Test','Studente137!','N86137','studente'),
('stud138@unina.it','Studente','Test','Studente138!','N86138','studente'),('stud139@unina.it','Studente','Test','Studente139!','N86139','studente'),
('stud140@unina.it','Studente','Test','Studente140!','N86140','studente'),('stud141@unina.it','Studente','Test','Studente141!','N86141','studente'),
('stud142@unina.it','Studente','Test','Studente142!','N86142','studente'),('stud143@unina.it','Studente','Test','Studente143!','N86143','studente'),
('stud144@unina.it','Studente','Test','Studente144!','N86144','studente'),('stud145@unina.it','Studente','Test','Studente145!','N86145','studente'),
('stud146@unina.it','Studente','Test','Studente146!','N86146','studente'),('stud147@unina.it','Studente','Test','Studente147!','N86147','studente'),
('stud148@unina.it','Studente','Test','Studente148!','N86148','studente'),('stud149@unina.it','Studente','Test','Studente149!','N86149','studente'),
('stud150@unina.it','Studente','Test','Studente150!','N86150','studente'),('stud151@unina.it','Studente','Test','Studente151!','N86151','studente'),
('stud152@unina.it','Studente','Test','Studente152!','N86152','studente'),('stud153@unina.it','Studente','Test','Studente153!','N86153','studente'),
('stud154@unina.it','Studente','Test','Studente154!','N86154','studente'),('stud155@unina.it','Studente','Test','Studente155!','N86155','studente'),
('stud156@unina.it','Studente','Test','Studente156!','N86156','studente'),('stud157@unina.it','Studente','Test','Studente157!','N86157','studente'),
('stud158@unina.it','Studente','Test','Studente158!','N86158','studente'),('stud159@unina.it','Studente','Test','Studente159!','N86159','studente'),
('stud160@unina.it','Studente','Test','Studente160!','N86160','studente'),('stud161@unina.it','Studente','Test','Studente161!','N86161','studente'),
('stud162@unina.it','Studente','Test','Studente162!','N86162','studente'),('stud163@unina.it','Studente','Test','Studente163!','N86163','studente'),
('stud164@unina.it','Studente','Test','Studente164!','N86164','studente'),('stud165@unina.it','Studente','Test','Studente165!','N86165','studente'),
('stud166@unina.it','Studente','Test','Studente166!','N86166','studente'),('stud167@unina.it','Studente','Test','Studente167!','N86167','studente'),
('stud168@unina.it','Studente','Test','Studente168!','N86168','studente'),('stud169@unina.it','Studente','Test','Studente169!','N86169','studente'),
('stud170@unina.it','Studente','Test','Studente170!','N86170','studente'),('stud171@unina.it','Studente','Test','Studente171!','N86171','studente'),
('stud172@unina.it','Studente','Test','Studente172!','N86172','studente'),('stud173@unina.it','Studente','Test','Studente173!','N86173','studente'),
('stud174@unina.it','Studente','Test','Studente174!','N86174','studente'),('stud175@unina.it','Studente','Test','Studente175!','N86175','studente'),
('stud176@unina.it','Studente','Test','Studente176!','N86176','studente'),('stud177@unina.it','Studente','Test','Studente177!','N86177','studente'),
('stud178@unina.it','Studente','Test','Studente178!','N86178','studente'),('stud179@unina.it','Studente','Test','Studente179!','N86179','studente'),
('stud180@unina.it','Studente','Test','Studente180!','N86180','studente'),('stud181@unina.it','Studente','Test','Studente181!','N86181','studente'),
('stud182@unina.it','Studente','Test','Studente182!','N86182','studente'),('stud183@unina.it','Studente','Test','Studente183!','N86183','studente'),
('stud184@unina.it','Studente','Test','Studente184!','N86184','studente'),('stud185@unina.it','Studente','Test','Studente185!','N86185','studente'),
('stud186@unina.it','Studente','Test','Studente186!','N86186','studente'),('stud187@unina.it','Studente','Test','Studente187!','N86187','studente'),
('stud188@unina.it','Studente','Test','Studente188!','N86188','studente'),('stud189@unina.it','Studente','Test','Studente189!','N86189','studente');

-- Chef
INSERT INTO Utente (email, nome, cognome, password, tipo_utente) 
VALUES ('chef.cannav@email.it', 'Antonino', 'Cannavacciulo', 'ChefTop1!', 'chef');
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
-- chef.cannav@email.it : Antonino Cannavacciulo (chef)
INSERT INTO Gestisce (email_chef, id_corso) VALUES
('chef.cannav@email.it', 1),
('chef.cannav@email.it', 5),
('chef.cannav@email.it', 7),
('chef.cannav@email.it', 10),
('chef.cannav@email.it', 13),
('chef.cannav@email.it', 20),
('chef.cannav@email.it', 22),
('chef.cannav@email.it', 26);

-- chef.barb@email.it : Giorgio Barbieri (chef)
INSERT INTO Gestisce (email_chef, id_corso) VALUES
('chef.barb@email.it', 2),
('chef.barb@email.it', 6),
('chef.barb@email.it', 8),
('chef.barb@email.it', 9),
('chef.barb@email.it', 11),
('chef.barb@email.it', 14),
('chef.barb@email.it', 16),
('chef.barb@email.it', 19),
('chef.barb@email.it', 23),
('chef.barb@email.it', 27);

-- chef.dem@email.com : Luca Demi (chef)
INSERT INTO Gestisce (email_chef, id_corso) VALUES
('chef.dem@email.com', 3),
('chef.dem@email.com', 17),
('chef.dem@email.com', 18),
('chef.dem@email.com', 24);

-- chef.grn@unina.it : Gianni Rossi (chefStudente)
INSERT INTO Gestisce (email_chef, id_corso) VALUES
('chef.grn@unina.it', 2),
('chef.grn@unina.it', 25);

-- chef.apr@unina.it : Alessia Prato (chefStudente)
INSERT INTO Gestisce (email_chef, id_corso) VALUES
('chef.apr@unina.it', 3);

-- chef.drg@unina.it : Dario Grasso (chefStudente)
INSERT INTO Gestisce (email_chef, id_corso) VALUES
('chef.drg@unina.it', 4),
('chef.drg@unina.it', 12),
('chef.drg@unina.it', 15),
('chef.drg@unina.it', 21);

-- Notifiche inviate dai chef per i corsi gestiti
INSERT INTO Notifica (messaggio, email_chef, id_corso) VALUES
('Non dimenticate di rivedere le tecniche base viste oggi e di annotare i vostri dubbi per la prossima lezione.', 'chef.cannav@email.it', 1),
('Per la prossima lezione assicuratevi di avere tutti gli ingredienti freschi e di leggere le note teoriche in piattaforma.', 'chef.cannav@email.it', 5),
('Non dimenticate di esercitarvi sui tagli di base e sulle tecniche di mise en place viste oggi.', 'chef.cannav@email.it', 7),
('Non dimenticate di compilare il diario di bordo del corso segnando le difficolta incontrate.', 'chef.cannav@email.it', 10),
('Non dimenticate di rivedere l organizzazione della brigata di cucina e i ruoli assegnati.', 'chef.cannav@email.it', 13),
('Per il prossimo incontro controllate il materiale caricato in piattaforma e preparate eventuali domande sulle ricette.', 'chef.cannav@email.it', 20),
('Non dimenticate di verificare gli strumenti necessari come coltelli e stampi per la prossima lezione.', 'chef.cannav@email.it', 22),
('Per la prossima lezione rileggete le note su impasti e lievitazioni e annotate le principali criticita riscontrate.', 'chef.cannav@email.it', 26),

('Per il prossimo incontro ricordate di controllare la lista ingredienti e di portare il grembiule.', 'chef.barb@email.it', 2),
('Ricordate di controllare le intolleranze considerate nelle esercitazioni e di adattare le ricette di conseguenza.', 'chef.barb@email.it', 6),
('Per il prossimo incontro preparate una breve idea di menu da discutere insieme in aula.', 'chef.barb@email.it', 8),
('Ripassate le temperature di cottura corrette e fate attenzione ai tempi di riposo delle preparazioni.', 'chef.barb@email.it', 9),
('Per la prossima lezione controllate le schede tecniche delle ricette e rivedete le dosi per porzione.', 'chef.barb@email.it', 11),
('Per il prossimo incontro portate le vostre idee per un impiattamento creativo legato al tema del corso.', 'chef.barb@email.it', 14),
('Non dimenticate di controllare la dispensa di casa e verificare quali ingredienti potete riutilizzare nelle ricette del corso.', 'chef.barb@email.it', 16),
('Non dimenticate di rivedere le tecniche di cottura salutare e di pensare a una variante piu leggera di una ricetta classica.', 'chef.barb@email.it', 19),
('Per il prossimo incontro pensate a una ricetta che vi rappresenta e che si colleghi al tema del corso.', 'chef.barb@email.it', 23),
('Ricordate di ripassare tutte le tecniche affrontate finora perche nella prossima sessione le useremo insieme.', 'chef.barb@email.it', 27),

('Ripassate la ricetta mostrata a lezione e provate a rifarla a casa prima della prossima sessione.', 'chef.dem@email.com', 3),
('Per la prossima lezione rileggete le note sulla gestione dei tempi in cucina e sulle preparazioni anticipate.', 'chef.dem@email.com', 17),
('Ricordate di fare attenzione agli abbinamenti tra sapori e consistenze pensando a un esempio da condividere in aula.', 'chef.dem@email.com', 18),
('Ricordate di rivedere le tecniche di conservazione e di pensare a come riutilizzare gli avanzi in modo creativo.', 'chef.dem@email.com', 24),

('Per il prossimo incontro ricordate di controllare la lista ingredienti e di portare il grembiule.', 'chef.grn@unina.it', 2),
('Non dimenticate di esercitarvi sulle basi della pasticceria e di misurare con cura tutti gli ingredienti.', 'chef.grn@unina.it', 25),

('Ripassate la ricetta mostrata a lezione e provate a rifarla a casa prima della prossima sessione.', 'chef.apr@unina.it', 3),

('Non dimenticate di organizzare il piano di lavoro in cucina con attenzione ai tempi di cottura.', 'chef.drg@unina.it', 4),
('Ricordate di provare almeno una delle ricette a casa e annotate eventuali varianti sperimentate.', 'chef.drg@unina.it', 12),
('Ripassate le regole di sicurezza e igiene in cucina che saranno centrali nella prossima esercitazione.', 'chef.drg@unina.it', 15),
('Ripassate gli appunti sulle cotture multiple e sulla gestione del servizio che useremo nella prossima pratica.', 'chef.drg@unina.it', 21);

-- Iscrizioni al corso
-- Iscrizioni al corso (raggruppate per corso)
-- Corso 1
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('mario.std@unina.it', 1),('giacomo.std@unina.it', 1),('sara.std@unina.it', 1),('chiara.std@unina.it', 1),
('luca.std@unina.it', 1),('matilde.std@unina.it', 1),
('stud010@unina.it', 1),('stud011@unina.it', 1),('stud012@unina.it', 1),('stud013@unina.it', 1);

-- Corso 2
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud014@unina.it', 2),('stud015@unina.it', 2),('stud016@unina.it', 2),('stud017@unina.it', 2),
('stud018@unina.it', 2),('stud019@unina.it', 2),('stud020@unina.it', 2),('stud021@unina.it', 2),
('stud022@unina.it', 2),('stud023@unina.it', 2);

-- Corso 3
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud024@unina.it', 3),('stud025@unina.it', 3),('stud026@unina.it', 3),('stud027@unina.it', 3),
('stud028@unina.it', 3),('stud029@unina.it', 3),('stud030@unina.it', 3),('stud031@unina.it', 3),
('stud032@unina.it', 3),('stud033@unina.it', 3);

-- Corso 4
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud034@unina.it', 4),('stud035@unina.it', 4),('stud036@unina.it', 4),('stud037@unina.it', 4),
('stud038@unina.it', 4),('stud039@unina.it', 4),('stud040@unina.it', 4),('stud041@unina.it', 4),
('stud042@unina.it', 4),('stud043@unina.it', 4);

-- Corso 5
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud044@unina.it', 5),('stud045@unina.it', 5),('stud046@unina.it', 5),('stud047@unina.it', 5),
('stud048@unina.it', 5),('stud049@unina.it', 5),('stud050@unina.it', 5),('stud051@unina.it', 5),
('stud052@unina.it', 5),('stud053@unina.it', 5);

-- Corso 6
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud054@unina.it', 6),('stud055@unina.it', 6),('stud056@unina.it', 6),('stud057@unina.it', 6),
('stud058@unina.it', 6),('stud059@unina.it', 6),('stud060@unina.it', 6),('stud061@unina.it', 6),
('stud062@unina.it', 6),('stud063@unina.it', 6);

-- Corso 7
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud064@unina.it', 7),('stud065@unina.it', 7),('stud066@unina.it', 7),('stud067@unina.it', 7),
('stud068@unina.it', 7),('stud069@unina.it', 7),('stud070@unina.it', 7),('stud071@unina.it', 7),
('stud072@unina.it', 7),('stud073@unina.it', 7);

-- Corso 8
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud074@unina.it', 8),('stud075@unina.it', 8),('stud076@unina.it', 8),('stud077@unina.it', 8),
('stud078@unina.it', 8),('stud079@unina.it', 8),('stud080@unina.it', 8),('stud081@unina.it', 8);

INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud082@unina.it', 9),('stud083@unina.it', 9),('stud084@unina.it', 9),('stud085@unina.it', 9),
('stud086@unina.it', 9),('stud087@unina.it', 9),('stud088@unina.it', 9),('stud089@unina.it', 9);
-- Corso 10
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('mario.std@unina.it', 10),('giacomo.std@unina.it', 10),('sara.std@unina.it', 10),
('stud014@unina.it', 10),('stud015@unina.it', 10),('stud016@unina.it', 10),('stud017@unina.it', 10),('stud018@unina.it', 10);

-- Corso 11
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('chiara.std@unina.it', 11),('luca.std@unina.it', 11),
('stud019@unina.it', 11),('stud020@unina.it', 11),('stud021@unina.it', 11),('stud022@unina.it', 11),('stud023@unina.it', 11);

-- Corso 12
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('matilde.std@unina.it', 12),('stud024@unina.it', 12),('stud025@unina.it', 12),('stud026@unina.it', 12),('stud027@unina.it', 12),('stud028@unina.it', 12);

-- Corso 13
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud029@unina.it', 13),('stud030@unina.it', 13),('stud031@unina.it', 13),('stud032@unina.it', 13),('stud033@unina.it', 13),('stud034@unina.it', 13);

-- Corso 14
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud035@unina.it', 14),('stud036@unina.it', 14),('stud037@unina.it', 14),('stud038@unina.it', 14),('stud039@unina.it', 14),('stud040@unina.it', 14);

-- Corso 15
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud041@unina.it', 15),('stud042@unina.it', 15),('stud043@unina.it', 15),('stud044@unina.it', 15),('stud045@unina.it', 15),('stud046@unina.it', 15);

-- Corso 16
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud047@unina.it', 16),('stud048@unina.it', 16),('stud049@unina.it', 16),('stud050@unina.it', 16),('stud051@unina.it', 16),('stud052@unina.it', 16);

-- Corso 17
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud053@unina.it', 17),('stud054@unina.it', 17),('stud055@unina.it', 17),('stud056@unina.it', 17),('stud057@unina.it', 17),('stud058@unina.it', 17);

-- Corso 18
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud059@unina.it', 18),('stud060@unina.it', 18),('stud061@unina.it', 18),('stud062@unina.it', 18),('stud063@unina.it', 18),('stud064@unina.it', 18);

-- Corso 19
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud065@unina.it', 19),('stud066@unina.it', 19),('stud067@unina.it', 19),('stud068@unina.it', 19),('stud069@unina.it', 19),('stud070@unina.it', 19);

-- Corso 20
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud071@unina.it', 20),('stud072@unina.it', 20),('stud073@unina.it', 20),('stud074@unina.it', 20),('stud075@unina.it', 20),('stud076@unina.it', 20);

-- Corso 21
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud077@unina.it', 21),('stud078@unina.it', 21),('stud079@unina.it', 21),('stud080@unina.it', 21),('stud081@unina.it', 21),('stud082@unina.it', 21);

-- Corso 22
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud083@unina.it', 22),('stud084@unina.it', 22),('stud085@unina.it', 22),('stud086@unina.it', 22),('stud087@unina.it', 22),('stud088@unina.it', 22);

-- Corso 23 (mix di studenti su più corsi)
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud010@unina.it', 23),('stud020@unina.it', 23),('stud030@unina.it', 23),('stud040@unina.it', 23),('stud050@unina.it', 23),('stud060@unina.it', 23);

-- Corso 24
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud011@unina.it', 24),('stud021@unina.it', 24),('stud031@unina.it', 24),('stud041@unina.it', 24),('stud051@unina.it', 24),('stud061@unina.it', 24);

-- Corso 25
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud012@unina.it', 25),('stud022@unina.it', 25),('stud032@unina.it', 25),('stud042@unina.it', 25),('stud052@unina.it', 25),('stud062@unina.it', 25);

-- Corso 26
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud013@unina.it', 26),('stud023@unina.it', 26),('stud033@unina.it', 26),('stud043@unina.it', 26),('stud053@unina.it', 26),('stud063@unina.it', 26);

-- Corso 27
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('mario.std@unina.it', 27),('matilde.std@unina.it', 27),('stud070@unina.it', 27),('stud071@unina.it', 27),('stud072@unina.it', 27),('stud073@unina.it', 27);

-- Registrazione studenti aggiuntivi (stud090..stud189) raggruppati per corso
-- Distribuzione round-robin: ogni corso riceve 3 o 4 nuovi studenti
-- Corso 1
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud090@unina.it',1),('stud117@unina.it',1),('stud144@unina.it',1),('stud171@unina.it',1);

-- Corso 2
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud091@unina.it',2),('stud118@unina.it',2),('stud145@unina.it',2),('stud172@unina.it',2);

-- Corso 3
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud092@unina.it',3),('stud119@unina.it',3),('stud146@unina.it',3),('stud173@unina.it',3);

-- Corso 4
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud093@unina.it',4),('stud120@unina.it',4),('stud147@unina.it',4),('stud174@unina.it',4);

-- Corso 5
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud094@unina.it',5),('stud121@unina.it',5),('stud148@unina.it',5),('stud175@unina.it',5);

-- Corso 6
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud095@unina.it',6),('stud122@unina.it',6),('stud149@unina.it',6),('stud176@unina.it',6);

-- Corso 7
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud096@unina.it',7),('stud123@unina.it',7),('stud150@unina.it',7),('stud177@unina.it',7);

-- Corso 8
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud097@unina.it',8),('stud124@unina.it',8),('stud151@unina.it',8),('stud178@unina.it',8);

-- Corso 9
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud098@unina.it',9),('stud125@unina.it',9),('stud152@unina.it',9),('stud179@unina.it',9);

-- Corso 10
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud099@unina.it',10),('stud126@unina.it',10),('stud153@unina.it',10),('stud180@unina.it',10);

-- Corso 11
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud100@unina.it',11),('stud127@unina.it',11),('stud154@unina.it',11),('stud181@unina.it',11);

-- Corso 12
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud101@unina.it',12),('stud128@unina.it',12),('stud155@unina.it',12),('stud182@unina.it',12);

-- Corso 13
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud102@unina.it',13),('stud129@unina.it',13),('stud156@unina.it',13),('stud183@unina.it',13);

-- Corso 14
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud103@unina.it',14),('stud130@unina.it',14),('stud157@unina.it',14),('stud184@unina.it',14);

-- Corso 15
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud104@unina.it',15),('stud131@unina.it',15),('stud158@unina.it',15),('stud185@unina.it',15);

-- Corso 16
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud105@unina.it',16),('stud132@unina.it',16),('stud159@unina.it',16),('stud186@unina.it',16);

-- Corso 17
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud106@unina.it',17),('stud133@unina.it',17),('stud160@unina.it',17),('stud187@unina.it',17);

-- Corso 18
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud107@unina.it',18),('stud134@unina.it',18),('stud161@unina.it',18),('stud188@unina.it',18);

-- Corso 19
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud108@unina.it',19),('stud135@unina.it',19),('stud162@unina.it',19),('stud189@unina.it',19);

-- Corso 20
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud109@unina.it',20),('stud136@unina.it',20),('stud163@unina.it',20);

-- Corso 21
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud110@unina.it',21),('stud137@unina.it',21),('stud164@unina.it',21);

-- Corso 22
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud111@unina.it',22),('stud138@unina.it',22),('stud165@unina.it',22);

-- Corso 23
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud112@unina.it',23),('stud139@unina.it',23),('stud166@unina.it',23);

-- Corso 24
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud113@unina.it',24),('stud140@unina.it',24),('stud167@unina.it',24);

-- Corso 25
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud114@unina.it',25),('stud141@unina.it',25),('stud168@unina.it',25);

-- Corso 26
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud115@unina.it',26),('stud142@unina.it',26),('stud169@unina.it',26);

-- Corso 27
INSERT INTO Iscrizione (email_utente, id_corso) VALUES
('stud116@unina.it',27),('stud143@unina.it',27),('stud170@unina.it',27);


 -- Adesioni alle sessioni pratiche (solo "presenza")
-- Inserimenti manuali raggruppati per sessione (almeno 4 aderenti ciascuna)

-- Sessione 1 (corso 1)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('mario.std@unina.it',1),('giacomo.std@unina.it',1),('sara.std@unina.it',1),('chiara.std@unina.it',1);

-- Sessione 2 (corso 2)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud014@unina.it',2),('stud015@unina.it',2),('stud016@unina.it',2),('stud017@unina.it',2);

-- Sessione 4 (corso 3)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud024@unina.it',4),('stud025@unina.it',4),('stud026@unina.it',4),('stud027@unina.it',4);

-- Sessione 6 (corso 3)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud024@unina.it',6),('stud025@unina.it',6),('stud026@unina.it',6),('stud027@unina.it',6);

-- Sessione 7 (corso 4)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud034@unina.it',7),('stud035@unina.it',7),('stud036@unina.it',7),('stud037@unina.it',7);

-- Sessione 9 (corso 4)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud034@unina.it',9),('stud035@unina.it',9),('stud036@unina.it',9),('stud037@unina.it',9);

-- Sessione 10 (corso 4)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud034@unina.it',10),('stud035@unina.it',10),('stud036@unina.it',10),('stud037@unina.it',10);

-- Sessione 11 (corso 5)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud044@unina.it',11),('stud045@unina.it',11),('stud046@unina.it',11),('stud047@unina.it',11);

-- Sessione 13 (corso 5)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud044@unina.it',13),('stud045@unina.it',13),('stud046@unina.it',13),('stud047@unina.it',13);

-- Sessione 14 (corso 6)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud054@unina.it',14),('stud055@unina.it',14),('stud056@unina.it',14),('stud057@unina.it',14);

-- Sessione 16 (corso 6)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud054@unina.it',16),('stud055@unina.it',16),('stud056@unina.it',16),('stud057@unina.it',16);

-- Sessione 17 (corso 6)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud054@unina.it',17),('stud055@unina.it',17),('stud056@unina.it',17),('stud057@unina.it',17);

-- Sessione 18 (corso 7)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud064@unina.it',18),('stud065@unina.it',18),('stud066@unina.it',18),('stud067@unina.it',18);

-- Sessione 20 (corso 7)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud064@unina.it',20),('stud065@unina.it',20),('stud066@unina.it',20),('stud067@unina.it',20);

-- Sessione 21 (corso 7)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud064@unina.it',21),('stud065@unina.it',21),('stud066@unina.it',21),('stud067@unina.it',21);

-- Sessione 22 (corso 7)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud064@unina.it',22),('stud065@unina.it',22),('stud066@unina.it',22),('stud067@unina.it',22);

-- Sessione 23 (corso 8)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud074@unina.it',23),('stud075@unina.it',23),('stud076@unina.it',23),('stud077@unina.it',23);

-- Sessione 25 (corso 8)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud074@unina.it',25),('stud075@unina.it',25),('stud076@unina.it',25),('stud077@unina.it',25);

-- Sessione 26 (corso 8)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud074@unina.it',26),('stud075@unina.it',26),('stud076@unina.it',26),('stud077@unina.it',26);

-- Sessione 27 (corso 9)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud082@unina.it',27),('stud083@unina.it',27),('stud084@unina.it',27),('stud085@unina.it',27);

-- Sessione 29 (corso 9)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud082@unina.it',29),('stud083@unina.it',29),('stud084@unina.it',29),('stud085@unina.it',29);

-- Sessione 30 (corso 9)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud082@unina.it',30),('stud083@unina.it',30),('stud084@unina.it',30),('stud085@unina.it',30);

-- Sessione 31 (corso 9)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud082@unina.it',31),('stud083@unina.it',31),('stud084@unina.it',31),('stud085@unina.it',31);

-- Sessione 32 (corso 9)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud082@unina.it',32),('stud083@unina.it',32),('stud084@unina.it',32),('stud085@unina.it',32);

-- Sessione 33 (corso 10)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('mario.std@unina.it',33),('giacomo.std@unina.it',33),('sara.std@unina.it',33),('stud014@unina.it',33);

-- Sessione 35 (corso 10)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('mario.std@unina.it',35),('giacomo.std@unina.it',35),('sara.std@unina.it',35),('stud014@unina.it',35);

-- Sessione 36 (corso 11)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('chiara.std@unina.it',36),('luca.std@unina.it',36),('stud019@unina.it',36),('stud020@unina.it',36);

-- Sessione 38 (corso 12)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('matilde.std@unina.it',38),('stud024@unina.it',38),('stud025@unina.it',38),('stud026@unina.it',38);

-- Sessione 40 (corso 12)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('matilde.std@unina.it',40),('stud024@unina.it',40),('stud025@unina.it',40),('stud026@unina.it',40);

-- Sessione 41 (corso 12)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('matilde.std@unina.it',41),('stud024@unina.it',41),('stud025@unina.it',41),('stud026@unina.it',41);

-- Sessione 42 (corso 13)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud029@unina.it',42),('stud030@unina.it',42),('stud031@unina.it',42),('stud032@unina.it',42);

-- Sessione 44 (corso 14)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud035@unina.it',44),('stud036@unina.it',44),('stud037@unina.it',44),('stud038@unina.it',44);

-- Sessione 46 (corso 14)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud035@unina.it',46),('stud036@unina.it',46),('stud037@unina.it',46),('stud038@unina.it',46);

-- Sessione 47 (corso 15)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud041@unina.it',47),('stud042@unina.it',47),('stud043@unina.it',47),('stud044@unina.it',47);

-- Sessione 49 (corso 16)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud047@unina.it',49),('stud048@unina.it',49),('stud049@unina.it',49),('stud050@unina.it',49);

-- Sessione 51 (corso 16)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud047@unina.it',51),('stud048@unina.it',51),('stud049@unina.it',51),('stud050@unina.it',51);

-- Sessione 52 (corso 16)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud047@unina.it',52),('stud048@unina.it',52),('stud049@unina.it',52),('stud050@unina.it',52);

-- Sessione 53 (corso 17)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud053@unina.it',53),('stud054@unina.it',53),('stud055@unina.it',53),('stud056@unina.it',53);

-- Sessione 55 (corso 17)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud053@unina.it',55),('stud054@unina.it',55),('stud055@unina.it',55),('stud056@unina.it',55);

-- Sessione 56 (corso 17)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud053@unina.it',56),('stud054@unina.it',56),('stud055@unina.it',56),('stud056@unina.it',56);

-- Sessione 57 (corso 17)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud053@unina.it',57),('stud054@unina.it',57),('stud055@unina.it',57),('stud056@unina.it',57);

-- Sessione 58 (corso 18)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud059@unina.it',58),('stud060@unina.it',58),('stud061@unina.it',58),('stud062@unina.it',58);

-- Sessione 60 (corso 18)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud059@unina.it',60),('stud060@unina.it',60),('stud061@unina.it',60),('stud062@unina.it',60);

-- Sessione 61 (corso 19)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud065@unina.it',61),('stud066@unina.it',61),('stud067@unina.it',61),('stud068@unina.it',61);

-- Sessione 63 (corso 19)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud065@unina.it',63),('stud066@unina.it',63),('stud067@unina.it',63),('stud068@unina.it',63);

-- Sessione 64 (corso 20)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud071@unina.it',64),('stud072@unina.it',64),('stud073@unina.it',64),('stud074@unina.it',64);

-- Sessione 66 (corso 20)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud071@unina.it',66),('stud072@unina.it',66),('stud073@unina.it',66),('stud074@unina.it',66);

-- Sessione 67 (corso 20)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud071@unina.it',67),('stud072@unina.it',67),('stud073@unina.it',67),('stud074@unina.it',67);

-- Sessione 68 (corso 21)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud077@unina.it',68),('stud078@unina.it',68),('stud079@unina.it',68),('stud080@unina.it',68);

-- Sessione 70 (corso 21)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud077@unina.it',70),('stud078@unina.it',70),('stud079@unina.it',70),('stud080@unina.it',70);

-- Sessione 71 (corso 21)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud077@unina.it',71),('stud078@unina.it',71),('stud079@unina.it',71),('stud080@unina.it',71);

-- Sessione 72 (corso 22)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud083@unina.it',72),('stud084@unina.it',72),('stud085@unina.it',72),('stud086@unina.it',72);

-- Sessione 74 (corso 22)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud083@unina.it',74),('stud084@unina.it',74),('stud085@unina.it',74),('stud086@unina.it',74);

-- Sessione 75 (corso 23)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud010@unina.it',75),('stud020@unina.it',75),('stud030@unina.it',75),('stud040@unina.it',75);

-- Sessione 77 (corso 23)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud010@unina.it',77),('stud020@unina.it',77),('stud030@unina.it',77),('stud040@unina.it',77);

-- Sessione 78 (corso 23)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud010@unina.it',78),('stud020@unina.it',78),('stud030@unina.it',78),('stud040@unina.it',78);

-- Sessione 79 (corso 24)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud011@unina.it',79),('stud021@unina.it',79),('stud031@unina.it',79),('stud041@unina.it',79);

-- Sessione 81 (corso 24)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud011@unina.it',81),('stud021@unina.it',81),('stud031@unina.it',81),('stud041@unina.it',81);

-- Sessione 82 (corso 25)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud012@unina.it',82),('stud022@unina.it',82),('stud032@unina.it',82),('stud042@unina.it',82);

-- Sessione 84 (corso 25)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud012@unina.it',84),('stud022@unina.it',84),('stud032@unina.it',84),('stud042@unina.it',84);

-- Sessione 85 (corso 25)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud012@unina.it',85),('stud022@unina.it',85),('stud032@unina.it',85),('stud042@unina.it',85);

-- Sessione 86 (corso 25)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud012@unina.it',86),('stud022@unina.it',86),('stud032@unina.it',86),('stud042@unina.it',86);

-- Sessione 87 (corso 26)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud013@unina.it',87),('stud023@unina.it',87),('stud033@unina.it',87),('stud043@unina.it',87);

-- Sessione 89 (corso 26)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud013@unina.it',89),('stud023@unina.it',89),('stud033@unina.it',89),('stud043@unina.it',89);

-- Sessione 90 (corso 26)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('stud013@unina.it',90),('stud023@unina.it',90),('stud033@unina.it',90),('stud043@unina.it',90);

-- Sessione 91 (corso 27)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('mario.std@unina.it',91),('matilde.std@unina.it',91),('stud070@unina.it',91),('stud071@unina.it',91);

-- Sessione 92 (corso 27)
INSERT INTO Adesione (email_utente, id_sessione) VALUES
('mario.std@unina.it',92),('matilde.std@unina.it',92),('stud070@unina.it',92),('stud071@unina.it',92);

COMMIT;