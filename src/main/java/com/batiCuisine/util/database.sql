CREATE TABLE clients (
                         id SERIAL PRIMARY KEY,
                         nom VARCHAR(255) NOT NULL,
                         adresse TEXT NOT NULL,
                         telephone VARCHAR(20) NOT NULL,
                         est_professionnel BOOLEAN NOT NULL,
                         remise DOUBLE PRECISION DEFAULT 0.0
);

CREATE TYPE etat_projet AS ENUM (
    'en_cours', 'termine', 'annule'
);

CREATE TABLE projets (
                         id SERIAL PRIMARY KEY,
                         nom_projet VARCHAR(255) NOT NULL,
                         marge_beneficiaire DOUBLE PRECISION,
                         cout_total DOUBLE PRECISION DEFAULT 0.0 ,
                         etat_projet etat_projet DEFAULT 'en_cours',
                         surface DOUBLE PRECISION NOT NULL,
                         client_id INT REFERENCES clients(id) ON DELETE CASCADE
);

CREATE TYPE TypeComposant AS ENUM (
    'Materiel', 'Main_doeuvre'
);

CREATE TABLE composants (
                            id SERIAL PRIMARY KEY,
                            nom VARCHAR(255) NOT NULL,
                            type_composant TypeComposant NOT NULL,
                            taux_tva DOUBLE PRECISION,
                            projet_id INT REFERENCES projets(id) ON DELETE CASCADE
);

CREATE TABLE materiaux (
                           cout_transport DOUBLE PRECISION DEFAULT 0.0,
                           cout_unitaire DOUBLE PRECISION NOT NULL,
                           quantite DOUBLE PRECISION NOT NULL,
                           coefficient_qualite DOUBLE PRECISION DEFAULT 1.0
) INHERITS (composants);

CREATE TABLE main_doeuvre (
                              taux_horaire DOUBLE PRECISION NOT NULL,
                              heures_travail DOUBLE PRECISION NOT NULL,
                              productivite_ouvrier DOUBLE PRECISION DEFAULT 1.0
) INHERITS (composants);

CREATE TABLE devis (
                       id SERIAL PRIMARY KEY,
                       montant_estime DOUBLE PRECISION NOT NULL,
                       date_emission DATE NOT NULL,
                       date_validite DATE NOT NULL,
                       accepte BOOLEAN DEFAULT FALSE,
                       projet_id INT REFERENCES projets(id) ON DELETE CASCADE
);


-- remove all of the tables

DO $$
DECLARE
r RECORD;
BEGIN
FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = 'public')
    LOOP
        EXECUTE 'DROP TABLE IF EXISTS ' || quote_ident(r.tablename) || ' CASCADE';
END LOOP;
END $$;

