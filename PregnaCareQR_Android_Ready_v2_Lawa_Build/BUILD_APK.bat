@echo off
setlocal
set "GRADLE_VERSION=8.13"
set "ROOT=%~dp0"
set "CACHE=%ROOT%.gradle-local"
set "ZIP=%CACHE%\gradle-%GRADLE_VERSION-bin.zip"
set "DIST=%CACHE%\gradle-%GRADLE_VERSION"

if not exist "%DIST%\bin\gradle.bat" (
  if not exist "%CACHE%" mkdir "%CACHE%"
  echo Downloading Gradle %GRADLE_VERSION%...
  powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -Uri 'https://services.gradle.org/distributions/gradle-%GRADLE_VERSION-bin.zip' -OutFile '%ZIP%'"
  if errorlevel 1 (
    echo Failed to download Gradle.
    pause
    exit /b 1
  )
  powershell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -Force '%ZIP%' '%CACHE%'"
)
call "%DIST%\bin\gradle.bat" -p "%ROOT%" assembleDebug
if errorlevel 1 (
  echo.
  echo BUILD FAILED.
  pause
  exit /b 1
)
echo.
echo BUILD SUCCESSFUL.
echo APK: %ROOT%app\build\outputs\apk\debug\app-debug.apk
pause
