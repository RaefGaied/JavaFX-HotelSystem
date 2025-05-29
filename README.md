![image](https://github.com/user-attachments/assets/9dcbdcf9-e3cc-4577-90e8-9816ce65e60f)
# 🏨 JavaFX Hotel Management System

![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk)
![JavaFX](https://img.shields.io/badge/JavaFX-19-blueviolet?logo=javafx)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?logo=mysql)
![License](https://img.shields.io/badge/License-MIT-green)

Un système complet de gestion hôtelière avec interface moderne développé en JavaFX et MySQL.

![Screenshot de l'application](https://via.placeholder.com/800x500.png?text=Hotel+Management+Screenshot) <!-- Remplacez par votre capture d'écran -->

## ✨ Fonctionnalités

### 🧑‍💻 Gestion des Clients
- Enregistrement des informations clients (ID, nationalité, contact)
- Recherche et filtrage avancé
- Historique des séjours

### 🛏️ Gestion des Chambres
- Catalogue des chambres (Single, Double, Suite...)
- Statut en temps réel (Disponible/Occupé)
- Prix par type de chambre

### 📅 Système de Réservation
- Prise de réservation avec dates
- Association client ↔ chambre
- Gestion des check-in/check-out

### 💰 Facturation Automatisée
- Génération de factures PDF
- Calcul du montant (jours × prix)
- Historique des paiements

## 🛠️ Technologies

| Composant       | Technologie           |
|-----------------|-----------------------|
| Frontend        | JavaFX 19            |
| Backend         | Java 17              |
| Base de données | MySQL 8.0            |
| Architecture    | Modèle MVC           |
| Build Tool      | Maven                |

## 🚀 Installation

### Prérequis
- JDK 17+
- MySQL Server 8.0
- JavaFX SDK 19

### Configuration
1. Importer la base de données :
```bash
mysql -u root -p < src/main/resources/database/hotel_system.sql
