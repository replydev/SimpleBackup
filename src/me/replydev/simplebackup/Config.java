package me.replydev.simplebackup;

import java.io.*;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class Config {

    private Vector<String> foldersToBackup;
    private String folderToStoreBackups;
    private int initialBackupDelay;
    private int backupFrequency;
    private TimeUnit backupFrequencyUnit;
    private int maxBackupsToStore;

    public int getInitialBackupDelay() {
        return initialBackupDelay;
    }
    public int getBackupFrequency() {
        return backupFrequency;
    }
    public TimeUnit getBackupFrequencyUnit() {
        return backupFrequencyUnit;
    }
    public Vector<String> getFoldersToBackup() {
        return foldersToBackup;
    }
    public String getFolderToStoreBackups() {
        return folderToStoreBackups;
    }
    public int getMaxBackupsToStore() {
        return maxBackupsToStore;
    }

    public Config(String filename) throws IOException {
        //default values
        initialBackupDelay = 0;
        backupFrequency = 2;
        backupFrequencyUnit = TimeUnit.HOURS;
        maxBackupsToStore = 5;

        BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
        String s;
        while((s = reader.readLine()) != null){
            parseLine(s);
        }
        reader.close();

        //
        if(foldersToBackup == null){
            System.err.println("Error during config parsing: please insert a valid \"FOLDERS_TO_BACKUPS\" attribute. Fix the config and retry.");
            System.exit(-1);
        }
        if(folderToStoreBackups == null){
            System.err.println("Error during config parsing: please insert a valid \"FOLDER_TO_STORE_BACKUPS\" attribute. Fix the config and retry.");
            System.exit(-1);
        }
    }

    private void parseLine(String line){
        if(line.isBlank()){
            return;
        }
        line = cutSpaces(line);
        if(line.startsWith("#")){
            return;
        }
        String separator = "=";
        String[] data = line.split(separator);
        if(data.length != 2){
            System.err.println("Error during config parsing: \"" + line + "\" is not a valid config instruction. Fix the config and retry.");
            System.exit(-1);
        }
        switch(data[0]){
            case "FOLDERS_TO_BACKUP":
                parseFoldersToBackup(data[1]);
                break;
            case "FOLDER_TO_STORE_BACKUPS":
                folderToStoreBackups = data[1];
                break;
            case "INITIAL_BACKUP_DELAY":
                initialBackupDelay = Integer.parseInt(data[1]);
                break;
            case "BACKUP_FREQUENCY":
                parseBackupFrequency(data[1]);
                break;
            case "MAX_BACKUPS_TO_STORE":
                maxBackupsToStore = Integer.parseInt(data[1]);
                break;
            default:
                System.err.println("Error during config parsing: \"" + data[0] + "\" is not a config attribute. Fix the config and retry.");
                System.exit(-1);
        }
    }

    private void parseFoldersToBackup(String s){
        String[] folders = s.split(",");
        this.foldersToBackup = new Vector<>();
        this.foldersToBackup.addAll(Arrays.asList(folders));
    }

    private void parseBackupFrequency(String s){
        String[] data = s.split(",");
        if(data.length != 2){
            System.err.println("Error during config parsing: \"" + s + "\" is not a valid backup frequency (it should be similar to \"2,HOURS\"). Fix the config and retry.");
            System.exit(-1);
        }
        backupFrequency = Integer.parseInt(data[0]);
        backupFrequencyUnit = getTimeUnitFromString(data[1]);
        if(backupFrequency <= 0){
            System.err.println("Error during config parsing: backup frequency val (\"" + backupFrequency + "\") should be positive. Fix the config and retry.");
            System.exit(-1);
        }
        if(backupFrequencyUnit == null){
            System.err.println("Error during config parsing: (\"" + data[1] + "\") is not a valid backup frequency unit. Fix the config and retry.");
            System.exit(-1);
        }
    }

    private TimeUnit getTimeUnitFromString(String s){
        switch (s.toUpperCase()){
            case "SECONDS": return TimeUnit.SECONDS;
            case "MINUTES": return TimeUnit.MINUTES;
            case "HOURS": return TimeUnit.HOURS;
            case "DAYS": return TimeUnit.DAYS;
            default: return null;
        }
    }

    private String cutSpaces(String s){
        return s.replace(" ","");
    }
}
