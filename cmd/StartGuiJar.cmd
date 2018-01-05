@ECHO OFF

REM Copy the latest jar from build folder
IF EXIST "%PRJ_HOME%\build\%PRJ_JAR%" (
COPY "%PRJ_HOME%\build\%PRJ_JAR%" "%PRJ_HOME%\%PRJ_JAR%"
)

REM Startup the gui
java -jar %PRJ_HOME%\%PRJ_JAR%