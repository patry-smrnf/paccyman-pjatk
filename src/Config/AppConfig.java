package Config;

import java.awt.*;

public class AppConfig {
    public static int WindowWidth = 800;
    public static int WindowHeight = 800;

    public static boolean dev_mode = false;

    public static Color walls_color = Color.decode("#0000ad");
    public static Color background_color = Color.decode("#0a0a0a");
    public static Color wall_border_color = Color.decode("#292929");

    public static String saves_dir_path = "C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\PacmanPjatkEdition";
    public static String saves_file_path = saves_dir_path + "\\saves.txt";
}
