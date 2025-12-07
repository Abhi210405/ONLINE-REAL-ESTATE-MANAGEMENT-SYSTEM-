@echo off
REM Build MSI Installer for MSP Real Estate System
REM Prerequisites: Java 21, Maven 3.8.0+, WiX Toolset v3.11+ (optional, for MSI)
REM 
REM Usage: build-installer.bat [msi|exe]
REM   msi - Create Windows MSI installer (default)
REM   exe - Create portable EXE executable

setlocal enabledelayedexpansion

REM Set Java 21
set "JAVA_HOME=C:\Program Files\Java\jdk-21"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo ===================================================================
echo MSP Real Estate System - Installer Build
echo ===================================================================
echo.

REM Verify Java 21
java -version
if errorlevel 1 (
    echo ERROR: Java 21 not found. Please install JDK 21 and set JAVA_HOME.
    pause
    exit /b 1
)

REM Verify jpackage
jpackage --version
if errorlevel 1 (
    echo ERROR: jpackage not found. This is included in JDK 21.
    pause
    exit /b 1
)

REM Determine installer type
set "INSTALLER_TYPE=msi"
if not "%~1"=="" (
    set "INSTALLER_TYPE=%~1"
)

echo.
echo Building project...
mvn -DskipTests clean package
if errorlevel 1 (
    echo ERROR: Maven build failed.
    pause
    exit /b 1
)

echo.
echo Packaging as %INSTALLER_TYPE% installer...
cd target

if "%INSTALLER_TYPE%"=="msi" (
    echo Creating Windows MSI installer...
    if not exist installer mkdir installer
    
    jpackage ^
      --input . ^
      --name MSPRealEstate ^
      --main-jar msp-realestate-1.0-SNAPSHOT.jar ^
      --main-class com.msp.realestate.MSPRealEstateSystem ^
      --type msi ^
      --dest installer ^
      --vendor "MSP Real Estate" ^
      --app-version 1.0 ^
      --win-menu ^
      --win-shortcut ^
      --description "MSP Real Estate System - Java 21 Desktop Application"
    
    if errorlevel 1 (
        echo ERROR: jpackage MSI build failed.
        echo Check that WiX Toolset v3.11+ is installed.
        pause
        exit /b 1
    )
    
    echo.
    echo SUCCESS! MSI installer created:
    echo Location: %CD%\installer\MSPRealEstate-1.0.msi
    
) else if "%INSTALLER_TYPE%"=="exe" (
    echo Creating portable EXE executable...
    if not exist installer mkdir installer
    
    jpackage ^
      --input . ^
      --name MSPRealEstate ^
      --main-jar msp-realestate-1.0-SNAPSHOT.jar ^
      --main-class com.msp.realestate.MSPRealEstateSystem ^
      --type exe ^
      --dest installer ^
      --vendor "MSP Real Estate" ^
      --app-version 1.0 ^
      --win-shortcut ^
      --description "MSP Real Estate System - Java 21 Desktop Application"
    
    if errorlevel 1 (
        echo ERROR: jpackage EXE build failed.
        pause
        exit /b 1
    )
    
    echo.
    echo SUCCESS! Portable EXE created:
    echo Location: %CD%\installer\MSPRealEstate-1.0.exe
    
) else (
    echo ERROR: Invalid installer type "%INSTALLER_TYPE%"
    echo Valid options: msi, exe
    pause
    exit /b 1
)

echo.
echo ===================================================================
echo To distribute the installer:
echo   1. Copy the installer file to distribution media
echo   2. Share with end users
echo   3. End users run the installer to install the application
echo ===================================================================
pause
