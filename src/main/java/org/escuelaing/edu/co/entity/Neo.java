package org.escuelaing.edu.co.entity;

import org.escuelaing.edu.co.model.Board;
import org.escuelaing.edu.co.model.Pathfinding;
import org.escuelaing.edu.co.record.GameState;
import org.escuelaing.edu.co.record.Pos;

import java.util.concurrent.Phaser;

public class Neo extends Entity {
    public Neo(Board b, Pos start, Phaser p) { super("Neo", b, start, p); }
    @Override
    public char symbol() { return 'N'; }

    @Override
    public Pos decideIntent(GameState view) {
        // BFS hacia el teléfono evitando muros y agentes; si no hay camino, quedarse
        return Pathfinding.nextStepBFS(view.selfPos(), view.phonePos(), view.cells(), view.occupants());
    }
}