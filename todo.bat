@echo off
REM Kompiliere alle notwendigen Java-Dateien und schreibe die .class-Dateien in den "out"-Ordner
javac -cp "%~dp0lib\picocli-4.7.1.jar" -d "%~dp0out" "%~dp0src\com\*.java" "%~dp0src\com\Connector\*.java" "%~dp0src\picocli\*.java"

REM Führe die Hauptklasse (cliNavigation) aus
java -cp "%~dp0lib\picocli-4.7.1.jar;%~dp0out" com.cliNavigation %*
