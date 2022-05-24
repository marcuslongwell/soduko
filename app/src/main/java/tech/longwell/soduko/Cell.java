package tech.longwell.soduko;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class Cell {
    private final int row;
    private final int col;
    private boolean isEditable;
    private Optional<Integer> num;
    private List<Integer> penciledNums;

    public int getSquare() {
        if (getRow() <= 2) {
            if (getCol() <= 2) return 0;
            else if (getCol() <= 5) return 1;
            else if (getCol() <= 8) return 2;
        } else if (getRow() <= 5) {
            if (getCol() <= 2) return 3;
            else if (getCol() <= 5) return 4;
            else if (getCol() <= 8) return 5;
        } else if (getRow() <= 8) {
            if (getCol() <= 2) return 6;
            else if (getCol() <= 5) return 7;
            else if (getCol() <= 8) return 8;
        }

        throw new RuntimeException("Could not calculate the square of a cell");
    }

    public List<Float> getCenterOnCanvas(Canvas canvas) {
        final float cellWidth = canvas.getWidth() / 9;
        final float cellHeight = canvas.getWidth() / 9;

        Log.d("TEST", String.valueOf(getRow()) + ", " + String.valueOf(getCol()));

        final float x = getCol() * cellWidth + cellWidth / 2;
        final float y = getRow() * cellHeight + cellHeight / 2;

        return List.of(x, y);
    }

    public List<Float> getPenciledNumCenterOnCanvas(Canvas canvas, int penciledNumber) {
        final float cellWidth = canvas.getWidth() / 9;
        final float cellHeight = canvas.getWidth() / 9;

        final float penciledWidth = cellWidth / 3;
        final float penciledHeight = cellHeight / 3;

        final float xStart = getCol() * cellWidth;
        final float yStart = getRow() * cellHeight;

        float xOffset = 0;
        float yOffset = 0;
        if (penciledNumber == 2 || penciledNumber == 5 || penciledNumber == 8)
            xOffset = 1;
        else if (penciledNumber == 3 || penciledNumber == 6 || penciledNumber == 9)
            xOffset = 2;

        if (penciledNumber >= 4 && penciledNumber <= 6)
            yOffset = 1;
        else if (penciledNumber >= 7 && penciledNumber <= 9)
            yOffset = 2;

        // idk why we divide by 3 here, but it looks better
        final float x = xStart + xOffset * penciledWidth + penciledWidth / 2;
        final float y = yStart + yOffset * penciledHeight + penciledHeight / 2;

        return List.of(x, y);
    }

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.isEditable = true;
        this.num = Optional.empty();
        this.penciledNums = List.of(1, 4, 5, 8, 9);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public Optional<Integer> getNum() {
        return num;
    }

    public void setNum(Optional<Integer> num) {
        this.num = num;
    }

    public List<Integer> getPenciledNums() {
        return penciledNums;
    }

    public void setPenciledNums(List<Integer> penciledNums) {
        this.penciledNums = penciledNums;
    }

    public void addPenciledNum(int num) {
        if (!penciledNums.contains(num))
            penciledNums.add(num);
    }

    public void removePenciledNum(int num) {
        if (penciledNums.contains(num))
            penciledNums.remove(num);
    }
}
