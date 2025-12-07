@echo off
REM Wrapper to run the PowerShell dev script with bypassed execution policy
powershell -ExecutionPolicy Bypass -File "%~dp0run-dev.ps1" %*
