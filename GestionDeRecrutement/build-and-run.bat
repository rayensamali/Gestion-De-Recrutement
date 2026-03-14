@echo off
echo ============================================
echo  GestionDeRecrutement - Build + Run
echo ============================================

:: ---- Build ----
call "C:\Users\rayen\.maven\maven-3.9.12\bin\mvn.cmd" package -q
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Build failed.
    pause
    exit /b 1
)
echo [OK] Build OK

:: ---- Run ----
set JAVA=C:\Program Files\Java\jdk-24\bin\java.exe
if not exist "%JAVA%" set JAVA=java

"%JAVA%" --enable-native-access=ALL-UNNAMED -jar "%~dp0target\app.jar"
