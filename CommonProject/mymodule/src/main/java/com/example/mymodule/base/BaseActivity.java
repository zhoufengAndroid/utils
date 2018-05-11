package com.example.mymodule.base;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.classic.common.MultipleStatusView;
import com.example.mymodule.R;
import com.example.mymodule.utils.HttpCode;

public abstract class BaseActivity extends FragmentActivity implements UIBase {

    public View contentView;
    protected FrameLayout flTitleBar;
    private FrameLayout flContent;
    public MultipleStatusView multipleStatusView;
    public RelativeLayout rlBack;
    protected TextView tvTitle;
    protected ImageView ivIconRight;
    protected TextView tvRightText;

    private static final RelativeLayout.LayoutParams DEFAULT_LAYOUT_PARAMS =
            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        flTitleBar =  findViewById(R.id.common_fl_title_bar);
        flContent =  findViewById(R.id.common_fl_content);
        rlBack =  findViewById(R.id.common_rl_back);
        tvTitle =  findViewById(R.id.common_tv_title);
        ivIconRight =  findViewById(R.id.common_iv_icon_right);
        tvRightText =  findViewById(R.id.common_tv_right);
        multipleStatusView =  findViewById(R.id.common_multiple_status);
        multipleStatusView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        contentView = LayoutInflater.from(this).inflate(getContentView(), null, false);
        flContent.addView(contentView);
        initView(savedInstanceState);
        initListener();
        initData();
    }

    public void setProgress() {
        try {
            multipleStatusView.showLoading(R.layout.common_default_loading, DEFAULT_LAYOUT_PARAMS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 切换为loading状态
     */
    public void showLoading() {
        try {
            if (multipleStatusView != null)
                multipleStatusView.showLoading();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏loading状态，并展示内容布局
     */
    public void dismissLoading() {
        showContent();
    }

    public void dismissLoading(boolean showEmpty, int code) {
        if (HttpCode.EXCEPTION_NO_CONNECT == code || HttpCode.EXCEPTION_TIME_OUT == code) {
            showNoNetwork();
        }else if (showEmpty) {
            showEmpty();
        } else {
            showContent();
        }
    }

    /**
     * 展示内容布局
     */
    public void showContent() {
        try {
            if (multipleStatusView != null)
                multipleStatusView.showContent();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 展示空数据布局
     */
    public void showEmpty() {

        try {
            if (multipleStatusView != null)
                multipleStatusView.showEmpty();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 展示错误内容布局
     */
    public void showError() {
        try {
            if (multipleStatusView != null)
                multipleStatusView.showError();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 展示网络异常布局
     */
    public void showNoNetwork() {
        try {
            if (multipleStatusView != null) {
                int noNetWorkLayoutId = noNetWorkLayoutId();
                if (noNetWorkLayoutId == 0) {//默认布局
                    multipleStatusView.showNoNetwork();
                } else {
                    multipleStatusView.showNoNetwork(noNetWorkLayoutId, DEFAULT_LAYOUT_PARAMS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int noNetWorkLayoutId() {
        return 0;
    }

    /**
     * 设置返回按键监听
     *
     * @param listener
     */
    public void setBackListener(View.OnClickListener listener) {
        if (rlBack != null)
            rlBack.setOnClickListener(listener);
    }

    /**
     * 隐藏title bar
     */
    public void hideTitleBar() {
        if (flTitleBar != null) {
            flTitleBar.setVisibility(View.GONE);
            setContentDistance(0);
        }
    }

    /**
     * 隐藏title bar返回键
     */
    public void hideTitleBarBack() {
        if (rlBack != null) {
            rlBack.setVisibility(View.GONE);
        }
    }

    /**
     * 展示title bar
     *
     * @param contentBelowTitle ： 内容是否在体title bar后面
     */
    public void showTitleBar(boolean contentBelowTitle) {
        if (flTitleBar != null) {
            flTitleBar.setVisibility(View.VISIBLE);
            setContentDistance(contentBelowTitle ? flTitleBar.getMeasuredHeight() : 0);
        }
    }

    /**
     * 设置title bar 标题
     *
     * @param title ： 标题
     */
    public void setCommonTitle(String title) {
        if (tvTitle != null)
            tvTitle.setText(title);
    }

    /**
     * 设置自定义title bar
     *
     * @param view
     */
    public void setTitleBar(View view) {
        if (flTitleBar != null && view != null) {
            flTitleBar.removeAllViews();
            flTitleBar.addView(view);
        }
    }

    public void setRightText(String right) {
        if (tvRightText != null) {
            tvRightText.setVisibility(View.VISIBLE);
            tvRightText.setText(right);
        }
    }

    /**
     * 设置右边icon点击事件
     */
    public void setRightTextClick(View.OnClickListener listener) {
        if (tvRightText != null)
            tvRightText.setOnClickListener(listener);
    }

    /**
     * 设置右边icon图片
     *
     * @param res
     */
    public void setRightIcon(int res) {
        if (ivIconRight != null && res > 0) {
            ivIconRight.setVisibility(View.VISIBLE);
            ivIconRight.setImageResource(res);
        }
    }

    /**
     * 设置右边icon点击事件
     */
    public void setRightIconClick(View.OnClickListener listener) {
        if (ivIconRight != null)
            ivIconRight.setOnClickListener(listener);
    }

    /**
     * 设置内容距离顶部的距离
     *
     * @param margin
     */
    public void setContentDistance(int margin) {
        if (multipleStatusView == null)
            return;
        ViewGroup.LayoutParams params = multipleStatusView.getLayoutParams();
        if (params == null || !(params instanceof RelativeLayout.LayoutParams))
            return;
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) params;
        lp.topMargin = margin;
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
