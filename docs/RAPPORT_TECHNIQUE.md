# Rapport de Spécifications Techniques
## Application CityReport

---

**Projet :** Application Android de signalement d'incidents urbains
**Date :** Janvier 2026
**Version :** 1.0

---

## Table des matières

1. [Choix techniques et justifications](#1-choix-techniques-et-justifications)
2. [Liste des tests unitaires](#2-liste-des-tests-unitaires)
3. [Contributions de l'équipe](#3-contributions-de-léquipe)
4. [Sources et documentation](#4-sources-et-documentation)

---

## 1. Choix techniques et justifications

### 1.1 Langage de programmation

| Technologie | Version | Justification |
|-------------|---------|---------------|
| **Kotlin** | 2.0.21 | Langage officiel recommandé par Google pour Android. Plus concis que Java, null-safety intégrée, coroutines pour l'asynchrone, interopérabilité Java complète. |

**Alternatives considérées :**
- **Java** : Plus verbeux, pas de null-safety native, moins moderne
- **Dart/Flutter** : Cross-platform mais courbe d'apprentissage différente

**Pourquoi Kotlin :** Kotlin est le langage officiel Android depuis 2019. Il réduit le code boilerplate de 40%, offre une sécurité null native, et les coroutines simplifient le code asynchrone.

---

### 1.2 Framework UI

| Technologie | Version | Justification |
|-------------|---------|---------------|
| **Jetpack Compose** | BOM 2024.09.00 | Framework UI déclaratif moderne remplaçant les layouts XML. Moins de code, prévisualisation en temps réel, intégration native Kotlin. |
| **Material 3** | Dernière | Design system officiel Google avec thèmes dynamiques et composants modernes. |

**Alternatives considérées :**
- **XML Layouts + View Binding** : Approche traditionnelle mais plus de code, séparation logique/UI moins claire
- **React Native** : Cross-platform mais performance moindre sur Android

**Pourquoi Compose :** Compose réduit le code UI de 50%, élimine les bugs liés au cycle de vie des Views, et offre une prévisualisation instantanée dans Android Studio.

---

### 1.3 Base de données locale

| Technologie | Version | Justification |
|-------------|---------|---------------|
| **Room** | 2.6.1 | Abstraction SQLite officielle Google avec vérification des requêtes à la compilation, support des coroutines et Flow. |

**Alternatives considérées :**
- **SQLite brut** : Plus complexe, pas de vérification à la compilation
- **Realm** : Performant mais moins intégré à l'écosystème Android
- **ObjectBox** : Moins documenté, communauté plus petite

**Pourquoi Room :**
```kotlin
// Exemple de DAO avec Room - Simple et typé
@Dao
interface ReportDao {
    @Query("SELECT * FROM reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<ReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ReportEntity)
}
```
Room vérifie les requêtes SQL à la compilation, évitant les erreurs runtime. L'intégration avec Flow permet une UI réactive automatique.

---

### 1.4 Base de données cloud

| Technologie | Version | Justification |
|-------------|---------|---------------|
| **Firebase Firestore** | BOM 32.7.0 | Base NoSQL temps réel, synchronisation automatique, SDK Android natif, plan gratuit généreux. |
| **Firebase Storage** | BOM 32.7.0 | Stockage de fichiers (photos) avec CDN intégré, règles de sécurité. |

**Alternatives considérées :**
- **Supabase** : Open-source mais SDK Android moins mature
- **AWS Amplify** : Plus complexe à configurer, courbe d'apprentissage plus longue
- **Backend custom (Node.js/Python)** : Nécessite hébergement et maintenance

**Pourquoi Firebase :**
- Gratuit jusqu'à 1GB de stockage et 50K lectures/jour
- SDK Android officiel bien documenté
- Console web pour visualiser les données
- Authentification intégrée (extensibilité future)

---

### 1.5 Injection de dépendances

| Technologie | Version | Justification |
|-------------|---------|---------------|
| **Hilt** | 2.48 | Framework DI officiel Android basé sur Dagger, intégration native ViewModels et WorkManager. |

**Alternatives considérées :**
- **Koin** : Plus simple mais moins performant (réflexion runtime)
- **Dagger 2** : Plus puissant mais configuration complexe
- **Manual DI** : Pas maintenable à l'échelle

**Pourquoi Hilt :**
```kotlin
// Injection simple avec Hilt
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val reportRepository: ReportRepository
) : ViewModel()
```
Hilt génère le code à la compilation (pas de réflexion), s'intègre avec les composants Android, et simplifie Dagger.

---

### 1.6 Cartes interactives

| Technologie | Version | Justification |
|-------------|---------|---------------|
| **Google Maps Compose** | 4.3.0 | Bibliothèque officielle pour intégrer Google Maps dans Jetpack Compose. |

**Alternatives considérées :**
- **OpenStreetMap (OSMDroid)** : Gratuit mais moins de fonctionnalités, UI moins moderne
- **Mapbox** : Payant au-delà d'un certain usage

**Pourquoi Google Maps :**
- Gratuit jusqu'à 28 000 chargements/mois
- Meilleure qualité de données cartographiques
- API Composable native
- Documentation extensive

---

### 1.7 Synchronisation en arrière-plan

| Technologie | Version | Justification |
|-------------|---------|---------------|
| **WorkManager** | 2.9.0 | API officielle pour tâches asynchrones garanties, survit aux redémarrages, respecte les contraintes batterie. |

**Alternatives considérées :**
- **JobScheduler** : Plus bas niveau, moins de fonctionnalités
- **AlarmManager** : Pas fait pour ce cas d'usage
- **Services en avant-plan** : Consomme plus de batterie

**Pourquoi WorkManager :**
```kotlin
// Synchronisation garantie même après redémarrage
val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(
    15, TimeUnit.MINUTES
).setConstraints(
    Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
).build()
```
WorkManager garantit l'exécution des tâches même si l'app est fermée ou le téléphone redémarré.

---

### 1.8 Chargement d'images

| Technologie | Version | Justification |
|-------------|---------|---------------|
| **Coil** | 2.5.0 | Bibliothèque de chargement d'images Kotlin-first, légère, support Compose natif. |

**Alternatives considérées :**
- **Glide** : Plus ancien, Java-first, plus lourd
- **Picasso** : Moins maintenu, moins de fonctionnalités

**Pourquoi Coil :**
```kotlin
// Chargement simple avec Coil
AsyncImage(
    model = report.photoUrl,
    contentDescription = "Photo du signalement",
    placeholder = painterResource(R.drawable.placeholder)
)
```
Coil est 2x plus léger que Glide, écrit en Kotlin, et offre une intégration Compose native.

---

### 1.9 Navigation

| Technologie | Version | Justification |
|-------------|---------|---------------|
| **Navigation Compose** | 2.8.0 | Bibliothèque officielle de navigation pour Compose, type-safe, gestion du back stack. |

**Pourquoi Navigation Compose :**
- Intégration native avec Compose
- Arguments typés entre écrans
- Gestion automatique du back stack
- Deep links supportés

---

### 1.10 Gestion des permissions

| Technologie | Version | Justification |
|-------------|---------|---------------|
| **Accompanist Permissions** | 0.32.0 | Bibliothèque Google pour gérer les permissions Android dans Compose. |

**Pourquoi Accompanist :**
Simplifie la gestion des permissions avec une API déclarative compatible Compose.

---

### 1.11 Préférences utilisateur

| Technologie | Version | Justification |
|-------------|---------|---------------|
| **DataStore** | 1.0.0 | Remplacement moderne de SharedPreferences, basé sur coroutines et Flow. |

**Alternatives considérées :**
- **SharedPreferences** : API synchrone bloquante, pas type-safe

**Pourquoi DataStore :**
DataStore est asynchrone (pas de blocage UI), type-safe avec Protocol Buffers, et gère les migrations.

---

### 1.12 Récapitulatif des dépendances

```kotlin
// gradle/libs.versions.toml
[versions]
kotlin = "2.0.21"
compose-bom = "2024.09.00"
room = "2.6.1"
hilt = "2.48"
firebase-bom = "32.7.0"
maps-compose = "4.3.0"
work = "2.9.0"
coil = "2.5.0"
navigation = "2.8.0"
datastore = "1.0.0"

[libraries]
# UI
compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "compose-bom" }
compose-material3 = { group = "androidx.compose.material3", name = "material3" }

# Database
room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }

# Firebase
firebase-bom = { group = "com.google.firebase", name = "firebase-bom", version.ref = "firebase-bom" }
firebase-firestore = { group = "com.google.firebase", name = "firebase-firestore-ktx" }
firebase-storage = { group = "com.google.firebase", name = "firebase-storage-ktx" }

# DI
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }

# Maps
maps-compose = { group = "com.google.maps.android", name = "maps-compose", version.ref = "maps-compose" }

# Background
work-runtime = { group = "androidx.work", name = "work-runtime-ktx", version.ref = "work" }

# Images
coil-compose = { group = "io.coil-kt", name = "coil-compose", version.ref = "coil" }
```

---

## 2. Liste des tests unitaires

### 2.1 Vue d'ensemble

| Catégorie | Fichier | Nombre de tests |
|-----------|---------|-----------------|
| Repository | ReportRepositoryTest.kt | 6 |
| ViewModel | CreateReportViewModelTest.kt | 12 |
| Worker | SyncWorkerTest.kt | 4 |
| Validation | ReportValidatorTest.kt | 19 |
| **Total** | | **41** |

---

### 2.2 Tests du Repository (ReportRepositoryTest.kt)

| Test | Description | Justification |
|------|-------------|---------------|
| `getAllReports_returnsFlowOfReports` | Vérifie que getAllReports retourne un Flow réactif | Assure la réactivité de l'UI |
| `getReportById_existingId_returnsReport` | Vérifie la récupération par ID | Fonctionnalité critique pour les détails |
| `getReportById_nonExistingId_returnsNull` | Vérifie le cas d'un ID inexistant | Gestion d'erreur |
| `createReport_savesWithPendingStatus` | Vérifie que les nouveaux rapports sont PENDING | Logique offline-first |
| `deleteReport_removesFromDatabase` | Vérifie la suppression | Fonctionnalité utilisateur |
| `syncPendingReports_updatesStatusToSynced` | Vérifie la synchronisation | Cœur de l'architecture |

---

### 2.3 Tests du ViewModel (CreateReportViewModelTest.kt)

| Test | Description | Justification |
|------|-------------|---------------|
| `initialState_hasDefaultValues` | État initial correct | Comportement prévisible |
| `updateTitle_updatesUiState` | Mise à jour du titre | Binding bidirectionnel |
| `updateDescription_updatesUiState` | Mise à jour de la description | Binding bidirectionnel |
| `updateCategory_updatesUiState` | Mise à jour de la catégorie | Sélection catégorie |
| `updateLocation_updatesUiState` | Mise à jour de la localisation | Capture GPS |
| `updatePhoto_updatesUiState` | Mise à jour de la photo | Capture photo |
| `clearPhoto_removesPhoto` | Suppression de la photo | UX utilisateur |
| `saveReport_withEmptyTitle_setsError` | Validation titre vide | Validation formulaire |
| `saveReport_withEmptyDescription_setsError` | Validation description vide | Validation formulaire |
| `saveReport_withNoLocation_setsError` | Validation localisation manquante | Validation formulaire |
| `saveReport_withValidData_savesAndSetsIsSaved` | Sauvegarde réussie | Scénario nominal |
| `clearError_removesError` | Effacement des erreurs | UX utilisateur |

---

### 2.4 Tests du Worker (SyncWorkerTest.kt)

| Test | Description | Justification |
|------|-------------|---------------|
| `doWork_withPendingReports_syncsSuccessfully` | Synchronisation réussie | Scénario nominal |
| `doWork_withNoReports_returnsSuccess` | Pas de rapports à sync | Cas limite |
| `doWork_withNetworkError_returnsRetry` | Erreur réseau → retry | Résilience |
| `doWork_withFirebaseError_returnsRetry` | Erreur Firebase → retry | Résilience |

---

### 2.5 Tests du Validateur (ReportValidatorTest.kt)

| Test | Description | Justification |
|------|-------------|---------------|
| `isValidTitle_withNonEmptyString_returnsTrue` | Titre valide | Cas nominal |
| `isValidTitle_withEmptyString_returnsFalse` | Titre vide | Validation |
| `isValidTitle_withBlankString_returnsFalse` | Titre espaces | Validation |
| `isValidDescription_withNonEmptyString_returnsTrue` | Description valide | Cas nominal |
| `isValidDescription_withEmptyString_returnsFalse` | Description vide | Validation |
| `isValidDescription_withBlankString_returnsFalse` | Description espaces | Validation |
| `isValidCoordinates_withValidLatLng_returnsTrue` | Coordonnées valides | Cas nominal |
| `isValidCoordinates_withLatitudeTooLow_returnsFalse` | Latitude < -90 | Limites GPS |
| `isValidCoordinates_withLatitudeTooHigh_returnsFalse` | Latitude > 90 | Limites GPS |
| `isValidCoordinates_withLongitudeTooLow_returnsFalse` | Longitude < -180 | Limites GPS |
| `isValidCoordinates_withLongitudeTooHigh_returnsFalse` | Longitude > 180 | Limites GPS |
| `isValidCoordinates_withBoundaryValues_returnsTrue` | Valeurs limites | Edge cases |
| `isValidCategory_withAnyCategory_returnsTrue` | Toute catégorie valide | Enum safety |
| `validateReport_withValidData_returnsValid` | Rapport complet valide | Cas nominal |
| `validateReport_withInvalidTitle_returnsInvalid` | Titre invalide | Validation |
| `validateReport_withInvalidDescription_returnsInvalid` | Description invalide | Validation |
| `validateReport_withNullLatitude_returnsInvalid` | Latitude null | Validation |
| `validateReport_withNullLongitude_returnsInvalid` | Longitude null | Validation |
| `validateReport_withInvalidCoordinates_returnsInvalid` | Coordonnées invalides | Validation |

---

### 2.6 Tests UI (Instrumented Tests)

| Fichier | Tests | Description |
|---------|-------|-------------|
| HomeScreenTest.kt | 7 | Navigation, affichage liste, recherche, filtres |
| CreateReportScreenTest.kt | 12 | Formulaire, validation, permissions, sauvegarde |

---

### 2.7 Frameworks de test utilisés

| Framework | Version | Usage |
|-----------|---------|-------|
| **JUnit 4** | 4.13.2 | Structure des tests |
| **Mockk** | 1.13.8 | Mocking Kotlin-friendly |
| **Kotlinx Coroutines Test** | 1.7.3 | Tests asynchrones |
| **Turbine** | 1.0.0 | Tests de Flow |
| **Hilt Testing** | 2.48 | Tests avec DI |
| **Espresso** | 3.7.0 | Tests UI |
| **Compose Testing** | BOM | Tests Compose |

---

## 3. Contributions de l'équipe

### 3.1 Graphe des contributions GitHub

**[INSÉRER CAPTURE DU GRAPHE GITHUB ICI]**

Pour obtenir le graphe :
1. Aller sur GitHub → Repository → Insights → Contributors
2. Capturer le graphe des contributions

### 3.2 Répartition des tâches

| Membre | Rôle | Contributions principales |
|--------|------|---------------------------|
| **[Nom 1]** | Lead Developer | Architecture, Room, Repository, Tests |
| **[Nom 2]** | UI Developer | Compose UI, Thème, Navigation |
| **[Nom 3]** | Backend/Cloud | Firebase, WorkManager, Sync |
| **[Nom 4]** | Intégration | Google Maps, Permissions, i18n |

### 3.3 Statistiques du projet

| Métrique | Valeur |
|----------|--------|
| Commits totaux | [À compléter] |
| Fichiers Kotlin | 29 |
| Lignes de code | ~3500 |
| Tests unitaires | 41 |
| Tests UI | 19 |
| Langues supportées | 3 |

---

## 4. Sources et documentation

### 4.1 Documentation officielle

| Ressource | URL | Usage |
|-----------|-----|-------|
| Android Developers | https://developer.android.com | Documentation officielle Android |
| Jetpack Compose | https://developer.android.com/jetpack/compose | Guide UI Compose |
| Room Database | https://developer.android.com/training/data-storage/room | Persistance locale |
| Hilt | https://developer.android.com/training/dependency-injection/hilt-android | Injection de dépendances |
| WorkManager | https://developer.android.com/topic/libraries/architecture/workmanager | Tâches en arrière-plan |
| Firebase | https://firebase.google.com/docs/android/setup | Configuration Firebase |
| Firestore | https://firebase.google.com/docs/firestore/quickstart | Base de données cloud |
| Google Maps | https://developers.google.com/maps/documentation/android-sdk | Cartes interactives |

### 4.2 Tutoriels et guides

| Ressource | URL | Sujet |
|-----------|-----|-------|
| Codelabs Android | https://developer.android.com/courses | Cours officiels Google |
| Philipp Lackner | https://www.youtube.com/@PhilippLackner | Tutoriels Compose/Kotlin |
| Coding with Mitch | https://codingwithmitch.com | Architecture Android |
| Firebase YouTube | https://www.youtube.com/firebase | Intégration Firebase |

### 4.3 Articles et références

| Article | Source | Sujet |
|---------|--------|-------|
| Guide to app architecture | Android Developers | Architecture MVVM |
| Room with a View | Android Codelabs | Room + LiveData/Flow |
| Hilt in multi-module apps | Android Developers | DI avancée |
| Offline-first apps | Medium/Android | Stratégie offline |

### 4.4 Bibliothèques tierces

| Bibliothèque | GitHub | Documentation |
|--------------|--------|---------------|
| Coil | https://github.com/coil-kt/coil | https://coil-kt.github.io/coil/ |
| Accompanist | https://github.com/google/accompanist | https://google.github.io/accompanist/ |
| Maps Compose | https://github.com/googlemaps/android-maps-compose | README GitHub |

### 4.5 Outils de développement

| Outil | Version | Usage |
|-------|---------|-------|
| Android Studio | Hedgehog 2023.1.1+ | IDE |
| Gradle | 8.13.2 | Build system |
| Git | Dernière | Versioning |
| Firebase Console | Web | Gestion cloud |
| Google Cloud Console | Web | API Maps |

---

## Annexes

### A. Configuration Gradle complète

```kotlin
// app/build.gradle.kts
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.cityreport"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.cityreport"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
```

### B. Structure des fichiers de test

```
app/src/test/java/com/cityreport/
├── data/
│   ├── repository/
│   │   └── ReportRepositoryTest.kt
│   └── worker/
│       └── SyncWorkerTest.kt
├── ui/
│   └── create/
│       └── CreateReportViewModelTest.kt
└── util/
    └── ReportValidatorTest.kt

app/src/androidTest/java/com/cityreport/
└── ui/
    ├── home/
    │   └── HomeScreenTest.kt
    └── create/
        └── CreateReportScreenTest.kt
```

---

*Document généré pour le projet CityReport - Janvier 2026*
