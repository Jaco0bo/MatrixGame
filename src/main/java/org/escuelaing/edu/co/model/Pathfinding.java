package org.escuelaing.edu.co.model;

import org.escuelaing.edu.co.entity.Entity;
import org.escuelaing.edu.co.enumeration.CellType;
import org.escuelaing.edu.co.record.Pos;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Pathfinding {
    public static Pos nextStepBFS(Pos start, Pos goal, CellType[][] cells, Entity[][] occ) {
        if (start == null || goal == null) return start;
        int[] dr = {-1,1,0,0}, dc = {0,0,-1,1};
        boolean[][] vis = new boolean[10][10];
        Pos[][] parent = new Pos[10][10];
        ArrayDeque<Pos> q = new ArrayDeque<>();
        q.add(start); vis[start.r()][start.c()] = true;
        while (!q.isEmpty()) {
            Pos p = q.poll();
            if (p.equals(goal)) break;
            for (int k=0;k<4;k++){
                Pos n = new Pos(p.r()+dr[k], p.c()+dc[k]);
                if (0<=n.r()&&n.r()<10&&0<=n.c()&&n.c()<10
                        && cells[n.r()][n.c()] != CellType.WALL
                        && !vis[n.r()][n.c()]) {
                    vis[n.r()][n.c()] = true;
                    parent[n.r()][n.c()] = p;
                    q.add(n);
                }
            }
        }
        if (!vis[goal.r()][goal.c()]) return start; // sin camino
        // reconstruir 1er paso
        Pos cur = goal, prev = parent[cur.r()][cur.c()];
        while (prev != null && !prev.equals(start)) { cur = prev; prev = parent[cur.r()][cur.c()]; }
        return cur;
    }

    public static Pos randomStep(Pos start, CellType[][] cells, Entity[][] occ) {
        List<Pos> options = new ArrayList<>();
        int[] dr = {-1,1,0,0}, dc = {0,0,-1,1};
        for (int k=0;k<4;k++){
            Pos n = new Pos(start.r()+dr[k], start.c()+dc[k]);
            if (0<=n.r()&&n.r()<10&&0<=n.c()&&n.c()<10
                    && cells[n.r()][n.c()] != CellType.WALL) {
                options.add(n);
            }
        }
        if (options.isEmpty()) return start;
        return options.get(new java.util.Random().nextInt(options.size()));
    }
}
