#!/bin/sh

java -Djava.net.preferIPv4Stack=true -Xms128m -Xmx256m -jar /app/*.jar -b 0.0.0.0 

