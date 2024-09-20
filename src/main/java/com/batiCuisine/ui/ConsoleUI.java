package com.batiCuisine.ui;

import com.batiCuisine.enums.TypeComposant;
import com.batiCuisine.model.Labor;
import com.batiCuisine.model.Material;
import com.batiCuisine.service.ClientService;
import com.batiCuisine.service.ComposantService;
import com.batiCuisine.service.ProjetService;

import java.sql.SQLException;
import java.util.Scanner;

public class ConsoleUI {
    Scanner scanner = new Scanner(System.in);
    private ClientService clientService = new ClientService();
    private ProjetService projetService = new ProjetService();
    private ComposantService composantService = new ComposantService();

    public void DisplayMenu() {
        System.out.println("----- Bienvenue dans l'application de gestion des projets de rénovation de cuisines -----");
        while (true) {
            System.out.println("----- Menu Principal -----");
            System.out.println("1. Créer un nouveau projet");
            System.out.println("2. Afficher les projets existants");
            System.out.println("3. Calculer le coût d'un projet");
            System.out.println("4. Quitter");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    CreateProject();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice, plz try again with a valid choice from the list");
            }
        }
    }

    public void CreateProject() {
        Scanner scanner = new Scanner(System.in);
        int clientId = -1;

        System.out.println("----- Recherche d'un client -----");
        System.out.print("Souhaitez-vous chercher un client existant ou en ajouter un nouveau ? ");
        System.out.println("1. Chercher un client existant");
        System.out.println("2. Ajouter un nouveau client");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                break;
            case 2:
                clientId = AddNewClient();
                if (clientId != -1) {
                    System.out.println("Client ajouté avec succès avec l'ID: " + clientId);
                } else {
                    System.out.println("Erreur lors de l'ajout du client.");
                    return;
                }
                break;
            default:
                System.out.println("Choix invalide, veuillez réessayer.");
                return;
        }

        if (clientId != -1) {
            System.out.println("-----  Création d'un Nouveau Projet -----");
            System.out.println("Entrez le nom du projet : ");
            String projectName = scanner.nextLine();
            System.out.println("Entrez la surface de la cuisine (en m²) :");
            double surface = scanner.nextDouble();

            try {
                int projectId = projetService.addProject(projectName, surface, clientId);
                System.out.println("Projet créé avec succès avec l'ID: " + projectId);
                System.out.println("----- Ajout des matériaux -----");
                AddNewMaterial(projectId);
            } catch (SQLException e) {
                System.out.println("Impossible d'ajouter le projet en raison de : " + e.getMessage());
            }
        } else {
            System.out.println("ID du client non valide, impossible de créer le projet.");
        }
    }

    public void AddNewMaterial(int projetId) {
        System.out.println("Entrez le nom du matériau: ");
        String materialName = scanner.nextLine();
        System.out.println("Entrez la quantité de ce matériau (en m²): ");
        double quantite = scanner.nextDouble();

        System.out.print("Entrez le coût unitaire de ce matériau (€/m²) :  ");
        double coutUnitaire = scanner.nextDouble();

        System.out.print("Entrez le coût de transport de ce matériau (€) : ");
        double coutTransport = scanner.nextDouble();

        System.out.print("Entrez le coefficient de qualité du matériau (1.0 = standard, > 1.0 = haute qualité) : ");
        double coefficientQualite = scanner.nextDouble();

        Material materiaux = new Material(materialName, TypeComposant.Materiel,0, projetId, coutTransport, coefficientQualite, coutUnitaire, quantite);

        try {
            composantService.createMateriaux(materiaux);
            System.out.println("Matériau ajouté avec succès !\n");
            System.out.println("Voulez-vous ajouter un autre matériau ? (y/n) : ");
            char answer = scanner.next().charAt(0);
            if(answer == 'y') {
                AddNewMaterial(projetId);
            } else {
                System.out.println("----- Ajout de la main-d'œuvre -----");
                AddNewMain_doeuvre(projetId);
            }
        } catch (SQLException | IllegalArgumentException e) {
            System.err.println("Error creating materiaux: " + e.getMessage());
        }
    }

    public void AddNewMain_doeuvre(int projetId) {
        System.out.println("Entrez le type de main-d'œuvre (e.g., Ouvrier de base, Spécialiste): ");
        String laborType = scanner.nextLine();
        System.out.println("Entrez le taux horaire de cette main-d'œuvre (€/h) : ");
        double tauxHoraire = scanner.nextDouble();

        System.out.print("Entrez le nombre d'heures travaillées :  ");
        double heuresTrav = scanner.nextDouble();

        System.out.print("Entrez le facteur de productivité (1.0 = standard, > 1.0 = haute productivité) : ");
        double facteurProductivite = scanner.nextDouble();

        Labor labor = new Labor(laborType, TypeComposant.Materiel,0, projetId, tauxHoraire, heuresTrav, facteurProductivite);

        try {
            composantService.createMainDoeuvre(labor);
            System.out.println("Main-d'œuvre ajoutée avec succès !");
            System.out.printf("Voulez-vous ajouter un autre type de main-d'œuvre ? (y/n) : ");
            char answer = scanner.next().charAt(0);
            if(answer == 'y') {
                AddNewMain_doeuvre(projetId);
            } else {
                System.out.println("----- Calcul du coût total -----");
                
            }
        } catch (SQLException | IllegalArgumentException e) {
            System.err.println("Error creating labor: " + e.getMessage());
        }
    }


    public int AddNewClient() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Entrez le nom du client : ");
        String nom = scanner.nextLine();
        System.out.println("Entrez l'adresse du client : ");
        String adresse = scanner.nextLine();
        System.out.println("Entrez le telephone du client : ");
        String telephone = scanner.nextLine();
        System.out.println("si le client est un professionnel indique (true), si non (false) : ");
        boolean est_professionnel = Boolean.parseBoolean(scanner.nextLine());
        System.out.println("Entrez le remise du client : ");
        double remise = Double.parseDouble(scanner.nextLine());

        try {
            int clientId = clientService.addClient(nom, adresse, telephone, est_professionnel, remise);
            if(clientId != -1) {
                System.out.println("Client added successfully with ID: " + clientId);
                System.out.println("----- Détails du client ajouté -----");
                System.out.println("Nom: " + nom);
                System.out.println("Adresse: " + adresse);
                System.out.println("Telephone: " + telephone);
                System.out.println("Souhaitez-vous continuer avec ce client ? (y/n) :");
                String response = scanner.nextLine();
                if (response.equals("y")) {
                    return clientId;
                } else {
                    CreateProject();
                }
            } else {
                System.out.println("Client not added.");
            }
        } catch (SQLException e) {
            System.out.println("can't add the client" + e);
        }
        return 0;
    }
}
