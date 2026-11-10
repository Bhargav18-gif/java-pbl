@echo off
title Hospital Appointment & Patient Queue Management System
echo ========================================================
echo   Launching Hospital System (Java Swing PBL Project)
echo ========================================================
java -cp bin hospital.Main
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo An error occurred while launching. If you made code changes, run build.bat first.
    pause
)
