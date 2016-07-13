package com.oasis.oasistagview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by liling on 2016/7/13.
 */
public class MoveTextView extends TextView {

    int lastX, lastY;
    int downX, downY;
    String tag = "MoveTextView";
    int screenWidth, screenHeight;
    long downTime;
    boolean left;
    static final int LongClickMessage = 99;
    private android.os.Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == LongClickMessage) {
                Log.e(tag, "LongClick");
            }
        }
    };

    public MoveTextView(Context context) {
        this(context, null);
    }

    public MoveTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoveTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setBackgroundResource(R.drawable.icon_tag_left);
        left = true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                downTime = System.currentTimeMillis();
                Message message = handler.obtainMessage();
                message.what = LongClickMessage;
                handler.sendMessageDelayed(message, 1500);

                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();

                downX = lastX;
                downY = lastY;
                Log.d(tag, "action down");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(tag, "action cancel");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(tag, "action up");
                int ddx = (int) event.getRawX() - downX;
                int ddy = (int) event.getRawY() - downY;

                if (ddx < 5 && ddy < 5 && System.currentTimeMillis() - downTime < 1500) {

                    handler.removeMessages(LongClickMessage);

                    Log.e(tag, "Click");

                    left = !left;
                    if (left) {
                        this.setBackgroundResource(R.drawable.icon_tag_left);
                    } else {
                        this.setBackgroundResource(R.drawable.icon_tag_right);
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                int dddx = (int) event.getRawX() - downX;
                int dddy = (int) event.getRawY() - downY;

                if (dddx > 5 || dddy > 5) {
                    handler.removeMessages(LongClickMessage);
                }


                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                int left = this.getLeft() + dx;
                int top = this.getTop() + dy;
                int right = this.getRight() + dx;
                int bottom = this.getBottom() + dy;
                // 设置不能出界
                if (left < 0) {
                    left = 0;
                    right = left + this.getWidth();
                }

                if (right > screenWidth) {
                    right = screenWidth;
                    left = right - this.getWidth();
                }

                if (top < 0) {
                    top = 0;
                    bottom = top + this.getHeight();
                }

                if (bottom > screenHeight) {
                    bottom = screenHeight;
                    top = bottom - this.getHeight();
                }

                this.layout(left, top, right, bottom);
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                Log.d(tag, "action move");
                break;
            default:
                Log.d(tag, "unknow action");
        }
        return true;
    }

    private void moveViewByLayout(View view, int rawX, int rawY) {
        int left = rawX - this.getWidth() / 2;
        int top = rawY - this.getHeight() / 2;
        int width = left + view.getWidth();
        int height = top + view.getHeight();
        view.layout(left, top, width, height);
    }
}
