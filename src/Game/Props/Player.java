package Game.Props;

import Game.PropsLogic.PropsLogic;

import javax.swing.*;
import java.awt.*;

public class Player extends Prop{

    public int player_score = 0;

    public boolean can_destroy_wall = false;

    public Player(int startX, int startY) {
        super(startX, startY, PropsAssets.ICON_player_path, PropsAssets.ICON_player_path, "Player");
    }

    public void eat(){
        player_score++;
    }

    public void moveTo(int newX, int newY, JPanel[][] cells) {
        cells[this.getY()][this.getX()].remove(playerLabel);
        cells[this.getY()][this.getX()].revalidate();
        cells[this.getY()][this.getX()].repaint();

        this.setX(newX);
        this.setY(newY);

        cells[this.getY()][this.getX()].add(playerLabel, BorderLayout.CENTER);
        cells[this.getY()][this.getX()].revalidate();
        cells[this.getY()][this.getX()].repaint();
        PropsLogic logic = PropsLogic.getInstance();
        if (logic != null) {
            logic.checkPlayerPowerUpCollision();
        }
    }


}
