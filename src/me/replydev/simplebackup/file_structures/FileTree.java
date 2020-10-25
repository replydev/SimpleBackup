package me.replydev.simplebackup.file_structures;

import me.replydev.simplebackup.zip.ZipHashUtils;

import java.io.File;
import java.util.Vector;

public class FileTree extends Vector<HashFile>{

    public FileTree(String backupFolder){
        super();
        File[] backups = new File(backupFolder).listFiles();
        if(backups == null){
            return;
        }
        int i = 1;
        for(File f : backups){
            System.out.print("Calculating hash of " + i + "° backup...");
            this.add(new HashFile(ZipHashUtils.getZipHash(f.getAbsolutePath()),f));
            System.out.println("...done");
            i++;
        }
    }

    public boolean exists(long CRC){
        for(HashFile hashFile : this){
            if(CRC == hashFile.getHash()){
                return true;
            }
        }
        return false;
    }
}
