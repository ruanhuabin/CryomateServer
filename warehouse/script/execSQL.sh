#!/bin/bash
mysql -uroot -p#git2018GIT -hdb001 -e "use cryomate; $1;" 2>&1 | awk '!/Warning/ {print $0}'
