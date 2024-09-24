package com.batiCuisine.ui;

import com.batiCuisine.enums.EtatProjet;
import com.batiCuisine.enums.TypeComposant;
import com.batiCuisine.model.*;
import com.batiCuisine.service.ClientService;
import com.batiCuisine.service.ComposantService;
import com.batiCuisine.service.DevisService;
import com.batiCuisine.service.ProjetService;
import com.batiCuisine.util.Validator;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ConsoleUI {
    Scanner scanner = new Scanner(System.in);
    private final ClientService clientService = new ClientService();
    private final ProjetService projetService = new ProjetService();
    private final ComposantService composantService = new ComposantService();
    private final DevisService devisService = new DevisService();
    private final Validator validatorDate = new Validator();

    public void DisplayMenu() {
        System.out.println("----- Bienvenue dans l'application de gestion des projets de rénovation de cuisines -----");
        while (true) {
            System.out.println("----- Menu Principal -----");
            System.out.println("1. Créer un nouveau projet");
            System.out.println("2. Afficher les projets existants");
            System.out.println("3. Mise à jour d'état de projet");
            System.out.println("4. Quitter");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    CreateProject();
                    break;
                case 2:
                    displayAllProjects();
                    break;
                case 3:
                    searchProjectByName();
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
                if (clientId == -1) {
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

        if (!Validator.isValidProjectName(projectName)) {
            System.out.println("Le nom du projet est invalide (caractères spéciaux non autorisés ou nom trop court).");
            return;
        }

        System.out.println("Entrez la surface de la cuisine (en m²) :");
        double surface = scanner.nextDouble();

        if (!Validator.isValidSurface(surface)) {
            System.out.println("La surface est invalide (doit être supérieure à 0).");
            return;
        }

        try {
            int projectId = projetService.addProject(projectName, surface, clientId);
            if (projectId != -1) {
                System.out.println("Projet créé avec succès avec le nom: " + projectName);
                System.out.println("----- Ajout des matériaux -----");
                AddNewMaterial(projectId);
            } else {
                System.out.println("Impossible d'ajouter le projet.");
                CreateProject();
            }
        } catch (SQLException e) {
            System.out.println("Impossible d'ajouter le projet en raison de : " + e.getMessage());
        }
    }

    public void AddNewMaterial(int projetId) {
        System.out.println("Entrez le nom du matériau : ");
        String materialName = scanner.nextLine();
        materialName = scanner.nextLine();

        if (!Validator.isValidMaterialName(materialName)) {
            System.out.println("Le nom du matériau est invalide (caractères spéciaux non autorisés ou nom trop court).");
            return;
        }

        System.out.println("Entrez la quantité de ce matériau (en m²) : ");
        double quantite = scanner.nextDouble();

        if (!Validator.isValidQuantity(quantite)) {
            System.out.println("La quantité est invalide (doit être supérieure à 0).");
            return;
        }

        System.out.print("Entrez le coût unitaire de ce matériau (€/m²) : ");
        double coutUnitaire = scanner.nextDouble();

        if (!Validator.isValidCost(coutUnitaire)) {
            System.out.println("Le coût unitaire est invalide.");
            return;
        }

        System.out.print("Entrez le coût de transport de ce matériau (€) : ");
        double coutTransport = scanner.nextDouble();

        if (!Validator.isValidCost(coutTransport)) {
            System.out.println("Le coût de transport est invalide.");
            return;
        }

        System.out.print("Entrez le coefficient de qualité du matériau (1.0 = standard, > 1.0 = haute qualité) : ");
        double coefficientQualite = scanner.nextDouble();

        if (!Validator.isValidQualityCoefficient(coefficientQualite)) {
            System.out.println("Le coefficient de qualité est invalide.");
            return;
        }

        Material materiaux = new Material(0, materialName, TypeComposant.Materiel, 0, projetId, coutTransport, coefficientQualite, coutUnitaire, quantite);

        try {
            composantService.createMateriaux(materiaux);
            System.out.println("Matériau ajouté avec succès !\n");
            System.out.println("Voulez-vous ajouter un autre matériau ? (y/n) : ");
            char answer = scanner.next().charAt(0);
            if (answer == 'y') {
                AddNewMaterial(projetId);
            } else {
                System.out.println("----- Ajout de la main-d'œuvre -----");
                AddNewMain_doeuvre(projetId);
            }
        } catch (SQLException | IllegalArgumentException e) {
            System.err.println("Erreur lors de la création du matériau : " + e.getMessage());
        }
    }

    public void AddNewMain_doeuvre(int projetId) {
        System.out.println("Entrez le type de main-d'œuvre (e.g., Ouvrier de base, Spécialiste) : ");
        String laborType = scanner.nextLine();
        laborType = scanner.nextLine();

        if (!Validator.isValidLaborType(laborType)) {
            System.out.println("Le type de main-d'œuvre est invalide.");
            return;
        }

        System.out.println("Entrez le taux horaire de cette main-d'œuvre (€/h) : ");
        double tauxHoraire = scanner.nextDouble();

        if (!Validator.isValidCost(tauxHoraire)) {
            System.out.println("Le taux horaire est invalide.");
            return;
        }

        System.out.print("Entrez le nombre d'heures travaillées : ");
        double heuresTrav = scanner.nextDouble();

        if (!Validator.isValidHours(heuresTrav)) {
            System.out.println("Le nombre d'heures est invalide.");
            return;
        }

        System.out.print("Entrez le facteur de productivité (1.0 = standard, > 1.0 = haute productivité) : ");
        double facteurProductivite = scanner.nextDouble();

        if (!Validator.isValidProductivityFactor(facteurProductivite)) {
            System.out.println("Le facteur de productivité est invalide.");
            return;
        }

        Labor labor = new Labor(0, laborType, TypeComposant.Materiel, 0, projetId, tauxHoraire, heuresTrav, facteurProductivite);

        try {
            composantService.createMainDoeuvre(labor);
            System.out.println("Main-d'œuvre ajoutée avec succès !");
            System.out.println("Voulez-vous ajouter un autre type de main-d'œuvre ? (y/n) : ");
            char answer = scanner.next().charAt(0);
            if (answer == 'y') {
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
                double finalCost = calcCoutTotal(projetId, margeBeneficiaire, tauxTva);
                saveDevis(finalCost, projetId);
            }
        } catch (SQLException | IllegalArgumentException e) {
            System.err.println("Erreur lors de la création de la main-d'œuvre : " + e.getMessage());
        }
    }


    public double calcCoutTotal(int projetId, double margeBeneficiaire, double tauxTva) {
        Project project = null;
        try {
            project = projetService.getProjectById(projetId);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du projet : " + e.getMessage());
            return -1;
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

            System.out.printf("- %s : %.2f € (quantité : %.2f, coût unitaire : %.2f €/%s, qualité : %.1f, transport : %.2f €)\n",
                    material.getNom(), matCost, material.getQuantite(), material.getCoutUnitaire(), material.getCoutUnitaire(),
                    material.getCoefficientQualite(), material.getCoutTransport());
            int composantId = material.getId();
            try {
                composantService.updateTauxTVA(composantId, tauxTva);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.printf("**Coût total des matériaux avant TVA : %.2f €**\n", totalMatCost);
        double totalMatCostWithTva = totalMatCost + ((totalMatCost * tauxTva) / 100);
        System.out.printf("**Coût total des matériaux avec TVA (%.0f%%) : %.2f €**\n", tauxTva, totalMatCostWithTva);

        System.out.println("2. Main-d'œuvre :");
        for (Labor labor : labors) {
            double laborCost = (labor.getTauxHoraire() * labor.getHeuresTravail()) * labor.getProductiviteOuvrier();
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
        double totalLaborCostWithTva = totalLaborCost + ((totalLaborCost * tauxTva) / 100);
        System.out.printf("**Coût total de la main-d'œuvre avec TVA (%.0f%%) : %.2f €**\n", tauxTva, totalLaborCostWithTva);

        double totalCostBeforeTVA = totalMatCostWithTva + totalLaborCostWithTva;

        double finalCost = totalCostBeforeTVA + (totalCostBeforeTVA * (margeBeneficiaire / 100));

        System.out.printf("3. Coût total avant marge : %.2f €\n", totalCostBeforeTVA);
        System.out.printf("4. Marge bénéficiaire (%.0f%%) : %.2f €\n", margeBeneficiaire, finalCost - totalCostBeforeTVA);

        try {
            projetService.updateProjectCost(projetId, totalCostBeforeTVA, margeBeneficiaire);
            System.out.println("Le coût total du projet est de : " + finalCost + " € (TVA incluse)");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du coût total : " + e.getMessage());
        }
        double finalCout;

        if(project.getClient().getEst_professionnel()) {
            double remise = project.getClient().getRemise();
            finalCout = (finalCost * remise)/100;
        } else {
            finalCout = finalCost;
        }

        return finalCout;
    }

    public void saveDevis(double finalCost, int projetId) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        System.out.println("----- Enregistrement du Devis -----");
        LocalDate emissionDate = null;
        LocalDate validateDate = null;

        while (emissionDate == null) {
            System.out.println("Entrez la date d'émission du devis (format : jj/mm/aaaa) :");
            String emissionDateInput = scanner.nextLine();
            try {
                emissionDate = LocalDate.parse(emissionDateInput, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Format de date invalide. Veuillez réessayer.");
            }
        }

        while (validateDate == null) {
            System.out.println("Entrez la date de validité du devis (format : jj/mm/aaaa) :");
            String validateDateInput = scanner.nextLine();
            try {
                validateDate = LocalDate.parse(validateDateInput, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Format de date invalide. Veuillez réessayer.");
            }
        }

        System.out.println("Souhaitez-vous valider le devis ? (y/n) :");
        String choice = scanner.nextLine();
        boolean validate = choice.equalsIgnoreCase("y");

        try {
            devisService.createDevis(projetId, finalCost, Date.valueOf(emissionDate), Date.valueOf(validateDate), validate);
            System.out.println("Devis enregistré avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'enregistrement du devis : " + e.getMessage());
        }
    }
    public void displayAllProjects() {
        try {
            List<Project> projects = projetService.getAllProjects();
            System.out.println("----- L'affichage des projets existants -----");

            for (Project project : projects) {
                Map<Integer, Composant> composants = composantService.getAllComposants(project.getId());
                double totalTauxTva = 0;

                for (Composant comp : composants.values()){
                    totalTauxTva = comp.getTauxTva();
                }

                displayAllCalcs(project.getId(), project.getMarge_beneficiaire(), totalTauxTva);
            }
        } catch (Exception e) {
            System.out.println("Error during displaying all projects: " + e.getMessage());
        }
    }

    public void displayAllCalcs (int projetId, double margeBeneficiaire, double tauxTva) {
        Project project = null;
        try {
            project = projetService.getProjectById(projetId);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du projet : " + e.getMessage());
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

            System.out.printf("- %s : %.2f € (quantité : %.2f, coût unitaire : %.2f €/%s, qualité : %.1f, transport : %.2f €)\n",
                    material.getNom(), matCost, material.getQuantite(), material.getCoutUnitaire(), material.getCoutUnitaire(),
                    material.getCoefficientQualite(), material.getCoutTransport());
        }

        System.out.printf("**Coût total des matériaux avant TVA : %.2f €**\n", totalMatCost);
        double totalMatCostWithTva = totalMatCost + ((totalMatCost * tauxTva) / 100);
        System.out.printf("**Coût total des matériaux avec TVA (%.0f%%) : %.2f €**\n", tauxTva, totalMatCostWithTva);

        System.out.println("2. Main-d'œuvre :");
        for (Labor labor : labors) {
            double laborCost = (labor.getTauxHoraire() * labor.getHeuresTravail()) * labor.getProductiviteOuvrier();
            totalLaborCost += laborCost;

            System.out.printf("- %s : %.2f € (taux horaire : %.2f €/h, heures travaillées : %.2f h, productivité : %.1f)\n",
                    labor.getNom(), laborCost, labor.getTauxHoraire(), labor.getHeuresTravail(), labor.getProductiviteOuvrier());
        }

        System.out.printf("**Coût total de la main-d'œuvre avant TVA : %.2f €**\n", totalLaborCost);
        double totalLaborCostWithTva = totalLaborCost + ((totalLaborCost * tauxTva) / 100);
        System.out.printf("**Coût total de la main-d'œuvre avec TVA (%.0f%%) : %.2f €**\n", tauxTva, totalLaborCostWithTva);

        double totalCostBeforeTVA = totalMatCostWithTva + totalLaborCostWithTva;

        double finalCost = totalCostBeforeTVA + (totalCostBeforeTVA * (margeBeneficiaire / 100));

        System.out.printf("3. Coût total avant marge : %.2f €\n", totalCostBeforeTVA);
        System.out.printf("4. Marge bénéficiaire (%.0f%%) : %.2f €\n", margeBeneficiaire, finalCost - totalCostBeforeTVA);

        System.out.println("Le coût total du projet est de : " + finalCost + " €");

        System.out.println("----- Les details de devis -----");
        try {
            Map<Integer, Devis> devisMap = devisService.getDevisByProjectId(projetId);
            devisMap.values().forEach(devis -> {
                System.out.println("Le Montant estime: " + devis.getMontant_estime());
                System.out.println("La date d'emission: " + devis.getDate_emission());
                System.out.println("La adte de validation: " + devis.getDate_validite());
                String status = null;
                if (devis.isAccepte()) {
                    status = "Accepter";
                } else {
                    status = "refuser";
                }
                System.out.println("Le status de devis: " + status);
            });
            System.out.println("Fin du details de projet");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void searchProjectByName() {
        System.out.println("Entrez le nom du projet à rechercher : ");

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        String name = scanner.nextLine();

        if (!Validator.isValidProjectName(name)) {
            System.out.println("Le nom du projet est invalide. Il doit contenir au moins 3 caractères alphanumériques.");
            return;
        }

        try {
            Optional<Project> foundProject = projetService.getProjectByName(name);
            if (foundProject.isPresent()) {
                Project project = foundProject.get();
                System.out.println("Détails du projet:");
                System.out.println("- Nom du projet : " + project.getNom_projet());
                System.out.println("- Surface de la cuisine : " + project.getSurface() + " m²");
                System.out.println("- Marge bénéficiaire : " + project.getMarge_beneficiaire() + "%");
                System.out.println("- Coût total calculé : " + project.getCout_total() + "€");

                if (project.getEtat_projet().equals(EtatProjet.en_cours.name())) {
                    System.out.println("1. Mettre à jour le statut du projet " + project.getNom_projet());
                    System.out.println("2. Retour");
                    int choice = scanner.nextInt();

                    switch (choice) {
                        case 1:
                            updateProjectStatus(project.getId());
                            break;
                        case 2:
                            return;
                        default:
                            System.out.println("Choix invalide.");
                    }
                } else {
                    System.out.println("Le projet est déjà terminé!");
                    System.out.println("1. Rechercher un autre projet");
                    System.out.println("2. Menu principal");
                    int choice = scanner.nextInt();
                    scanner.nextLine();
                    switch (choice) {
                        case 1:
                            searchProjectByName();
                            break;
                        case 2:
                            DisplayMenu();
                            break;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du projet : " + e.getMessage());
        }
    }

    public void updateProjectStatus(int projectId) {
        System.out.println("Mise à jour du statut du projet");
        System.out.println("Choisissez l'état du projet :");
        System.out.println("1. Terminé");
        System.out.println("2. Annulé");

        int choice = scanner.nextInt();

        if (choice != 1 && choice != 2) {
            System.out.println("Choix invalide, veuillez réessayer.");
            return;
        }

        try {
            projetService.updateProjectStatus(projectId, choice);
            System.out.println("Le statut du projet a été mis à jour avec succès!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public int searchClientByNameUI() {
        System.out.print("Entrez le nom du client à rechercher : ");
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Le nom du client ne peut pas être vide.");
            return -1;
        }

        try {
            Optional<Client> foundClient = clientService.searchClientByName(name);

            if (foundClient.isPresent()) {
                Client client = foundClient.get();
                System.out.println("Client trouvé : " + client.getNom());
                System.out.println("Adresse : " + client.getAddress());
                System.out.println("Téléphone : " + client.getTelephone());
                System.out.println("Souhaitez-vous continuer avec ce client ? (y/n) :");
                String response = scanner.nextLine().trim().toLowerCase();

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
        String nom = scanner.nextLine().trim();
        if (nom.isEmpty()) {
            System.out.println("Le nom ne peut pas être vide.");
            return -1;
        }

        System.out.println("Entrez l'adresse du client : ");
        String adresse = scanner.nextLine().trim();
        if (adresse.isEmpty()) {
            System.out.println("L'adresse ne peut pas être vide.");
            return -1;
        }

        System.out.println("Entrez le téléphone du client : ");
        String telephone = scanner.nextLine().trim();
        if (telephone.isEmpty()) {
            System.out.println("Le numéro de téléphone ne peut pas être vide.");
            return -1;
        }

        System.out.println("Si le client est un professionnel, indiquez (true), sinon (false) : ");
        boolean est_professionnel = Boolean.parseBoolean(scanner.nextLine().trim());

        double remise = 0.0;
        if (est_professionnel) {
            System.out.println("Entrez la remise pour le client professionnel : ");
            try {
                remise = Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Remise invalide. Veuillez entrer un nombre.");
                return -1;
            }
        }

        try {
            int clientId = clientService.addClient(nom, adresse, telephone, est_professionnel, remise);

            if (clientId != -1) {
                System.out.println("Client ajouté avec succès avec ID: " + clientId);
                System.out.println("----- Détails du client ajouté -----");
                System.out.println("Nom: " + nom);
                System.out.println("Adresse: " + adresse);
                System.out.println("Téléphone: " + telephone);
                System.out.println("Souhaitez-vous continuer avec ce client ? (y/n) :");
                String response = scanner.nextLine().trim().toLowerCase();

                if (response.equals("y")) {
                    return clientId;
                } else {
                    System.out.println("Création d'un nouveau projet...");
                    CreateProject();
                }
            } else {
                System.out.println("Le client n'a pas pu être ajouté.");
                CreateProject();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du client : " + e.getMessage());
        }

        return 0;
    }
}
