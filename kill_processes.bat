@echo off
setlocal enabledelayedexpansion

REM List of ports to check and kill
set ports=8071 8090 8080 8091 8092 8093 8094 8095

for %%p in (%ports%) do (
    echo Checking port %%p ...
    for /f "tokens=5" %%a in ('netstat -ano ^| findstr :%%p ^| findstr LISTENING') do (
        echo Killing process with PID %%a on port %%p ...
        taskkill /PID %%a /F >nul 2>&1
    )
)

echo Done.
pause
