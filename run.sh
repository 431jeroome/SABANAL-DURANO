#!/bin/bash
echo "Compiling CPU Scheduling Visualization..."
javac *.java
if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "Starting CPU Scheduling Visualization..."
    echo
    java CPUSchedulingVisualization
else
    echo "Compilation failed! Please check your Java installation."
    read -p "Press Enter to continue..."
fi 