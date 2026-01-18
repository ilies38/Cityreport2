# Rapport de Spécifications Fonctionnelles
## Application CityReport

---

**Projet :** Application Android de signalement d'incidents urbains
**Date :** Janvier 2026
**Version :** 1.0

---

## Table des matières

1. [Lien GitHub du projet](#1-lien-github-du-projet)
2. [Description complète du projet](#2-description-complète-du-projet)
3. [Diagramme de cas d'utilisation](#3-diagramme-de-cas-dutilisation)
4. [Diagramme de classes](#4-diagramme-de-classes)
5. [Captures d'écran et fonctionnalités](#5-captures-décran-et-fonctionnalités)
6. [Défis rencontrés et solutions](#6-défis-rencontrés-et-solutions)

---

## 1. Lien GitHub du projet

**Repository :** `[À COMPLÉTER - Insérer le lien GitHub ici]`

```
https://github.com/[votre-username]/Cityreport2
```

---

## 2. Description complète du projet

### 2.1 Contexte

CityReport est une application mobile Android permettant aux citoyens de signaler des incidents urbains dans leur ville. L'application répond au besoin croissant de participation citoyenne dans l'amélioration du cadre de vie urbain.

### 2.2 Objectifs

- Permettre aux citoyens de signaler facilement des problèmes urbains
- Géolocaliser précisément les incidents
- Documenter visuellement les problèmes via des photos
- Assurer la disponibilité des données même hors connexion
- Synchroniser automatiquement les signalements avec le cloud

### 2.3 Public cible

- Citoyens souhaitant signaler des problèmes dans leur quartier
- Municipalités souhaitant collecter les signalements citoyens
- Services techniques de maintenance urbaine

### 2.4 Fonctionnalités principales

| Fonctionnalité | Description |
|----------------|-------------|
| **Création de signalement** | Formulaire complet avec titre, description, catégorie, photo et localisation GPS |
| **Liste des signalements** | Affichage de tous les rapports avec recherche et filtres |
| **Carte interactive** | Visualisation des signalements sur Google Maps |
| **Détails du signalement** | Vue complète avec mini-carte et photo en plein écran |
| **Synchronisation cloud** | Sauvegarde automatique sur Firebase |
| **Mode hors-ligne** | Fonctionnement sans connexion internet |
| **Multi-langue** | Français, Anglais, Arabe (avec RTL) |

### 2.5 Catégories de signalements

| Catégorie | Icône | Exemples |
|-----------|-------|----------|
| **Propreté** (CLEANLINESS) | 🧹 | Déchets, graffitis, dépôts sauvages |
| **Voirie** (ROAD) | 🛣️ | Nids de poule, trottoirs abîmés |
| **Éclairage** (LIGHTING) | 💡 | Lampadaires en panne |
| **Sécurité** (SAFETY) | ⚠️ | Dangers, obstacles |
| **Autre** (OTHER) | 📝 | Autres problèmes |

---

## 3. Diagramme de cas d'utilisation

### 3.1 Diagramme UML

```
┌─────────────────────────────────────────────────────────────────────────┐
│                        SYSTÈME CITYREPORT                                │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│    ┌─────────────────────────────────────────────────────────────┐      │
│    │                    Gérer les signalements                    │      │
│    │  ┌───────────────────────────────────────────────────────┐  │      │
│    │  │                                                       │  │      │
│    │  │   ┌─────────────────────┐                             │  │      │
│    │  │   │ Créer un signalement│◄──────────┐                 │  │      │
│    │  │   └─────────────────────┘           │                 │  │      │
│    │  │            │                        │                 │  │      │
│    │  │            │ «include»              │                 │  │      │
│    │  │            ▼                        │                 │  │      │
│    │  │   ┌─────────────────────┐           │                 │  │      │
│    │  │   │ Sélectionner        │           │                 │  │      │
│    │  │   │ catégorie           │           │                 │  │      │
│    │  │   └─────────────────────┘           │                 │  │      │
│    │  │            │                        │                 │  │      │
│    │  │            │ «include»              │                 │  │      │
│    │  │            ▼                        │                 │  │      │
│    │  │   ┌─────────────────────┐           │                 │  │      │
│    │  │   │ Capturer la         │           │                 │  │      │
│    │  │   │ localisation GPS    │           │                 │  │      │
│    │  │   └─────────────────────┘           │                 │  │      │
│    │  │            │                        │                 │  │      │
│    │  │            │ «extend»               │                 │  │      │
│    │  │            ▼                        │                 │  │      │
│    │  │   ┌─────────────────────┐           │                 │  │      │
│    │  │   │ Prendre une photo   │           │                 │  │      │
│    │  │   └─────────────────────┘           │                 │  │      │
│    │  │                                     │                 │  │      │
│    │  │   ┌─────────────────────┐           │                 │  │      │
│    │  │   │ Consulter la liste  │───────────┘                 │  │      │
│    │  │   │ des signalements    │                             │  │      │
│    │  │   └─────────────────────┘                             │  │      │
│    │  │            │                                          │  │      │
│    │  │            │ «extend»                                 │  │      │
│    │  │            ▼                                          │  │      │
│    │  │   ┌─────────────────────┐                             │  │      │
│    │  │   │ Filtrer par         │                             │  │      │
│    │  │   │ catégorie           │                             │  │      │
│    │  │   └─────────────────────┘                             │  │      │
│    │  │            │                                          │  │      │
│    │  │            │ «extend»                                 │  │      │
│    │  │            ▼                                          │  │      │
│    │  │   ┌─────────────────────┐                             │  │      │
│    │  │   │ Rechercher un       │                             │  │      │
│    │  │   │ signalement         │                             │  │      │
│    │  │   └─────────────────────┘                             │  │      │
│    │  │                                                       │  │      │
│    │  │   ┌─────────────────────┐                             │  │      │
│    │  │   │ Voir les détails    │                             │  │      │
│    │  │   │ d'un signalement    │                             │  │      │
│    │  │   └─────────────────────┘                             │  │      │
│    │  │            │                                          │  │      │
│    │  │            │ «extend»                                 │  │      │
│    │  │            ▼                                          │  │      │
│    │  │   ┌─────────────────────┐                             │  │      │
│    │  │   │ Supprimer un        │                             │  │      │
│    │  │   │ signalement         │                             │  │      │
│    │  │   └─────────────────────┘                             │  │      │
│    │  │                                                       │  │      │
│    │  └───────────────────────────────────────────────────────┘  │      │
│    └─────────────────────────────────────────────────────────────┘      │
│                                                                          │
│    ┌─────────────────────────────────────────────────────────────┐      │
│    │                    Visualiser sur carte                      │      │
│    │  ┌───────────────────────────────────────────────────────┐  │      │
│    │  │                                                       │  │      │
│    │  │   ┌─────────────────────┐                             │  │      │
│    │  │   │ Afficher la carte   │                             │  │      │
│    │  │   │ des signalements    │                             │  │      │
│    │  │   └─────────────────────┘                             │  │      │
│    │  │            │                                          │  │      │
│    │  │            │ «include»                                │  │      │
│    │  │            ▼                                          │  │      │
│    │  │   ┌─────────────────────┐                             │  │      │
│    │  │   │ Voir les marqueurs  │                             │  │      │
│    │  │   │ colorés par statut  │                             │  │      │
│    │  │   └─────────────────────┘                             │  │      │
│    │  │                                                       │  │      │
│    │  └───────────────────────────────────────────────────────┘  │      │
│    └─────────────────────────────────────────────────────────────┘      │
│                                                                          │
│    ┌─────────────────────────────────────────────────────────────┐      │
│    │                    Configurer l'application                  │      │
│    │  ┌───────────────────────────────────────────────────────┐  │      │
│    │  │                                                       │  │      │
│    │  │   ┌─────────────────────┐                             │  │      │
│    │  │   │ Changer la langue   │                             │  │      │
│    │  │   │ (FR/EN/AR)          │                             │  │      │
│    │  │   └─────────────────────┘                             │  │      │
│    │  │                                                       │  │      │
│    │  └───────────────────────────────────────────────────────┘  │      │
│    └─────────────────────────────────────────────────────────────┘      │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘
                              │
                              │
                    ┌─────────┴─────────┐
                    │                   │
                ┌───┴───┐           ┌───┴───┐
                │       │           │       │
                │ 👤    │           │ ☁️    │
                │Citoyen│           │Firebase│
                │       │           │(Système)│
                └───────┘           └───────┘
                  Actor              Actor
                (Principal)        (Secondaire)
```

### 3.2 Description des cas d'utilisation

| Cas d'utilisation | Acteur | Description |
|-------------------|--------|-------------|
| Créer un signalement | Citoyen | Remplir le formulaire avec titre, description, catégorie, photo et localisation |
| Consulter la liste | Citoyen | Voir tous les signalements créés |
| Filtrer par catégorie | Citoyen | Afficher uniquement une catégorie |
| Rechercher | Citoyen | Trouver un signalement par mots-clés |
| Voir les détails | Citoyen | Afficher les informations complètes |
| Supprimer | Citoyen | Retirer un signalement |
| Afficher la carte | Citoyen | Voir la carte avec les marqueurs |
| Changer la langue | Citoyen | Modifier la langue de l'interface |
| Synchroniser | Firebase | Sauvegarder/récupérer les données cloud |

---

## 4. Diagramme de classes

### 4.1 Diagramme UML complet

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              DOMAIN LAYER                                    │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌──────────────────────┐      ┌──────────────────┐    ┌──────────────────┐ │
│  │       Report         │      │  ReportCategory  │    │   SyncStatus     │ │
│  │    <<data class>>    │      │  <<enumeration>> │    │  <<enumeration>> │ │
│  ├──────────────────────┤      ├──────────────────┤    ├──────────────────┤ │
│  │ - id: String         │      │ CLEANLINESS      │    │ PENDING          │ │
│  │ - title: String      │      │ ROAD             │    │ SYNCED           │ │
│  │ - description: String│──────│ LIGHTING         │    │ FAILED           │ │
│  │ - category           │      │ SAFETY           │    └──────────────────┘ │
│  │ - latitude: Double   │      │ OTHER            │              ▲          │
│  │ - longitude: Double  │      └──────────────────┘              │          │
│  │ - photoUrl: String?  │                                        │          │
│  │ - timestamp: Long    │────────────────────────────────────────┘          │
│  │ - syncStatus         │                                                    │
│  └──────────────────────┘                                                    │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                               DATA LAYER                                     │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌──────────────────────────────────────────────────────────────────────┐   │
│  │                         ReportRepository                              │   │
│  │                          <<@Singleton>>                               │   │
│  ├──────────────────────────────────────────────────────────────────────┤   │
│  │ - reportDao: ReportDao                                                │   │
│  │ - firebaseDataSource: FirebaseDataSource                             │   │
│  ├──────────────────────────────────────────────────────────────────────┤   │
│  │ + getAllReports(): Flow<List<Report>>                                 │   │
│  │ + getReportById(id: String): Report?                                  │   │
│  │ + getReportsByCategory(category: ReportCategory): Flow<List<Report>> │   │
│  │ + createReport(report: Report)                                        │   │
│  │ + deleteReport(id: String)                                            │   │
│  │ + uploadImage(localUri: Uri, reportId: String): Result<String>       │   │
│  │ + syncPendingReports(): Result<Int>                                   │   │
│  │ + getPendingReports(): List<Report>                                   │   │
│  └──────────────────────────────────────────────────────────────────────┘   │
│                          │                    │                              │
│                          ▼                    ▼                              │
│  ┌─────────────────────────────┐  ┌─────────────────────────────┐           │
│  │        ReportDao            │  │    FirebaseDataSource       │           │
│  │       <<interface>>         │  │       <<@Singleton>>        │           │
│  ├─────────────────────────────┤  ├─────────────────────────────┤           │
│  │ + getAllReports()           │  │ - firestore: FirebaseFirest │           │
│  │ + getReportById(id)         │  │ - storage: FirebaseStorage  │           │
│  │ + getReportsByCategory()    │  ├─────────────────────────────┤           │
│  │ + getReportsBySyncStatus()  │  │ + saveReport(Report)        │           │
│  │ + insertReport(entity)      │  │ + uploadImage(Uri, String)  │           │
│  │ + updateReport(entity)      │  │ + getAllReports()           │           │
│  │ + deleteReport(entity)      │  └─────────────────────────────┘           │
│  │ + deleteReportById(id)      │                                            │
│  │ + updateSyncStatus(id, s)   │                                            │
│  └─────────────────────────────┘                                            │
│               │                                                              │
│               ▼                                                              │
│  ┌─────────────────────────────┐  ┌─────────────────────────────┐           │
│  │      ReportEntity           │  │        SyncWorker           │           │
│  │       <<@Entity>>           │  │      <<@HiltWorker>>        │           │
│  ├─────────────────────────────┤  ├─────────────────────────────┤           │
│  │ - id: String <<@PrimaryKey>>│  │ - reportRepository          │           │
│  │ - title: String             │  ├─────────────────────────────┤           │
│  │ - description: String       │  │ + doWork(): Result          │           │
│  │ - category: String          │  └─────────────────────────────┘           │
│  │ - latitude: Double          │                                            │
│  │ - longitude: Double         │                                            │
│  │ - photoUrl: String?         │                                            │
│  │ - timestamp: Long           │                                            │
│  │ - syncStatus: String        │                                            │
│  ├─────────────────────────────┤                                            │
│  │ + toReport(): Report        │                                            │
│  │ + fromReport(r): Entity     │                                            │
│  └─────────────────────────────┘                                            │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                                UI LAYER                                      │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌─────────────────────┐  ┌─────────────────────┐  ┌─────────────────────┐  │
│  │   HomeViewModel     │  │CreateReportViewModel│  │  DetailsViewModel   │  │
│  │  <<@HiltViewModel>> │  │  <<@HiltViewModel>> │  │  <<@HiltViewModel>> │  │
│  ├─────────────────────┤  ├─────────────────────┤  ├─────────────────────┤  │
│  │ - reportRepository  │  │ - reportRepository  │  │ - reportRepository  │  │
│  ├─────────────────────┤  ├─────────────────────┤  ├─────────────────────┤  │
│  │ + reports: StateFlow│  │ + uiState: StateFlow│  │ + report: StateFlow │  │
│  │ + isLoading         │  │ + updateTitle()     │  │ + isLoading         │  │
│  │ + selectedCategory  │  │ + updateDescription │  │ + retrySync()       │  │
│  │ + searchQuery       │  │ + updateCategory()  │  │ + deleteReport()    │  │
│  │ + filterByCategory()│  │ + updateLocation()  │  └─────────────────────┘  │
│  │ + updateSearchQuery │  │ + updatePhoto()     │                           │
│  │ + clearSearch()     │  │ + saveReport()      │  ┌─────────────────────┐  │
│  └─────────────────────┘  │ + clearError()      │  │    MapViewModel     │  │
│                           └─────────────────────┘  │  <<@HiltViewModel>> │  │
│                                                    ├─────────────────────┤  │
│  ┌─────────────────────────────────────────────┐  │ - reportRepository  │  │
│  │           CreateReportUiState               │  ├─────────────────────┤  │
│  │             <<data class>>                  │  │ + reports: StateFlow│  │
│  ├─────────────────────────────────────────────┤  └─────────────────────┘  │
│  │ + title: String                             │                           │
│  │ + description: String                       │                           │
│  │ + category: ReportCategory                  │                           │
│  │ + location: LatLng?                         │                           │
│  │ + photoUri: Uri?                            │                           │
│  │ + isLoading: Boolean                        │                           │
│  │ + error: String?                            │                           │
│  │ + isSaved: Boolean                          │                           │
│  └─────────────────────────────────────────────┘                           │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                              UTIL LAYER                                      │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                       ReportValidator                                │    │
│  │                         <<object>>                                   │    │
│  ├─────────────────────────────────────────────────────────────────────┤    │
│  │ + isValidTitle(title: String): Boolean                               │    │
│  │ + isValidDescription(description: String): Boolean                   │    │
│  │ + isValidCoordinates(latitude: Double, longitude: Double): Boolean  │    │
│  │ + isValidCategory(category: ReportCategory): Boolean                 │    │
│  │ + validateReport(title, desc, lat, lng): Pair<Boolean, String?>     │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 4.2 Relations entre classes

| Relation | Type | Description |
|----------|------|-------------|
| Report → ReportCategory | Composition | Un rapport a une catégorie |
| Report → SyncStatus | Composition | Un rapport a un statut de sync |
| ReportEntity → Report | Conversion | Mapping entre entité et modèle |
| ReportRepository → ReportDao | Dépendance | Accès base locale |
| ReportRepository → FirebaseDataSource | Dépendance | Accès cloud |
| ViewModels → ReportRepository | Dépendance | Injection Hilt |
| SyncWorker → ReportRepository | Dépendance | Synchronisation |

---

## 5. Captures d'écran et fonctionnalités

### 5.1 Écran d'accueil (Liste des signalements)

**[INSÉRER CAPTURE D'ÉCRAN ICI]**

**Description :**
- Affiche la liste de tous les signalements créés
- Barre de recherche en haut pour filtrer par mots-clés
- Chips de filtrage par catégorie
- Chaque carte affiche :
  - Titre du signalement
  - Catégorie avec icône
  - Date de création
  - Indicateur de statut de synchronisation (vert/orange/rouge)
- Bouton flottant (+) pour créer un nouveau signalement
- Navigation vers la carte et les paramètres

### 5.2 Écran de création de signalement

**[INSÉRER CAPTURE D'ÉCRAN ICI]**

**Description :**
- Formulaire complet pour créer un signalement
- Champs :
  - Titre (obligatoire)
  - Description (obligatoire)
  - Catégorie (menu déroulant)
- Section photo :
  - Bouton pour prendre une photo
  - Bouton pour choisir depuis la galerie
  - Aperçu de la photo sélectionnée
- Section localisation :
  - Mini-carte avec le marqueur de position
  - Bouton "Localiser" pour capturer le GPS actuel
  - Possibilité de déplacer le marqueur manuellement
- Bouton "Enregistrer" pour sauvegarder

### 5.3 Écran carte (Google Maps)

**[INSÉRER CAPTURE D'ÉCRAN ICI]**

**Description :**
- Carte Google Maps interactive plein écran
- Marqueurs colorés selon le statut de synchronisation :
  - 🟢 Vert = Synchronisé (SYNCED)
  - 🟠 Orange = En attente (PENDING)
  - 🔴 Rouge = Échec (FAILED)
- Clic sur un marqueur affiche une infobulle avec le titre
- Contrôles de zoom (+/-)
- Bouton de recentrage sur la position actuelle

### 5.4 Écran détails du signalement

**[INSÉRER CAPTURE D'ÉCRAN ICI]**

**Description :**
- Affichage complet des informations du signalement
- Photo en grand format (clic pour plein écran)
- Titre et description
- Catégorie avec icône
- Date et heure de création
- Mini-carte avec le marqueur de localisation
- Indicateur de statut de synchronisation
- Bouton "Réessayer la synchronisation" si échec
- Bouton "Supprimer" le signalement

### 5.5 Interface en Français

**[INSÉRER CAPTURE D'ÉCRAN ICI]**

**Description :**
- Démonstration de l'interface en langue française
- Tous les textes sont traduits
- Navigation intuitive

### 5.6 Interface en Arabe (RTL)

**[INSÉRER CAPTURE D'ÉCRAN ICI]**

**Description :**
- Démonstration du support RTL (Right-to-Left)
- Interface miroir pour l'arabe
- Alignement du texte à droite
- Navigation inversée
- Tous les 70+ textes traduits en arabe

---

## 6. Défis rencontrés et solutions

### 6.1 Synchronisation hors-ligne

**Défi :**
Assurer que l'application fonctionne correctement sans connexion internet et synchronise les données une fois la connexion rétablie.

**Solution :**
- Architecture "Offline-First" avec Room comme base de données principale
- WorkManager pour la synchronisation en arrière-plan
- Statut de synchronisation (PENDING, SYNCED, FAILED) pour suivre l'état
- Retry automatique en cas d'échec

### 6.2 Gestion des permissions Android

**Défi :**
Demander les permissions (Caméra, GPS, Stockage) de manière fluide sans bloquer l'utilisateur.

**Solution :**
- Utilisation de Accompanist Permissions pour Compose
- Demande contextuelle des permissions uniquement au moment nécessaire
- Messages explicatifs pour l'utilisateur
- Fallback gracieux si permission refusée

### 6.3 Support multilingue avec RTL

**Défi :**
Supporter l'arabe avec son écriture de droite à gauche (RTL) tout en gardant une UI cohérente.

**Solution :**
- Configuration `android:supportsRtl="true"` dans le manifest
- Utilisation de `start/end` au lieu de `left/right` dans les layouts
- LocaleHelper pour le changement dynamique de langue
- Tests manuels sur chaque écran en mode RTL

### 6.4 Intégration Google Maps avec Compose

**Défi :**
Intégrer Google Maps dans une application 100% Jetpack Compose.

**Solution :**
- Utilisation de la bibliothèque `maps-compose` officielle de Google
- Gestion des marqueurs via des Composables
- StateFlow pour la réactivité des données sur la carte

### 6.5 Gestion des photos volumineuses

**Défi :**
Les photos prises par la caméra peuvent être très volumineuses et ralentir l'upload.

**Solution :**
- Compression des images avant upload
- Utilisation de Coil pour le chargement efficace des images
- Upload asynchrone vers Firebase Storage
- Placeholder pendant le chargement

### 6.6 Injection de dépendances avec Hilt

**Défi :**
Configurer correctement Hilt avec Room, Firebase et WorkManager.

**Solution :**
- Modules Hilt séparés (AppModule, FirebaseModule)
- @AssistedInject pour le SyncWorker
- Configuration personnalisée de WorkManager
- Tests avec HiltAndroidTest

---

## Annexes

### A. Glossaire

| Terme | Définition |
|-------|------------|
| **Room** | Bibliothèque de persistance SQLite pour Android |
| **Firebase** | Plateforme cloud de Google |
| **Firestore** | Base de données NoSQL temps réel |
| **Hilt** | Framework d'injection de dépendances Android |
| **WorkManager** | API pour tâches asynchrones garanties |
| **RTL** | Right-to-Left (écriture de droite à gauche) |
| **StateFlow** | Flux de données observable Kotlin |
| **Compose** | Framework UI déclaratif Android |

### B. Ressources

- [Documentation Android officielle](https://developer.android.com)
- [Firebase Documentation](https://firebase.google.com/docs)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Google Maps SDK](https://developers.google.com/maps/documentation/android-sdk)

---

*Document généré pour le projet CityReport - Janvier 2026*
