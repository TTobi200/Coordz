@ECHO OFF

REM Change directory to binary files
CD %PRJ_HOME%\bin

REM Unzip all external libs
for /f %%f in ('dir /b .\include\jar\*.jar') do (
	ECHO Unzipping %%f...
	jar xf ".\include\jar\%%f"
)

REM Add out load driver to service file
ECHO org.apache.derby.jdbc.AutoloadedDriver> .\META-INF\services\java.sql.Driver

REM Build the Startup jar file
ECHO Building jar file...
jar cfe "%PRJ_HOME%\%PRJ_JAR%" de.CooStartup *
ECHO %PRJ_JAR% created!

REM Change directory back to project folder
CD %PRJ_HOME%