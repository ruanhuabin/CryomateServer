#!/bin/bash - 
set -o nounset                              # Treat unset variables as an error
set -x
set -e

if [ $# != 6 ]
then
    echo "Usage: ./create_user.sh <group name> <user name> <password> <home dir prefix> <work dir> <data dir>"
    exit 1
fi
groupName=$1
userName=$2
password=$3
homeDirPrefix=$4
workDir=$5
dataDir=$6
loginNode=10.10.103.111


str="shuangqing604@2020"
#Copy create user script to $loginNode
sshpass -p${str} scp -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no ./warehouse/script/create_user.sh root@${loginNode}:/tmp/create_user.sh

sleep 1

#Running user creating related operation by user root
sshpass -p $str ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no root@${loginNode} "chmod u+x /tmp/create_user.sh;bash /tmp/create_user.sh ${groupName} ${userName} ${password} ${homeDirPrefix} ${workDir} ${dataDir}"

#Copy ssh password-less script to $loginNode 
sshpass -p${str} scp -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no ./warehouse/script/ssh_passless.sh root@${loginNode}:/tmp/ssh_passless.sh

sleep 1

# Running ssh password-less operation on $loginNode
userPassword=${password}
sshpass -p ${userPassword} ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no ${userName}@${loginNode} "chmod u+x /tmp/ssh_passless.sh;bash /tmp/ssh_passless.sh"

# Update NIS info
sshpass -p $str ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no root@${loginNode} "make -C /var/yp/ "
