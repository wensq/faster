@echo off

java -cp ..\lib\commons-lang3-3.1.jar;..\lib\dom4j-1.6.1.jar;..\lib\freemarker-2.3.20.jar;..\lib\faster-generator-1.0.0-SNAPSHOT.jar org.faster.generator.Main ../conf/generator.xml
