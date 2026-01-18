# SPÉCIFICATIONS CITY REPORT - PROJET ACADÉMIQUE

## CONTRAINTES ABSOLUES
- Date limite : 18/01/2026
- Kotlin + Jetpack Compose UNIQUEMENT
- Room (local) + Firebase (remote) OBLIGATOIRE
- Tests unitaires + UI OBLIGATOIRES
- 3 langues : FR/EN/AR (RTL pour arabe)
- Architecture MVVM + Repository
- Hilt pour DI
- Google Maps SDK
- Code SIMPLE niveau étudiant fin d'études

## FONCTIONNALITÉS MINIMALES
1. Créer rapport (titre, description, catégorie, photo, localisation)
2. Liste rapports avec filtres
3. Carte Google Maps avec marqueurs
4. Détails rapport
5. Sync automatique offline→online
6. Paramètres langue

## MODÈLE DE DONNÉES
```kotlin
data class Report(
    val id: String,
    val title: String,
    val description: String,
    val category: ReportCategory, // enum: CLEANLINESS, ROAD, LIGHTING, SAFETY, OTHER
    val latitude: Double,
    val longitude: Double,
    val photoUrl: String?,
    val timestamp: Long,
    val syncStatus: SyncStatus // enum: PENDING, SYNCED, FAILED
)
```

## STRUCTURE DOSSIERS
```
app/src/main/java/com/cityreport/
├── data/
│   ├── local/ (Room: Database, Dao, Entity)
│   ├── remote/ (Firebase)
│   ├── repository/
│   └── worker/ (WorkManager sync)
├── domain/model/
├── ui/
│   ├── home/
│   ├── create/
│   ├── details/
│   ├── map/
│   ├── settings/
│   └── navigation/
└── di/ (Hilt modules)
```

## DÉPENDANCES GRADLE
- Compose BOM 2024.x
- Room 2.6.x
- Hilt 2.48
- Firebase BOM 32.x
- Google Maps Compose 4.x
- WorkManager 2.9.x
- Coil, JUnit, Mockk

## TESTS REQUIS
### Unitaires (4)
1. ReportRepositoryTest
2. CreateReportViewModelTest
3. SyncWorkerTest
4. ReportFilterTest

### UI (2)
1. CreateReportScreenTest
2. HomeScreenTest

## RÈGLES DE CODE
❌ PAS DE : sur-ingénierie, patterns complexes, multi-modules
✅ OUI À : code simple, commentaires FR, Material 3 standard

## PERMISSIONS
- INTERNET
- ACCESS_FINE_LOCATION
- CAMERA
- READ_MEDIA_IMAGES

## STRINGS (3 langues)
- values/strings.xml (EN)
- values-fr/strings.xml
- values-ar/strings.xml
```

---

### ÉTAPE 2 : Utiliser Claude Code par étapes

Voici la **meilleure méthode** pour travailler avec Claude Code dans Android Studio :

#### A) Si vous utilisez le Terminal intégré d'Android Studio

1. **Ouvrez le Terminal** dans Android Studio (Alt+F12 ou View > Tool Windows > Terminal)

2. **Placez le fichier PROJECT_SPEC.md** dans un dossier accessible (ex: `/home/votre_user/cityreport_spec/`)

3. **Utilisez cette approche par CHECKPOINTS** :

**PROMPT ÉTAPE 1 - Setup initial**
```
Lis attentivement le fichier PROJECT_SPEC.md situé à [CHEMIN_COMPLET].

Ta mission : créer un projet Android "City Report" qui respecte TOUTES les contraintes.

ÉTAPE 1 - Setup projet :
1. Crée la structure de dossiers Android standard
2. Génère build.gradle.kts (root et app) avec TOUTES les dépendances listées
3. Génère settings.gradle.kts
4. Génère AndroidManifest.xml avec permissions
5. Crée google-services.json (template commenté)

IMPORTANT :
- Vérifie que chaque dépendance est présente
- Utilise les versions stables récentes
- Ne saute AUCUNE étape

Quand tu as fini, dis "ÉTAPE 1 TERMINÉE - CHECKPOINT" et attends ma confirmation avant de continuer.
```

**PROMPT ÉTAPE 2 - Modèles et Database**
```
ÉTAPE 2 - Data Layer :
1. Crée domain/model/ avec Report.kt, ReportCategory.kt, SyncStatus.kt
2. Crée data/local/ avec ReportDatabase.kt, ReportDao.kt, ReportEntity.kt
3. Crée data/remote/FirebaseDataSource.kt (méthodes : saveReport, uploadImage)
4. Crée data/repository/ReportRepository.kt (abstraction local + remote)

Respecte le modèle de données dans PROJECT_SPEC.md.

Quand terminé : "ÉTAPE 2 TERMINÉE - CHECKPOINT"
```

**PROMPT ÉTAPE 3 - Hilt + Worker**
```
ÉTAPE 3 - DI et Sync :
1. Crée di/AppModule.kt et di/DatabaseModule.kt (Hilt)
2. Crée data/worker/SyncWorker.kt (WorkManager pour sync auto)
3. Crée CityReportApp.kt (@HiltAndroidApp)
4. Crée MainActivity.kt (Hilt + setContent)

Vérifie que Hilt est bien configuré partout.

Terminé : "ÉTAPE 3 TERMINÉE - CHECKPOINT"
```

**PROMPT ÉTAPE 4 - UI Screens**
```
ÉTAPE 4 - UI (une feature à la fois) :

4A) HomeScreen :
- ui/home/HomeScreen.kt (liste + filtres + recherche)
- ui/home/HomeViewModel.kt (StateFlow)
  Material 3, simple, fonctionnel.

4B) CreateReportScreen :
- ui/create/CreateReportScreen.kt (formulaire + photo + map)
- ui/create/CreateReportViewModel.kt
  Gestion permissions caméra/location.

4C) DetailsScreen :
- ui/details/DetailsScreen.kt
- ui/details/DetailsViewModel.kt

4D) MapScreen :
- ui/map/MapScreen.kt (Google Maps + marqueurs)
- ui/map/MapViewModel.kt

4E) SettingsScreen :
- ui/settings/SettingsScreen.kt (sélecteur langue)
- ui/settings/SettingsViewModel.kt

Fais UNE fonctionnalité à la fois. Attends ma validation entre chaque.

Terminé chaque : "SCREEN X TERMINÉE - CHECKPOINT"
```

**PROMPT ÉTAPE 5 - Navigation et Theme**
```
ÉTAPE 5 - Finitions UI :
1. ui/navigation/NavGraph.kt (toutes les routes)
2. ui/theme/ (Color.kt, Theme.kt, Type.kt) Material 3
3. Connecte tout dans MainActivity

Terminé : "ÉTAPE 5 TERMINÉE - CHECKPOINT"
```

**PROMPT ÉTAPE 6 - Ressources multilingues**
```
ÉTAPE 6 - Internationalisation :
1. res/values/strings.xml (EN - toutes les strings UI)
2. res/values-fr/strings.xml (traductions FR)
3. res/values-ar/strings.xml (traductions AR)

Minimum 30 strings par langue.
RTL configuré pour arabe.

Terminé : "ÉTAPE 6 TERMINÉE - CHECKPOINT"
```

**PROMPT ÉTAPE 7 - Tests**
```
ÉTAPE 7 - Tests (selon PROJECT_SPEC.md) :

Tests unitaires (app/src/test/) :
1. ReportRepositoryTest.kt
2. CreateReportViewModelTest.kt
3. SyncWorkerTest.kt
4. ReportFilterTest.kt

Tests UI (app/src/androidTest/) :
1. CreateReportScreenTest.kt
2. HomeScreenTest.kt

Utilise JUnit, Mockk, Compose Testing.
Code simple mais fonctionnel.

Terminé : "ÉTAPE 7 TERMINÉE - CHECKPOINT"
```

**PROMPT ÉTAPE 8 - Documentation**
```
ÉTAPE 8 - README et docs :
Génère README.md avec :
- Description projet
- Prérequis (Android Studio, Firebase)
- Instructions configuration (API keys, Firebase)
- Instructions build APK
- Architecture (schéma texte)
- Screenshots placeholders
- Stack technique

Terminé : "PROJET COMPLET - VÉRIFICATION FINALE"
```

---

#### B) Si vous utilisez une extension/plugin Claude dans Android Studio

Certains plugins comme **Continue** ou autres permettent d'utiliser Claude :

1. **Créez le fichier PROJECT_SPEC.md** à la racine de votre projet Android Studio

2. **Utilisez la fonction "Add Context"** ou "@file" pour référencer le fichier :
```
@PROJECT_SPEC.md

Crée le projet City Report en suivant EXACTEMENT les spécifications.
Commence par l'étape 1 : Setup Gradle et structure de base.