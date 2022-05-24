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
    private Paint numPaintHighlighted;
    private Paint pencilPaint;
    private Paint pencilPaintHighlighted;

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
        connectedHighlightPaint.setColor(Color.rgb(24, 33, 24));
        connectedHighlightPaint.setStyle(Paint.Style.FILL);

        // the small grid lines
        gridPaint = new Paint();
        gridPaint.setAntiAlias(true);
        gridPaint.setColor(Color.rgb(69, 69, 69));
        gridPaint.setStyle(Paint.Style.STROKE);

        // the big grid lines
        gridSquarePaint = new Paint();
        gridSquarePaint.setAntiAlias(true);
        gridSquarePaint.setColor(Color.rgb(102, 102, 102));
        gridSquarePaint.setStyle(Paint.Style.STROKE);
        gridSquarePaint.setStrokeWidth(5);

        // big numbers
        numPaint = new Paint();
        numPaint.setAntiAlias(true);
        numPaint.setColor(Color.rgb(255, 255, 255));
        numPaint.setStyle(Paint.Style.STROKE);
        numPaint.setTextSize(70);
        numPaint.setTextAlign(Paint.Align.CENTER);

        // highlighted big numbers
        numPaintHighlighted = new Paint(numPaint);
        numPaintHighlighted.setColor(Color.rgb(255, 255, 0));

        // small penciled in numbers
        pencilPaint = new Paint();
        pencilPaint.setAntiAlias(true);
        pencilPaint.setStyle(Paint.Style.STROKE);
        pencilPaint.setColor(Color.rgb(180, 180, 180));
        pencilPaint.setTextSize(30);
        pencilPaint.setTextAlign(Paint.Align.CENTER);

        // highlighted small numbers
        pencilPaintHighlighted = new Paint(pencilPaint);
        pencilPaintHighlighted.setColor(Color.rgb(255, 255, 0));

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

    private Optional<Cell> getTappedCell(Canvas canvas) {
        Optional<Cell> tappedCell = Optional.empty();

        if (tapPoint.x >= 0 && tapPoint.x <= canvas.getWidth() && tapPoint.y >= 0 && tapPoint.y <= canvas.getWidth()) {
            List<Integer> coords = board.convertCanvasPixelsToCoords(canvas, tapPoint.x, tapPoint.y);
            tappedCell = Optional.of(board.getCell(coords.get(0), coords.get(1)));
        }

        return tappedCell;
    }

    private void drawCells(Canvas canvas) {
//        Optional<Cell> selectedCell = Optional.
//
//                board.getCells().stream().filter(cell -> cell.getNum().g)
//




        for (Cell cell : board.getCells()) {
            if (cell.getNum().isPresent()) {
                List<Float> coords = cell.getCenterOnCanvas(canvas);
                Paint paint = getTappedCell(canvas).map(Cell::getNum).orElse(Optional.of(-1)).orElse(-1) == cell.getNum().orElse(-2)
                    ? numPaintHighlighted
                    : numPaint;

                float x = coords.get(0);
                float y = coords.get(1) - ((paint.descent() + paint.ascent()) / 2);
                canvas.drawText(cell.getNum().get().toString(), x, y, paint);
            } else {
                for (int penciledNum : cell.getPenciledNums()) {
                    List<Float> penciledCoords = cell.getPenciledNumCenterOnCanvas(canvas, penciledNum);

                    Paint paint = getTappedCell(canvas).map(Cell::getNum).orElse(Optional.of(-1)).orElse(-1) == penciledNum
                            ? pencilPaintHighlighted
                            : pencilPaint;

                    float penciledX = penciledCoords.get(0);
                    float penciledY = penciledCoords.get(1) - ((paint.descent() + paint.ascent()) / 2);
                    canvas.drawText(String.valueOf(penciledNum), penciledX, penciledY, paint);
                }
            }
        }
    }
}