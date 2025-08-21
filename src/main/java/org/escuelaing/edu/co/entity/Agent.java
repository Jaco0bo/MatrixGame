package org.escuelaing.edu.co.entity;

import org.escuelaing.edu.co.model.Board;
import org.escuelaing.edu.co.model.Pathfinding;
import org.escuelaing.edu.co.record.GameState;
import org.escuelaing.edu.co.record.Pos;
import java.util.concurrent.Phaser;

public class Agent extends Entity {
    public Agent(String id, Board b, Pos start, Phaser p) { super("Agent-"+id, b, start, p); }
    @Override
    public char symbol() { return 'A'; }

    @Override
    public Pos decideIntent(GameState view) {
        // BFS hacia Neo; si no se ve a Neo, moverse aleatorio válido
        Pos target = view.neoPos();
        return (target != null)
                ? Pathfinding.nextStepBFS(view.selfPos(), target, view.cells(), view.occupants())
                : Pathfinding.randomStep(view.selfPos(), view.cells(), view.occupants());
    }
}
