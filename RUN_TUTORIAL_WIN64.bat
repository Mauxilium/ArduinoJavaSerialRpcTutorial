echo off
rem
rem ╔════════════════════════╗
rem   Author: Gabriele Maris
rem   Project: Mauxilium
rem ╚════════════════════════╝
rem  Copyright 2020 Gabriele Maris
rem
rem Before to run this Java program, please install the sketch ArduinoJavaSerialRpcTutorial\ArduinoSketch\ArduinoSketch.ino into your Arduino Card
rem

java -Djava.library.path=RxTx\mfz-rxtx-2.2-20081207-win-x64 -cp ".\target\classes;%HOMEPATH%\.m2\repository\it\mauxilium\ArduinoJavaSerialRpc\1.0.0\ArduinoJavaSerialRpc-1.0.0.jar;%HOMEPATH%\.m2\repository\org\bidib\jbidib\org\qbang\rxtx\rxtxcomm\2.2\rxtxcomm-2.2.jar" it.mauxilium.arduinojavaserialrpctutorial.JavaRpcTutorial COM5 9600

pause