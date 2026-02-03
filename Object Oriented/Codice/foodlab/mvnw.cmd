@ECHO OFF
SETLOCAL EnableDelayedExpansion

SET MVNW_REPOURL=https://repo.maven.apache.org/maven2
SET WRAPPER_JAR=.mvn\wrapper\maven-wrapper.jar
SET WRAPPER_PROPS=.mvn\wrapper\maven-wrapper.properties
SET WRAPPER_URL=%MVNW_REPOURL%/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar

IF NOT EXIST "%WRAPPER_PROPS%" (
  ECHO Missing %WRAPPER_PROPS% 1>&2
  EXIT /B 1
)

IF NOT EXIST "%WRAPPER_JAR%" (
  ECHO Downloading Maven Wrapper...
  powershell -NoProfile -ExecutionPolicy Bypass -Command "New-Item -ItemType Directory -Force -Path '.mvn/wrapper' | Out-Null; Invoke-WebRequest -UseBasicParsing -Uri '%WRAPPER_URL%' -OutFile '%WRAPPER_JAR%'" 
  IF ERRORLEVEL 1 (
    ECHO Failed to download Maven Wrapper jar. 1>&2
    EXIT /B 1
  )
)

SET JAVA_EXE=java
IF NOT "%JAVA_HOME%"=="" SET JAVA_EXE=%JAVA_HOME%\bin\java.exe

REM Read extra JVM arguments from .mvn/jvm.config (one arg per line)
SET JVM_CONFIG_FILE=.mvn\jvm.config
SET JVM_CONFIG_MAVEN_OPTS=
IF EXIST "%JVM_CONFIG_FILE%" (
  FOR /F "usebackq delims=" %%A IN ("%JVM_CONFIG_FILE%") DO (
    SET "LINE=%%A"
    IF NOT "!LINE!"=="" (
      IF NOT "!LINE:~0,1!"=="#" (
        SET "JVM_CONFIG_MAVEN_OPTS=!JVM_CONFIG_MAVEN_OPTS! !LINE!"
      )
    )
  )
)

"%JAVA_EXE%" %JVM_CONFIG_MAVEN_OPTS% "-Dmaven.multiModuleProjectDirectory=%CD%" -classpath "%WRAPPER_JAR%" org.apache.maven.wrapper.MavenWrapperMain %*
ENDLOCAL
