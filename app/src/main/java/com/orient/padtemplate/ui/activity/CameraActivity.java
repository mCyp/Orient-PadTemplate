package com.orient.padtemplate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orient.padtemplate.R;
import com.orient.padtemplate.base.activity.BaseActivity;
import com.orient.padtemplate.base.app.App;
import com.orient.padtemplate.ui.activity.video.VideoActivity;
import com.orient.padtemplate.ui.activity.video.VideoPlayerActivity;
import com.orient.padtemplate.utils.GlideUtils;
import com.orient.padtemplate.utils.video.CompressListener;
import com.orient.padtemplate.utils.video.Compressor;
import com.orient.padtemplate.utils.video.InitListener;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class CameraActivity extends BaseActivity {

    private String TAG = CameraActivity.class.getName();

    @BindView(R.id.iv_video)
    ImageView mVideoIv;
    @BindView(R.id.iv_play_start)
    ImageView mStartPlayer;

    private SweetAlertDialog mSweetAlertDialog;

    private Compressor mCompressor;
    // 视频存储路径
    private String currentInputVideoPath = "";
    // 视频压缩的路径
    private String currentOutputVideoPath = App.getInstance().getExternalCacheDir().getAbsolutePath() + "/video/out.mp4";
    // 视频时长 s
    private Double videoLength = 0.00;
    // 命令
    private String cmd = "";

    private static final int REQUEST_CODE_FOR_PERMISSIONS = 0;//
    private static final int REQUEST_CODE_FOR_RECORD_VIDEO = 1;//录制视频请求码
    public static final int RESULT_CODE_FOR_RECORD_VIDEO_SUCCEED = 2;//视频录制成功
    public static final int RESULT_CODE_FOR_RECORD_VIDEO_FAILED = 3;//视频录制出错
    public static final int RESULT_CODE_FOR_RECORD_VIDEO_CANCEL = 4;//取消录制
    public static final String INTENT_EXTRA_VIDEO_PATH = "intent_extra_video_path";//录制的视频路径

    public static void show(Context context) {
        Intent intent = new Intent(context, CameraActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_camera;
    }


    @Override
    protected void initWidget() {
        super.initWidget();

        mCompressor = new Compressor(this);
        mCompressor.loadBinary(new InitListener() {
            @Override
            public void onLoadSuccess() {

            }

            @Override
            public void onLoadFail(String reason) {

            }
        });
    }

    // 摄影开始
    @OnClick(R.id.tv_video)
    public void onVideoClick() {
        VideoActivity.startActivityForResult(this, REQUEST_CODE_FOR_RECORD_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FOR_RECORD_VIDEO) {
            //录制视频
            if (resultCode == RESULT_CODE_FOR_RECORD_VIDEO_SUCCEED) {
                //录制成功
                String videoPath = data.getStringExtra(INTENT_EXTRA_VIDEO_PATH);
                if (!TextUtils.isEmpty(videoPath)) {
                    currentInputVideoPath = videoPath;
                    MediaMetadataRetriever retr = new MediaMetadataRetriever();
                    retr.setDataSource(currentInputVideoPath);
                    String time = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);//获取视频时长
                    //7680
                    try {
                        videoLength = Double.parseDouble(time) / 1000.00;
                    } catch (Exception e) {
                        e.printStackTrace();
                        videoLength = 0.00;
                    }
                    refreshCurrentPath();
                    showToast("video time:"+videoLength);
                }
            } else if (resultCode == RESULT_CODE_FOR_RECORD_VIDEO_FAILED) {
                //录制失败
                currentInputVideoPath = "";
            }
        }
    }

    private void refreshCurrentPath() {
        cmd = "-y -i " + currentInputVideoPath + " -strict -2 -vcodec libx264 -preset ultrafast " +
                "-crf 24 -acodec aac -ar 44100 -ac 2 -b:a 96k -s 480x320 -aspect 16:9 " + currentOutputVideoPath;

        Glide.with(this)
                .load(currentInputVideoPath)
                .into(mVideoIv);
        mStartPlayer.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.iv_video)
    public void onVideoPlay(){
        if(!TextUtils.isEmpty(currentOutputVideoPath) && new File(currentOutputVideoPath).exists()){
            Log.e(TAG,"播放压缩视频："+currentOutputVideoPath);
            VideoPlayerActivity.show(CameraActivity.this,currentOutputVideoPath);
        }else if(!TextUtils.isEmpty(currentInputVideoPath)){
            Log.e(TAG,"播放视频："+currentInputVideoPath);
            VideoPlayerActivity.show(CameraActivity.this,currentInputVideoPath);
        }else {
            showToast("没有视频!");
        }
    }

    @OnClick(R.id.tv_video_compress)
    public void onCompressVideo(){
        String command = cmd;
        if (TextUtils.isEmpty(command)) {
            Toast.makeText(CameraActivity.this, getString(R.string.compree_please_input_command)
                    , Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(currentInputVideoPath)) {
            Toast.makeText(CameraActivity.this, R.string.no_video_tips, Toast.LENGTH_SHORT).show();
        } else {
            File file = new File(currentOutputVideoPath);
            if (file.exists()) {
                file.delete();
            }
            execCommand(command);
        }
    }

    private void execCommand(String cmd) {
        File mFile = new File(currentOutputVideoPath);
        if (mFile.exists()) {
            mFile.delete();
        }
        mSweetAlertDialog = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE);
        mSweetAlertDialog.setCancelable(false);
        mSweetAlertDialog.setCanceledOnTouchOutside(false);
        mSweetAlertDialog.setTitleText("压缩中")
                .show();
        mCompressor.execCommand(cmd, new CompressListener() {
            @Override
            public void onExecSuccess(String message) {
                Toast.makeText(getApplicationContext(), R.string.compress_succeed, Toast.LENGTH_SHORT).show();
                String result = getString(R.string.compress_result_input_output, currentInputVideoPath
                        , getFileSize(currentInputVideoPath), currentOutputVideoPath, getFileSize(currentOutputVideoPath));
                if(mSweetAlertDialog != null){
                    mSweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    mSweetAlertDialog.setTitleText(getString(R.string.compress_succeed))
                            .setContentText(result)
                            .setConfirmButton("确认", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    mSweetAlertDialog.dismiss();
                                }
                            });
                    mSweetAlertDialog.show();
                }else {
                    mSweetAlertDialog = new SweetAlertDialog(CameraActivity.this,SweetAlertDialog.SUCCESS_TYPE);
                    mSweetAlertDialog.setTitleText(getString(R.string.compress_succeed))
                            .setContentText(result)
                            .setConfirmButton("确认", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    mSweetAlertDialog.dismiss();
                                }
                            });
                    mSweetAlertDialog.show();
                }

            }

            @Override
            public void onExecFail(String reason) {
                if(mSweetAlertDialog != null){
                    mSweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    mSweetAlertDialog.setTitleText(getString(R.string.compress_failed))
                            .setConfirmButton("确认", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    mSweetAlertDialog.dismiss();
                                }
                            });
                    mSweetAlertDialog.show();

                }else {
                    mSweetAlertDialog = new SweetAlertDialog(CameraActivity.this,SweetAlertDialog.ERROR_TYPE);
                    mSweetAlertDialog.setTitleText(getString(R.string.compress_failed))
                            .setConfirmButton("确认", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    mSweetAlertDialog.dismiss();
                                }
                            });
                    mSweetAlertDialog.show();
                }
            }

            @Override
            public void onExecProgress(String message) {
                if(mSweetAlertDialog != null){
                    mSweetAlertDialog.setContentText(message);
                }
            }
        });
    }

    private String getFileSize(String path) {
        File f = new File(path);
        if (!f.exists()) {
            return "0 MB";
        } else {
            long size = f.length();
            return (size / 1024f) / 1024f + "MB";
        }
    }
}
