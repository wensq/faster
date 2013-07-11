#!/bin/bash
#####################################################################################
#  EASTCOM Software Code Generator
#  Function:    Auto generate faster-framework source code
#  Author:      sqwen
#  Date:        2013-07-09
#  Abstract:    generate model/criteria/service/service_impl/dto/rs... source code
#####################################################################################

#=======================================================================
# Define arguments for process
#=======================================================================

#-----------------------------------------------------------------------
# Process Name to display
PROC_NAME="Faster Code Generator"
#-----------------------------------------------------------------------

#-----------------------------------------------------------------------
# Flags for JVM
# Example:
#       VM_FLAG="-DIPNET_BASE=/export/home/ipnet -Xrs -Xms200M -Xmx256M"
#VM_FLAG="-Xrs -Xms100M -Xmx120M -Djava.rmi.server.hostname=ip.gd.chinamobile.com"
VM_FLAG="-d64 -server -Xrs -Xms100M -Xmx100M"
#-----------------------------------------------------------------------

#-----------------------------------------------------------------------
# List of blank-separated paths defining the contents of the classes
# and jars
# Examples:
#       LOADER_PATH="foo foo/*.jar lib/ipnet.jar"
#     "foo": Add this folder as a class repository
#     "foo/*.jar": Add all the JARs of the specified folder as class
#                  repositories
#     "lib/ipnet.jar": Add lib/ipnet.jar as a class repository
LOADER_PATH="../lib/*.jar"
#-----------------------------------------------------------------------

#-----------------------------------------------------------------------
# Specify the java_home
# if it has JAVA_HOME environment argument,specify to it,
# else change it to correct java path
#-----------------------------------------------------------------------
java_home=$JAVA_HOME

#-----------------------------------------------------------------------
# Process Entrance class
MAIN_CLASS=org.faster.generator.Main
#-----------------------------------------------------------------------

#-----------------------------------------------------------------------
# Process Arguments
PROC_ARGS=
#-----------------------------------------------------------------------

#=======================================================================
# Define functions for process
#=======================================================================

set_classpath(){
        set ${LOADER_PATH}
        while [ $# -gt 0 ]; do
                classpath=${classpath}:$1
                shift
        done
        CLASSPATH=${classpath}:${CLASSPATH}
}

echo "Starting ${PROC_NAME} ..."
set_classpath
if [ $# -eq 0 ]; then
    cfg_file="../conf/generator.xml"
else
    cfg_file=$1
fi
${java_home}/bin/java ${VM_FLAG} -cp ${CLASSPATH} ${MAIN_CLASS} $cfg_file
