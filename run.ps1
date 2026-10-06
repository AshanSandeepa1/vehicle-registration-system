# =========================================================
# Vehicle Registration System (VRS) - PowerShell Launcher
# =========================================================

$ErrorActionPreference = "Continue"

Write-Host "=========================================================" -ForegroundColor Cyan
Write-Host "      Vehicle Registration System (VRS) Launcher         " -ForegroundColor Cyan
Write-Host "=========================================================" -ForegroundColor Cyan

# 1. Locate Java & Javac
$javaCmd = Get-Command java -ErrorAction SilentlyContinue
$javacCmd = Get-Command javac -ErrorAction SilentlyContinue
$javaExe = if ($javaCmd) { $javaCmd.Source } else { $null }
$javacExe = if ($javacCmd) { $javacCmd.Source } else { $null }

if (-not $javaExe) {
    $oracleJdk = "C:\Users\shamil\develop\oracleJdk-27\bin\java.exe"
    $oracleJavac = "C:\Users\shamil\develop\oracleJdk-27\bin\javac.exe"
    $vscodeJava = "C:\Users\shamil\.vscode\extensions\redhat.java-1.56.0-win32-x64\jre\21.0.12.1-win32-x86_64\bin\java.exe"
    $vscodeJavac = "C:\Users\shamil\.vscode\extensions\redhat.java-1.56.0-win32-x64\jre\21.0.12.1-win32-x86_64\bin\javac.exe"
    if (Test-Path $oracleJdk) {
        $javaExe = $oracleJdk
        $javacExe = $oracleJavac
    } elseif (Test-Path $vscodeJava) {
        $javaExe = $vscodeJava
        $javacExe = $vscodeJavac
    } elseif ($env:JAVA_HOME) {
        $javaExe = Join-Path $env:JAVA_HOME "bin\java.exe"
        $javacExe = Join-Path $env:JAVA_HOME "bin\javac.exe"
    }
}

if (-not $javaExe -or -not (Test-Path $javaExe)) {
    Write-Host "Error: Java could not be located. Please ensure JDK 17+ is installed." -ForegroundColor Red
    exit 1
}

Write-Host "[VRS] Using Java: $javaExe" -ForegroundColor Green

# 2. Prepare directories
if (-not (Test-Path "build\classes")) {
    New-Item -ItemType Directory -Path "build\classes" -Force | Out-Null
}
if (-not (Test-Path "build\classes\Images")) {
    New-Item -ItemType Directory -Path "build\classes\Images" -Force | Out-Null
}

# 3. Copy resource images
if (Test-Path "src\Images") {
    Copy-Item -Path "src\Images\*" -Destination "build\classes\Images" -Recurse -Force
}

# 4. Classpath setup
$classpath = "build\classes;lib\flatlaf-3.5.4.jar;lib\jcalendar-1.4.jar;lib\ojdbc11.jar;lib\h2-2.2.224.jar;src"

# 5. Compile sources
Write-Host "[VRS] Compiling sources..." -ForegroundColor Yellow
$javaSources = (Get-ChildItem -Path "src" -Filter "*.java" -Recurse).FullName
& $javacExe -encoding UTF-8 -cp $classpath -d "build\classes" $javaSources

if ($LASTEXITCODE -eq 0) {
    Write-Host "[VRS] Compilation successful." -ForegroundColor Green
} else {
    Write-Host "[VRS] Warning: Some compilation warnings/issues occurred." -ForegroundColor Yellow
}

# 6. Launch Application
Write-Host "[VRS] Launching GUI application..." -ForegroundColor Cyan
Write-Host "=========================================================" -ForegroundColor Cyan
& $javaExe -cp $classpath vehicleregsystem.VehicleRegSystem
