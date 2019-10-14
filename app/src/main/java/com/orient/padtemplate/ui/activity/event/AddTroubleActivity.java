package com.orient.padtemplate.ui.activity.event;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orient.padtemplate.R;
import com.orient.padtemplate.base.activity.BaseMvpActivity;
import com.orient.padtemplate.base.recyclerview.RecyclerAdapter;
import com.orient.padtemplate.common.Common;
import com.orient.padtemplate.contract.presenter.AddTroublePresenter;
import com.orient.padtemplate.contract.view.AddTroubleView;
import com.orient.padtemplate.core.data.db.Trouble;
import com.orient.padtemplate.ui.activity.video.VideoActivity;
import com.orient.padtemplate.ui.activity.video.VideoPlayerActivity;
import com.orient.padtemplate.utils.AppPrefUtils;
import com.orient.padtemplate.utils.IdUtils;
import com.orient.padtemplate.utils.PhotoUtils;
import com.orient.padtemplate.utils.UIUtils;
import com.orient.padtemplate.utils.audio.AudioPlayManager;
import com.orient.padtemplate.utils.audio.AudioRecordListenerImpl;
import com.orient.padtemplate.utils.audio.AudioRecordManager;
import com.orient.padtemplate.utils.audio.IAudioPlayListener;
import com.orient.padtemplate.utils.video.CompressListener;
import com.orient.padtemplate.utils.video.Compressor;
import com.orient.padtemplate.utils.video.InitListener;
import com.orient.photopagerview.utils.FileUtils;
import com.orient.photopagerview.widget.IPhotoPager;
import com.orient.photopagerview.widget.PhotoPagerViewProxy;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

@SuppressWarnings("unused")
/**
 * 添加故障的界面
 */
public class AddTroubleActivity extends BaseMvpActivity<AddTroublePresenter>
        implements AddTroubleView {

    private static final String TAG = AddTroubleActivity.class.getSimpleName();

    public static final String TROUBLE_ID = "TROUBLE_ID";
    public static final String EDIT = "EDIT";
    private static final int REQUEST_TAKE_PHOTO = 1;

    private static final int REQUEST_CODE_FOR_PERMISSIONS = 0;//
    private static final int REQUEST_CODE_FOR_RECORD_VIDEO = 1;//录制视频请求码
    public static final int RESULT_CODE_FOR_RECORD_VIDEO_SUCCEED = 2;//视频录制成功
    public static final int RESULT_CODE_FOR_RECORD_VIDEO_FAILED = 3;//视频录制出错
    public static final int RESULT_CODE_FOR_RECORD_VIDEO_CANCEL = 4;//取消录制
    public static final String INTENT_EXTRA_VIDEO_PATH = "intent_extra_video_path";//录制的视频路径

    // 注意：
    // 时间流程下，大多会绑定设备
    // 而设备大部分是通过扫描二维码查询的
    // 按需添加二维码扫描

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_position)
    TextView mPositionTv;
    @BindView(R.id.tv_submit)
    TextView mSubmit;
    @BindView(R.id.lay_content)
    View mRoot;
    @BindView(R.id.voice_container)
    View voiceContainer;
    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;
    // 录音按钮
    @BindView(R.id.btn_audio)
    View mVoice;
    @BindView(R.id.tv_time)
    TextView mVoiceTime;
    @BindView(R.id.iv_play)
    ImageView mAudioPlay;
    @BindView(R.id.et_content)
    EditText mDesc;

    // 相机
    @BindView(R.id.iv_camera)
    View mCamera;

    // 视频录制
    @BindView(R.id.iv_video)
    ImageView mVideoIv;
    @BindView(R.id.iv_play_start)
    ImageView mStartPlayer;
    @BindView(R.id.tv_video)
    TextView mVideoTv;
    @BindView(R.id.tv_video_compress)
    TextView mVideoCompressTv;


    // 文件的存放的地址
    @SuppressWarnings("FieldCanBeLocal")
    private File mAudioFile;
    // 录音的时长
    private int voiceTime;
    private String id;
    // 播放录音的路径
    private Uri targetDir;

    // 视频存放的地址
    private Compressor mCompressor;
    // 视频存储路径
    private String currentInputVideoPath = "";
    // 视频压缩的路径
    private String currentOutputVideoPath = "";
    // 视频时长 s
    private float videoTime = 0;
    // 命令
    private String cmd = "";

    // 照片存放的地址
    private String path;
    // 传递过来的Id查询的记录
    private Trouble trouble;
    // 照片的适配器
    private RecyclerAdapter<String> mAdapter;
    private List<Bitmap> bitmaps;
    // 是否可编辑
    private boolean isEdit;

    private SweetAlertDialog mSweetAlertDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.add_trouble_activity;
    }

    public static void show(Context context, String id) {
        show(context, id, true);
    }

    public static void show(Context context, String id, boolean isEdit) {
        Intent intent = new Intent(context, AddTroubleActivity.class);
        intent.putExtra(TROUBLE_ID, id);
        intent.putExtra(EDIT, isEdit);
        context.startActivity(intent);
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        // 创建Id
        id = IdUtils.createId();
        path = PhotoUtils.getTroubleStoragePath(id);
        if (!TextUtils.isEmpty(getIntent().getStringExtra(TROUBLE_ID))) {
            id = getIntent().getStringExtra(TROUBLE_ID);
            trouble = mPresenter.searchTroubleById(id);
            path = trouble.getPhotoDirectory();
            if(!TextUtils.isEmpty(trouble.getVideoPath()) && new File(trouble.getVideoPath()).exists()){
                currentInputVideoPath = trouble.getVideoPath();

                // 加载视频第一帧和设置播放按钮
                Glide.with(this)
                        .load(currentInputVideoPath)
                        .into(mVideoIv);
                mStartPlayer.setVisibility(View.VISIBLE);
            }
            mDesc.setText(trouble.getDesc());
            mPositionTv.setText(trouble.getPosition());
        }
        currentOutputVideoPath = PhotoUtils.getTroubleStoragePath(id) + "/out.mp4";

        // 设置Toolbar的状态
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        //setToolbarState(false, 0.5f);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<String>() {
            @Override
            public ViewHolder<String> onCreateViewHolder(View root, int ViewType) {
                return new TroubleViewHolder(root, AddTroubleActivity.this);
            }

            @Override
            public int getItemLayout(String s, int position) {
                return R.layout.add_trouble_album_recycle_item;
            }
        });

        isEdit = getIntent().getBooleanExtra(EDIT, true);
        if (!isEdit) {
            onEditDisable();
        }


        // 初始化视频录制
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

    /**
     * 不可编辑下界面设置
     */
    private void onEditDisable() {
        mDesc.setEnabled(false);
        mVoice.setEnabled(false);
        mCamera.setEnabled(false);
        mVideoIv.setEnabled(false);
        mVideoCompressTv.setEnabled(false);
    }

    // 设置更多的状态
    private void setToolbarState(boolean enable, float alpha) {
        mSubmit.setEnabled(enable);
        mSubmit.setAlpha(alpha);
    }

    @Override
    protected void initData() {
        super.initData();

        // 初始化音频文件和加载图片
        loadVoiceContainer();

        mAudioFile = new File(PhotoUtils.getTroubleStoragePath(id));
        // 設置最長的錄音的時間
        AudioRecordManager.getInstance(this).setMaxVoiceDuration(12);
        // 设置文件的存放地址
        AudioRecordManager.getInstance(this).setAudioSavePath(mAudioFile.getAbsolutePath());
        AudioRecordManager.getInstance(this).setAudioRecordListener(new AudioRecordListenerImpl() {

            private TextView mTimerTV;
            private TextView mStateTV;
            private ImageView mStateIV;
            private PopupWindow mRecordWindow;

            @Override
            public void initTipView() {
                @SuppressLint("InflateParams")
                View view = LayoutInflater.from(AddTroubleActivity.this).inflate(R.layout.audio_popup_view, null);
                mStateIV = view.findViewById(R.id.iv_state);
                mStateTV = view.findViewById(R.id.tv_state);
                mTimerTV = view.findViewById(R.id.tv_time);
                // 显示PopupWindow
                mRecordWindow = new PopupWindow(view, -1, -1);
                mRecordWindow.showAtLocation(mRoot, 17, 0, 0);
                mRecordWindow.setFocusable(true);
                mRecordWindow.setOutsideTouchable(false);
                mRecordWindow.setTouchable(false);
                //mVoice.setText("松开结束");
            }

            @Override
            public void setTimeoutTipView(int counter) {

                if (mRecordWindow != null) {
                    mStateIV.setVisibility(View.GONE);
                    mStateTV.setVisibility(View.VISIBLE);
                    mStateTV.setText(getString(R.string.voice_rec));
                    mStateTV.setBackgroundResource(R.drawable.audio_bg_voice_popup);
                    mTimerTV.setText(String.format("%s", Integer.toString(counter)));
                    mTimerTV.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void setRecordingTipView() {
                if (mRecordWindow != null) {
                    mStateIV.setVisibility(View.VISIBLE);
                    mStateIV.setImageResource(R.drawable.ic_volume_1);
                    mStateTV.setVisibility(View.VISIBLE);
                    mStateTV.setText(R.string.voice_rec);
                    mStateTV.setBackgroundResource(R.drawable.audio_bg_voice_popup);
                    mTimerTV.setVisibility(View.GONE);
                    // mVoice.setText("松开结束");
                }
            }


            @Override
            public void setAudioShortTipView() {

                if (mRecordWindow != null) {
                    mStateTV.setText("录音时间少于1秒");
                    mStateIV.setImageResource(R.drawable.ic_volume_wraning);
                }
            }

            @Override
            public void setCancelTipView() {
                if (this.mRecordWindow != null) {
                    this.mTimerTV.setVisibility(View.GONE);
                    this.mStateIV.setVisibility(View.VISIBLE);
                    this.mStateIV.setImageResource(R.drawable.ic_volume_cancel);
                    this.mStateTV.setVisibility(View.VISIBLE);
                    this.mStateTV.setText(R.string.voice_cancel);
                    this.mStateTV.setBackgroundResource(R.drawable.audio_bg_voice_style);
                    // mVoice.setText(R.string.voice_cancel);
                }
            }

            @Override
            public void destroyTipView() {
                if (this.mRecordWindow != null) {
                    this.mRecordWindow.dismiss();
                    this.mRecordWindow = null;
                    this.mStateIV = null;
                    this.mStateTV = null;
                    this.mTimerTV = null;
                    // mVoice.setText("按住说话");
                }
            }

            @Override
            public void onFinish(Uri audioPath, int duration) {
                voiceTime = duration;
                mVoiceTime.setText(duration + "s");
                if (targetDir != null) {
                    FileUtils.deleteFile(targetDir.getPath());
                }
                if (trouble != null) {
                    trouble.setAudioPath(audioPath.getPath());
                    trouble.setAudioTime(duration);
                    trouble.update();
                }
                targetDir = audioPath;
                mAudioPlay.setVisibility(View.VISIBLE);
                // mVoice.setText("按住说话");
                voiceContainer.setVisibility(View.VISIBLE);
            }


            @Override
            public void onAudioDBChanged(int db) {
                switch (db / 5) {
                    case 0:
                        this.mStateIV.setImageResource(R.drawable.ic_volume_1);
                        break;
                    case 1:
                        this.mStateIV.setImageResource(R.drawable.ic_volume_2);
                        break;
                    case 2:
                        this.mStateIV.setImageResource(R.drawable.ic_volume_3);
                        break;
                    case 3:
                        this.mStateIV.setImageResource(R.drawable.ic_volume_4);
                        break;
                    case 4:
                        this.mStateIV.setImageResource(R.drawable.ic_volume_5);
                        break;
                    case 5:
                        this.mStateIV.setImageResource(R.drawable.ic_volume_6);
                        break;
                    case 6:
                        this.mStateIV.setImageResource(R.drawable.ic_volume_7);
                        break;
                    default:
                        this.mStateIV.setImageResource(R.drawable.ic_volume_8);
                }
            }
        });
        mVoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        AudioRecordManager.getInstance(AddTroubleActivity.this).startRecord();
                        mVoice.getBackground().setLevel(2);
                        break;
                    case MotionEvent.ACTION_MOVE: {
                        if (isCancel(v, event)) {
                            AudioRecordManager.getInstance(AddTroubleActivity.this).willCancelRecord();
                        } else {
                            AudioRecordManager.getInstance(AddTroubleActivity.this).continueRecord();
                        }
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                        AudioRecordManager.getInstance(AddTroubleActivity.this).stopRecord();
                        mVoice.getBackground().setLevel(1);
                        break;
                }
                return true;
            }
        });

        bitmaps = PhotoUtils.getAlbumByPath(path, Common.Constant.PHOTO_NAME_SUFFIX,
                UIUtils.dip2px(this, 156), UIUtils.dip2px(this, 156));
    }

    /*
        是否取消
     */
    private boolean isCancel(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return event.getRawX() < location[0] || event.getRawX() > location[0] + view.getWidth()
                || event.getRawY() < location[1] - 40;
    }

    /*
        初始化声音
     */
    private void loadVoiceContainer() {
        if (trouble != null) {
            if (trouble.getAudioTime() == 0) {
                voiceContainer.setVisibility(View.GONE);
            } else {
                targetDir = Uri.fromFile(new File(trouble.getAudioPath()));
                voiceContainer.setVisibility(View.VISIBLE);
                mVoiceTime.setText(String.format("%.2f", trouble.getAudioTime()));
            }
        } else {
            voiceContainer.setVisibility(View.GONE);
        }
    }

    /**
     * 检查图片和音频
     */
    private void checkPhotoAndAudio() {
        if (targetDir != null) {
            String audioPath = targetDir.getPath();
            trouble.setAudioPath(audioPath);
            //trouble.setIsAudioSync(false);
            trouble.setAudioTime(voiceTime);
        } else {
            //trouble.setIsAudioSync(true);
        }
        trouble.setPhotoDirectory(path);
        int count = PhotoUtils.getAlbumCountByPath(path, Common.Constant.PHOTO_NAME_SUFFIX);
        if (count > 0) {
            //trouble.setIsPhotoSync(false);
        } else {
            //trouble.setIsPhotoSync(true);
        }

        // 确认视频路径
        if(!TextUtils.isEmpty(currentOutputVideoPath)&&new File(currentOutputVideoPath).exists()){
            trouble.setVideoPath(currentOutputVideoPath);
            trouble.setVideoTime(videoTime);
        }else if(!TextUtils.isEmpty(currentInputVideoPath)&&new File(currentInputVideoPath).exists()){
            trouble.setVideoPath(currentInputVideoPath);
            trouble.setVideoTime(videoTime);
        }
    }

    @OnClick(R.id.tv_submit)
    public void onSubmit() {

        String pos = mPositionTv.getText().toString();
        String desc = mDesc.getText().toString();

        if(TextUtils.isEmpty(pos) || TextUtils.isEmpty(desc)){
            showToast("位置或者描述信息不能为空！");
            return;
        }

        if (trouble == null) {
            String userId = AppPrefUtils.getString(Common.Constant.KEY_USER_ID);
            trouble = new Trouble();
            checkPhotoAndAudio();
            trouble.setId(id);
            trouble.setPosition(pos);
            trouble.setDesc(desc);
            trouble.setUserId(userId);
            trouble.setCreateDate(new Date());

            // 新增记录
            mPresenter.addTrouble(trouble);
        }
    }

    // 打开相机
    @OnClick(R.id.iv_camera)
    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = PhotoUtils.getImageUri(path);
        //path = uri.getPath();
        Log.e(TAG, "new path:" + path);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    // 点击语音进行播放
    @OnClick(R.id.voice_container)
    public void onVoicePlay() {
        AudioPlayManager.getInstance().stopPlay();

        AudioPlayManager.getInstance().startPlay(AddTroubleActivity.this, targetDir, new IAudioPlayListener() {
            @Override
            public void onStart(Uri var1) {
                if (mAudioPlay.getBackground() instanceof AnimationDrawable) {
                    AnimationDrawable animationDrawable = (AnimationDrawable) mAudioPlay.getBackground();
                    animationDrawable.start();
                }
            }

            @Override
            public void onStop(Uri var1) {
                if (mAudioPlay.getBackground() instanceof AnimationDrawable) {
                    AnimationDrawable animationDrawable = (AnimationDrawable) mAudioPlay.getBackground();
                    animationDrawable.stop();
                    animationDrawable.selectDrawable(0);
                }
            }

            @Override
            public void onComplete(Uri var1) {
                if (mAudioPlay.getBackground() instanceof AnimationDrawable) {
                    AnimationDrawable animationDrawable = (AnimationDrawable) mAudioPlay.getBackground();
                    animationDrawable.stop();
                    animationDrawable.selectDrawable(0);
                }
            }
        });
    }

    // 摄影开始
    @OnClick(R.id.tv_video)
    public void onVideoClick() {
        VideoActivity.startActivityForResult(this, REQUEST_CODE_FOR_RECORD_VIDEO,PhotoUtils.getTroubleStoragePath(id));
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
                        videoTime = (float) (Float.parseFloat(time) / 1000.00);
                    } catch (Exception e) {
                        e.printStackTrace();
                        videoTime = 0;
                    }
                    refreshCurrentPath();
                    showToast("video time:"+videoTime);
                }
            } else if (resultCode == RESULT_CODE_FOR_RECORD_VIDEO_FAILED) {
                //录制失败
                currentInputVideoPath = "";
            }
        }
    }

    /**
     * 执行压缩命令
     */
    private void refreshCurrentPath() {
        cmd = "-y -i " + currentInputVideoPath + " -strict -2 -vcodec libx264 -preset ultrafast " +
                "-crf 24 -acodec aac -ar 44100 -ac 2 -b:a 96k -s 480x320 -aspect 16:9 " + currentOutputVideoPath;

        Glide.with(this)
                .load(currentInputVideoPath)
                .into(mVideoIv);
        mStartPlayer.setVisibility(View.VISIBLE);
    }

    // 播放视频
    @OnClick(R.id.iv_video)
    public void onVideoPlay(){
        if(!TextUtils.isEmpty(currentOutputVideoPath) && new File(currentOutputVideoPath).exists()){
            Log.e(TAG,"播放压缩视频："+currentOutputVideoPath);
            VideoPlayerActivity.show(AddTroubleActivity.this,currentOutputVideoPath);
        }else if(!TextUtils.isEmpty(currentInputVideoPath)){
            Log.e(TAG,"播放视频："+currentInputVideoPath);
            VideoPlayerActivity.show(AddTroubleActivity.this,currentInputVideoPath);
        }else {
            showToast("没有视频!");
        }
    }

    @OnClick(R.id.tv_video_compress)
    public void onCompressVideo(){
        String command = cmd;
        if (TextUtils.isEmpty(command)) {
            Toast.makeText(AddTroubleActivity.this, getString(R.string.compree_please_input_command)
                    , Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(currentInputVideoPath)) {
            Toast.makeText(AddTroubleActivity.this, R.string.no_video_tips, Toast.LENGTH_SHORT).show();
        } else {
            File file = new File(currentOutputVideoPath);
            if (file.exists()) {
                file.delete();
            }
            execCommand(command);
        }
    }

    /**
     * 压缩指令
     * @param cmd 指令
     */
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
                    mSweetAlertDialog = new SweetAlertDialog(AddTroubleActivity.this,SweetAlertDialog.SUCCESS_TYPE);
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
                    mSweetAlertDialog = new SweetAlertDialog(AddTroubleActivity.this,SweetAlertDialog.ERROR_TYPE);
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


    @Override
    public void onAddResult(boolean result) {
        if (result) {
            // 结束掉当前的界面
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadImages();
    }

    private void loadImages() {
        File photoFile = new File(path);
        Log.e(TAG, "onStart");
        if (photoFile.exists()) {
            bitmaps = PhotoUtils.getAlbumByPath(path, Common.Constant.PHOTO_NAME_SUFFIX,
                    UIUtils.dip2px(this, 156), UIUtils.dip2px(this, 156));
            List<String> paths = FileUtils.getPhotoPaths(path);
            Log.e(TAG, "path size:" + paths.size());
            mAdapter.replace(paths);
        }
    }

    class TroubleViewHolder extends RecyclerAdapter.ViewHolder<String> {

        private String mPath;
        private Context mContext;

        @BindView(R.id.iv_content)
        ImageView mContent;

        @BindView(R.id.iv_delete)
        ImageView mDelete;

        public TroubleViewHolder(View itemView, Context context) {
            super(itemView);
            mContext = context;
        }

        @OnClick(R.id.iv_delete)
        public void onDelete() {
            final SweetAlertDialog dialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
            dialog.setTitleText("删除")
                    .setContentText("确定要删除该张图片吗？")
                    .setConfirmButton("删除", (sweetAlertDialog) -> {
                        FileUtils.deleteFile(mPath);
                        loadImages();
                        dialog.dismiss();
                    }).setCancelText("取消");
            dialog.show();
        }

        @OnClick(R.id.iv_content)
        public void onShowImage() {
            List<String> paths = FileUtils.getPhotoPaths(path);
            ArrayList<Bitmap> bitmaps = new ArrayList<>(FileUtils.getAlbumByPath(path, Common.Constant.PHOTO_NAME_SUFFIX, AddTroubleActivity.this));

            IPhotoPager pageView = new PhotoPagerViewProxy.Builder(AddTroubleActivity.this)
                    .addBitmaps(bitmaps)
                    .showAnimation(true)
                    .showDelete(false)
                    .setAnimationType(PhotoPagerViewProxy.ANIMATION_TRANSLATION)
                    .setStartPosition(getAdapterPosition())
                    .create();
            pageView.show();
        }

        @Override
        protected void onBind(String s) {
            mPath = s;
            //GlideUtils.loadUrl(mContext, mPath, mContent);
            mContent.setImageBitmap(bitmaps.get(getAdapterPosition()));
            if (!isEdit)
                mDelete.setVisibility(View.INVISIBLE);
        }
    }
}
