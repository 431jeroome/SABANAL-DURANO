@echo off
echo Compiling CPU Scheduling Visualization...
javac *.java
if %errorlevel% equ 0 (
    echo Compilation successful!
    echo Starting CPU Scheduling Visualization...
    echo.
    java CPUSchedulingVisualization
) else (
    echo Compilation failed! Please check your Java installation.
    pause
) 