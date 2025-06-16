package Game;
import Config.AppConfig;
import Game.Render.Chunk;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

enum lines_types { UP, DOWN, LEFT, RIGHT };

public class DrawingStuff {
    public static void DrawRandomFigure(JPanel[][] cells, Chunk chunk) {
        if(AppConfig.dev_mode == true && (chunk.type == Chunk.type_of_chunk.SPAWNER_DUCHOW || chunk.type == Chunk.type_of_chunk.SPAWNER_PACMAN)){
            int startX = chunk.x;
            int startY = chunk.y;
            int max_X = chunk.x + (chunk.szerokosc_x - 1);
            int max_Y = chunk.y + (chunk.szerokosc_y - 1);

            for(int i = startX; i < max_X; i++) {
                for(int y = startY; y < max_Y; y ++) {
                    paintCell(cells, i, y, Color.RED);
                }
            }
        }
        if(chunk.type == Chunk.type_of_chunk.NORMAL) {
            //[!-- Jakies zmienne do madrych obloczen --!]
            int startX = chunk.x;
            int startY = chunk.y;
            int max_X = chunk.x + (chunk.szerokosc_x - 1);
            int max_Y = chunk.y + (chunk.szerokosc_y - 1);

            int srodek_x = startX + ((max_X-startX) / 2);
            int srodek_y = startY + ((max_Y-startY) / 2);

            int ilosc_kierunkow_chunka = 2 + (int)(Math.random() * 3); //[!-- Losuje od 0 do 2, lecz po dodaniu do tego +2 bedzie od 2, do 4

            //[!-- Dla kazdego chunka tworzone sa pary lin oraz ich dlugosci --!]
            Map<lines_types, Integer> linie_wlasciwosci = new HashMap<>();

            //[!-- Flagi, beda pomagac przy uzupelnianiu ksztaltow --!]
            boolean up_line = false, down_line = false, left_line =false, right_line = false;

            //[!-- Pomocna zmienna do tasowania --!]
            List<lines_types> allLines = Arrays.asList(lines_types.values());

            //[!-- Przystosowanie mozlwisoci istnien lin pod dziwne chunki
            if (chunk.szerokosc_x <= 2 || chunk.szerokosc_y <= 2) {
                if(chunk.szerokosc_x <= 2) {
                    System.out.println("---[!] Ten CHUNK MOŻE MIEĆ TYLKO UP I DOWN ---");
                    allLines = new ArrayList<>(List.of(lines_types.UP, lines_types.DOWN));
                    ilosc_kierunkow_chunka = 2;
                }
                if(chunk.szerokosc_y <= 2) {
                    System.out.println("---[!] Ten CHUNK MOŻE MIEĆ TYLKO LEFT I RIGHT ---");
                    allLines = new ArrayList<>(List.of(lines_types.LEFT, lines_types.RIGHT));
                    ilosc_kierunkow_chunka = 2;
                }
            }
            else {
                allLines = new ArrayList<>(List.of(
                        lines_types.UP, lines_types.DOWN,
                        lines_types.LEFT, lines_types.RIGHT
                ));
            }
            if(chunk.szerokosc_x <= 2 && chunk.szerokosc_y <= 2) {
                System.out.println("---[!] Ten CHUNK MA NIC---");
                allLines = new ArrayList<>();
                ilosc_kierunkow_chunka = 0;
            }


            Random rand = new Random();
            Collections.shuffle(allLines, rand);
            for(var i = 0; i < ilosc_kierunkow_chunka; i++) {
                int mozliwa_dlugosc = 2+(int)(Math.random() * 2);
                if(chunk.szerokosc_x != 5 || chunk.szerokosc_y != 5) {
                    mozliwa_dlugosc = 2+(int)(Math.random() * 1);
                }
                linie_wlasciwosci.put(allLines.get(i), mozliwa_dlugosc);

                switch(allLines.get(i)) {
                    case UP -> up_line = true;
                    case DOWN -> down_line = true;
                    case LEFT -> left_line = true;
                    case RIGHT -> right_line = true;
                }
            }

            //[!-- Tutaj Jest rysowanie szkieletow --!]
            for(var entry : linie_wlasciwosci.entrySet()) {
                switch(entry.getKey()) {
                    case UP:
                        if(AppConfig.dev_mode)
                            System.out.printf("\t[^] Trwa Rysowanie lini w UP o dlugosci %d\n", entry.getValue());

                        for(int counter = 0; counter < entry.getValue(); counter++) {
                            paintCell(cells, srodek_x, srodek_y-counter, AppConfig.walls_color);
                        }
                        break;

                    case DOWN:
                        if(AppConfig.dev_mode)
                            System.out.printf("\t[_] Trwa Rysowanie lini w DOWN o dlugosci %d\n", entry.getValue());

                        for(int counter = 0; counter < entry.getValue(); counter++) {
                            paintCell(cells, srodek_x, srodek_y+counter, AppConfig.walls_color);
                        }
                        break;
                    case LEFT:
                        if(AppConfig.dev_mode)
                            System.out.printf("\t[<] Trwa Rysowanie lini w LEFT o dlugosci %d\n", entry.getValue());

                        for(int counter = 0; counter < entry.getValue(); counter++) {
                            paintCell(cells, srodek_x-counter, srodek_y, AppConfig.walls_color);
                        }
                        break;
                    case RIGHT:
                        if(AppConfig.dev_mode)
                            System.out.printf("\t[>] Trwa Rysowanie lini w LEFT o dlugosci %d\n", entry.getValue());

                        for(int counter = 0; counter < entry.getValue(); counter++) {
                            paintCell(cells, srodek_x+counter, srodek_y, AppConfig.walls_color);
                        }
                        break;
                }
            }


            //[!-- Uzupelnienia
            int ilosc_uzupelnien = 0;
            if(up_line && left_line) {

                System.out.printf(AppConfig.dev_mode ? "\t\t[*] Ten Chunk ma linie UP [%d] i LEFT [%d]\n" :  "", linie_wlasciwosci.get(lines_types.UP), linie_wlasciwosci.get(lines_types.LEFT));
                switch((int)(Math.random() * 3)){
                    case 0:
                        paintCell(cells, srodek_x-1, srodek_y-1, AppConfig.walls_color);
                        ilosc_uzupelnien++;
                        break;
                    case 1:
                        paintCell(cells, srodek_x-1, srodek_y-1, AppConfig.walls_color);
                        paintCell(cells, srodek_x-2, srodek_y-1, AppConfig.walls_color);
                        paintCell(cells, srodek_x-1, srodek_y-2, AppConfig.walls_color);
                        ilosc_uzupelnien++;
                }
            }
            if(down_line && left_line ) {
                System.out.printf(AppConfig.dev_mode ? "\t\t[*] Ten Chunk ma linie DOWN [%d] i LEFT [%d]\n" : "", linie_wlasciwosci.get(lines_types.DOWN), linie_wlasciwosci.get(lines_types.LEFT));
                switch((int)(Math.random() * 3)){
                    case 0:
                        paintCell(cells, srodek_x-1, srodek_y+1, AppConfig.walls_color);
                        ilosc_uzupelnien++;
                        break;
                    case 1:
                        paintCell(cells, srodek_x-1, srodek_y+1, AppConfig.walls_color);
                        paintCell(cells, srodek_x-2, srodek_y+1, AppConfig.walls_color);
                        paintCell(cells, srodek_x-1, srodek_y+2, AppConfig.walls_color);
                        ilosc_uzupelnien++;
                }
            }
            if(down_line && right_line) {
                System.out.printf(AppConfig.dev_mode ? "\t\t[*] Ten Chunk ma linie DOWN [%d] i RIGHT [%d]\n" : "", linie_wlasciwosci.get(lines_types.DOWN), linie_wlasciwosci.get(lines_types.RIGHT));
                switch((int)(Math.random() * 3)){
                    case 0:
                        paintCell(cells, srodek_x+1, srodek_y+1, AppConfig.walls_color);
                        ilosc_uzupelnien++;
                        break;
                    case 1:
                        paintCell(cells, srodek_x+1, srodek_y+1, AppConfig.walls_color);
                        paintCell(cells, srodek_x+2, srodek_y+1, AppConfig.walls_color);
                        paintCell(cells, srodek_x+1, srodek_y+2, AppConfig.walls_color);
                        ilosc_uzupelnien++;
                }
            }
            if(up_line && down_line) {
                System.out.printf(AppConfig.dev_mode ? "\t\t[*] Ten Chunk ma linie UP [%d] i LEFT [%d]\n": "", linie_wlasciwosci.get(lines_types.UP), linie_wlasciwosci.get(lines_types.LEFT));
                switch((int)(Math.random() * 3)){
                    case 0:
                        paintCell(cells, srodek_x+1, srodek_y-1, AppConfig.walls_color);
                        ilosc_uzupelnien++;
                        break;
                    case 1:
                        paintCell(cells, srodek_x+1, srodek_y-1, AppConfig.walls_color);
                        paintCell(cells, srodek_x+2, srodek_y-1, AppConfig.walls_color);
                        paintCell(cells, srodek_x+1, srodek_y-2, AppConfig.walls_color);
                        ilosc_uzupelnien++;
                }
            }

            //[!-- Jesli zadna z lini nie ma dopelnienia, to po prostu pogrubienie jednej z lini
            if(ilosc_uzupelnien == 0 && allLines.size() > 2) {
                System.out.printf(AppConfig.dev_mode ? "---[*] Ten chunk ma dodatkowe uzupelnienie bo jest pusty cn?---\n" : "");
                for(var entry : linie_wlasciwosci.entrySet()) {
                    switch(entry.getKey()) {
                        case UP:
                            //[!-- Decyduje czy wogole ma sie rysowac cos obok
                            if(woman_decision()) {
                                int dlugosc = (int)(Math.random() * (entry.getValue()-1)) + 2;

                                //[!-- Decyduje po ktorej stronie od strzalki
                                if(woman_decision()) { // <-- Tutaj bedzie ze po prawej dla lini
                                    for(var i = 0; i < dlugosc; i++) {
                                        paintCell(cells, srodek_x+1, srodek_y-i, AppConfig.walls_color);
                                    }
                                }
                                else {
                                    for(var i = 0; i < dlugosc; i++) {
                                        paintCell(cells, srodek_x-1, srodek_y-i, AppConfig.walls_color);
                                    }
                                }
                            }
                            break;
                        case DOWN:
                            //[!-- Decyduje czy wogole ma sie rysowac cos obok
                            if(woman_decision()) {
                                int dlugosc = (int)(Math.random() * (entry.getValue()-1)) + 2;

                                //[!-- Decyduje po ktorej stronie od strzalki
                                if(woman_decision()) { // <-- Tutaj bedzie ze po prawej dla lini
                                    for(var i = 0; i < dlugosc; i++) {
                                        paintCell(cells, srodek_x+1, srodek_y+i, AppConfig.walls_color);
                                    }
                                }
                                else {
                                    for(var i = 0; i < dlugosc; i++) {
                                        paintCell(cells, srodek_x-1, srodek_y+i, AppConfig.walls_color);
                                    }
                                }
                            }
                            break;
                        case LEFT:
                            //[!-- Decyduje czy wogole ma sie rysowac cos obok
                            if(woman_decision()) {
                                int dlugosc = (int)(Math.random() * (entry.getValue()-1)) + 2;

                                //[!-- Decyduje po ktorej stronie od strzalki
                                if(woman_decision()) { // <-- Tutaj bedzie ze po prawej dla lini
                                    for(var i = 0; i < dlugosc; i++) {
                                        paintCell(cells, srodek_x-i, srodek_y-1, AppConfig.walls_color);
                                    }
                                }
                                else {
                                    for(var i = 0; i < dlugosc; i++) {
                                        paintCell(cells, srodek_x-i, srodek_y+1, AppConfig.walls_color);
                                    }
                                }
                            }
                            break;
                        case RIGHT:
                            //[!-- Decyduje czy wogole ma sie rysowac cos obok
                            if(woman_decision()) {
                                int dlugosc = (int)(Math.random() * (entry.getValue()-1)) + 2;

                                //[!-- Decyduje po ktorej stronie od strzalki
                                if(woman_decision()) { // <-- Tutaj bedzie ze po prawej dla lini
                                    for(var i = 0; i < dlugosc; i++) {
                                        paintCell(cells, srodek_x+i, srodek_y-1, AppConfig.walls_color);
                                    }
                                }
                                else {
                                    for(var i = 0; i < dlugosc; i++) {
                                        paintCell(cells, srodek_x+i, srodek_y+1, AppConfig.walls_color);
                                    }
                                }
                            }
                            break;
                    }
                }
            }


            if(AppConfig.dev_mode) {
                //[!-- Checking if its wall chunk
                if(startX == 1 || startX == (cells[0].length - 1) - chunk.szerokosc_x) {
                    System.out.printf("------- Chunk scianowy -------\n");
                }
                else {
                    System.out.printf("%d i %d", startX, (cells[0].length - 1) - chunk.szerokosc_x);
                }

                System.out.printf("[+] Dla chunka (%d,%d) przewidziane jest %d kierunkow | Ma szerokosc x = %d y = %d \n", startX, startY, ilosc_kierunkow_chunka, chunk.szerokosc_x, chunk.szerokosc_y);
                System.out.printf("[~] Ma na sobie %d uzupelnien\n", ilosc_uzupelnien);
                System.out.printf("[!] Wedlug danych w mapie: \n");
                for(var entry : linie_wlasciwosci.entrySet()) {
                    System.out.println("\tKierunek: " + entry.getKey() + " Dlugosc: " + entry.getValue());
                }
                System.out.printf("[!] Wedlug flag: \n");
                System.out.print(up_line ? "\tUP\n" : "");
                System.out.print(down_line ? "\tDOWN\n" : "");
                System.out.printf(left_line ? "\tLEFT\n" : "");
                System.out.print(right_line ? "\tRIGHT\n" : "");
                System.out.println("------------------------------------------------------");

            }

//        paintCell(cells, startX, startY, Color.GREEN);
//        paintCell(cells, max_X, startY, Color.RED);
//        paintCell(cells, startX, max_Y, AppConfig.walls_color);
//        paintCell(cells, max_X, max_Y, Color.WHITE);
//        paintCell(cells, srodek_x, srodek_y, Color.YELLOW);
        }
    }

    //[!-- Bedzie dawac losowa decyzje bez zadnego sensu
    private static boolean woman_decision() {
        int number = (int)(Math.random() * 2);
        if(number == 1) {
            return true;
        }
        else {
            return false;
        }
    }

    private static void paintCell(JPanel[][] cells, int x, int y, Color color) {
        if (y >= 0 && y < cells.length && x >= 0 && x < cells[0].length) {
            JPanel cell = new JPanel(new BorderLayout());
            cell.setBackground(color);
            cell.setBorder(BorderFactory.createLineBorder(Color.decode("#363636"), 1));
            cells[y][x] = cell;
        }
        else {
            System.out.printf("[-] Invalid Coordinate: %d, %d\n", x, y);
        }
    }
}
