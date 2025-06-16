package Game.Score;

import Config.AppConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Score {
    public static void SaveGame(int score, String time, String name) {
        String output = Integer.toString(score) + " " + time + " " + name + "\n";

        File pacman_folder = new File(AppConfig.saves_dir_path);
        if (!pacman_folder.exists()) {
            boolean created = pacman_folder.mkdirs();
            if (created) {
                System.out.println("Folder created at: " + AppConfig.saves_dir_path);
            } else {
                System.out.println("Failed to create folder.");
            }
        }

        try {
            File file = new File(AppConfig.saves_file_path);

            if (!file.exists()) {
                file.createNewFile();
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }

            // Write to the file
            FileWriter writer = new FileWriter(file, true);
            writer.write(output);
            writer.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


}
