@echo off
if exist target rmdir /s /q target
pause
mvn clean package
