package tech.longwell.soduko;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

public abstract class Drawable {
    private Rect bounds;

    public Drawable() {
        this.bounds = new Rect();
    }

    abstract void draw(Canvas canvas);
    abstract void resize(View view);

    public Point getCenter() {
        return new Point(
                (getBounds().left + getBounds().right) / 2,
                (getBounds().top + getBounds().bottom) / 2);
    }

    public Rect getBounds() {
        return bounds;
    }

    public void setBounds(Rect bounds) {
        this.bounds = bounds;
    }

    public int getWidth() {
        return Math.abs(this.bounds.left - this.bounds.right);
    }

    public int getHeight() {
        return Math.abs(this.bounds.top - this.bounds.bottom);
    }
}
