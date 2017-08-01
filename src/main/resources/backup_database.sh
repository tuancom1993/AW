#!/bin/bash
####################################
#
# Backup DATABASE
#
####################################

dest="/amway-backup"
filename="amway_dev"_`date +%Y-%m-%d_%H:%M:%S`".sql"
username="root"
password="root"

dumDB(){
  echo "Stating backup DB Amway"
  mysqldump -u $username -p$password amway_dev > $dest"/"$filename
  echo "Finish"
}

main(){
  dumDB
}

main

exit 0




