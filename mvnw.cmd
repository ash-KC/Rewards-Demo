@REM Maven Wrapper script for Windows
@REM Downloads and uses a project-local Maven installation

@echo off
setlocal

set "MAVEN_PROJECTBASEDIR=%~dp0"
set "WRAPPER_PROPERTIES=%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.properties"

@REM Read distributionUrl from properties
for /f "usebackq tokens=1,* delims==" %%a in ("%WRAPPER_PROPERTIES%") do (
    if "%%a"=="distributionUrl" set "DIST_URL=%%b"
)

@REM Determine Maven home directory
for %%i in ("%DIST_URL%") do set "DIST_NAME=%%~ni"
set "MAVEN_HOME=%MAVEN_PROJECTBASEDIR%.mvn\wrapper\dists\%DIST_NAME%"

@REM Download Maven if not present
if not exist "%MAVEN_HOME%" (
    echo Downloading Maven from %DIST_URL%
    if not exist "%MAVEN_PROJECTBASEDIR%.mvn\wrapper\dists" mkdir "%MAVEN_PROJECTBASEDIR%.mvn\wrapper\dists"
    powershell -Command "Invoke-WebRequest -Uri '%DIST_URL%' -OutFile '%MAVEN_PROJECTBASEDIR%.mvn\wrapper\dists\maven.zip'"
    powershell -Command "Expand-Archive -Path '%MAVEN_PROJECTBASEDIR%.mvn\wrapper\dists\maven.zip' -DestinationPath '%MAVEN_PROJECTBASEDIR%.mvn\wrapper\dists'"
    del "%MAVEN_PROJECTBASEDIR%.mvn\wrapper\dists\maven.zip"
)

"%MAVEN_HOME%\bin\mvn.cmd" %*
