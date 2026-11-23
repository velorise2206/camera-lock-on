@echo off
setlocal
cd /d "%~dp0"
set "CHOICE=%~1"
if not "%CHOICE%"=="" goto :HANDLE

:MENU
cls
echo =====================================================================
echo               CAMERA LOCK-ON - CLIENT LAUNCHER (17 PORTS)
echo =====================================================================
echo   1) Forge 1.20.1     (Java 17)    2) Fabric 1.20.1    (Java 17)
echo   3) Forge 1.21.1     (Java 21)    4) NeoForge 1.21.1  (Java 21)
echo   5) Fabric 1.21.1    (Java 21)    6) Forge 1.21.11    (Java 21)
echo   7) NeoForge 1.21.11 (Java 21)    8) Fabric 1.21.11   (Java 21)
echo   9) Forge 26.1.2     (Java 25)   10) NeoForge 26.1.2  (Java 25)
echo  11) Fabric 26.1.2    (Java 25)   12) Forge 26.2       (Java 25)
echo  13) NeoForge 26.2    (Java 25)   14) Fabric 26.2      (Java 25)
echo  15) Forge 26.3       (Java 25)   16) NeoForge 26.3    (Java 25)
echo  17) Fabric 26.3      (Java 25)    0) Exit
echo ---------------------------------------------------------------------
set /p "CHOICE=Select a port [1-17]: "

:HANDLE
if "%CHOICE%"=="0" exit /b 0
if "%CHOICE%"=="1"  call "%~dp0scripts\run-port.bat" 1.20.1\forge 17 & exit /b %ERRORLEVEL%
if "%CHOICE%"=="2"  call "%~dp0scripts\run-port.bat" 1.20.1\fabric 17 & exit /b %ERRORLEVEL%
if "%CHOICE%"=="3"  call "%~dp0scripts\run-port.bat" 1.21.1\forge 21 & exit /b %ERRORLEVEL%
if "%CHOICE%"=="4"  call "%~dp0scripts\run-port.bat" 1.21.1\neoforge 21 & exit /b %ERRORLEVEL%
if "%CHOICE%"=="5"  call "%~dp0scripts\run-port.bat" 1.21.1\fabric 21 & exit /b %ERRORLEVEL%
if "%CHOICE%"=="6"  call "%~dp0scripts\run-port.bat" 1.21.11\forge 21 & exit /b %ERRORLEVEL%
if "%CHOICE%"=="7"  call "%~dp0scripts\run-port.bat" 1.21.11\neoforge 21 & exit /b %ERRORLEVEL%
if "%CHOICE%"=="8"  call "%~dp0scripts\run-port.bat" 1.21.11\fabric 21 & exit /b %ERRORLEVEL%
if "%CHOICE%"=="9"  call "%~dp0scripts\run-port.bat" 26.1\forge 25 & exit /b %ERRORLEVEL%
if "%CHOICE%"=="10" call "%~dp0scripts\run-port.bat" 26.1\neoforge 25 & exit /b %ERRORLEVEL%
if "%CHOICE%"=="11" call "%~dp0scripts\run-port.bat" 26.1\fabric 25 & exit /b %ERRORLEVEL%
if "%CHOICE%"=="12" call "%~dp0scripts\run-port.bat" 26.2\forge 25 & exit /b %ERRORLEVEL%
if "%CHOICE%"=="13" call "%~dp0scripts\run-port.bat" 26.2\neoforge 25 & exit /b %ERRORLEVEL%
if "%CHOICE%"=="14" call "%~dp0scripts\run-port.bat" 26.2\fabric 25 & exit /b %ERRORLEVEL%
if "%CHOICE%"=="15" call "%~dp0scripts\run-port.bat" 26.3\forge 25 & exit /b %ERRORLEVEL%
if "%CHOICE%"=="16" call "%~dp0scripts\run-port.bat" 26.3\neoforge 25 & exit /b %ERRORLEVEL%
if "%CHOICE%"=="17" call "%~dp0scripts\run-port.bat" 26.3\fabric 25 & exit /b %ERRORLEVEL%
echo Invalid selection: %CHOICE%
pause
goto :MENU
