package tech.longwell.soduko;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class Cell extends Drawable {
    private final int row;
    private final int col;
    private boolean isEditable;
    private Optional<Integer> num;
    private List<Integer> penciledNums;

    // drawing
    private Paint numPaint;
    private Paint numPaintHighlighted;
    private Paint pencilPaint;
    private Paint pencilPaintHighlighted;

    public Cell(int row, int col) {
        super();

        this.row = row;
        this.col = col;
        this.isEditable = true;

        this.num = (int)((Math.random() * (3 - 1) + 1)) == 1
                ? Optional.of((int)((Math.random() * (10 - 1)) + 1))
                : Optional.empty();

        this.penciledNums = List.of(1, 4);

        // big numbers
        numPaint = new Paint();
        numPaint.setAntiAlias(true);
        numPaint.setColor(Color.rgb(255, 255, 255));
        numPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        numPaint.setTextSize(70);
        numPaint.setTextAlign(Paint.Align.CENTER);

        // highlighted big numbers
        numPaintHighlighted = new Paint(numPaint);
        numPaintHighlighted.setColor(Color.rgb(255, 255, 0));

        // small penciled in numbers
        pencilPaint = new Paint();
        pencilPaint.setAntiAlias(true);
        pencilPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        pencilPaint.setColor(Color.rgb(180, 180, 180));
        pencilPaint.setTextSize(30);
        pencilPaint.setTextAlign(Paint.Align.CENTER);

        // highlighted small numbers
        pencilPaintHighlighted = new Paint(pencilPaint);
        pencilPaintHighlighted.setColor(Color.rgb(255, 255, 0));
    }

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

    public Point getPenciledNumCenterOnCanvas(Canvas canvas, int penciledNumber) {
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

        return new Point((int)x, (int)y);
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

    @Override
    void resize(View view) {
        float cellWidth = view.getWidth() / 9;
        float cellHeight = cellWidth;

        int left = (int)(this.col * cellWidth);
        int top = (int)(this.row * cellHeight);
        int right = (int)(left + cellWidth);
        int bottom = (int)(top + cellHeight);
        setBounds(new Rect(left, top, right, bottom));
    }

    void drawNum(Canvas canvas) {
        if (getNum().isPresent()) {
            Paint paint = numPaint;
            float x = getCenter().x;
            float y = getCenter().y - (paint.descent() + paint.ascent()) / 2;
            canvas.drawText(getNum().get().toString(), x, y, numPaint);
        }
    }

    void drawPenciledNums(Canvas canvas) {
        Paint paint = pencilPaint;
        if (!getNum().isPresent()) {
            for (int penciledNum : getPenciledNums()) {
                Point point = getPenciledNumCenterOnCanvas(canvas, penciledNum);
                float x = point.x;
                float y = point.y - (pencilPaint.descent() + pencilPaint.ascent()) / 2;
                canvas.drawText(String.valueOf(penciledNum), x, y, paint);
            }
        }
    }

    @Override
    void draw(Canvas canvas) {
        drawNum(canvas);
        drawPenciledNums(canvas);
//        for (Cell cell : board.getCells()) {
//            if (cell.getNum().isPresent()) {
//                List<Float> coords = cell.getCenterOnCanvas(canvas);
//                Paint paint = getTappedCell(canvas).map(Cell::getNum).orElse(Optional.of(-1)).orElse(-1) == cell.getNum().orElse(-2)
//                        ? numPaintHighlighted
//                        : numPaint;
//
//                float x = coords.get(0);
//                float y = coords.get(1) - ((paint.descent() + paint.ascent()) / 2);
//                canvas.drawText(cell.getNum().get().toString(), x, y, paint);
//            } else {
//                for (int penciledNum : cell.getPenciledNums()) {
//                    List<Float> penciledCoords = cell.getPenciledNumCenterOnCanvas(canvas, penciledNum);
//
//                    Paint paint = getTappedCell(canvas).map(Cell::getNum).orElse(Optional.of(-1)).orElse(-1) == penciledNum
//                            ? pencilPaintHighlighted
//                            : pencilPaint;
//
//                    float penciledX = penciledCoords.get(0);
//                    float penciledY = penciledCoords.get(1) - ((paint.descent() + paint.ascent()) / 2);
//                    canvas.drawText(String.valueOf(penciledNum), penciledX, penciledY, paint);
//                }
//            }
//        }
    }
}
