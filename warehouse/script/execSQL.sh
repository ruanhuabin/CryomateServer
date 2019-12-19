#!/bin/bash
mysql -uroot -p#git2018GIT -e "use cryomate; $1;" 2>&1 | awk '!/Warning/ {print $0}'
