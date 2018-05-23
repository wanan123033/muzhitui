package base.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/9/8.
 */

public class RoundImageView extends ImageView{
    float width, height;

    public float getRound() {
        return round;
    }

    private float round = 12;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (width > round && height > round) {
            Path path = new Path();
            path.moveTo(round, 0);
            path.lineTo(width - round, 0);
            path.quadTo(width, 0, width, round);
            path.lineTo(width, height - round);
            path.quadTo(width, height, width - round, height);
            path.lineTo(round, height);
            path.quadTo(0, height, 0, height - round);
            path.lineTo(0, round);
            path.quadTo(0, 0, round, 0);
            canvas.clipPath(path);
        }
        super.onDraw(canvas);
    }

    public void setRound(float round){
        this.round = round;
//        invalidate();
    }

}
