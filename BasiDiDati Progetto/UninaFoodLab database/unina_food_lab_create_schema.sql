-- =============================================================
-- PROGETTO: UninaFoodLab
-- =============================================================

-- PULIZIA AMBIENTE E CREAZIONE SCHEMA ---
DROP SCHEMA IF EXISTS uninafoodlab CASCADE;
CREATE SCHEMA uninafoodlab;
GRANT ALL ON SCHEMA uninafoodlab TO public;
SET search_path TO uninafoodlab, public;