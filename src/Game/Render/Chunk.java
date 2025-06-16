package Game.Render;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Chunk {
    public static int counter_chunkow = 0;
    public int id_chunka;
    public int x, y;
    public int szerokosc_x, szerokosc_y;
    public type_of_chunk type;
    public static int distance_prefered = 5;

    public enum type_of_chunk { NORMAL, SPAWNER_DUCHOW, SPAWNER_PACMAN };

    public Chunk(int x, int y, int szerokosc_x, int szerokosc_y, type_of_chunk type_of_chunk) {
        this.x = x;
        this.y = y;
        this.szerokosc_x = szerokosc_x;
        this.szerokosc_y = szerokosc_y;
        this.id_chunka = counter_chunkow;
        this.type = type_of_chunk;
        counter_chunkow++;
    }

    public void draw_me_DEV(JPanel[][] cells, boolean start_chunk) {
        if(start_chunk) {
            JPanel wall = new JPanel();
            wall.setBackground(Color.GREEN);
            wall.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            cells[this.y][this.x] = wall;
        }

        for(int i = 0; i < szerokosc_x-1; i++) {
            JPanel bg = new JPanel();
            bg.setBackground(Color.decode("#001f08"));
            bg.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
            cells[this.y+this.szerokosc_y-1][this.x+i] = bg;
        }

        for(int i = 0; i < szerokosc_y-1; i++) {
            JPanel bg = new JPanel();
            bg.setBackground(Color.decode("#001f08"));
            bg.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.WHITE));
            cells[this.y+i][this.x+this.szerokosc_x-1] = bg;
        }

        JPanel bg = new JPanel();
        bg.setBackground(Color.decode("#001f08"));
        bg.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.WHITE));
        cells[this.y+this.szerokosc_y-1][this.x+this.szerokosc_x-1] = bg;


    }

    public static Chunk[] random_spawners(Chunk[] chunks) {
        Random rand = new Random();
        int num1, num2;

        do {
            num1 = rand.nextInt(chunks.length);
            num2 = rand.nextInt(chunks.length);
        } while (
                Math.abs(num1 - num2) < 3 ||
                        chunks[num1].szerokosc_x != 5 ||
                        chunks[num1].szerokosc_y != 5 ||
                        chunks[num2].szerokosc_x != 5 ||
                        chunks[num2].szerokosc_y != 5
        );

        Chunk spawn_duchow = chunks[num1];
        Chunk spawn_pacman = chunks[num2];

        spawn_duchow.type = Chunk.type_of_chunk.SPAWNER_DUCHOW;
        spawn_pacman.type = Chunk.type_of_chunk.SPAWNER_PACMAN;

        return new Chunk[]{spawn_pacman, spawn_duchow};
    }

    @Override
    public String toString() {
        return "[!] Chunk na koordynatach (" + this.x + ", " + this.y + ") Szerokosc = [ " + this.szerokosc_x + ", " + this.szerokosc_y + "]";
    }
}
