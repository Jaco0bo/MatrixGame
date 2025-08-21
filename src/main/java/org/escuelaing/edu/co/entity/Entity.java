package org.escuelaing.edu.co.entity;

import org.escuelaing.edu.co.model.Board;
import org.escuelaing.edu.co.record.GameState;
import org.escuelaing.edu.co.record.Pos;

import java.util.concurrent.Phaser;

public abstract class Entity implements Runnable {
    protected final String name;
    protected Pos pos;
    protected final Board board;
    protected final Phaser phaser;
    public volatile Pos intent; // intención de movimiento
    protected volatile boolean running = true;

    Entity(String name, Board board, Pos start, Phaser phaser) {
        this.name = name;
        this.board = board;
        this.pos = start;
        this.phaser = phaser;
        this.phaser.register();
    }

    public abstract char symbol();
    abstract Pos decideIntent(GameState view);

    @Override public void run() {
        while (running) {
            // 1) Decidir intención
            intent = decideIntent(GameState.snapshot(board, pos));
            phaser.arriveAndAwaitAdvance(); // todos decidieron

            // 2) Esperar a que el Game resuelva/aplique movimientos
            phaser.arriveAndAwaitAdvance();

            // 3) Tick terminado (render hecho). Siguiente ciclo.
            phaser.arriveAndAwaitAdvance();
        }
        phaser.arriveAndDeregister();
    }

    public void stop() { running = false; }
    public Pos getPos() { return pos; }
    public void setPos(Pos p) { this.pos = p; }
}
