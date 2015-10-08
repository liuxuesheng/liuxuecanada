package com.liuxuecanada.liuxuecanada.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class WheelView extends ScrollView {
    public static final String TAG = WheelView.class.getSimpleName();
    public static final int OFF_SET_DEFAULT = 2;
    private static final int SCROLL_DIRECTION_UP = 0;
    //    private ScrollView scrollView;
    private static final int SCROLL_DIRECTION_DOWN = 1;
    private static final int displayItemCount = 5; // 每页显示的数量
    int offset = OFF_SET_DEFAULT; // 偏移量（需要在最前面和最后面补全）
    int selectedIndex = 1;
    int initialY;
    Runnable scrollerTask;
    int newCheck = 50;
    int itemHeight = 20;
    /**
     * 获取选中区域的边界
     */
    int[] selectedAreaBorder = null;
    Paint paint;
    int viewWidth = 0;
    //    String[] items;
    private List<String> items = null;
    private List<TextView> itemBuffer = null;
    private Context context;
    private LinearLayout linearView;
    private int scrollDirection = -1;
    private OnWheelViewListener onWheelViewListener;

    public WheelView(Context context) {
        super(context);
        init(context);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private List<TextView> getItems() {
        return this.itemBuffer;
    }

    public void setItems(List<String> list) {
        items = new ArrayList<String>();
        itemBuffer = new ArrayList<TextView>();

        items.clear();
        itemBuffer.clear();

        items.addAll(list);

        linearView.addView(new TextView(context));
        linearView.addView(new TextView(context));

        for (int i = 0; i < 5; i++) {
            if (items.size() - 1 < i)
                break;
            TextView tv = createTextView(items.get(i));
            itemBuffer.add(i, tv);
            linearView.addView(tv);
        }

        // 前面和后面补全/*


        linearView.addView(new TextView(context));
        linearView.addView(new TextView(context));

        refreshItemView(0);

    }

    private TextView createTextView(String item) {
        TextView tv = new TextView(context);
        RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        p1.addRule(RelativeLayout.CENTER_IN_PARENT);
/*        if ((relation != 0) && (relationid != 0))
            p.addRule(relation, relationid);*/
        //LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //p.gravity = Gravity.CENTER_HORIZONTAL;

        tv.setLayoutParams(p1);
        tv.setSingleLine(true);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv.setText(item);

        //int padding = ABTextUtil.dip2px(context, 15);
        //tv.setPadding(padding, padding, padding, padding);
/*        if (itemHeight == 0) {
            itemHeight = tv.getMeasuredHeight();
            Log.d("viewasdlajslsd", " " + itemHeight);
            linearView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight * displayItemCount));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.getLayoutParams();
            this.setLayoutParams(new LinearLayout.LayoutParams(lp.width, itemHeight * displayItemCount));
        }*/
        return tv;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    private void init(Context context) {
        this.context = context;

        Log.d("viewasdlajsl", " second");

        this.setVerticalScrollBarEnabled(false);


        linearView = new LinearLayout(context);
        linearView.setOrientation(LinearLayout.VERTICAL);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);


        RelativeLayout.LayoutParams p2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, displaymetrics.heightPixels / 4);
        p2.addRule(RelativeLayout.CENTER_HORIZONTAL);

        this.setLayoutParams(p2);

        this.addView(linearView);

        scrollerTask = new Runnable() {

            @Override
            public void run() {

                int newY = getScrollY();
                Log.d("asd28dh ", " init " + initialY + " new " + newY);
                if (initialY - newY == 0) { // SCROLL STOPPED
                    final int remainder = initialY % itemHeight;
                    final int divided = initialY / itemHeight;
                    Log.d("asd28dh ", "" + remainder + " " + divided);
                    if (remainder == 0) {
                        selectedIndex = divided + offset;

                        onSeletedCallBack();
                    } else {
                        if (remainder > itemHeight / 2) {
                            WheelView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    WheelView.this.smoothScrollTo(0, initialY - remainder + itemHeight);
                                    selectedIndex = divided + offset + 1;
                                    onSeletedCallBack();
                                }
                            });
                        } else {
                            WheelView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    WheelView.this.smoothScrollTo(0, initialY - remainder);
                                    selectedIndex = divided + offset;
                                    onSeletedCallBack();
                                }
                            });
                        }


                    }


                } else {
                    initialY = getScrollY();
                    WheelView.this.postDelayed(scrollerTask, newCheck);
                }
            }
        };


    }

    public void smoothScrollToTextView(TextView tv) {

    }

    public void startScrollerTask() {
        initialY = getScrollY();
        this.postDelayed(scrollerTask, newCheck);
    }


    private TextView createView(String item) {
        TextView tv = new TextView(context);
        RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        p1.addRule(RelativeLayout.CENTER_IN_PARENT);
/*        if ((relation != 0) && (relationid != 0))
            p.addRule(relation, relationid);*/
        //LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //p.gravity = Gravity.CENTER_HORIZONTAL;

        tv.setLayoutParams(p1);
        tv.setSingleLine(true);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv.setText(item);
        tv.setGravity(Gravity.CENTER);
        //int padding = ABTextUtil.dip2px(context, 15);
        //tv.setPadding(padding, padding, padding, padding);
        if (itemHeight == 0) {
            itemHeight = tv.getMeasuredHeight();
            Log.d("viewasdlajslsd", " " + itemHeight);
            linearView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight * displayItemCount));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.getLayoutParams();
            this.setLayoutParams(new LinearLayout.LayoutParams(lp.width, itemHeight * displayItemCount));
        }
        return tv;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

//        Logger.d(TAG, "l: " + l + ", t: " + t + ", oldl: " + oldl + ", oldt: " + oldt);

//        try {
//            Field field = ScrollView.class.getDeclaredField("mScroller");
//            field.setAccessible(true);
//            OverScroller mScroller = (OverScroller) field.get(this);
//
//
//            if(mScroller.isFinished()){
//                Logger.d(TAG, "isFinished...");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        refreshItemView(t);

        if (t > oldt) {
//            Logger.d(TAG, "向下滚动");
            scrollDirection = SCROLL_DIRECTION_DOWN;
        } else {
//            Logger.d(TAG, "向上滚动");
            scrollDirection = SCROLL_DIRECTION_UP;

        }

    }

    private void refreshItemView(int y) {
        int position = y / itemHeight + offset;
        int remainder = y % itemHeight;
        int divided = y / itemHeight;

        Log.d("viewasdlajsl", " " + position + " " + remainder + " " + divided);

        if (remainder == 0) {
            position = divided + offset;
        } else {
            if (remainder > itemHeight / 2) {
                position = divided + offset + 1;
            }

        }

        int childSize = linearView.getChildCount();
        for (int i = 0; i < childSize; i++) {
            TextView itemView = (TextView) linearView.getChildAt(i);
            if (null == itemView) {
                return;
            }
            if (position == i) {
                itemView.setTextColor(Color.parseColor("#0288ce"));
            } else {
                itemView.setTextColor(Color.parseColor("#bbbbbb"));
            }
        }
    }

    private int[] obtainSelectedAreaBorder() {
        if (selectedAreaBorder == null) {
            selectedAreaBorder = new int[2];
            selectedAreaBorder[0] = itemHeight * offset;
            selectedAreaBorder[1] = itemHeight * (offset + 1);
        }
        Log.d("viewasdlajsl2", " " + selectedAreaBorder[0] + " " + selectedAreaBorder[1]);
        return selectedAreaBorder;
    }

  /*  @Override
    public void setBackground(Drawable background) {

        int height = 0;
        int width =0;

        if (viewWidth == 0) {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            height = displaymetrics.heightPixels;
            width = displaymetrics.widthPixels;
            viewWidth = width;
        }

        if (null == paint) {
            paint = new Paint();
            paint.setColor(Color.parseColor("#83cde6"));
            //paint.setStrokeWidth(ABTextUtil.dip2px(context, 1f));
        }

        background = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                canvas.drawLine(viewWidth * 1 / 6, obtainSelectedAreaBorder()[0], viewWidth * 5 / 6, obtainSelectedAreaBorder()[0], paint);
                canvas.drawLine(viewWidth * 1 / 6, obtainSelectedAreaBorder()[1], viewWidth * 5 / 6, obtainSelectedAreaBorder()[1], paint);
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter cf) {

            }

            @Override
            public int getOpacity() {
                return 0;
            }
        };


        super.setBackground(background);

    }*/

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        //setBackground(null);
    }

    /**
     * 选中回调
     */
    private void onSeletedCallBack() {
        if (onWheelViewListener != null) {
            onWheelViewListener.onSelected(selectedIndex, items.get(selectedIndex));
        }

    }

    public void setSeletion(int position) {
        final int p = position;
        selectedIndex = p + offset;
        this.post(new Runnable() {
            @Override
            public void run() {
                WheelView.this.smoothScrollTo(0, p * itemHeight);
            }
        });

    }

    public String getSeletedItem() {
        return items.get(selectedIndex);
    }

    public int getSeletedIndex() {
        return selectedIndex - offset;
    }


    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            startScrollerTask();
        }
        return super.onTouchEvent(ev);
    }

    public OnWheelViewListener getOnWheelViewListener() {
        return onWheelViewListener;
    }

    public void setOnWheelViewListener(OnWheelViewListener onWheelViewListener) {
        this.onWheelViewListener = onWheelViewListener;
    }

    public static class OnWheelViewListener {
        public void onSelected(int selectedIndex, String item) {
        }

        ;
    }


}