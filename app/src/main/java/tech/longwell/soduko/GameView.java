package tech.longwell.soduko;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;
import java.util.Optional;

/**
 * TODO: document your custom view class.
 */
public class GameView extends View {
    private final Board board;

    private Paint buttonPaint;
    private Paint buttonTextPaint;
    private Paint connectedHighlightPaint;

    private Point tapPoint;

    public GameView(Context context) {
        super(context);

        // button backgrounds
        buttonPaint = new Paint();
        buttonPaint.setAntiAlias(true);
        buttonPaint.setColor(Color.rgb(0, 169, 69));
        buttonPaint.setStyle(Paint.Style.FILL);

        // button numbers
        buttonTextPaint = new Paint();
        buttonTextPaint.setAntiAlias(true);
        buttonTextPaint.setColor(Color.rgb(255, 255, 255));
        buttonTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        buttonTextPaint.setTextSize(70);
        buttonTextPaint.setTextAlign(Paint.Align.CENTER);

        tapPoint = new Point(-1, -1);
        this.board = new Board();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        board.resize(this);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    // no allocations in this method
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // update tapped cell
        for (Cell cell : board.getCells()) {
            if (cell.getBounds().contains(tapPoint.x, tapPoint.y))
                cell.setSelected(true);
            else
                cell.setSelected(false);
        }

        board.draw(canvas);
//        drawButtons(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d("LOL", String.valueOf(eventX) + ", " + String.valueOf(eventY));
            tapPoint = new Point((int)eventX, (int)eventY);
            invalidate();
        }

        return super.onTouchEvent(event);
    }

    private void drawButtons(Canvas canvas) {
        final float buttonWidth = canvas.getWidth() / 5;
        final float buttonHeight = buttonWidth;

        for (int r = 2; r > 0; r--) {
            for (int c = 0; c < 5; c++) {
                final int left = (int)(buttonWidth * c);
                final int top = (int)(canvas.getHeight() - (buttonHeight * (r + 1)));
                final int right = (int)(left + buttonWidth);
                final int bottom = (int)(top + buttonHeight);

                final Rect buttonRect = new Rect(left + 1, top + 1, right - 1, bottom - 1);

                final float buttonCenterX = (buttonRect.left + buttonRect.right) / 2;
                final float buttonCenterY = (buttonRect.top + buttonRect.bottom) / 2;

                float textX = buttonCenterX;
                float textY = buttonCenterY - ((buttonTextPaint.descent() + buttonTextPaint.ascent()) / 2);

                int num = r * 5 + c;
                canvas.drawRect(buttonRect, buttonPaint);
                canvas.drawText(String.valueOf(num), textX, textY, buttonTextPaint);
            }
        }
    }

//    private Optional<Cell> getTappedCell(Canvas canvas) {
//        Optional<Cell> tappedCell = Optional.empty();
//
//        if (tapPoint.x >= 0 && tapPoint.x <= canvas.getWidth() && tapPoint.y >= 0 && tapPoint.y <= canvas.getWidth()) {
//            List<Integer> coords = board.convertCanvasPixelsToCoords(canvas, tapPoint.x, tapPoint.y);
//            tappedCell = Optional.of(board.getCell(coords.get(0), coords.get(1)));
//        }
//
//        return tappedCell;
//    }
}