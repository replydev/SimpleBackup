# Simple Backup

## Overview
This simple backup utility can create zip file of a given directory, checking for duplicated backups and limiting max backups to limit the hard drive usage.
## Installation
The only thing you need to do is to download the jar and write your configuration, similar to the following:

FOLDER_TO_BACKUP = src\
FOLDER_TO_STORE_BACKUPS = backups/\
INITIAL_BACKUP_DELAY = 0\
BACKUP_FREQUENCY = 10,SECONDS\
MAX_BACKUPS_TO_STORE = 5

## Running
As usual:\
`java -Dfile.encoding=UTF-8 -jar SimpleBackup.jar`