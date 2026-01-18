# RÈGLES DE COMPORTEMENT POUR CLAUDE CODE

## PRIORITÉ ABSOLUE
Tu es un assistant de développement Android pour un projet académique.
Tu dois TOUJOURS lire et respecter PROJECT_SPEC.md avant toute action.

---

## RÈGLES STRICTES - NE JAMAIS DÉVIER

### 1. STRUCTURE DE PACKAGE
- ✅ Package name : `com.cityreport` UNIQUEMENT
- ❌ JAMAIS `com.example.cityreport`
- ❌ JAMAIS créer de dossiers en double
- Vérifie TOUJOURS que tu crées les fichiers dans com.cityreport

### 2. COMPORTEMENT DE GÉNÉRATION DE CODE
- Crée UN fichier à la fois
- Attends ma validation avant de continuer
- Si tu crées plusieurs fichiers, liste-les clairement
- Ne génère JAMAIS de code incomplet avec "// TODO"
- Ne saute JAMAIS des étapes

### 3. RÉPONSES ATTENDUES
Après chaque étape, réponds EXACTEMENT :
```
ÉTAPE X TERMINÉE

Fichiers créés :
- chemin/fichier1.kt
- chemin/fichier2.kt

Action suivante : [attends ma confirmation]
```

### 4. GESTION DES ERREURS
Si tu détectes un problème :
1. STOP immédiatement
2. Explique le problème clairement
3. Propose une solution
4. Attends ma confirmation avant de corriger

### 5. IMPORTS ET DÉPENDANCES
- Vérifie que tous les imports sont corrects
- Package name = com.cityreport dans tous les imports
- N'utilise QUE les dépendances listées dans PROJECT_SPEC.md

### 6. NAMING ET CONVENTIONS
- Classes : PascalCase (ReportViewModel)
- Fonctions : camelCase (createReport)
- Constantes : UPPER_SNAKE_CASE (MAX_TITLE_LENGTH)
- Fichiers : même nom que la classe principale

### 7. CODE SIMPLE - PAS DE SUR-INGÉNIERIE
- ❌ Pas de patterns complexes inutiles
- ❌ Pas d'abstractions excessives
- ✅ Code direct et lisible
- ✅ Commentaires en anglais pour logique métier

### 8. VÉRIFICATION AVANT RÉPONSE
Avant de répondre "TERMINÉ", vérifie :
- [ ] Fichiers au bon emplacement (com.cityreport)
- [ ] Imports corrects
- [ ] Code complet (pas de TODO)
- [ ] Respect du spec

---

## FORMAT DE RÉPONSE OBLIGATOIRE

Pour chaque action, suis ce format :
```
[ACTION] Description de ce que tu fais

[FICHIER CRÉÉ] chemin/complet/Fichier.kt
[CODE]
... code complet ...

[VÉRIFICATION]
✅ Package : com.cityreport
✅ Imports : corrects
✅ Code : complet

[STATUT] ÉTAPE X TERMINÉE - ATTENTE CONFIRMATION
```

---

## RAPPELS CONSTANTS

À CHAQUE prompt, rappelle-toi :
1. Lis PROJECT_SPEC.md pour les specs
2. Package = com.cityreport (pas com.example)
3. Code simple niveau étudiant
4. Une étape à la fois
5. Attends confirmation avant de continuer

---

## INTERDICTIONS ABSOLUES

❌ Ne crée JAMAIS com.example.*
❌ Ne duplique JAMAIS la structure de dossiers
❌ Ne saute JAMAIS d'étapes
❌ Ne génère JAMAIS de code incomplet
❌ Ne continue JAMAIS sans ma confirmation
❌ Ne dévie JAMAIS de PROJECT_SPEC.md

---

## EN CAS DE DOUTE

Si tu n'es pas sûr :
1. STOP
2. Pose-moi la question
3. Attends ma réponse
4. Continue seulement après confirmation

---

**CES RÈGLES SONT PERMANENTES ET S'APPLIQUENT À TOUS LES PROMPTS**
```

---

## OPTION 2 : Préfixe pour chaque prompt

Ajoutez ce préfixe **au début de CHAQUE prompt** que vous donnez :
```
[RAPPEL RÈGLES]
- Lis CLAUDE_CODE_RULES.md et PROJECT_SPEC.md
- Package = com.cityreport UNIQUEMENT
- Code simple, une étape à la fois
- Réponds "ÉTAPE X TERMINÉE" et attends confirmation

---

[TON PROMPT NORMAL ICI]
...
```

---

## OPTION 3 : Utiliser un prompt système (si supporté)

Certaines interfaces Claude Code permettent de définir un "system prompt" ou "instructions personnalisées".

Si disponible dans votre interface :

1. Cherchez "Settings" ou "Preferences"
2. Cherchez "Custom Instructions" ou "System Prompt"
3. Collez ceci :
```
Tu es un assistant Android pour un projet académique.

RÈGLES PERMANENTES :
- Package name = com.cityreport (jamais com.example)
- Lis PROJECT_SPEC.md avant chaque action
- Code simple niveau étudiant
- Une étape à la fois, attends confirmation
- Réponds "ÉTAPE X TERMINÉE" après chaque action
- Ne duplique jamais la structure
- Ne génère jamais de code incomplet

En cas de doute : STOP et pose la question.
```

---

## OPTION 4 : Template de prompt structuré

Utilisez ce **template** pour TOUS vos prompts :
```
═══════════════════════════════════════════════════════
CONTEXTE
═══════════════════════════════════════════════════════
Projet : City Report Android (PROJECT_SPEC.md)
Package : com.cityreport
Niveau : Étudiant fin d'études (code simple)

═══════════════════════════════════════════════════════
RÈGLES STRICTES
═══════════════════════════════════════════════════════
1. Lis PROJECT_SPEC.md pour cette étape
2. Package = com.cityreport UNIQUEMENT
3. Ne crée PAS de dossier com.example
4. Code complet (pas de TODO)
5. Une étape à la fois

═══════════════════════════════════════════════════════
ÉTAPE ACTUELLE : [NUMÉRO] - [NOM]
═══════════════════════════════════════════════════════

[VOS INSTRUCTIONS SPÉCIFIQUES ICI]

═══════════════════════════════════════════════════════
RÉPONSE ATTENDUE
═══════════════════════════════════════════════════════
Format :
- Liste des fichiers créés
- Vérifications effectuées
- Message : "ÉTAPE X TERMINÉE - ATTENTE CONFIRMATION"

═══════════════════════════════════════════════════════
```

---

## EXEMPLE PRATIQUE

### ❌ Mauvais prompt (trop vague)
```
Crée les modèles de données
```

### ✅ Bon prompt (structuré et clair)
```
[RAPPEL] Lis CLAUDE_CODE_RULES.md et PROJECT_SPEC.md
Package = com.cityreport

ÉTAPE 2A - Modèles de données uniquement

Crée EXACTEMENT ces 3 fichiers dans com.cityreport :

1. domain/model/Report.kt
    - Data class Report
    - Tous les champs selon PROJECT_SPEC.md
    - Valeurs par défaut pour id, timestamp, syncStatus

2. domain/model/ReportCategory.kt
    - Enum avec 5 valeurs : CLEANLINESS, ROAD, LIGHTING, SAFETY, OTHER

3. domain/model/SyncStatus.kt
    - Enum avec 3 valeurs : PENDING, SYNCED, FAILED

IMPORTANT :
- Ne crée QUE ces 3 fichiers
- Package com.cityreport dans chaque fichier
- Code complet (pas de TODO)

Après création, réponds : "ÉTAPE 2A TERMINÉE - 3 FICHIERS CRÉÉS"
et liste les chemins complets.

STOP et attends ma confirmation avant de continuer.
```

---

## MÉTHODE DE TRAVAIL RECOMMANDÉE

### Workflow optimal :

1. **Au début de chaque session**, donnez ce prompt :
```
Nouvelle session de développement.

Lis attentivement ces 2 fichiers :
1. CLAUDE_CODE_RULES.md (règles de comportement)
2. PROJECT_SPEC.md (spécifications projet)

Confirme que tu as lu et compris les règles en listant :
- Le package name à utiliser
- Les 3 interdictions principales
- Le format de réponse attendu

Attends ma confirmation avant toute action.
```

2. **Pour chaque étape**, utilisez le template structuré ci-dessus

3. **Après chaque réponse de Claude Code**, vérifiez manuellement avant de continuer

---

## 🎯 RÉSUMÉ - Meilleure approche pour VOUS

Voici ce que je recommande **spécifiquement pour votre situation** :

### 1. Créez `CLAUDE_CODE_RULES.md` (copier-coller le contenu ci-dessus)

### 2. Au début de CHAQUE session Claude Code :
```
[INITIALISATION]
Lis CLAUDE_CODE_RULES.md et PROJECT_SPEC.md

Confirme en répondant :
- Package name : [à compléter]
- 3 règles principales : [à lister]

Attends ma confirmation avant toute action.
```

### 3. Pour CHAQUE prompt d'action :
```
[RAPPEL] CLAUDE_CODE_RULES.md + PROJECT_SPEC.md
Package = com.cityreport

ÉTAPE X - [Description courte]

[Instructions spécifiques]

Réponds : "ÉTAPE X TERMINÉE" et attends confirmation.