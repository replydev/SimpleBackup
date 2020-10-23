package me.replydev.simplebackup.file_structures;

import java.io.File;

public class HashFile {
    private final long hash;
    private final File f;

    public HashFile(long hash, File f) {
        this.hash = hash;
        this.f = f;
    }

    public long getHash() {
        return hash;
    }

    public File getF() {
        return f;
    }
}
