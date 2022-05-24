package tech.longwell.soduko;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class Potential extends Drawable {
    private final int num;
    private final int row;
    private final int col;
    private boolean isHidden;
    private boolean isEntered;
    private boolean isMatching;

    private Paint textPaint;
    private Paint textHighlightedPaint;

    public Potential(int num, int row, int col) {
        super();

        this.num = num;
        this.row = row;
        this.col = col;
        isEntered = false;
        isMatching = false;

        // small penciled in numbers
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setColor(Color.rgb(180, 180, 180));
        textPaint.setTextSize(30);
        textPaint.setTextAlign(Paint.Align.CENTER);

        // highlighted small numbers
        textHighlightedPaint = new Paint(textPaint);
        textHighlightedPaint.setColor(Color.rgb(255, 255, 0));
    }

    @Override
    void draw(Canvas canvas) {
        if (isEntered() && !isHidden()) {
            Paint paint = isMatching() ? textHighlightedPaint : textPaint;
            float x = getCenter().x;
            float y = getCenter().y - (paint.descent() + paint.ascent()) / 2;
            canvas.drawText(String.valueOf(getNum()), x, y, paint);
        }
    }

    @Override
    void resize(View view) {
        float cellWidth = view.getWidth() / 9;
        float cellHeight = cellWidth;

        float potentialWidth = cellWidth / 3;
        float potentialHeight = cellHeight / 3;

        int xOffset = 0;
        int yOffset = 0;

        if (getNum() == 2 || getNum() == 5 || getNum() == 8)
            xOffset = 1;
        else if (getNum() == 3 || getNum() == 6 || getNum() == 9)
            xOffset = 2;

        if (getNum() >=4 && getNum() <= 6)
            yOffset = 1;
        else if (getNum() >= 7 && getNum() <= 9)
            yOffset = 2;

        int left = (int)(getCol() * cellWidth + xOffset * potentialWidth);
        int top = (int)(getRow() * cellHeight + yOffset * potentialHeight);
        int right = (int)(left + potentialWidth);
        int bottom = (int)(top + potentialHeight);
        setBounds(new Rect(left, top, right, bottom));
    }

    public int getNum() {
        return num;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public boolean isEntered() {
        return isEntered;
    }

    public void setEntered(boolean entered) {
        isEntered = entered;
    }

    public boolean isMatching() {
        return isMatching;
    }

    public void setMatching(boolean matching) {
        isMatching = matching;
    }
}
