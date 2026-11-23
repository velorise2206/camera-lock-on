@echo off
setlocal
set "TARGET=%~1"
set "JVER=%~2"
set "REPO_ROOT=%~dp0.."

for /d %%D in ("C:\Program Files\Eclipse Adoptium\jdk-%JVER%*") do set "JAVA_HOME=%%D"
for /d %%D in ("C:\Program Files\Eclipse Adoptium\jdk-8*") do set "JDK8=%%D"
if defined JAVA_HOME set "PATH=%JAVA_HOME%\bin;%PATH%"

echo [Camera Lock-On] Launching %TARGET% with Java %JVER% (%JAVA_HOME%)...
cd /d "%REPO_ROOT%\%TARGET%"
call gradlew.bat runClient
exit /b %ERRORLEVEL%
