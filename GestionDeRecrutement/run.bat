@echo off
echo ============================================
echo  GestionDeRecrutement - Lancement
echo ============================================

set JAR=%~dp0target\app.jar

if not exist "%JAR%" (
    echo [ERREUR] app.jar introuvable. Lancez d'abord build.bat
    pause
    exit /b 1
)

set JAVA=C:\Program Files\Java\jdk-24\bin\java.exe
if not exist "%JAVA%" set JAVA=java

"%JAVA%" --enable-native-access=ALL-UNNAMED -jar "%JAR%"

if %ERRORLEVEL% neq 0 (
    echo.
    echo [ERREUR] L'application s'est terminee avec une erreur.
    echo Verifiez que XAMPP MySQL est demarree.
    pause
)
