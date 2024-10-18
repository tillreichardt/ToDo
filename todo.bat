@echo off
javac -cp "%~dp0lib\picocli-4.7.1.jar;%~dp0lib\mariadb-java-client-3.4.1.jar" -d "%~dp0out" "%~dp0src\com\*.java" "%~dp0src\com\Connector\*.java" "%~dp0src\picocli\*.java"

java -cp "%~dp0lib\picocli-4.7.1.jar;%~dp0lib\mariadb-java-client-3.4.1.jar;%~dp0out" com.cliNavigation %*
