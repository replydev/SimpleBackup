package me.replydev.simplebackup;

import me.replydev.simplebackup.file_structures.FileTree;
import me.replydev.simplebackup.file_structures.HashFile;
import me.replydev.simplebackup.zip.Zip;
import me.replydev.simplebackup.zip.ZipHashUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class BackupTask implements Runnable {
    private final String folder_to_store_backup;
    private final Zip zipInstance;
    private final Config c;
    private final FileTree fileTree;

    public BackupTask(Config c,FileTree fileTree){
        this.c = c;
        this.folder_to_store_backup = c.getFolderToStoreBackups().endsWith("/") ? c.getFolderToStoreBackups() : c.getFolderToStoreBackups() + '/';
        this.zipInstance = new Zip();
        this.fileTree = fileTree;
    }

    @Override
    public void run() {
        String currentTime = getCurrentTime();
        String filePath = folder_to_store_backup + currentTime + ".zip";
        try {
            checkBackups();
            zipInstance.run(c.getFolderToBackup(),filePath);
            long newFileHash = ZipHashUtils.getZipHash(filePath);
            File f = new File(filePath);
            if(fileTree.exists(newFileHash)){
                System.out.println("Found a duplicated backup(\"" + filePath + "\"), removal in progress.");
                if(!f.delete()){
                    System.err.println("Error during file removal.");
                }
            }
            else{
                System.out.println("Backup done: " + filePath + " - " + newFileHash);
                fileTree.add(new HashFile(newFileHash,f));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkBackups() throws IOException { //count if we got >= x backups, if so delete the oldest one
        if(fileTree.size() < c.getMaxBackupsToStore()){
            return;
        }
        File oldestFile = fileTree.firstElement().getF();
        int oldestFileIndex = 0;
        for(int i = 1; i < fileTree.size(); i++){
            if(getFileCreationDate(oldestFile).compareTo(getFileCreationDate(fileTree.get(i).getF())) > 0){
                oldestFile = fileTree.get(i).getF();
                oldestFileIndex = i;
            }
        }
        if(!oldestFile.delete()){
            System.err.println("Error during oldest file deletion: " + oldestFile.getName());
        }
        else{
            System.out.println("We got too much backups, so i deleted the oldest one.");
            fileTree.remove(oldestFileIndex);
        }
    }

    private FileTime getFileCreationDate(File f) throws IOException {
        Path path = f.toPath();
        BasicFileAttributes fileAttributes = Files.readAttributes(path,
                BasicFileAttributes.class);
        return fileAttributes.creationTime();
    }

    private String getCurrentTime(){
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("GMT+02:00"));
        char separator = '-';
        return String.valueOf(localDateTime.getDayOfMonth()) + separator +
                localDateTime.getMonthValue() + separator +
                localDateTime.getYear() + separator +
                localDateTime.getHour() + separator +
                localDateTime.getMinute() + separator +
                localDateTime.getSecond();
    }
}
