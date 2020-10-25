package me.replydev.simplebackup.file_structures;

import me.replydev.simplebackup.zip.ZipHashUtils;

import java.io.File;
import java.util.Vector;

public class FileTree {
    private final Vector<HashFile> files;

    public FileTree(String backupFolder){
        files = new Vector<>();
        File[] backups = new File(backupFolder).listFiles();
        if(backups == null){
            return;
        }
        int i = 1;
        for(File f : backups){
            System.out.print("Calculating hash of " + i + "° backup...");
            files.add(new HashFile(ZipHashUtils.getZipHash(f.getAbsolutePath()),f));
            System.out.println("...done");
            i++;
        }
    }

    public boolean exists(long CRC){
        for(HashFile hashFile : files){
            if(CRC == hashFile.getHash()){
                return true;
            }
        }
        return false;
    }

    public int length(){
        return files.size();
    }

    public HashFile firstElement(){
        return files.firstElement();
    }

    public HashFile get(int index){
        return files.get(index);
    }

    public void add(HashFile hashFile){
        files.add(hashFile);
    }
}
