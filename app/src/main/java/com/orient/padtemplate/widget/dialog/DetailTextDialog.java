package com.orient.padtemplate.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.orient.padtemplate.R;
import com.orient.padtemplate.base.recyclerview.RecyclerAdapter;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 自定义设置的Dialog
 *
 * @Auther WangJie on 2018/2/2.
 */

@SuppressWarnings({"RegExpRedundantEscape", "ConstantConditions"})
public class DetailTextDialog extends Dialog {

    private Context mContext;
    private String text;

    public DetailTextDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    public void setContent(String str){
        this.text = str;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_text_dialog);

        initWidget();
        initWindow();
    }

    // 初始化控件
    private void initWidget() {
        TextView mContent = findViewById(R.id.tv_content);

        mContent.setText(text);
    }

    private void initWindow() {
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 0.35);
        lp.height = (int) (d.heightPixels * 0.5);
        dialogWindow.setAttributes(lp);
    }

}
