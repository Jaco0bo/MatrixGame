package org.escuelaing.edu.co;

import org.escuelaing.edu.co.model.*;
import org.escuelaing.edu.co.entity.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        String ascii =
                "A###.T#.#.\n" +
                        ".........A\n" +
                        ".#........\n" +
                        "N#..T.#..\n" +
                        "..........\n" +
                        ".##.......\n" +
                        ".........#\n" +
                        "..........\n" +
                        "..........\n" +
                        "......A...";

        Board board = MapParser.parse(ascii);
        Phaser phaser = new Phaser(1);
        List<Entity> entities = MapParser.instantiateEntitiesFromMap(board, phaser, ascii);

        Game game = new Game(board, entities, phaser);

        List<Thread> threads = new ArrayList<>();
        for (Entity e : entities) {
            Thread t = new Thread(e, e.toString());
            t.start();
            threads.add(t);
        }

        Thread gameThread = new Thread(game, "Game");
        gameThread.start();

        gameThread.join();

        for (Thread t : threads) {
            t.interrupt();
            t.join(200);
        }

        System.out.println("Main finished.");
    }
}

