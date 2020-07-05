rem @echo off

rem SET JAVA_HOME="C:\Javajdk"
rem SET JDK_HOME=%JAVA_HOME%
rem SET JRE_HOME="C:\Javajdk\jre"

rem SET CLASSPATH=".;%JAVA_HOME%\lib;%JAVA_HOME%\jre\lib

SET PATH=%PATH%;%JAVA_HOME%\bin;

cd \EricMain\CodeEditor3
Java MainApp "\EricMain\CodeEditor3"
