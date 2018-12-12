package com.timsimonhughes.popularmovies_p2.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class RoundedCornerImageView extends AppCompatImageView {

    private float radius = 4.0f;
    private Path mPath;
    private RectF mRectF;

    public RoundedCornerImageView(Context context) {
        super(context);
        init();
    }

    public RoundedCornerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundedCornerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPath = new Path();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        mRectF = new RectF(0, 0, this.getWidth(), this.getHeight());
        mPath.addRoundRect(mRectF, radius, radius, Path.Direction.CW);
        canvas.clipPath(mPath);
        super.onDraw(canvas);
    }
}
