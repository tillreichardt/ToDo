@echo off
REM Kompiliere alle notwendigen Java-Dateien
javac -cp "%~dp0lib\picocli-4.7.1.jar" "%~dp0src\com\picocli\cliNavigation.java" "%~dp0src\com\picocli\ShowCommand.java" "%~dp0src\com\picocli\CreateCommand.java" "%~dp0src\com\picocli\UpdateCommand.java" "%~dp0src\com\picocli\DeleteCommand.java" "%~dp0src\com\picocli\CustomHelpCommand.java"

REM Führe die Hauptklasse (cliNavigation) aus
java -cp "%~dp0lib\picocli-4.7.1.jar;%~dp0src" com.picocli.cliNavigation %*