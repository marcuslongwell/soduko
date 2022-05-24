package tech.longwell.soduko;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

    private Paint boardBackgroundPaint;
    private Paint connectedHighlightPaint;
    private Paint gridPaint;
    private Paint gridSquarePaint;
    private Paint numPaint;
    private Paint pencilPaint;

    private PointF tapPoint;

    public GameView(Context context) {
        super(context);

        // the board background
        boardBackgroundPaint = new Paint();
        boardBackgroundPaint.setAntiAlias(true);
        boardBackgroundPaint.setColor(Color.rgb(25, 25, 25));
        boardBackgroundPaint.setStyle(Paint.Style.FILL);

        // cell highlighting
        connectedHighlightPaint = new Paint();
        connectedHighlightPaint.setAntiAlias(true);
        connectedHighlightPaint.setColor(Color.rgb(69, 69, 69));
        connectedHighlightPaint.setStyle(Paint.Style.FILL);

        // the small grid lines
        gridPaint = new Paint();
        gridPaint.setAntiAlias(true);
        gridPaint.setColor(Color.rgb(45, 45, 45));
        gridPaint.setStyle(Paint.Style.STROKE);

        // the big grid lines
        gridSquarePaint = new Paint();
        gridSquarePaint.setAntiAlias(true);
        gridSquarePaint.setColor(Color.rgb(69, 69, 69));
        gridSquarePaint.setStyle(Paint.Style.STROKE);
        gridSquarePaint.setStrokeWidth(5);

        // big numbers
        numPaint = new Paint();
        numPaint.setAntiAlias(true);
        numPaint.setColor(Color.rgb(255, 255, 255));
        numPaint.setStyle(Paint.Style.STROKE);
        numPaint.setTextSize(70);
        numPaint.setTextAlign(Paint.Align.CENTER);

        // small penciled in numbers
        pencilPaint = new Paint();
        pencilPaint.setAntiAlias(true);
        pencilPaint.setStyle(Paint.Style.STROKE);
        pencilPaint.setColor(Color.rgb(180, 180, 180));
        pencilPaint.setTextSize(30);
        pencilPaint.setTextAlign(Paint.Align.CENTER);

        tapPoint = new PointF(-1, -1);

        this.board = new Board();
    }

    // no allocations in this method
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("REDRAW", "redrawing");
        drawBoardBackground(canvas);
        drawHighlights(canvas);
        drawGrid(canvas);
        drawCells(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d("LOL", String.valueOf(eventX) + ", " + String.valueOf(eventY));
            tapPoint.set(eventX, eventY);
            invalidate();
        }

        return super.onTouchEvent(event);
    }

    private void drawBoardBackground(Canvas canvas) {
        canvas.drawRect(new Rect(0, 0, canvas.getWidth(), canvas.getWidth()), boardBackgroundPaint);
    }

    private void drawHighlights(Canvas canvas) {
        if (tapPoint.x >= 0 && tapPoint.y >= 0 && tapPoint.x <= canvas.getWidth() && tapPoint.y <= canvas.getWidth()) {
            Log.d("YO", "tapped board");

            List<Integer> coords = board.convertCanvasPixelsToCoords(canvas, tapPoint.x, tapPoint.y);
            int focusedRow = coords.get(0);
            int focusedCol = coords.get(1);

            canvas.drawRect(board.getRowRect(canvas, focusedRow), connectedHighlightPaint);
            canvas.drawRect(board.getColRect(canvas, focusedCol), connectedHighlightPaint);
            canvas.drawRect(board.getSquareRect(canvas, board.getSquare(focusedRow, focusedCol)), connectedHighlightPaint);
        }
    }

    private void drawGrid(Canvas canvas) {
        final float colWidth = canvas.getWidth() / 9;
        final float colHeight = canvas.getWidth() / 9;

        for (int i = 0; i < 10; i++) {
            final Paint linePaint = i % 3 == 0 && i != 0 && i != 9 ? gridSquarePaint : gridPaint;
            canvas.drawLine(i * colWidth, 0, i * colWidth, canvas.getWidth(), linePaint);
            canvas.drawLine(0, i * colHeight, canvas.getWidth(), i * colHeight, linePaint);
        }
    }

    private void drawCells(Canvas canvas) {
        for (Cell cell : board.getCells()) {
            if (cell.getNum().isPresent()) {
                List<Float> coords = cell.getCenterOnCanvas(canvas);
                float x = coords.get(0);
                float y = coords.get(1) - ((numPaint.descent() + numPaint.ascent()) / 2);
                canvas.drawText(cell.getNum().get().toString(), x, y, numPaint);
            } else {
                for (int penciledNum : cell.getPenciledNums()) {
                    List<Float> penciledCoords = cell.getPenciledNumCenterOnCanvas(canvas, penciledNum);
                    float penciledX = penciledCoords.get(0);
                    float penciledY = penciledCoords.get(1) - ((pencilPaint.descent() + pencilPaint.ascent()) / 2);
                    canvas.drawText(String.valueOf(penciledNum), penciledX, penciledY, pencilPaint);
                }
            }
        }
    }
}