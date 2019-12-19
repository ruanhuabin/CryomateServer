#/bash/bin
#mysql -uroot -p#git2018GIT << END
#use cryomate;
#show tables;
#show columns from $1;
#exit;
#END
#mysql -uroot -p#git2018GIT -e "use cryomate; show columns from $1;" 2>&1 | awk -F' ' '{print $1}' | sed -n '2,$p'
mysql -uroot -p#git2018GIT -e "use cryomate; show columns from $1;" 2>&1 | awk -F' ' '!/Warning/ {print $1}' | sed -n '2,$p'
