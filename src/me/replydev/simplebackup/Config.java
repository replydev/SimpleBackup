package me.replydev.simplebackup;

import java.io.*;

public class Config {

    private String folder_to_backup;
    private String folder_to_store_backups;
    private int initial_delay_backup;
    private int backup_frequency;
    private int max_backups_to_store;

    public int getInitial_delay_backup() {
        return initial_delay_backup;
    }

    public int getBackup_frequency() {
        return backup_frequency;
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
                backup_frequency = Integer.parseInt(data[1]);
                break;
            case "MAX_BACKUPS_TO_STORE":
                max_backups_to_store = Integer.parseInt(data[1]);
                break;
            default:
                System.err.println("Error during config parsing: \"" + data[0] + "\" is not a config attribute. Fix the config and retry.");
                System.exit(-1);
        }
    }

    private String cutSpaces(String s){
        return s.replace(" ","");
    }
}
