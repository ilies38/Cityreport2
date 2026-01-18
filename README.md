# CityReport

**Application Android de signalement d'incidents urbains**

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-blue.svg)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.09-blueviolet.svg)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-Academic-lightgrey.svg)]()

---

## Sommaire

- [Presentation](#presentation)
- [Fonctionnalites](#fonctionnalites)
- [Captures d'ecran](#captures-decran)
- [Architecture](#architecture)
- [Technologies](#technologies)
- [Installation](#installation)
- [Configuration](#configuration)
- [Utilisation](#utilisation)
- [Tests](#tests)
- [Structure du projet](#structure-du-projet)
- [Auteurs](#auteurs)

---

## Presentation

CityReport est une application mobile Android permettant aux citoyens de signaler des incidents urbains dans leur ville. L'application fonctionne en mode **offline-first** : les signalements sont d'abord sauvegardés localement puis synchronises automatiquement avec le cloud lorsqu'une connexion est disponible.

### Objectifs du projet

- Permettre aux citoyens de signaler facilement des problemes urbains
- Geolocaliser precisement les incidents avec GPS
- Documenter visuellement les problemes via des photos
- Garantir la disponibilite des donnees meme hors connexion
- Offrir une interface multilingue accessible

### Public cible

- Citoyens souhaitant signaler des problemes dans leur quartier
- Municipalites collectant les signalements citoyens
- Services techniques de maintenance urbaine

---

## Fonctionnalites

### Gestion des signalements

| Fonctionnalite | Description |
|----------------|-------------|
| **Creation** | Formulaire complet avec titre, description, categorie, photo et localisation GPS |
| **Liste** | Affichage de tous les rapports avec recherche textuelle et filtres par categorie |
| **Details** | Vue complete avec photo en plein ecran et mini-carte de localisation |
| **Suppression** | Possibilite de supprimer un signalement |

### Carte interactive

- Visualisation de tous les signalements sur Google Maps
- Marqueurs colores selon le statut de synchronisation :
  - Vert : Synchronise
  - Orange : En attente
  - Rouge : Echec
- Navigation vers les details depuis la carte

### Synchronisation cloud

- Mode **offline-first** : fonctionne sans connexion internet
- Sauvegarde locale avec Room Database
- Synchronisation automatique avec Firebase (Firestore + Storage)
- WorkManager pour la synchronisation en arriere-plan
- Indicateurs visuels du statut de synchronisation

### Internationalisation

L'application supporte 3 langues avec changement dynamique :

| Langue | Support RTL |
|--------|-------------|
| Francais | Non |
| English | Non |
| العربية (Arabe) | Oui |

### Interface utilisateur

- Design Material 3 moderne
- Mode sombre automatique selon les preferences systeme
- Interface responsive et intuitive

---

## Captures d'ecran

| Accueil | Creation | Carte | Details |
|---------|----------|-------|---------|
| ![Home](docs/screenshots/home.png) | ![Create](docs/screenshots/create.png) | ![Map](docs/screenshots/map.png) | ![Details](docs/screenshots/details.png) |

> Les captures d'ecran sont a ajouter dans le dossier `docs/screenshots/`

---

## Architecture

L'application suit l'architecture **MVVM (Model-View-ViewModel)** avec le pattern **Repository** pour la gestion des donnees.

### Diagramme d'architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        UI LAYER                             │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐         │
│  │ HomeScreen  │  │CreateScreen │  │  MapScreen  │   ...   │
│  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘         │
│         │                │                │                 │
│  ┌──────▼──────┐  ┌──────▼──────┐  ┌──────▼──────┐         │
│  │ HomeViewModel│  │CreateViewModel│ │ MapViewModel│        │
│  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘         │
└─────────┼────────────────┼────────────────┼─────────────────┘
          └────────────────┼────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                       DATA LAYER                            │
│                 ┌─────────────────┐                         │
│                 │ ReportRepository │                        │
│                 └────────┬────────┘                         │
│            ┌─────────────┴─────────────┐                    │
│     ┌──────▼──────┐            ┌───────▼───────┐            │
│     │  ReportDao  │            │FirebaseDataSource│         │
│     │   (Room)    │            │  (Firestore)  │            │
│     └─────────────┘            └───────────────┘            │
└─────────────────────────────────────────────────────────────┘
```

### Flux de synchronisation

```
1. Utilisateur cree un signalement
              │
              ▼
2. Sauvegarde locale (Room) avec status = PENDING
              │
              ▼
3. WorkManager detecte les rapports PENDING
              │
              ▼
4. Tentative de synchronisation Firebase
              │
       ┌──────┴──────┐
       ▼             ▼
   Succes         Echec
       │             │
       ▼             ▼
 Status=SYNCED  Status=FAILED
                     │
                     ▼
              Retry automatique
```

---

## Technologies

### Stack technique

| Categorie | Technologie | Version | Description |
|-----------|-------------|---------|-------------|
| **Langage** | Kotlin | 2.0.21 | Langage principal |
| **UI** | Jetpack Compose | BOM 2024.09 | Framework UI declaratif |
| **Design** | Material 3 | Latest | Design system Google |
| **DI** | Hilt | 2.48 | Injection de dependances |
| **DB Locale** | Room | 2.6.1 | Base de donnees SQLite |
| **DB Cloud** | Firebase Firestore | BOM 32.7.0 | Base NoSQL temps reel |
| **Storage** | Firebase Storage | BOM 32.7.0 | Stockage des photos |
| **Cartes** | Google Maps Compose | 4.3.0 | Cartes interactives |
| **Background** | WorkManager | 2.9.0 | Taches en arriere-plan |
| **Images** | Coil | 2.5.0 | Chargement d'images |
| **Navigation** | Navigation Compose | 2.8.0 | Navigation entre ecrans |
| **Preferences** | DataStore | 1.0.0 | Stockage preferences |
| **Permissions** | Accompanist | 0.32.0 | Gestion permissions |

### Outils de test

| Outil | Usage |
|-------|-------|
| JUnit 4 | Tests unitaires |
| Mockk | Mocking Kotlin |
| Espresso | Tests UI |
| Compose Testing | Tests Compose |
| Turbine | Tests Flow |

---

## Installation

### Prerequis

- **Android Studio** Hedgehog (2023.1.1) ou superieur
- **JDK** 17
- **Android SDK** 26 (minimum) / 36 (target)
- **Compte Firebase** avec projet configure
- **Cle API Google Maps**

### Etapes d'installation

#### 1. Cloner le repository

```bash
git clone https://github.com/[username]/Cityreport2.git
cd Cityreport2
```

#### 2. Configurer Firebase

1. Creer un projet sur [Firebase Console](https://console.firebase.google.com)
2. Ajouter une application Android avec le package `com.cityreport`
3. Telecharger le fichier `google-services.json`
4. Placer le fichier dans le dossier `app/`
5. Activer **Firestore Database** dans Firebase Console
6. Activer **Storage** dans Firebase Console

#### 3. Configurer Google Maps

1. Aller sur [Google Cloud Console](https://console.cloud.google.com)
2. Creer un projet ou selectionner un projet existant
3. Activer l'API **Maps SDK for Android**
4. Creer une cle API
5. Creer/modifier le fichier `local.properties` a la racine :

```properties
sdk.dir=C:\\Users\\[username]\\AppData\\Local\\Android\\Sdk
MAPS_API_KEY=votre_cle_api_google_maps
```

#### 4. Build et execution

```bash
# Build debug
./gradlew assembleDebug

# Installer sur appareil/emulateur
./gradlew installDebug
```

---

## Configuration

### Fichiers de configuration

| Fichier | Emplacement | Description |
|---------|-------------|-------------|
| `google-services.json` | `app/` | Configuration Firebase |
| `local.properties` | Racine | Cle API Google Maps |
| `build.gradle.kts` | `app/` | Dependances et configuration build |
| `libs.versions.toml` | `gradle/` | Catalogue des versions |

### Permissions requises

L'application necessite les permissions suivantes :

```xml
<!-- Internet et reseau -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

<!-- Localisation -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

<!-- Camera et stockage -->
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
```

---

## Utilisation

### Creer un signalement

1. Appuyer sur le bouton **+** depuis l'ecran d'accueil
2. Remplir le titre et la description
3. Selectionner une categorie :
   - **Proprete** : Dechets, graffitis, depots sauvages
   - **Voirie** : Nids de poule, trottoirs abimes
   - **Eclairage** : Lampadaires en panne
   - **Securite** : Dangers, obstacles
   - **Autre** : Autres problemes
4. Ajouter une photo (optionnel) via camera ou galerie
5. La localisation GPS est capturee automatiquement
6. Appuyer sur **Enregistrer**

### Consulter les signalements

- **Liste** : Voir tous les signalements avec leur statut
- **Recherche** : Filtrer par mots-cles
- **Filtres** : Afficher par categorie
- **Carte** : Visualiser sur Google Maps

### Changer la langue

1. Aller dans **Parametres** (icone engrenage)
2. Selectionner la langue souhaitee
3. L'application redemarrera avec la nouvelle langue

---

## Tests

### Tests unitaires (41 tests)

```bash
./gradlew test
```

| Fichier | Tests | Description |
|---------|-------|-------------|
| `ReportValidatorTest` | 19 | Validation des donnees |
| `CreateReportViewModelTest` | 12 | Logique de creation |
| `ReportRepositoryTest` | 6 | Couche donnees |
| `SyncWorkerTest` | 4 | Synchronisation |

### Tests UI (19 tests)

```bash
./gradlew connectedAndroidTest
```

| Fichier | Tests | Description |
|---------|-------|-------------|
| `CreateReportScreenTest` | 12 | Ecran de creation |
| `HomeScreenTest` | 7 | Ecran d'accueil |

### Couverture

Les tests couvrent :
- Validation des champs du formulaire
- Logique des ViewModels
- Operations du Repository
- Synchronisation en arriere-plan
- Interactions UI principales

---

## Structure du projet

```
Cityreport2/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/cityreport/
│   │   │   │   ├── data/
│   │   │   │   │   ├── local/           # Room (Entity, DAO, Database)
│   │   │   │   │   ├── remote/          # Firebase DataSource
│   │   │   │   │   ├── repository/      # ReportRepository
│   │   │   │   │   └── worker/          # SyncWorker
│   │   │   │   ├── di/                  # Modules Hilt
│   │   │   │   ├── domain/
│   │   │   │   │   └── model/           # Report, ReportCategory, SyncStatus
│   │   │   │   ├── ui/
│   │   │   │   │   ├── create/          # Ecran creation
│   │   │   │   │   ├── details/         # Ecran details
│   │   │   │   │   ├── home/            # Ecran accueil
│   │   │   │   │   ├── map/             # Ecran carte
│   │   │   │   │   ├── navigation/      # NavGraph
│   │   │   │   │   ├── settings/        # Ecran parametres
│   │   │   │   │   └── theme/           # Material Theme
│   │   │   │   ├── util/                # Utilitaires
│   │   │   │   ├── CityReportApp.kt     # Application
│   │   │   │   └── MainActivity.kt      # Activity principale
│   │   │   └── res/
│   │   │       ├── values/              # Strings EN
│   │   │       ├── values-fr/           # Strings FR
│   │   │       └── values-ar/           # Strings AR
│   │   ├── test/                        # Tests unitaires
│   │   └── androidTest/                 # Tests UI
│   ├── build.gradle.kts
│   └── google-services.json
├── docs/                                # Documentation
├── gradle/
│   └── libs.versions.toml               # Catalogue versions
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

### Modele de donnees

```kotlin
data class Report(
    val id: String,              // Identifiant unique (UUID)
    val title: String,           // Titre du signalement
    val description: String,     // Description detaillee
    val category: ReportCategory,// Categorie (enum)
    val latitude: Double,        // Coordonnee GPS
    val longitude: Double,       // Coordonnee GPS
    val photoUrl: String?,       // URL de la photo (nullable)
    val timestamp: Long,         // Date de creation
    val syncStatus: SyncStatus   // Statut de synchronisation
)

enum class ReportCategory {
    CLEANLINESS,  // Proprete
    ROAD,         // Voirie
    LIGHTING,     // Eclairage
    SAFETY,       // Securite
    OTHER         // Autre
}

enum class SyncStatus {
    PENDING,      // En attente
    SYNCED,       // Synchronise
    FAILED        // Echec
}
```

---

## Auteurs

Projet academique realise dans le cadre du cours de developpement mobile Android.

| Membre | Role |
|--------|------|
| [Nom 1] | [Role] |
| [Nom 2] | [Role] |
| [Nom 3] | [Role] |

**Annee universitaire** : 2024/2025

---

## Licence

Ce projet est developpe dans un cadre academique. Tous droits reserves.

---

## Ressources

### Documentation officielle

- [Android Developers](https://developer.android.com)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Firebase Documentation](https://firebase.google.com/docs)
- [Google Maps SDK](https://developers.google.com/maps/documentation/android-sdk)

### Guides utilises

- [Room Database Guide](https://developer.android.com/training/data-storage/room)
- [Hilt Dependency Injection](https://developer.android.com/training/dependency-injection/hilt-android)
- [WorkManager Guide](https://developer.android.com/topic/libraries/architecture/workmanager)
