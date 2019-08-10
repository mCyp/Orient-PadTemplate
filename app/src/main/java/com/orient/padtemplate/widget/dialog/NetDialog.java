package com.orient.padtemplate.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.orient.padtemplate.R;
import com.orient.padtemplate.common.Common;
import com.orient.padtemplate.utils.AppPrefUtils;

import net.qiujuer.genius.ui.widget.Button;

/**
 * 设置网段的Dialog
 *
 * @Auther WangJie on 2018/2/2.
 */

@SuppressWarnings({"RegExpRedundantEscape", "ConstantConditions"})
public class NetDialog extends Dialog {

    private EditText mIp;
    private EditText mPort;
    private Context mContext;

    public NetDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.net_dialog);

        initWidget();
        initWindow();
    }

    private void initWindow() {
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 0.5);
        lp.height = (int) (d.heightPixels * 0.35);
        dialogWindow.setAttributes(lp);
    }

    // 初始化控件
    private void initWidget() {
        // 确定按钮
        Button mSure = findViewById(R.id.btn_yes);
        // 取消按钮
        Button mNo = findViewById(R.id.btn_no);
        mIp =  findViewById(R.id.et_ip);
        mPort =  findViewById(R.id.et_port);

        String ipAddress = AppPrefUtils.getString(Common.Constant.SP_IP_ADDRESS);
        if(!TextUtils.isEmpty(ipAddress)){
            int startPos = ipAddress.indexOf(":")+3;
            int lastPos = ipAddress.lastIndexOf(":")+5;
            ipAddress = ipAddress.substring(startPos,lastPos);
            String[] ips = ipAddress.split("\\:");
            mIp.setText(ips[0]);
            mPort.setText(ips[1]);
        }

        mNo.setOnClickListener(v -> {
            dismiss();
        });

        mSure.setOnClickListener(v -> {
            String ip = mIp.getText().toString();
            String port = mPort.getText().toString();
            if(TextUtils.isEmpty(ip)|| TextUtils.isEmpty(port))
                return;
            String ipAddress1 = "http://"+ip+":"+port+"/OrientEDM/api/";
            AppPrefUtils.putString(ipAddress1,"");
            // TODO 重置Retrofit的网址
            dismiss();
        });
    }
}
