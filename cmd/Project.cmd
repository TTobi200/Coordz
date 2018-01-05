@ECHO OFF
REM Initialize the project variables
SET PRJ_NAME=%1
SET PRJ_HOME=%~dp0..
SET PRJ_JAR=%PRJ_NAME%.jar

REM Add the cmd folder to PATH variable
PATH=%PRJ_HOME%\cmd;%PATH%

REM startup a new command line in project folder
start "Project %PRJ_NAME%" cmd /K "CD %PRJ_HOME%"