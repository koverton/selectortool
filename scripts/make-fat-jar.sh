#!/bin/bash
cd `dirname $0`/..

mvn clean compile assembly:single
