#!/bin/bash - 
set -o nounset                              # Treat unset variables as an error
set -x 

groupName=$1
userName=$2
password=$3
homeDirPrefix=$4
workDir=$5
dataDir=$6
computeNodeList="cpu001 cpu002 gpu001 gpu002 gpu003"

#The server should ensure the group name is not empty
groupadd $groupName
if [ $? -ne 0 ]
then
    echo "Warning: the group name [${groupName}] is already exist"
fi

id ${userName}
if [ $? -ne 0 ]
then
    #set user home dir to /Share/UserHome/username
    useradd -g ${groupName} -b ${homeDirPrefix} $userName; 
    #set password for new created user 
    echo $password | passwd --stdin $userName; 
    make -C /var/yp/
else
    echo "Warning: user [${userName}] is already exist, skipping create user"
fi

#create data dir for new created user
mkdir -p $dataDir; 
chown -R ${userName}:${groupName} $dataDir; 

#create work dir for new created user
mkdir -p ${workDir}
chown -R ${userName}:${groupName} ${workDir} 


#set ssh password-less between all this nodes: loginnode, cpu001,cpu002,gpu001,gpu002,gpu003
#ssh-keygen -q -t rsa -N '' -f ~/.ssh/id_rsa3 2>/dev/null <<< y >/dev/null


