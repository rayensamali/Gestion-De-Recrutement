@echo off
echo ============================================
echo  Building GestionDeRecrutement fat JAR...
echo ============================================
call "C:\Users\rayen\.maven\maven-3.9.12\bin\mvn.cmd" package -q
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Build failed. See output above.
    pause
    exit /b 1
)
echo.
echo [OK] Build successful! JAR created at: target\app.jar
echo.
pause
