@echo off
SETLOCAL

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.

set APP_BASE_NAME=%~n0
set APP_NAME=Gradle

set DEFAULT_JVM_OPTS=

set GRADLE_HOME=%DIRNAME%

set CLASSPATH=%GRADLE_HOME%\gradle\wrapper\gradle-wrapper.jar

java %DEFAULT_JVM_OPTS% -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*