#!/bin/bash - 
#===============================================================================
#
#          FILE:  execSysCommand.sh
# 
#         USAGE:  ./execSysCommand.sh 
# 
#   DESCRIPTION:  
# 
#       OPTIONS:  ---
#  REQUIREMENTS:  ---
#          BUGS:  ---
#         NOTES:  ---
#        AUTHOR: Ruan Huabin <ruanhuabin@gmail.com>
#       COMPANY: Tsinghua University
#       CREATED: 03/08/2020 10:23:35 PM CST
#      REVISION:  ---
#===============================================================================

set -o nounset                              # Treat unset variables as an error
#set -x

if [ $# -ne 3 ]
then
    echo "argument num: $#"
    echo "Usage: $0 <user name> <password> <command>"
    exit
fi

userName=$1
password=$2
cmd=$3
#loginNode=10.10.103.111

sshpass -p ${password} ssh -o UserKnownHostsFile=~/.ssh/known_hosts -o StrictHostKeyChecking=no ${userName}@loginnode "${cmd}"
