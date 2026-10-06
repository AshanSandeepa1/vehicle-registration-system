@echo off
setlocal enabledelayedexpansion

echo =========================================================
echo       Vehicle Registration System (VRS) Launcher
echo =========================================================

:: Locate Java / Javac
set "JAVA_BIN="
set "JAVAC_BIN="

where java >nul 2>&1
if %errorlevel% equ 0 (
    set "JAVA_BIN=java"
    set "JAVAC_BIN=javac"
) else (
    if exist "C:\Users\shamil\develop\oracleJdk-27\bin\java.exe" (
        set "JAVA_BIN=C:\Users\shamil\develop\oracleJdk-27\bin\java.exe"
        set "JAVAC_BIN=C:\Users\shamil\develop\oracleJdk-27\bin\javac.exe"
    ) else if exist "C:\Users\shamil\.vscode\extensions\redhat.java-1.56.0-win32-x64\jre\21.0.12.1-win32-x86_64\bin\java.exe" (
        set "JAVA_BIN=C:\Users\shamil\.vscode\extensions\redhat.java-1.56.0-win32-x64\jre\21.0.12.1-win32-x86_64\bin\java.exe"
        set "JAVAC_BIN=C:\Users\shamil\.vscode\extensions\redhat.java-1.56.0-win32-x64\jre\21.0.12.1-win32-x86_64\bin\javac.exe"
    ) else if defined JAVA_HOME (
        set "JAVA_BIN=%JAVA_HOME%\bin\java.exe"
        set "JAVAC_BIN=%JAVA_HOME%\bin\javac.exe"
    )
)

if "%JAVA_BIN%"=="" (
    echo Error: Java could not be located on this machine.
    echo Please install JDK 17+ or add Java to your PATH.
    pause
    exit /b 1
)

echo [VRS] Using Java: "%JAVA_BIN%"

:: Ensure build\classes directory exists
if not exist "build\classes" mkdir "build\classes"

:: Ensure images are copied to classes folder
if not exist "build\classes\Images" mkdir "build\classes\Images"
if exist "src\Images" xcopy /y /q "src\Images\*" "build\classes\Images\" >nul 2>&1

:: Classpath setup
set "CP=build\classes;lib\flatlaf-3.5.4.jar;lib\jcalendar-1.4.jar;lib\ojdbc11.jar;lib\h2-2.2.224.jar;src"

:: Compile Java sources
echo [VRS] Compiling sources...
dir /s /b "src\*.java" > "build\sources.txt"
"%JAVAC_BIN%" -encoding UTF-8 -cp "%CP%" -d "build\classes" @"build\sources.txt"
if %errorlevel% neq 0 (
    echo [VRS] Compilation encountered issues. Attempting to run with existing build...
) else (
    echo [VRS] Compilation successful.
)

:: Launch Application
echo [VRS] Starting Vehicle Registration System GUI...
echo =========================================================
"%JAVA_BIN%" -cp "%CP%" vehicleregsystem.VehicleRegSystem

endlocal

