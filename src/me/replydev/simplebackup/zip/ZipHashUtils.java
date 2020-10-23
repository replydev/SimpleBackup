package me.replydev.simplebackup.zip;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipHashUtils {
    public static long getZipHash(String filename) {
        long totalHash = 0;
        try {
            ZipFile zipName = new ZipFile(filename);
            Enumeration<? extends ZipEntry> enumEntries = zipName.entries();
            while (enumEntries.hasMoreElements()) {
                ZipEntry zipEntry = enumEntries.nextElement();
                long crc = zipEntry.getCrc();
                totalHash = totalHash ^ crc; //xor of all so we get unique fingerprint
            }
            zipName.close();
            return totalHash;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
