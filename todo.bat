@echo off
REM Kompiliere alle notwendigen Java-Dateien und stelle sicher, dass picocli im Klassenpfad ist
javac -cp lib\picocli-4.7.1.jar src\com\picocli\cliNavigation.java src\com\picocli\ShowCommand.java src\com\picocli\CreateCommand.java src\com\picocli\UpdateCommand.java src\com\picocli\DeleteCommand.java

REM Führe die Hauptklasse (cliNavigation) aus und leite alle Argumente weiter
java -cp lib\picocli-4.7.1.jar;src com.picocli.cliNavigation %*
