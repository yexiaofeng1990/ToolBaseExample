package cn.com.toolbase.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.com.toolbase.R;
import cn.com.toolbase.bean.BottomMoldBean;


/**
 * 底部弹出类型菜单选项
 * Created by Mr.ye on 2017/11/11.
 */

public class BottomMoldView extends Dialog {
    private NextNodeAdapter mAdapter;
    private View mRootView;
    private Animation mShowAnimation;
    private Animation mDismissAnimation;
    private List<BottomMoldBean> mList;
    private TextView titleTv;//标题
    private ImageButton btnCancel;//底部删除按钮
    private boolean isDismissing;
    private MaxListView mListView;
    //-------颜色配置-------
    private String nodeColor;
    private int itemResource = R.drawable.item_default_bg;
    private boolean isOpen = false;

    public BottomMoldView(Context context) {
        super(context, R.style.BottomDialog);
        initView(context);
    }

    @SuppressLint("CutPasteId")
    private void initView(Context context) {
        mRootView = View.inflate(context, R.layout.layout_bottom_list_mold_view, null);
        mListView = (MaxListView) mRootView.findViewById(R.id.list_view);
        titleTv = (TextView) mRootView.findViewById(R.id.tv_title);
        btnCancel = (ImageButton) mRootView.findViewById(R.id.btn_cancel);

        mAdapter = new NextNodeAdapter();
        mListView.setAdapter(mAdapter);
        setContentView(mRootView);
        initAnim(context);
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        assert wm != null;
        wm.getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;// 屏幕高度（像素）
        mListView.setListViewHeight(height / 2);
        setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initAnim(Context context) {
        mShowAnimation = AnimationUtils.loadAnimation(context,
                R.anim.popshow_anim);
        mDismissAnimation = AnimationUtils.loadAnimation(context,
                R.anim.pophidden_anim);
        mDismissAnimation
                .setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        dismissMe();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
    }

    public void toggle() {
        if (isShowing()) {
            dismiss();
        } else {
            show();
        }
    }

    @Override
    public void dismiss() {
        if (isDismissing) {
            return;
        }
        isDismissing = true;
        mRootView.startAnimation(mDismissAnimation);
    }

    private void dismissMe() {
        super.dismiss();
        isDismissing = false;
    }

    @Override
    public void show() {
        super.show();
        //宽度沾满
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM); //可设置dialog的位置
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        mAdapter.notifyDataSetChanged();
        mRootView.startAnimation(mShowAnimation);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public interface Listner {
        void onItemSelected(String name, Object vluse);
    }

    private Listner mListener;

    public Listner getListener() {
        return mListener;
    }

    public void setListener(Listner Listener) {
        mListener = Listener;
    }

    class NextNodeAdapter extends BaseAdapter {
        public NextNodeAdapter() {
        }


        @Override
        public int getCount() {
            if (mList == null) {
                return 0;
            }
            return mList.size();
        }


        @Override
        public BottomMoldBean getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                convertView = layoutInflater.inflate(R.layout.item_bottom_list, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.mLlPressed = (LinearLayout) convertView.findViewById(R.id.ll_pressed);
                viewHolder.nodeTextView = (TextView) convertView.findViewById(R.id.tv_node);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            BottomMoldBean item = getItem(position);
            if (!TextUtils.isEmpty(nodeColor)) {
                viewHolder.nodeTextView.setTextColor(Color.parseColor(nodeColor));
            }
            if (isOpen) {
                viewHolder.nodeTextView.setBackgroundResource(itemResource);
            }
            viewHolder.nodeTextView.setText(item.getName());
            viewHolder.mLlPressed.setTag(item);
            viewHolder.mLlPressed.setOnClickListener(clickListener);
            return convertView;
        }

        class ViewHolder {
            LinearLayout mLlPressed;
            TextView nodeTextView;
        }
    }

    public void setList(List<BottomMoldBean> mList) {
        this.mList = mList;
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 添加标题
     */
    public void setTitle(String title) {
        titleTv.setText(title);
    }

    /**
     * 添加标题文字颜色
     */
    public void setTitleColor(String color) {
        titleTv.setTextColor(Color.parseColor(color));
    }

    /**
     * 是否显示底部删除按钮
     */
    public void isShowCancel(boolean isVisibility) {
        btnCancel.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置删除按钮图标
     */
    public void setCancelIcon(int resource) {
        btnCancel.setImageResource(resource);
    }

    /**
     * 设置删除按钮图标(显示，和图标)
     */
    public void isShowCancel(boolean isVisibility, int resource) {
        btnCancel.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
        btnCancel.setImageResource(resource);
    }
    //----------------------设置Adapter的Item------------------------------------

    /**
     * 样式(打开item按钮样式)
     */
    public void openItemBackground(boolean open) {
        this.isOpen = open;
        mListView.setDivider(new BitmapDrawable());
        mListView.setDividerHeight(8);
        mListView.setSelector(new BitmapDrawable());
        findViewById(R.id.ll_line).setVisibility(View.GONE);
    }

    /**
     * 样式(打开并设置item按钮样式)
     */
    public void openItemBackground(boolean open, int resource) {
        this.isOpen = open;
        this.itemResource = resource;
        mListView.setDivider(new BitmapDrawable());
        mListView.setDividerHeight(8);
        mListView.setSelector(new BitmapDrawable());
        findViewById(R.id.ll_line).setVisibility(View.GONE);
    }

    /**
     * 样式(打开并设置item按钮样式)
     */
    public void openItemBackground(boolean open, String color) {
        this.isOpen = open;
        this.nodeColor = color;
        mListView.setDivider(new BitmapDrawable());
        mListView.setDividerHeight(8);
        mListView.setSelector(new BitmapDrawable());
        findViewById(R.id.ll_line).setVisibility(View.GONE);
    }

    /**
     * 样式(打开并设置item按钮样式)
     */
    public void openItemBackground(boolean open, String color, int resource) {
        this.isOpen = open;
        this.nodeColor = color;
        this.itemResource = resource;
        mListView.setDivider(new BitmapDrawable());
        mListView.setDividerHeight(8);
        mListView.setSelector(new BitmapDrawable());
        findViewById(R.id.ll_line).setVisibility(View.GONE);
    }

    /**
     * 设置文字颜色
     */
    public void setItemTextColor(String color) {
        this.nodeColor = color;
    }


    View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            BottomMoldBean data = (BottomMoldBean) v.getTag();
            if (mListener != null) {
                mListener.onItemSelected(data.getName(), data.getValue());
            }
            dismiss();
        }
    };

}
