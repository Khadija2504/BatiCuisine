CREATE TABLE clients (
                         id SERIAL PRIMARY KEY,
                         nom VARCHAR(255) NOT NULL,
                         adresse TEXT NOT NULL,
                         telephone VARCHAR(20) NOT NULL,
                         est_professionnel BOOLEAN NOT NULL,
                         remise DOUBLE PRECISION DEFAULT 0.0
);

CREATE TABLE projets (
                         id SERIAL PRIMARY KEY,
                         nom_projet VARCHAR(255) NOT NULL,
                         marge_beneficiaire DOUBLE PRECISION NOT NULL,
                         cout_total DOUBLE PRECISION DEFAULT 0.0,
                         etat_projet VARCHAR(50) CHECK (etat_projet IN ('En cours', 'Terminé', 'Annulé')),
                         client_id INT REFERENCES clients(id) ON DELETE CASCADE
);

CREATE TABLE composants (
                            id SERIAL PRIMARY KEY,
                            nom VARCHAR(255) NOT NULL,
                            cout_unitaire DOUBLE PRECISION NOT NULL,
                            quantite DOUBLE PRECISION NOT NULL,
                            type_composant VARCHAR(50) CHECK (type_composant IN ('Matériel', 'Main-dœuvre')) NOT NULL,
                            taux_tva DOUBLE PRECISION NOT NULL,
                            cout_transport DOUBLE PRECISION DEFAULT 0.0,
                            coefficient_qualite DOUBLE PRECISION DEFAULT 1.0,
                            taux_horaire DOUBLE PRECISION DEFAULT 0.0,
                            heures_travail DOUBLE PRECISION DEFAULT 0.0,
                            productivite_ouvrier DOUBLE PRECISION DEFAULT 1.0
);

CREATE TABLE projet_composants (
                                   projet_id INT REFERENCES projets(id) ON DELETE CASCADE,
                                   composant_id INT REFERENCES composants(id) ON DELETE CASCADE,
                                   PRIMARY KEY (projet_id, composant_id)
);

CREATE TABLE devis (
                       id SERIAL PRIMARY KEY,
                       montant_estime DOUBLE PRECISION NOT NULL,
                       date_emission DATE NOT NULL,
                       date_validite DATE NOT NULL,
                       accepte BOOLEAN DEFAULT FALSE,
                       projet_id INT REFERENCES projets(id) ON DELETE CASCADE
);

CREATE TABLE remises (
                         id SERIAL PRIMARY KEY,
                         type_client VARCHAR(50) CHECK (type_client IN ('Professionnel', 'Particulier')),
                         taux_remise DOUBLE PRECISION NOT NULL
);
