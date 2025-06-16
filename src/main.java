import Windows.CreateGameWindow;
import Windows.GameWindow;
import Windows.MainMenuWindow;

import javax.swing.*;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenuWindow());
    }
}
