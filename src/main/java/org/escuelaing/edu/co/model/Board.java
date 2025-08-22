package org.escuelaing.edu.co.model;

import org.escuelaing.edu.co.entity.Entity;
import org.escuelaing.edu.co.enumeration.CellType;
import org.escuelaing.edu.co.record.Pos;

public class Board {
    public static final int N = 10;
    public final CellType[][] cells = new CellType[N][N];
    // Mapa de ocupantes actuales (solo una entidad por celda)
    public final Entity[][] occupants = new Entity[N][N];

    public Board() {
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                cells[r][c] = CellType.EMPTY;
            }
        }
    }

    public synchronized boolean isFree(Pos p) {
        return inBounds(p) && cells[p.r()][p.c()] != CellType.WALL && occupants[p.r()][p.c()] == null;
    }

    public boolean inBounds(Pos p) {
        return p != null && 0 <= p.r() && p.r() < N && 0 <= p.c() && p.c() < N;
    }

    public synchronized void place(Entity e, Pos p) {
        if (inBounds(p)) occupants[p.r()][p.c()] = e;
    }

    public synchronized void clear(Pos p) {
        if (inBounds(p)) occupants[p.r()][p.c()] = null;
    }

    public synchronized Entity at(Pos p) {
        if (!inBounds(p)) return null;
        return occupants[p.r()][p.c()];
    }

    public synchronized CellType cell(Pos p) {
        if (!inBounds(p)) return CellType.WALL;
        return cells[p.r()][p.c()];
    }

    public synchronized void setCell(Pos p, CellType t) {
        if (inBounds(p)) cells[p.r()][p.c()] = t;
    }

    public synchronized void print() {
        for (int r = 0; r < N; r++) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < N; c++) {
                Entity occ = occupants[r][c];
                if (occ != null) sb.append(occ.symbol());
                else if (cells[r][c] == CellType.WALL) sb.append('#');
                else if (cells[r][c] == CellType.PHONE) sb.append('T');
                else sb.append('.');
                sb.append(' ');
            }
            System.out.println(sb);
        }
        System.out.println();
    }
}
