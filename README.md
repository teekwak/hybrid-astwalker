# ASTWalker #

This README would normally document whatever steps are necessary to get your application up and running.

### What is this repository for? ###

* ASTWalker
* GitHubData

### What does ASTWalker do? ###

* Walks along Java source code and extracts different standalone.entities from it
* Prints those standalone.entities to the screen depending on which standalone.entities are selected
* Each entity extracted is also labeled by class and method (if applicable)
* Classes and method declarations contain counters for each entity

### What does GitHubData do? ###

* Takes a .git file from a cloned repository and extracts data about commits
* Returns number of changes between commits, author name, email, time of commit, etc.

### How do I get set up? ###

* Clone the project

##### ASTWalker #####

* Open up FileModel.java
* Change File inputFolder on line 222 to any directory
* Change the else block of traverseUntilJava(File parentNode)
* Safe to print after fileModel.parseDeclarations has been called
* Run FileModel.java

##### GitHubData #####

* Point line 186 to the directory of the Git project (not the .git file exactly)
* Run GitHubData.java