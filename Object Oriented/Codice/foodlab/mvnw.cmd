@ECHO OFF
SETLOCAL

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

"%JAVA_EXE%" "-Dmaven.multiModuleProjectDirectory=%CD%" -classpath "%WRAPPER_JAR%" org.apache.maven.wrapper.MavenWrapperMain %*
ENDLOCAL
