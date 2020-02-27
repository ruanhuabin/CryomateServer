#!/bin/bash - 
set -o nounset                              # Treat unset variables as an error

nodeList="cpu001 cpu002 gpu001 gpu002 gpu003"
set -x
ssh-keygen -q -t rsa -N '' -f ~/.ssh/id_rsa 2>/dev/null <<< y >/dev/null
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
ssh-keyscan -t rsa $nodeList >> ~/.ssh/known_hosts


