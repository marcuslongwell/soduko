package tech.longwell.soduko;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Board {
    private List<Cell> cells;

    public Board() {
        this.cells = new ArrayList<>();

        int row = 0;
        int col = 0;
        for (int i = 0; i <= 81; i++) {
            cells.add(new Cell(row, col));

            col++;

            if (i % 9 == 0 && i != 0) {
                row++;
                col = 0;
            }
        }
    }

    public List<Cell> getCellsInSquare(int square) {
        return getCells().stream().filter(cell -> cell.getSquare() == square).collect(Collectors.toList());
    }

    public List<Cell> getCellsInRow(int row) {
        return getCells().stream().filter(cell -> cell.getRow() == row).collect(Collectors.toList());
    }

    public List<Cell> getCellsInCol(int col) {
        return getCells().stream().filter(cell -> cell.getCol() == col).collect(Collectors.toList());
    }

    public Cell getCell(int row, int col) {
        return getCells().stream().filter(cell -> cell.getRow() == row && cell.getCol() == col).findFirst().get();
    }

    public int getSquare(int row, int col) {
        if (row <= 2) {
            if (col <= 2) return 0;
            else if (col <= 5) return 1;
            else if (col <= 8) return 2;
        } else if (row <= 5) {
            if (col <= 2) return 3;
            else if (col <= 5) return 4;
            else if (col <= 8) return 5;
        } else if (row <= 8) {
            if (col <= 2) return 6;
            else if (col <= 5) return 7;
            else if (col <= 8) return 8;
        }

        throw new RuntimeException("Could not calculate the square of a cell");
    }

    public List<Integer> convertCanvasPixelsToCoords(Canvas canvas, float x, float y) {
        final float cellWidth = canvas.getWidth() / 9;
        final float cellHeight = canvas.getWidth() / 9;

        int row = -1;
        int col = -1;

        for (int r = 0; r < 9; r++) {
            float rStart = cellHeight * r;
            float rEnd = cellHeight * (r + 1);
            if (y >= rStart && y <= rEnd) {
                row = r;
                break;
            }
        }

        for (int c = 0; c < 9; c++) {
            float cStart = cellWidth * c;
            float cEnd = cellWidth * (c + 1);
            if (x >= cStart && x <= cEnd) {
                col = c;
                break;
            }
        }

        if (row < 0 || col < 0) throw new RuntimeException("Unable to convert canvas pixels to coordinates (may be out of bounds)");
        return List.of(row, col);
    }

    public Rect getRowRect(Canvas canvas, int row) {
        final float rowWidth = canvas.getWidth();
        final float rowHeight = canvas.getWidth() / 9;

        final int left = 0;
        final int right = (int)rowWidth;
        final int top = (int)rowHeight * row;
        final int bottom = top + (int)rowHeight;

        return new Rect(left, top, right, bottom);
    }

    public Rect getColRect(Canvas canvas, int col) {
        final float colWidth = canvas.getWidth() / 9;
        final float colHeight = canvas.getWidth();

        final int left = (int)colWidth * col;
        final int right = left + (int)colWidth;
        final int top = 0;
        final int bottom = (int)colHeight;

        return new Rect(left, top, right, bottom);
    }

    public Rect getSquareRect(Canvas canvas, int square) {
        Log.d("haha", "saure is " + String.valueOf(square));
        final float squareWidth = canvas.getWidth() / 3;
        final float squareHeight = canvas.getWidth() / 3;

        int xOffset = 0;
        int yOffset = 0;
        if (square == 1 || square == 4 || square == 7)
            xOffset = 1;
        else if (square == 2 || square == 5 || square == 8)
            xOffset = 2;

        if (square >= 3 && square <= 5)
            yOffset = 1;
        else if (square >= 6 && square <= 8)
            yOffset = 2;

        Log.d("haha", "x offset: " + String.valueOf(xOffset) + ", y offset: " + String.valueOf(yOffset));

        final int left = (int)(xOffset * squareWidth);
        final int right = left + (int)squareWidth;
        final int top = (int)(yOffset * squareHeight);
        final int bottom = top + (int)squareHeight;

        return new Rect(left, top, right, bottom);
    }

    public List<Cell> getCells() {
        return cells;
    }
}
