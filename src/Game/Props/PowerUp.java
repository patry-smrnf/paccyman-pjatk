package Game.Props;

import java.util.Random;


public class PowerUp extends Prop {
    public PowerUpType type;
    public enum PowerUpType { STOP_ENEMIES, ENEMIES_SLOWER, PLAYER_DESTROY_ONE_WALL, ENEMIES_FASTER, KILL_ENEMY }

    public static int STOP_time = 4;
    public static int ENEMIES_SLOWER = 5;
    public static int ENEMIES_FASTER = 2;

    public static int ENEMIES_DEFAUL_SPEED = 500;
    public static int ENEMIES_SLOWER_SPEED = 800;
    public static int ENEMIES_FASTER_SPEED = 400;

    public PowerUp(int startX, int startY) {
        super(startX, startY, PropsAssets.ICON_powerUP_path, PropsAssets.ICON_powerUP_path, "PowerUP");
        this.type = pickPowerType();
    }

    private PowerUpType pickPowerType() {
        Random rand = new Random();
        int randomNum = rand.nextInt(100); // 0 to 99

        if (randomNum < 30) {
            return PowerUpType.ENEMIES_SLOWER;  // 30%
        } else if (randomNum < 60) {
            return PowerUpType.STOP_ENEMIES;  // next 30%
        } else if (randomNum < 75) {
            return PowerUpType.ENEMIES_FASTER;  // next 15%
        } else if (randomNum < 80) {
            return PowerUpType.KILL_ENEMY;  // next 5%
        } else {
            return PowerUpType.PLAYER_DESTROY_ONE_WALL;  // last 20%
        }
    }
}
