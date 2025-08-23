package org.escuelaing.edu.co.model;

import org.escuelaing.edu.co.entity.Agent;
import org.escuelaing.edu.co.entity.Entity;
import org.escuelaing.edu.co.entity.Neo;
import org.escuelaing.edu.co.enumeration.CellType;
import org.escuelaing.edu.co.record.Pos;

import java.util.*;
import java.util.concurrent.Phaser;


// Motor del juego
public class Game implements Runnable {
    private final Board board;
    private final List<Entity> entities;
    private final Phaser phaser;
    private volatile boolean running = true;
    private boolean neoAlive = true;
    private boolean neoWon = false;

    public Game(Board board, List<Entity> entities, Phaser phaser) {
        this.board = board; this.entities = entities; this.phaser = phaser;
    }

    @Override public void run() {
        while (running) {
            // Fase 1 ya esperó: entidades decidieron (arrive in Entities)
            phaser.arriveAndAwaitAdvance();

            // Fase 2: resolver/aplicar movimientos
            resolveAndApply();
            if (!neoAlive || neoWon) running = false;

            phaser.arriveAndAwaitAdvance(); // entidades esperan fin de aplicación

            // Fase 3: render
            board.print();
            phaser.arriveAndAwaitAdvance();
        }
        // detener entidades
        entities.forEach(Entity::stop);
    }

    private void resolveAndApply() {
        // recolectar intenciones
        Map<Pos, List<Entity>> intents = new HashMap<>();
        for (Entity e : entities) {
            Pos to = e.intent != null ? e.intent : e.getPos();
            intents.computeIfAbsent(to, k -> new ArrayList<>()).add(e);
        }

        for (Map.Entry<Pos, List<Entity>> entry : intents.entrySet()) {
            Pos target = entry.getKey();
            List<Entity> claimers = entry.getValue();
            claimers.sort(Comparator.comparing(Object::hashCode));
            Entity winner = claimers.get(0);

            for (Entity e : claimers) {
                Pos from = e.getPos();
                if (e != winner) continue;

                if (!board.inBounds(target) || board.cell(target) == CellType.WALL) continue;

                Entity occupant = board.at(target);

                if (e instanceof Neo) {
                    if (board.cell(target) == CellType.PHONE) { neoWon = true; }
                    if (occupant instanceof Agent) { neoAlive = false; }
                } else if (e instanceof Agent) {
                    if (occupant instanceof Neo) { neoAlive = false; }
                }

                // mover si la celda está vacía o si es captura válida
                if (occupant == null || occupant == e || occupant instanceof Neo || occupant instanceof Agent) {
                    synchronized (board) {
                        board.clear(from);
                        e.setPos(target);
                        if (neoAlive || !(occupant instanceof Neo)) board.place(e, target);
                    }
                }
            }
        }
    }
}
