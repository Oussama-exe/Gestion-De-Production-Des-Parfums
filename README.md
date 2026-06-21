# OSMAR — Maison de Parfum

Une application de bureau (Desktop) JavaFX pour gérer une production de parfums
artisanale, sur mesure : les clients parcourent un catalogue
d'ingrédients, composent un mélange personnalisé, choisissent un
format de flacon, paient, puis se font livrer. Construite selon une
architecture en couches Model / DAO / Controller / View, avec une base
de données MySQL.

▶️ **Vidéo de démonstration :** [ https://drive.google.com/file/d/15FaAAHojgrr7EWXTaZUsCYTDEO_yWjgX/view?usp=sharing ]

---

## Table des matières

- [Stack technique](#stack-technique)
- [Structure du projet](#structure-du-projet)
- [Architecture et concepts de programmation](#architecture-et-concepts-de-programmation)
- [Schéma de base de données](#schéma-de-base-de-données)
- [Parcours écran par écran](#parcours-écran-par-écran)
- [Installation et exécution dans IntelliJ](#installation-et-exécution-dans-intellij)
- [Choix de conception et limites connues](#choix-de-conception-et-limites-connues)

---

## Stack technique

| Couche | Technologie |
|---|---|
| UI | JavaFX 23 (FXML + CSS), aucune librairie UI tierce |
| Langage | Java 21 |
| Outil de build | Maven (`javafx-maven-plugin`) |
| Base de données | MySQL, accédée en JDBC pur (`mysql-connector-j`) |
| Tests | JUnit 5 (Jupiter) — dépendance présente, aucun test écrit pour le moment |
| Système de modules | **Non utilisé.** Pas de `module-info.java` — le projet fonctionne entièrement via le classpath. Ce choix a été fait délibérément en cours de développement pour éviter les frictions du Java Platform Module System (JPMS) (`opens`/`exports`) avec le chargement réflexif des contrôleurs par FXML. |

---

## Structure du projet

```
osmar/
├── pom.xml
└── src/main/
    ├── java/
    │   ├── com/example/osmar/        <-- singletons globaux & point d'entrée
    │   │   ├── MainApp.java
    │   │   ├── SceneManager.java
    │   │   ├── Session.java
    │   │   └── SignUpDraft.java
    │   ├── controller/                <-- un contrôleur par vue FXML (package à plat)
    │   │   ├── WelcomeController.java
    │   │   ├── LoginController.java
    │   │   ├── SignUpStepOneController.java
    │   │   ├── SignUpStepTwoController.java
    │   │   ├── HomeController.java
    │   │   ├── OrderHistoryController.java
    │   │   ├── CommandeRow.java             (wrapper de ligne pour le TableView, pas un contrôleur d'écran)
    │   │   ├── CatalogController.java
    │   │   ├── ProductDetailController.java
    │   │   ├── IngredientCardFactory.java   (utilitaire de construction d'UI partagé, pas un contrôleur d'écran)
    │   │   ├── CartController.java
    │   │   ├── QuantityController.java
    │   │   ├── CompositionSummaryController.java
    │   │   ├── PaymentMethodController.java
    │   │   ├── LocationController.java
    │   │   └── OrderConfirmationController.java
    │   ├── dao/                       <-- un DAO par table, package à plat
    │   │   ├── DB_connection.java
    │   │   ├── ClientDAO.java
    │   │   ├── CommandeDAO.java
    │   │   ├── IngredientsCmdDAO.java
    │   │   ├── IngredientTypeResolver.java  (utilitaire interne, pas un DAO)
    │   │   ├── FixateurDAO.java
    │   │   ├── MatierePremiereNaturelleDAO.java
    │   │   ├── MatiereSynthetiqueDAO.java
    │   │   └── SolvantSupportDAO.java
    │   └── model/                     <-- objets Java simples + enums, package à plat
    │       ├── Client.java
    │       ├── Commande.java
    │       ├── Paiement.java
    │       ├── Ingredient.java              (classe abstraite de base)
    │       ├── Fixateur.java                + FIX_enum.java
    │       ├── Matiere_Premiere_naturelle.java + MPN_enum.java
    │       ├── Matiere_synthetique.java     + MS_enum.java
    │       └── Solvant_Support.java         + SOLV_SUPP_enum.java
    └── resources/
        └── view/
            ├── css/
            │   └── theme.css           <-- feuille de style unique partagée, jetons de design en variables CSS
            └── fxml/
                ├── WelcomeView.fxml
                ├── LoginView.fxml
                ├── SignUpStepOneView.fxml
                ├── SignUpStepTwoView.fxml
                ├── HomeView.fxml
                ├── OrderHistoryView.fxml
                ├── CatalogView.fxml
                ├── ProductDetailView.fxml
                ├── CartView.fxml
                ├── QuantityView.fxml
                ├── CompositionSummaryView.fxml
                ├── PaymentMethodView.fxml
                ├── LocationView.fxml
                └── OrderConfirmationView.fxml
```

> **Remarque sur l'organisation des packages :** ce projet mélange
> volontairement deux conventions. `MainApp`, `SceneManager`,
> `Session` et `SignUpDraft` vivent sous le package nommé
> `com.example.osmar`, tandis que `controller`, `dao` et `model` sont
> des packages plats, de premier niveau
> (`package controller;`, et non `package com.example.osmar.controller;`).
> Il s'agit d'une décision structurelle prise en cours de
> développement pour simplifier la majorité du code, tout en gardant
> le point d'entrée de l'application et les singletons globaux
> regroupés sous le namespace du projet. Tous les imports du code
> reflètent déjà cette organisation — ne « rangez » donc pas tout dans
> une seule structure imbriquée sans mettre à jour chaque chaîne
> `fx:controller="controller.X"` dans chaque fichier FXML en
> conséquence (les références de contrôleur dans les FXML sont de
> simples chaînes de texte ; les refactors de l'IDE ne les modifient
> pas).

---

## Architecture et concepts de programmation

Ce projet suit une **architecture en couches** avec une séparation
claire des responsabilités, ainsi que plusieurs patrons de conception
spécifiques qu'il vaut la peine d'expliciter (utile si ce README sert
aussi de support pour une présentation ou une soutenance) :

### 1. Découpage en couches de type MVC
- **Model** (`model/`) — objets Java simples représentant les entités
  du domaine (`Client`, `Commande`, `Ingredient` et ses 4
  sous-classes). Aucun code JavaFX ou SQL ne s'infiltre dans cette
  couche.
- **DAO** (`dao/`) — Data Access Objects, un par table, isolant tout
  le JDBC/SQL du reste de l'application. Chaque DAO suit la même
  forme : `findAll()`, `findById(int)`, `insert(...)`, `update(...)`,
  `delete(int)`, plus une méthode privée `mapRow(ResultSet)` pour
  convertir une ligne SQL en objet du modèle.
- **Controller** (`controller/`) — une classe contrôleur par vue
  FXML, avec des champs annotés `@FXML` liés aux nœuds nommés dans le
  fichier FXML correspondant. Les contrôleurs appellent les DAO pour
  la persistance, et `Session`/`SceneManager` pour l'état partagé et
  la navigation.
- **View** (`resources/view/`) — FXML pour la structure, une seule
  feuille `theme.css` partagée pour le style. Aucun style en ligne à
  part quelques attributs `style="..."` ponctuels pour la taille des
  émojis.

### 2. Héritage et polymorphisme
`Ingredient` est une **classe abstraite** avec une méthode abstraite
`calculer_prix()`. Quatre sous-classes concrètes
(`Fixateur`, `Matiere_Premiere_naturelle`, `Matiere_synthetique`,
`Solvant_Support`) la redéfinissent chacune, en déléguant à
`getPrixParGramme()` de leur propre enum. `CatalogController` et
`IngredientCardFactory` travaillent partout avec le type de base
`Ingredient`, et n'utilisent le filtrage par `instanceof` que là où un
comportement spécifique à la famille (icône, libellé) est réellement
nécessaire.

### 3. Les enums comme données typées et autonomes
Chaque famille d'ingrédients possède un enum correspondant
(`FIX_enum`, `MPN_enum`, `MS_enum`, `SOLV_SUPP_enum`) dont les
constantes portent leur propre prix au gramme en argument de
constructeur — par exemple `FLORAL(5.5), BOISEE(0.5), ...` — plutôt
que d'utiliser des champs `static` parallèles ou une table de
correspondance séparée. Ceci résulte d'un refactor délibéré effectué
en cours de développement : une version antérieure utilisait une
interface `*_inter` par famille, contenant des constantes `static`
non rattachées (et non `final`, donc techniquement mutables et
partagées entre toutes les instances — un vrai bug), qui a été
consolidée directement dans l'enum.

### 4. Association polymorphique via une colonne discriminante
`ingredients_cmd` (table de jonction entre `commande` et les 4 tables
d'ingrédients) utilise une colonne discriminante `type_ingredient`
(`'fixateur' | 'naturelle' | 'synthetique' | 'solvant'`) pour
enregistrer *à laquelle* des 4 tables `id_ing` fait référence.
`IngredientsCmdDAO` effectue un branchement sur cette valeur pour
appeler le `*DAO.findById(...)` correspondant, et
`IngredientTypeResolver` effectue le mappage inverse (classe Java →
chaîne discriminante) lors de l'enregistrement. **Il n'y a
volontairement aucune clé étrangère SQL sur `id_ing`** — une clé
étrangère sur une seule colonne ne peut pas référencer 4 tables
différentes de façon conditionnelle ; l'intégrité référentielle de
cette colonne est donc entièrement assurée par la couche Java plutôt
que par la base de données.

### 5. Patron Singleton pour l'état partagé entre écrans
JavaFX/FXML n'offre aucun mécanisme natif pour faire transiter des
données entre les scènes. `Session` (un singleton avec uniquement des
membres statiques) conserve le `Client` actuellement authentifié, la
`Commande` en cours de construction, et l'`Ingredient` actuellement
sélectionné pour la popup de détail. `SignUpDraft` est un singleton
plus petit, de forme similaire, qui conserve l'email/mot de passe
entre les deux étapes d'inscription, avant que le `Client` n'existe
réellement en base. Chaque contrôleur lit/écrit via ces singletons
plutôt que de passer des arguments de constructeur entre des
contrôleurs chargés par FXML (ce que `FXMLLoader` ne permet pas sans
plomberie additionnelle).

### 6. Construction d'UI par programmation
Plusieurs écrans (`CatalogView`, `CartView`,
`CompositionSummaryView`) ont un conteneur `VBox`/une racine vide
définie en FXML, mais leur contenu réel (cartes produit, lignes du
panier) est entièrement construit en Java à l'exécution par
`IngredientCardFactory`, puisque le nombre d'ingrédients est
dynamique et inconnu au moment de l'écriture du FXML. Cela mélange une
mise en page FXML déclarative pour la « coquille » statique de chaque
écran, avec du Java impératif pour les parties pilotées par les
données.

### 7. Liaisons de propriétés et écouteurs JavaFX (binding & listeners)
`QuantityController` lie un écouteur sur la `valueProperty()` d'un
`Slider` pour mettre à jour en direct à la fois le texte affiché « X
ml » et la mise en surbrillance des boutons de préréglage.
`CatalogController` écoute la `textProperty()` du champ de recherche
pour refiltrer la grille de produits à chaque frappe, sans aucun clic
de bouton explicite.

### 8. Animation JavaFX (transitions basées sur `Timeline`)
`WelcomeController` joue un `ParallelTransition` combinant une
`FadeTransition` et une `TranslateTransition` au chargement de
l'écran — un exemple concret de l'API d'animation de JavaFX que les
futurs écrans peuvent reprendre. `theme.css` déclare également des
règles CSS `transition:` sur les états de survol (mise à l'échelle des
boutons, du curseur du slider) en utilisant la fonctionnalité CSS
Transitions de JavaFX 23.

### 9. Stratégie de gestion des exceptions
Les DAO interceptent les `SQLException` et les relancent sous forme de
`RuntimeException` non vérifiées, avec un message descriptif, plutôt
que d'obliger chaque contrôleur à gérer individuellement des
exceptions SQL vérifiées. C'est un compromis de simplicité délibéré
pour une application de cette taille — les contrôleurs ne sont pas
censés « se rétablir » d'une panne de base de données en plein clic,
ils se contentent de laisser remonter l'erreur (actuellement : elle se
propage et JavaFX la journalise sur stderr ; il n'y a pas encore de
boîte de dialogue d'erreur dans l'application, voir Limites connues).

---

## Schéma de base de données

7 tables dans le schéma MySQL `osmar_db` :

| Table | Rôle |
|---|---|
| `client` | Utilisateurs inscrits. `id, nom, prenom, adresse, email, pwd, paiement` |
| `commande` | Une ligne par commande. `id, id_cl, montant_tt, quantite, date_cmd, date_livraison` — `quantite` est le **format du flacon en ml** pour l'ensemble de la commande. |
| `matiere_prem_naturelle` | Catalogue des matières premières naturelles. `id, nom, type, disponibilite, prix_par_gramme` |
| `matiere_synthetique` | Catalogue des matières synthétiques. Même structure que ci-dessus. |
| `fixateurs` | Catalogue des fixateurs. Même structure que ci-dessus. |
| `solvants_supports` | Catalogue des solvants/supports. Même structure que ci-dessus. |
| `ingredients_cmd` | Table de jonction : quels ingrédients (parmi les 4 tables de catalogue ci-dessus) sont entrés dans quelle commande, et en quelle quantité (en grammes). `id_cmd, id_ing, quantite, type_ingredient` |

> **Les colonnes `prix_par_gramme` sont présentes mais inutilisées.**
> Le prix est lu depuis l'enum Java de chaque ingrédient
> (`getPrixParGramme()`), pas depuis cette colonne, par choix
> délibéré — voir « Choix de conception » plus bas. La colonne est
> conservée au cas où vous souhaiteriez rendre le prix piloté par la
> base de données plus tard.

> **Aucune clé étrangère sur `ingredients_cmd.id_ing`** — voir
> « Association polymorphique » ci-dessus. Il existe en revanche bien
> une clé étrangère de `ingredients_cmd.id_cmd` → `commande.id`, qui
> est correcte et doit être conservée.

---

## Parcours écran par écran

```
WelcomeView ──▶ LoginView ──▶ HomeView ──▶ OrderHistoryView
                   │              │
                   ▼              ▼
          SignUpStepOneView   CatalogView ──▶ ProductDetailView
                   │              ▲  │              │
                   │              │  └────"Ajouter"──┘
                   ▼              │
          SignUpStepTwoView       │
            (crée le Client,    CartView
             auto-connexion,      │
             → HomeView)     "Confirmer ma commande"
                                   ▼
                            QuantityView
                       (définit le format du flacon,
                          Commande.quantite)
                                   ▼
                       CompositionSummaryView
                     (récapitulatif, "Modifier" → Catalog)
                                   ▼
                         PaymentMethodView
                      (enregistré dans Client.paiement)
                                   ▼
                            LocationView
                  (enregistre Client.adresse, PUIS
                   persiste la commande complète via
                   CommandeDAO.insertWithIngredients)
                                   ▼
                       OrderConfirmationView
                      │                        │
           "Ajouter une autre commande"   "Revenir à l'accueil"
                      ▼                        ▼
                 CatalogView                HomeView
          (nouvelle Commande démarrée) (Session.commande réinitialisée)
```

### Le rôle de chaque écran

| Écran | Responsabilité |
|---|---|
| **Welcome** | Écran de bienvenue avec animation d'entrée en fondu/glissement. « Suivant » → Login. |
| **Login** | `ClientDAO.findByEmail` + vérification du mot de passe en clair. Définit `Session.currentClient` en cas de succès. |
| **SignUp (1/2)** | Vérifie que l'email n'est pas déjà pris + correspondance/longueur du mot de passe. Stocke les identifiants dans `SignUpDraft`. |
| **SignUp (2/2)** | Récupère nom/prénom, crée la ligne `Client` réelle, connecte automatiquement l'utilisateur, → Home. |
| **Home** | Salue le client connecté par son prénom. « Faire une Commande » démarre une nouvelle `Commande` et → Catalog. « Voir mes historiques » → OrderHistory. |
| **OrderHistory** | `TableView` peuplé via `CommandeDAO.findByClientId`, chaque ligne enveloppée dans `CommandeRow` pour l'affichage. |
| **Catalog** | Charge les 4 tables d'ingrédients via leurs DAO, affiche les cartes via `IngredientCardFactory`, prend en charge la recherche en direct et les filtres par famille (chips). |
| **ProductDetail** | Affiche l'ingrédient cliqué dans le Catalog (transmis via `Session.selectedIngredient`). « Ajouter à la commande » l'ajoute à la `Commande` en cours. Implémenté comme un remplacement plein écran, pas une popup flottante. |
| **Cart** | Affiche `Session.currentCommande.getElementsCmd()`, permet la suppression, sous-total en direct. « Confirmer » → Quantity. |
| **Quantity** | Le slider/les préréglages définissent directement `Commande.quantite` (format du flacon en ml). |
| **CompositionSummary** | Récapitulatif en lecture seule du panier + format de flacon choisi. « Modifier » → retour au Catalog. |
| **PaymentMethod** | La sélection radio est enregistrée dans `Client.paiement` via `ClientDAO.update`. |
| **Location** | L'adresse est enregistrée dans `Client.adresse`. **C'est ici que la commande est réellement persistée** — `CommandeDAO.insertWithIngredients` écrit la ligne `commande` et chaque ligne `ingredients_cmd` en un seul appel. |
| **OrderConfirmation** | Écran de succès. Démarre soit une nouvelle commande, soit réinitialise `Session.currentCommande` et retourne à l'accueil. |

---

## Installation et exécution dans IntelliJ

### Prérequis
- **JDK 21** (ou plus récent) installé et sélectionné comme SDK du
  projet
- Serveur **MySQL** local en fonctionnement, avec une base de données
  nommée `osmar_db` contenant les 7 tables décrites ci-dessus
- IntelliJ IDEA (Community ou Ultimate) avec le support Maven —
  inclus par défaut

### 1. Configuration de la base de données
Créez le schéma et les tables (à adapter selon votre DDL réel), puis
remplissez les 4 tables de catalogue d'ingrédients avec quelques
lignes chacune — la colonne `type` **doit** correspondre exactement
(sensible à la casse) à l'une des constantes de l'enum Java
correspondant :

| Table | Valeurs valides pour `type` |
|---|---|
| `matiere_prem_naturelle` | `FLORAL`, `BOISEE`, `AGRUME`, `EPICE`, `RESINE_BAUME`, `RACINE_MOUSSE`, `ANIMAL` |
| `matiere_synthetique` | `MUSCS_SYNTHETIQUE`, `ALDEHYDE`, `CETONE`, `MOLECULES_ESOLES` |
| `fixateurs` | `RESINES_NATURELLE`, `MUSCS_SYNTHESE`, `AMBRE` |
| `solvants_supports` | `ALCOOL_ETHYLIQUE`, `HUILE`, `EAU_DISTILLEE` |

### 2. Configurer la connexion à la base de données
Modifiez `dao/DB_connection.java` avec votre véritable URL/utilisateur/
mot de passe MySQL :

```java
private static final String URL = "jdbc:mysql://localhost:3306/osmar_db";
private static final String USER = "root";
private static final String PASSWORD = "";
```

### 3. Ouvrir le projet
File → Open → sélectionnez le dossier `osmar` (celui qui contient
`pom.xml`). Laissez IntelliJ indexer et télécharger automatiquement
les dépendances Maven — si ce n'est pas le cas, clic droit sur
`pom.xml` → **Maven → Reload Project**.

### 4. Lancer l'application
**Option A — terminal, depuis la racine du projet :**
```bash
mvn clean javafx:run
```

**Option B — panneau Maven :** dépliez
`osmar → Plugins → javafx`, puis double-cliquez sur `javafx:run`.

**Option C — configuration de run de l'IDE :** lancer directement
`MainApp.main()` fonctionne aussi, mais seulement si les options VM de
votre configuration de run incluent le module-path JavaFX
(`--module-path ... --add-modules javafx.controls,javafx.fxml`), ou si
vous vous appuyez plutôt sur le goal `javafx:run` du
`javafx-maven-plugin`, qui gère cela automatiquement — **c'est pour
cette raison que `mvn clean javafx:run` est la méthode de lancement
recommandée**, plutôt que la flèche verte « Run » sur `MainApp`
directement.

### Dépendances (déclarées dans `pom.xml`)
| Dépendance | Version | Rôle |
|---|---|---|
| `org.openjfx:javafx-controls` | 23.0.2 | Contrôles UI JavaFX de base |
| `org.openjfx:javafx-fxml` | 23.0.2 | Chargement/analyse des fichiers FXML |
| `com.mysql:mysql-connector-j` | 9.7.0 | Driver JDBC pour MySQL |
| `org.junit.jupiter:junit-jupiter-api` / `-engine` | 5.10.0 | Tests (scope test uniquement, aucun test écrit pour le moment) |

Plugins de build : `maven-compiler-plugin` (cible Java 21),
`org.openjfx:javafx-maven-plugin` (fournit le goal `javafx:run`,
configuré avec `mainClass = com.example.osmar.MainApp`).

---

## Choix de conception et limites connues

Il s'agit de compromis explicites faits en cours de développement —
listés ici pour qu'ils soient perçus comme des décisions, et non des
bugs, si la question vous est posée :

- **Les mots de passe sont stockés et comparés en clair.** Choix
  délibéré pour le périmètre actuel du projet. Le premier endroit à
  modifier serait `ClientDAO`/`LoginController` — utiliser un hachage
  (par ex. BCrypt) au lieu d'un `.equals()`.
- **Le prix provient des enums Java, pas de la base de données.**
  Chaque table d'ingrédients possède une colonne `prix_par_gramme`
  qui n'est jamais lue — la méthode `calculer_prix()` de chaque
  sous-classe d'`Ingredient` multiplie `quantite` par le prix codé en
  dur dans la constante de l'enum. Cela centralise la logique de prix
  en un seul endroit (l'enum) plutôt que de devoir synchroniser deux
  sources. Les 4 enums (`MPN_enum`, `MS_enum`, `FIX_enum`,
  `SOLV_SUPP_enum`) ont bien des prix réels au gramme renseignés.
- **Aucune clé étrangère SQL sur `ingredients_cmd.id_ing`** — par
  nécessité, puisqu'il s'agit d'une référence polymorphique vers 4
  tables possibles. L'intégrité de cette colonne est assurée en Java
  (`IngredientsCmdDAO`/`IngredientTypeResolver`), pas dans le schéma.
- **ProductDetail est une vue plein écran, pas une fenêtre modale
  flottante.** `SceneManager.switchTo(...)` remplace l'intégralité de
  la racine de la scène, donc le Catalog est entièrement masqué en
  dessous pendant que le détail produit est affiché, plutôt que
  visible-mais-assombri derrière une popup. Une véritable fenêtre
  modale flottante nécessiterait un `Stage` séparé plutôt qu'un
  remplacement de scène FXML.
- **`Commande.quantite` (format du flacon) et `Ingredient.quantite`
  (grammes d'un ingrédient spécifique dans la recette) sont
  volontairement deux champs distincts répondant à des questions
  différentes** — le premier vit sur `commande`, le second sur
  `ingredients_cmd`. Ils ont presque été confondus en cours de
  développement ; conserver les deux était un choix délibéré pour ne
  pas perdre le détail de la recette.
- **Pas de réinitialisation de mot de passe, d'écran de profil/
  paramètres, ni de véritable géolocalisation GPS.** Les boutons « Mot
  de passe oublié », l'icône de profil et « Utiliser ma position »
  existent visuellement mais n'ont aucun gestionnaire câblé — aucun
  d'entre eux ne dispose encore d'un écran ou d'une librairie
  correspondante dans le projet.
- **Aucune boîte de dialogue d'erreur dans l'application.** Les
  échecs DAO/SQL remontent actuellement sous forme d'exceptions non
  interceptées, journalisées dans la console, plutôt qu'un message
  d'erreur visible par l'utilisateur. Suffisant en développement ;
  mériterait l'ajout de boîtes de dialogue `Alert` avant de
  considérer le projet comme prêt pour la production.
- **`Set<Ingredient>` repose sur les implémentations par défaut de
  `Object.equals()`/`hashCode()`** (identité de référence), car
  `Ingredient` ne les redéfinit pas. La suppression par référence
  fonctionne correctement partout dans l'application car la même
  instance d'objet est toujours réutilisée du chargement jusqu'à la
  suppression, mais reconstruire un `Ingredient` « équivalent » depuis
  la base de données, en tant que nouvel objet, ne correspondrait pas
  à une entrée existante dans l'ensemble.