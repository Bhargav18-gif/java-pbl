@echo off
title Build Hospital System
echo ========================================================
echo   Compiling Java Source Code...
echo ========================================================
if not exist "bin" mkdir bin
javac -d bin -sourcepath src src\hospital\Main.java src\hospital\SystemTestSuite.java
if %ERRORLEVEL% EQU 0 (
    echo.
    echo [SUCCESS] Build succeeded! Classes generated in bin/
) else (
    echo.
    echo [ERROR] Build failed! Check compiler output above.
)
pause
