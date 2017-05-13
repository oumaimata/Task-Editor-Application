# Task-Editor-Application

Le but du projet et de penser, concevoir et développer une interface graphique, qui permettra de visualiser et éditer un arbre de tâche.

## Usage

Les principales fonctionnalités explicitées par les utilisateurs étaient que : 
* L’application devra disposer de fonctionnalités dites génériques :		
	* Créer un nouveau modèle de tâche		
	* Importer un modèle de tâche existant,
	* Enregistrer le modèle de tâche,
	* Quitter l’application.
* L’interface devra permettre en particulier de :
	* Visualiser et éditer l’arbre de tâche,
	* Visualiser et éditer  les propriétés des tâches,
	* Visualiser et éditer les propriétés générales du modèle,
	* Visualiser et éditer le fichier XML correspondant.

Les fonctionnalités de visualisation/édition de l’arbre ainsi que celle de visualisation/édition des propriétés générales du modèle peuvent être regroupées en une seule entité dans la limite où l’on admet que les propriétés générales du modèle sont limitées à l’existence de tags. 

Il y a donc trois parties principales de l’interface à développer : 
* **arbre de tâche** avec la gestion des tags,
* la section **XML**,
* et enfin l’**édition des propriétés** des tâches.

## Installation

1. Télécharger/Installer [IntelliJ IDEA](https://www.jetbrains.com/idea/) en se créant un compte avec l'adresse ETU (gratuit) : 
	1. Surement nécessiter de définir le path JAVA (1.8).
2. Ouvrir le dossier Task-Editor-Application dans intelliJ, normalement tout est y embarqué.

## Architecture du projet

**.idea** 
	> fichiers utilent à IntelliJ pour le fonctionnement

**Documentation** 
	> Documentation détaillé de chaque bloc : arbre, propriété, xml

**lib** 
	> librairie yWorks pour le rendu graphique de l'arbre

**src** 
	> ressources du projet (contient pour le moment le starting pack Hello World et la clé de licence yWorks)


## Credits

Les contributeurs du projet : 

* Judas Théo 
* Lacorte Pierre-Louis 
* Soetaert Camille 
* Talouka Oumaima 