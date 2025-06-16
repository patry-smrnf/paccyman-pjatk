package Game.Props;

public class Food extends Prop {
    public static int food_count = 0;

    public int id_food;

    public Food(int startX, int startY) {
        super(startX, startY, PropsAssets.ICON_point_path, PropsAssets.ICON_point_path, "Food");
        this.id_food = food_count;
        food_count++;
    }
}
