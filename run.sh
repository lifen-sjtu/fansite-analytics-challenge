#!/usr/bin/env bash

# one example of run.sh script for implementing the features using python
# the contents of this script could be replaced with similar files from any major language

# I'll execute my programs, with the input directory log_input and output the files in the directory log_output
javac ./src/com/insights/data/Main.java ./src/com/insights/data/models/*.java ./src/com/insights/data/processors/*.java ./src/com/insights/data/util/*.java 
java -classpath ./src com/insights/data/Main ./log_input/log.txt ./log_output/hosts.txt ./log_output/hours.txt ./log_output/resources.txt ./log_output/blocked.txt