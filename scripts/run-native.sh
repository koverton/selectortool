#!/bin/bash
cd `dirname $0`/..

function getjar () {
	ls -1 target/selector-tool-*-jar-with-dependencies.jar
}

jarfile=`getjar`
while [ "$jarfile" == "" ]; do
	./scripts/make-fat-jar.sh
	jarfile=`getjar`
done

java -jar $jarfile $*

