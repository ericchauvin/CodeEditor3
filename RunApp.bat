
rem @echo off

SET JAVA_HOME="C:\Javajdk"
SET JDK_HOME=%JAVA_HOME%
SET JRE_HOME="C:\JavaJre"
SET CLASSPATH=".;%JAVA_HOME%\lib;%JAVA_HOME%\jre\lib
SET PATH=%PATH%;%JAVA_HOME%\bin;

cd \Eric\CodeEditorJava

Java MainApp

rem pause

