package me.replydev.simplebackup;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class Config {

    private String folder_to_backup;
    private String folder_to_store_backups;
    private int initial_delay_backup;
    private int backup_frequency;
    private TimeUnit backup_frequency_unit;
    private int max_backups_to_store;

    public int getInitial_delay_backup() {
        return initial_delay_backup;
    }

    public int getBackup_frequency() {
        return backup_frequency;
    }

    public TimeUnit getBackup_frequency_unit() {
        return backup_frequency_unit;
    }

    public String getFolder_to_backup() {
        return folder_to_backup;
    }

    public String getFolder_to_store_backups() {
        return folder_to_store_backups;
    }

    public int getMax_backups_to_store() {
        return max_backups_to_store;
    }

    public Config(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
        String s;
        while((s = reader.readLine()) != null){
            parseLine(s);
        }
        reader.close();
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
            case "FOLDER_TO_BACKUP":
                folder_to_backup = data[1];
                break;
            case "FOLDER_TO_STORE_BACKUPS":
                folder_to_store_backups = data[1];
                break;
            case "INITIAL_BACKUP_DELAY":
                initial_delay_backup = Integer.parseInt(data[1]);
                break;
            case "BACKUP_FREQUENCY":
                parseBackupFrequency(data[1]);
                break;
            case "MAX_BACKUPS_TO_STORE":
                max_backups_to_store = Integer.parseInt(data[1]);
                break;
            default:
                System.err.println("Error during config parsing: \"" + data[0] + "\" is not a config attribute. Fix the config and retry.");
                System.exit(-1);
        }
    }

    private void parseBackupFrequency(String s){
        String[] data = s.split(",");
        if(data.length != 2){
            System.err.println("Error during config parsing: \"" + data[1] + "\" is not a valid backup frequency (it should be similar to \"2,HOURS\"). Fix the config and retry.");
            System.exit(-1);
        }
        backup_frequency = Integer.parseInt(data[0]);
        backup_frequency_unit = getTimeUnitFromString(data[1]);
        if(backup_frequency <= 0){
            System.err.println("Error during config parsing: backup frequency val (\"" + backup_frequency + "\") should be positive. Fix the config and retry.");
            System.exit(-1);
        }
        if(backup_frequency_unit == null){
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
