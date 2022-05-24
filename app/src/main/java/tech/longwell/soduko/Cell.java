package tech.longwell.soduko;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Cell extends Drawable {
    private final int row;
    private final int col;
    private boolean isEditable;
    private boolean isSelected;
    private boolean isHighlighted;
    private boolean isMatching;
    private Optional<Integer> num;
    private List<Potential> potentials;

    // drawing
    private Paint connectedHighlightPaint;
    private Paint selectedOutlinePaint;
    private Paint numPaint;
    private Paint numPaintHighlighted;

    public Cell(int row, int col) {
        super();

        this.row = row;
        this.col = col;
        isEditable = true;
        isSelected = false;
        isHighlighted = false;
        isMatching = false;

        num = (int)((Math.random() * (3 - 1) + 1)) == 1
                ? Optional.of((int)((Math.random() * (10 - 1)) + 1))
                : Optional.empty();

        potentials = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            potentials.add(new Potential(i + 1, row, col));

            if (i == 0 || i == 3 || i == 8) potentials.get(i).setEntered(true);
        }

        // cell highlighting
        connectedHighlightPaint = new Paint();
        connectedHighlightPaint.setAntiAlias(true);
        connectedHighlightPaint.setColor(Color.rgb(24, 33, 24));
        connectedHighlightPaint.setStyle(Paint.Style.FILL);

        // currently selected cell outline
        selectedOutlinePaint = new Paint();
        selectedOutlinePaint.setAntiAlias(true);
        selectedOutlinePaint.setColor(Color.rgb(52, 69, 52));
        selectedOutlinePaint.setStyle(Paint.Style.FILL);
        selectedOutlinePaint.setStrokeWidth(3);

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
    }

    public int getSquare() {
        Log.d("WUT", String.valueOf(getRow()) + ", " + String.valueOf(getCol()));
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
    }

    public boolean isMatching() {
        return isMatching;
    }

    public void setMatching(boolean matching) {
        isMatching = matching;
    }

    public Optional<Integer> getNum() {
        return num;
    }

    public void setNum(Optional<Integer> num) {
        this.num = num;
    }

    public List<Potential> getPotentials() {
        return potentials;
    }

    public void setPotentials(List<Potential> potentials) {
        this.potentials = potentials;
    }

    public void enterPotential(int num) {
        potentials.stream().filter(p -> p.getNum() == num).findFirst().ifPresent(potential -> potential.setEntered(true));
    }

    public void erasePotential(int num) {
        potentials.stream().filter(p -> p.getNum() == num).findFirst().ifPresent(potential -> potential.setEntered(false));
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

        for (Potential potential : potentials) {
            potential.resize(view);
        }
    }

    void drawHighlight(Canvas canvas) {
        if (isHighlighted())
            canvas.drawRect(getBounds(), connectedHighlightPaint);

        if (isSelected())
            canvas.drawRect(getBounds(), selectedOutlinePaint);
    }

    void drawNum(Canvas canvas) {
        if (getNum().isPresent()) {
            Paint paint = isMatching() ? numPaintHighlighted : numPaint;
            float x = getCenter().x;
            float y = getCenter().y - (paint.descent() + paint.ascent()) / 2;
            canvas.drawText(getNum().get().toString(), x, y, paint);
        }
    }

    @Override
    void draw(Canvas canvas) {
        drawHighlight(canvas);
        drawNum(canvas);

        potentials.forEach(potential -> {
            potential.setHidden(getNum().isPresent());
            potential.draw(canvas);
        });
    }
}
