package com.batiCuisine.ui;

import com.batiCuisine.enums.TypeComposant;
import com.batiCuisine.model.*;
import com.batiCuisine.service.ClientService;
import com.batiCuisine.service.ComposantService;
import com.batiCuisine.service.ProjetService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleUI {
    Scanner scanner = new Scanner(System.in);
    private final ClientService clientService = new ClientService();
    private final ProjetService projetService = new ProjetService();
    private final ComposantService composantService = new ComposantService();

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
                case 2:
                    displayAllProjects();
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
        System.out.print("Souhaitez-vous chercher un client existant ou en ajouter un nouveau ? \n");
        System.out.println("1. Chercher un client existant");
        System.out.println("2. Ajouter un nouveau client");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                clientId = searchClientByNameUI();
                if (clientId == -1) {
                    System.out.println("Client introuvable.");
                    return;
                }
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

        System.out.println("-----  Création d'un Nouveau Projet -----");
        System.out.println("Entrez le nom du projet : ");
        String projectName = scanner.nextLine();
        System.out.println("Entrez la surface de la cuisine (en m²) :");
        double surface = scanner.nextDouble();

        try {
            int projectId = projetService.addProject(projectName, surface, clientId);
            System.out.println("Projet créé avec succès avec le nom: " + projectName);
            System.out.println("----- Ajout des matériaux -----");
            AddNewMaterial(projectId);
        } catch (SQLException e) {
            System.out.println("Impossible d'ajouter le projet en raison de : " + e.getMessage());
        }
    }

    public void AddNewMaterial(int projetId) {
        System.out.println("Entrez le nom du matériau: ");
        String materialName = scanner.nextLine();
        scanner.next();
        System.out.println("Entrez la quantité de ce matériau (en m²): ");
        double quantite = scanner.nextDouble();

        System.out.print("Entrez le coût unitaire de ce matériau (€/m²) :  ");
        double coutUnitaire = scanner.nextDouble();

        System.out.print("Entrez le coût de transport de ce matériau (€) : ");
        double coutTransport = scanner.nextDouble();

        System.out.print("Entrez le coefficient de qualité du matériau (1.0 = standard, > 1.0 = haute qualité) : ");
        double coefficientQualite = scanner.nextDouble();

        Material materiaux = new Material(0, materialName, TypeComposant.Materiel,0, projetId, coutTransport, coefficientQualite, coutUnitaire, quantite);

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
        scanner.nextLine();
        System.out.println("Entrez le taux horaire de cette main-d'œuvre (€/h) : ");
        double tauxHoraire = scanner.nextDouble();

        System.out.print("Entrez le nombre d'heures travaillées :  ");
        double heuresTrav = scanner.nextDouble();

        System.out.print("Entrez le facteur de productivité (1.0 = standard, > 1.0 = haute productivité) : ");
        double facteurProductivite = scanner.nextDouble();

        Labor labor = new Labor(0, laborType, TypeComposant.Materiel,0, projetId, tauxHoraire, heuresTrav, facteurProductivite);

        try {
            composantService.createMainDoeuvre(labor);
            System.out.println("Main-d'œuvre ajoutée avec succès !");
            System.out.println("Voulez-vous ajouter un autre type de main-d'œuvre ? (y/n) : ");
            char answer = scanner.next().charAt(0);
            if(answer == 'y') {
                AddNewMain_doeuvre(projetId);
            } else {
                System.out.println("----- Calcul du coût total -----");
                System.out.println("Souhaitez-vous appliquer une marge bénéficiaire au projet ? (y/n) : ");
                char choice2 = scanner.next().charAt(0);
                double margeBeneficiaire = 0;
                if (choice2 == 'y') {
                    System.out.println("Entrez le pourcentage de marge bénéficiaire (%) : ");
                    margeBeneficiaire = scanner.nextDouble();
                }

                System.out.println("Souhaitez-vous appliquer une TVA au projet ? (y/n) : ");
                char choice1 = scanner.next().charAt(0);
                double tauxTva = 0;
                if (choice1 == 'y') {
                    System.out.println("Entrez le pourcentage de TVA (%) : ");
                    tauxTva = scanner.nextDouble();
                }
                calcCoutTotal(projetId, margeBeneficiaire, tauxTva);
            }
        } catch (SQLException | IllegalArgumentException e) {
            System.err.println("Error creating labor: " + e.getMessage());
        }
    }

    public void calcCoutTotal(int projetId, double margeBeneficiaire, double tauxTva) {
        Project project = null;
        try {
            project = projetService.getProjectById(projetId);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du projet : " + e.getMessage());
            return;
        }
        List<Material> materials = composantService.getMateriauxByProject(projetId);
        List<Labor> labors = composantService.getMainDoeuvreByProject(projetId);

        double totalMatCost = 0;
        double totalLaborCost = 0;

        System.out.println("Calcul du coût en cours...");

        System.out.println("----- Résultat du Calcul -----");

        System.out.println("Détails du projet:");
        System.out.println("- Nom du projet : " + project.getNom_projet());
        System.out.println("- Client : " + project.getClient().getNom());
        System.out.println("- Surface de la cuisine : " + project.getSurface() + " m²");

        System.out.println("----- Détail des Coûts -----");

        System.out.println("1. Matériaux :");
        for (Material material : materials) {
            double matCost = (material.getQuantite() * material.getCoutUnitaire() * material.getCoefficientQualite())
                    + material.getCoutTransport();
            totalMatCost += matCost;

            System.out.printf("- %s : %.2f € (quantité : %.2f %s, coût unitaire : %.2f €/%s, qualité : %.1f, transport : %.2f €)\n",
                    material.getNom(), matCost, material.getQuantite(), material.getCoutUnitaire(), material.getCoutUnitaire(),
                    material.getCoutUnitaire(), material.getCoefficientQualite(), material.getCoutTransport());
            int composantId = material.getId();
            try {
                composantService.updateTauxTVA(composantId, tauxTva);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.printf("**Coût total des matériaux avant TVA : %.2f €**\n", totalMatCost);

        System.out.println("2. Main-d'œuvre :");
        for (Labor labor : labors) {
            double laborCost = (labor.getTauxHoraire() * labor.getHeuresTravail()) / labor.getProductiviteOuvrier();
            totalLaborCost += laborCost;

            System.out.printf("- %s : %.2f € (taux horaire : %.2f €/h, heures travaillées : %.2f h, productivité : %.1f)\n",
                    labor.getNom(), laborCost, labor.getTauxHoraire(), labor.getHeuresTravail(), labor.getProductiviteOuvrier());
            int composantId = labor.getId();
            try {
                composantService.updateTauxTVA(composantId, tauxTva);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.printf("**Coût total de la main-d'œuvre avant TVA : %.2f €**\n", totalLaborCost);

        double totalCostBeforeTVA = totalMatCost + totalLaborCost;

        double totalWithTVA = totalCostBeforeTVA + (totalCostBeforeTVA * (tauxTva / 100));

        System.out.printf("**Coût total avec TVA (%.0f%%) : %.2f €**\n", tauxTva, totalWithTVA);

        double finalCost = totalWithTVA + (totalWithTVA * (margeBeneficiaire / 100));

        System.out.printf("3. Coût total avant marge : %.2f €\n", totalWithTVA);
        System.out.printf("4. Marge bénéficiaire (%.0f%%) : %.2f €\n", margeBeneficiaire, finalCost - totalWithTVA);
        try {
            projetService.updateProjectCost(projetId, finalCost, margeBeneficiaire);
            System.out.println("Le coût total du projet est de : " + finalCost + " € (TVA incluse)");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du coût total : " + e.getMessage());
        }
    }

    public void displayAllProjects() {
        try {
            List<Project> projects = projetService.getAllProjects();
            System.out.println("----- L'affichage des projets existants -----");

            for (Project project : projects) {
                List<Composant> composants = composantService.getAllComposants(project.getId());
                double totalTauxTva = 0;

                for (Composant comp : composants) {
                    totalTauxTva += comp.getTauxTva();
                }

                calcCoutTotal(project.getId(), project.getMarge_beneficiaire(), totalTauxTva);
            }
        } catch (Exception e) {
            System.out.println("Error during displaying all projects: " + e.getMessage());
        }
    }


    public int searchClientByNameUI(){
        System.out.print("Entrez le nom du client à rechercher : ");
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        String name = scanner.nextLine();

        try {
            Optional<Client> foundClient = clientService.searchClientByName(name);

            if (foundClient.isPresent()) {
                Client client = foundClient.get();
                System.out.println("Client trouvé : " + client.getNom());
                System.out.println("Adresse : " + client.getAddress());
                System.out.println("Téléphone : " + client.getTelephone());
                System.out.println("Souhaitez-vous continuer avec ce client ? (y/n) :");
                String response = scanner.nextLine();
                if (response.equals("y")) {
                    return client.getId();
                } else {
                    CreateProject();
                }
            } else {
                System.out.println("Aucun client trouvé avec le nom : " + name);
                return -1;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du client : " + e.getMessage());
            return -1;
        }
        return 0;
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
