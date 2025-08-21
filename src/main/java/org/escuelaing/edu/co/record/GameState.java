package org.escuelaing.edu.co.record;

import org.escuelaing.edu.co.entity.Entity;
import org.escuelaing.edu.co.entity.Neo;
import org.escuelaing.edu.co.enumeration.CellType;
import org.escuelaing.edu.co.model.*;


public record GameState(CellType[][] cells, Entity[][] occupants, Pos selfPos, Pos phonePos, Pos neoPos) {
    public static GameState snapshot(Board board, Pos selfPos) {
        // Copias superficiales suficientes para 10x10
        CellType[][] cellsCopy = new CellType[10][10];
        Entity[][] occCopy = new Entity[10][10];
        for (int r=0;r<10;r++){
            System.arraycopy(board.cells[r], 0, cellsCopy[r], 0, 10);
            System.arraycopy(board.occupants[r], 0, occCopy[r], 0, 10);
        }
        Pos phone = null;
        Pos neo = null;
        for (int r=0;r<10;r++) for (int c=0;c<10;c++) {
            if (cellsCopy[r][c] == CellType.PHONE) phone = new Pos(r,c);
            if (occCopy[r][c] instanceof Neo) neo = new Pos(r,c);
        }
        return new GameState(cellsCopy, occCopy, selfPos, phone, neo);
    }
}
