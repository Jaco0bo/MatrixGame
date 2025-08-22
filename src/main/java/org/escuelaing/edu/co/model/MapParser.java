package org.escuelaing.edu.co.model;

import org.escuelaing.edu.co.entity.Agent;
import org.escuelaing.edu.co.entity.Neo;
import org.escuelaing.edu.co.entity.Entity;
import org.escuelaing.edu.co.enumeration.CellType;
import org.escuelaing.edu.co.record.Pos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;

/**
 * MapParser público: parsea ASCII map y crea Board + Entities.
 */
public class MapParser {

    /**
     * Parsea el mapa ASCII (puede tener espacios entre símbolos) y devuelve un Board
     */
    public static Board parse(String asciiMap) {
        Board board = new Board();
        String[] rawLines = asciiMap.split("\\r?\\n");
        List<String> lines = new ArrayList<>();
        for (String ln : rawLines) {
            String trimmed = ln.trim();
            if (trimmed.isEmpty()) continue;
            String compressed = trimmed.replaceAll("\\s+", "");
            lines.add(compressed);
        }

        int N = board.N;
        for (int r = 0; r < N; r++) {
            String row = r < lines.size() ? lines.get(r) : "";
            for (int c = 0; c < N; c++) {
                char ch = c < row.length() ? row.charAt(c) : '.';
                Pos p = new Pos(r, c);
                switch (ch) {
                    case '.': board.setCell(p, CellType.EMPTY); break;
                    case '#': board.setCell(p, CellType.WALL); break;
                    case 'T': board.setCell(p, CellType.PHONE); break;
                    default: board.setCell(p, CellType.EMPTY); break;
                }
            }
        }
        return board;
    }

    /**
     * Crea las entidades (Neo y Agents) según asciiMap y las coloca en el board.
     * Devuelve la lista de entidades creadas.
     */
    public static List<Entity> instantiateEntitiesFromMap(Board board, Phaser phaser, String asciiMap) {
        List<Entity> list = new ArrayList<>();
        String[] rawLines = asciiMap.split("\\r?\\n");
        List<String> lines = new ArrayList<>();
        for (String ln : rawLines) {
            String trimmed = ln.trim();
            if (trimmed.isEmpty()) continue;
            String compressed = trimmed.replaceAll("\\s+", "");
            lines.add(compressed);
        }
        int agentCounter = 1;
        int N = board.N;
        for (int r = 0; r < N; r++) {
            String row = r < lines.size() ? lines.get(r) : "";
            for (int c = 0; c < N; c++) {
                char ch = c < row.length() ? row.charAt(c) : '.';
                Pos p = new Pos(r, c);
                switch (ch) {
                    case 'N': {
                        Neo neo = new Neo(board, p, phaser);
                        list.add(neo);
                        board.place(neo, p);
                        break;
                    }
                    case 'A': {
                        Agent a = new Agent(String.valueOf(agentCounter++), board, p, phaser);
                        list.add(a);
                        board.place(a, p);
                        break;
                    }
                    default: break;
                }
            }
        }
        return list;
    }
}
