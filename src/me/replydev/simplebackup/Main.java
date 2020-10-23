package me.replydev.simplebackup;

import me.replydev.simplebackup.file_structures.FileTree;

import java.io.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) throws IOException {
        System.out.println(getLogo());
        Config c = new Config("config.conf");
        System.out.println("Config loaded.");
        System.out.println("Checking backup folder...");
        File backupFolder = new File(c.getFolder_to_store_backups());
        if(!backupFolder.exists()){
            if(!backupFolder.mkdir()){
                System.err.println("Error during server backup folder creation");
                System.exit(-1);
            }
        }
        FileTree fileTree = new FileTree(c.getFolder_to_store_backups());
        System.out.println("Starting scheduled executor...");
        scheduledExecutorService.scheduleAtFixedRate(new BackupTask(c,fileTree),c.getInitial_delay_backup(),c.getBackup_frequency(), TimeUnit.HOURS);
    }

    private static String getLogo(){
        return "   _____ _                 _        ____             _                \n" +
                "  / ____(_)               | |      |  _ \\           | |               \n" +
                " | (___  _ _ __ ___  _ __ | | ___  | |_) | __ _  ___| | ___   _ _ __  \n" +
                "  \\___ \\| | '_ ` _ \\| '_ \\| |/ _ \\ |  _ < / _` |/ __| |/ / | | | '_ \\ \n" +
                "  ____) | | | | | | | |_) | |  __/ | |_) | (_| | (__|   <| |_| | |_) |\n" +
                " |_____/|_|_| |_| |_| .__/|_|\\___| |____/ \\__,_|\\___|_|\\_\\\\__,_| .__/ \n" +
                "                    | |                                        | |    \n" +
                "                    |_|                                        |_|    \n\n";
    }
}
