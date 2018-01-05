@ECHO OFF

REM Copy the latest jar from build folder
IF EXIST "%PRJ_HOME%\build\%PRJ_JAR%" (
COPY "%PRJ_HOME%\build\%PRJ_JAR%" "%PRJ_HOME%\%PRJ_JAR%"
)

REM Run the database initialization from xml in command line
java -jar %PRJ_HOME%\%PRJ_JAR% INITDBXML