
# ToDo-Projekt

Dies ist ein Java-Konsolenprogramm, mit dem man Nutzer, ToDos und Kategorien erstellen, anzeigen, löschen und aktualisieren kann. Nachfolgend findest du eine Anleitung, wie du das Projekt installierst und ausführen kannst.


## Voraussetzungen

1. Stelle sicher, dass du eine lauffähige Java-Version installiert hast (mindestens Java 8).  
2. Bibliotheken (.jar-Dateien)  
	Installiere bzw. lade diese Bibliotheken herunter:
   - [org.mariadb.jdbc:mariadb-java-client:3.4.1](https://mvnrepository.com/artifact/org.mariadb.jdbc/mariadb-java-client/3.4.1)  
   - [info.picocli:picocli:4.7.1](https://mvnrepository.com/artifact/info.picocli/picocli/4.7.1)  



## Installation und Ausführung

1. **Projekt herunterladen**  
   Lade das Projekt-Repository von GitHub herunter.
2. **Bibliotheken einbinden**  
   Erstelle einen `lib`-Ordner, in welchem du die oben genannten .jar-Dateien (`mariadb-java-client-3.4.1.jar` und `picocli-4.7.1.jar`) hinzufügst.

3. **Ausführen**  
	
	Stelle sicher, dass die Ordner im Speicherpfad keine Leerzeichen beinhalten. 
   - **Windows Command Line Prompt (cmd)**  

		1. Wechsle in das Projektverzeichnis 
			``` 
			C:\Users\user>cd /d E:\ToDo-main\ToDo-main
			```
		2. führe die `.bat`-Datei mit folgendem Befehl aus:
		     ```
		     E:\ToDo-main\ToDo-main>todo
   - **PowerShell**  
     1. Wechsle ebenfalls in das Projektverzeichnis 
		   ```powershell
		   PS C:\Users\user> cd E:\ToDo-main\ToDo-main\
	     ```

     3. führe die `.bat`-Datei mit folgendem Befehl aus:
		   ```powershell
		   PS E:\ToDo-main\ToDo-main> .\todo.bat
	     ```
---

## Verwendung

Um das Programm zu nutzen, rufe in **Windows Command Line Prompt** den Befehl `todo` auf und in **PowerShell** den Befehl `.\todo.bat` und übergib ihm den passenden **Command** und **Type** mit den entsprechenden **Options**.

Im Folgenden wird davon ausgegangen, dass **Windows Command Line Prompt** verwendet wird.
```bash
Usage: todo [COMMAND] [TYPE] [OPTIONS]

Type of items available for all commands: [user, todo, category]

Commands:
  show    Show an entity based on type
  create  Create a new entity based on type
  delete  Delete an entity based on type
  update  Update an existing entity based on type
  login   Log in to your account
  logout  Log out of your account
  help    Show this help message
```

### ShowCommand
Zeige eine Liste von Einträgen oder einen einzelnen Eintrag, ggf. sortiert.

- **Optionen:**
  - `--sort-by <String>` : Mögliche Werte: `[priority, date]`  
  - `--order <String>` : Mögliche Werte: `[asc, desc]`

**Beispiel:**
```bash
todo show todo --sort-by priority --order asc
```
Zeigt alle ToDos, sortiert nach `priority` in `asc ` Reihenfolge.

### CreateCommand
Erstelle eine neue Entität vom Typ `user`, `todo` oder `category`.

- **Optionen:**
  - **user**:  
    - `--username <String>` : Benutzername (max. 32 Zeichen)  
    - `--password <String>` : Passwort
  - **todo**:  
    - `--title <String>` : Titel (max. 128 Zeichen)  
    - `--description <String>` : Detaillierte Beschreibung  
    - `--priority <Integer>` : Priorität: `[0 = high, 1 = low]`  
    - `--category <String>` : Zugehörige Kategorie
  - **category**:  
    - `--title <String>` : Titel (max. 32 Zeichen)

**Beispiel:**
```bash
todo create user --username Max --password geheim
```
Erstellt einen neuen Benutzer namens *Max* mit dem Passwort *geheim*.

### DeleteCommand
Löscht eine Entität (Benutzer, ToDo oder Kategorie) anhand ihrer ID.

- **Optionen:**
  - `--id <Integer>` : ID des Elements, das gelöscht werden soll

**Beispiel:**
```bash
todo delete todo --id 42
```
Löscht das ToDo-Element mit der ID `42`.

### UpdateCommand
Aktualisiert eine bestehende Entität (Benutzer, ToDo oder Kategorie).

- **Optionen:**
  - `--id <Integer>` : ID des Elements, das aktualisiert werden soll
  - **user**:  
    - `--username <String>` : Neuer Benutzername (max. 32 Zeichen)  
    - `--password <String>` : Neues Passwort
  - **todo**:  
    - `--title <String>` : Neuer Titel  
    - `--description <String>` : Aktualisierte Beschreibung  
    - `--priority <Integer>` : Neue Priorität: `[0 = high, 1 = low]`  
    - `--category <String>` : Neue Kategorie
  - **category**:  
    - `--title <String>` : Neuer Titel

**Beispiel:**
```bash
todo update todo --id 42 --title newTitle --priority 0
```
Aktualisiert das ToDo mit ID `42`, indem der Titel auf *newTitle* und die Priorität auf *high* (0) gesetzt wird.

### Login
Melde dich mit einem Benutzernamen und Passwort an.

```bash
todo login --username Max --password geheim
```

### Logout
Wenn du das Programm nicht mehr verwenden möchtest, melde dich von deinem Konto ab.

```bash
todo logout
```

### Hilfe anzeigen
Zeigt eine Hilfeübersicht aller verfügbaren Befehle.

```bash
todo help
```

---
Viel Spaß beim Benutzen des ToDo-Projekts!
