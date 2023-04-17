package com.zhaoxiao.zhiying.fragment.study;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xui.adapter.simple.XUISimpleAdapter;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.widget.popupwindow.popup.XUIListPopup;
import com.xuexiang.xui.widget.popupwindow.popup.XUIPopup;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.adapter.study.SentenceAdapter;
import com.zhaoxiao.zhiying.api.StudyService;
import com.zhaoxiao.zhiying.entity.study.ArticleDetail;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.Sentence;
import com.zhaoxiao.zhiying.fragment.BaseFragment;
import com.zhaoxiao.zhiying.service.MediaService;
import com.zhaoxiao.zhiying.util.LinearTopSmoothScroller;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.util.TopSmoothScroller;
import com.zhaoxiao.zhiying.view.MyVideoView;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.BIND_AUTO_CREATE;

public class ListenFragment extends BaseFragment {

    //    @BindView(R.id.tv_title)
//    TextView tvTitle;
//    @BindView(R.id.tv_add_time)
//    TextView tvAddTime;
    @BindView(R.id.rv)
    RecyclerView rv;
    //    @BindView(R.id.srl)
//    SmartRefreshLayout srl;
    @BindView(R.id.sb)
    SeekBar sb;
    @BindView(R.id.iv_translation)
    ImageView ivTranslation;
    @BindView(R.id.iv_previous)
    ImageView ivPrevious;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.iv_next)
    ImageView ivNext;
    @BindView(R.id.tv_speed)
    TextView tvSpeed;
    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.fl_video)
    FrameLayout flVideo;
//    @BindView(R.id.nsv)
//    NestedScrollView nsv;

    private int articleId;

    private StudyService studyService;

    private ArticleDetail articleDetail;
    private List<String> paragraphList;
    private SentenceAdapter sentenceAdapter;
    private LinearLayoutManager linearLayoutManager;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private static final String TAG = "ListenFragment";
    private MediaService.MyBinder mMyBinder;
    //进度条下面的当前进度文字，将毫秒化为m:ss格式
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");
    //“绑定”服务的intent
    Intent mediaServiceIntent;
    //记录服务是否被解绑，默认没有
    private boolean isBind = false;

    public static final String ACTION_MUSIC_INIT = "ACTION_MUSIC_INIT";
    public static final String ACTION_MUSIC_START = "ACTION_MUSIC_START";
    public static final String ACTION_MUSIC_STOP = "ACTION_MUSIC_STOP";
    public static final String ACTION_MUSIC_COMPLETE = "ACTION_MUSIC_COMPLETE";
    public static final String ACTION_VIDEO_INIT = "ACTION_VIDEO_INIT";
    public static final String ACTION_VIDEO_START = "ACTION_VIDEO_START";
    public static final String ACTION_VIDEO_STOP = "ACTION_VIDEO_STOP";
    public static final String ACTION_VIDEO_COMPLETE = "ACTION_VIDEO_COMPLETE";
    private MusicReceiver musicReceiver;

    private int position;//当前播放的句子
    private List<Sentence> sentenceList;

    private TopSmoothScroller mScroller;
    private LinearTopSmoothScroller smoothScroller;

    //视频播放
    private VideoFragment videoFragment;
    private boolean isVideo;

    //倍速
    private XUIListPopup mListPopup;

    //点击阅读模式暂停音频
    public void readPause() {
        if(!isVideo){
            mMyBinder.pauseMusic();
            ivPlay.setImageResource(R.drawable.play1_yellow);
            mHandler.removeCallbacks(mRunnable);
        } else {
            videoFragment.pause();
            ivPlay.setImageResource(R.drawable.play1_yellow);
            mHandler.removeCallbacks(mRunnableVideo);
        }
    }

    public ListenFragment() {
    }

    public static ListenFragment newInstance(int articleId) {
        ListenFragment fragment = new ListenFragment();
        fragment.articleId = articleId;
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_listen;
    }

    @Override
    protected void initData() {
        videoFragment = (VideoFragment) getChildFragmentManager().findFragmentById(R.id.fl_video);
        System.out.println(videoFragment);
//        flVideo.setOnClickListener(v -> navigateTo(VideoTestActivity.class));
        mScroller = new TopSmoothScroller(getContext());
        smoothScroller = new LinearTopSmoothScroller(getContext(), true);

        //刷新和加载
//        srl.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
//                getArticleDetail();
//            }
//        });

        studyService = (StudyService) getService(StudyService.class);

        getArticleDetail();
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        sentenceAdapter = new SentenceAdapter(getContext());
        rv.setAdapter(sentenceAdapter);
        sentenceAdapter.setOnItemClickListener(sentenceId -> showToast(sentenceId.toString()));

//        rv.setNestedScrollingEnabled(false);
//        rv.setFocusable(false);
//        nsv.fullScroll(View.FOCUS_DOWN);
    }

    private void getArticleDetail() {
        Call<Data<ArticleDetail>> articleDetailCall = studyService.getArticleDetail(articleId);
        articleDetailCall.enqueue(new Callback<Data<ArticleDetail>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<Data<ArticleDetail>> call, Response<Data<ArticleDetail>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    articleDetail = response.body().getData();
                    sentenceList = articleDetail.getSentenceList();
//                    tvTitle.setText(articleDetail.getTitle());
//                    tvAddTime.setText(StringUtils.formatDate2English(articleDetail.getAddTime()));
                    sentenceAdapter.setTitle(articleDetail.getTitle());
                    sentenceAdapter.setAddTime(StringUtils.formatDate2English(articleDetail.getAddTime()));
                    sentenceAdapter.setList(sentenceList);
                    sentenceAdapter.notifyDataSetChanged();
//                    srl.finishRefresh();

                    isVideo= articleDetail.getVideo() != null && !articleDetail.getVideo().equals("");

                    if(isVideo){
                        flVideo.setVisibility(View.VISIBLE);
                        //注册广播
                        musicReceiver = new MusicReceiver();
                        IntentFilter intentFilter = new IntentFilter();
                        intentFilter.addAction(ACTION_VIDEO_INIT);
                        intentFilter.addAction(ACTION_VIDEO_START);
                        intentFilter.addAction(ACTION_VIDEO_STOP);
                        intentFilter.addAction(ACTION_VIDEO_COMPLETE);
                        getContext().registerReceiver(musicReceiver, intentFilter);
                        //播放视频
                        videoFragment.play(articleDetail.getVideo());
//                        XToastUtils.toast("开始播放");
//                        mHandler.post(mRunnable);
                    }else {
                        flVideo.setVisibility(View.GONE);
                        //注册广播
                        musicReceiver = new MusicReceiver();
                        IntentFilter intentFilter = new IntentFilter();
                        intentFilter.addAction(ACTION_MUSIC_INIT);
                        intentFilter.addAction(ACTION_MUSIC_START);
                        intentFilter.addAction(ACTION_MUSIC_STOP);
                        intentFilter.addAction(ACTION_MUSIC_COMPLETE);
                        getContext().registerReceiver(musicReceiver, intentFilter);
                        //播放音频
                        mediaServiceIntent = new Intent(getContext(), MediaService.class);
                        mediaServiceIntent.putExtra("audio", articleDetail.getAudio());
                        mediaServiceIntent.putExtra("id", articleDetail.getId());
                        getContext().startForegroundService(mediaServiceIntent);
                        getContext().bindService(mediaServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    //                    isBind=true;
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<ArticleDetail>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
//        Intent intent = new Intent(getContext(), MediaService.class);
//        getContext().bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
//        mHandler.removeCallbacks(mRunnable);
//        getContext().unbindService(mServiceConnection);
//        isBind = false;
        super.onStop();
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMyBinder = (MediaService.MyBinder) service;
//            mMediaService = ((MediaService.MyBinder) service).getInstance();
            isBind = true;

            Log.d(TAG, "Service与Activity已连接");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMyBinder = null;
            isBind = false;
        }
    };

    @OnClick({R.id.iv_translation, R.id.iv_previous, R.id.iv_play, R.id.iv_next, R.id.tv_speed})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_translation:
                System.out.println(videoFragment.getCurrentPlayState());
                break;
            case R.id.iv_previous:
                break;
            case R.id.iv_play:
                if(isVideo){
                    if(videoFragment.isPlaying()){
                        videoFragment.pause();
//                        ivPlay.setImageResource(R.drawable.play1_yellow);
//                        mHandler.removeCallbacks(mRunnableVideo);
                    } else if(videoFragment.getCurrentPlayState() == MyVideoView.STATE_PAUSED){
                        System.out.println(videoFragment.getCurrentPlayState());
                        videoFragment.resume();
//                        ivPlay.setImageResource(R.drawable.pause1_yellow);
//                        mHandler.post(mRunnableVideo);
                    } else if(videoFragment.getCurrentPlayState() == MyVideoView.STATE_PLAYBACK_COMPLETED){
                        System.out.println(videoFragment.getCurrentPlayState());
//                        videoFragment.release();
//                        videoFragment.play();
                        videoFragment.replay();
//                        ivPlay.setImageResource(R.drawable.pause1_yellow);
//                        mHandler.post(mRunnableVideo);
                    }
                    break;
                }
                if (mMyBinder.isPlaying()) {
                    mMyBinder.pauseMusic();
                    ivPlay.setImageResource(R.drawable.play1_yellow);
                    mHandler.removeCallbacks(mRunnable);
                } else {
                    mMyBinder.playMusic();
                    ivPlay.setImageResource(R.drawable.pause1_yellow);
                    mHandler.post(mRunnable);
                }
                break;
            case R.id.iv_next:
                break;
            case R.id.tv_speed:
                initListPopupIfNeed();
                mListPopup.setAnimStyle(XUIPopup.ANIM_GROW_FROM_CENTER);
                mListPopup.setPreferredDirection(XUIPopup.DIRECTION_TOP);
                mListPopup.show(tvSpeed);
                break;
        }
    }

    //倍速播放弹窗
    private void initListPopupIfNeed() {
        if (mListPopup == null) {

            String[] listItems = new String[]{
//                    "0.25x",
                    "0.5x",
                    "0.75x",
                    "1.0x",
                    "1.25x",
                    "1.5x",
                    "2.0x",
//                    "3.0x",
            };
            float[] listItemsFolat = new float[]{
//                    0.25f,
                    0.5f,
                    0.75f,
                    1.0f,
                    1.25f,
                    1.5f,
                    2.0f,
//                    3.0f,
            };

            XUISimpleAdapter adapter = XUISimpleAdapter.create(getContext(), listItems);
            mListPopup = new XUIListPopup(getContext(), adapter);
            mListPopup.create(DensityUtils.dp2px(getContext(), 100), ViewGroup.LayoutParams.WRAP_CONTENT, (adapterView, view, i, l) -> {
                if (isVideo) {
                    videoFragment.changePlayerSpeed(listItemsFolat[i]);
                    tvSpeed.setText(listItems[i]);
                } else {
                    mMyBinder.changePlayerSpeed(listItemsFolat[i]);
                    tvSpeed.setText(listItems[i]);
//                    tvSpeed.setText(mMyBinder.getSpeed()+"x");
                }
                mListPopup.dismiss();
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!isVideo) {
            unbind();
        } else {
//            mHandler.removeCallbacks(mRunnableVideo);
            System.out.println("onDestroy ---> mRunnableVideo");
        }
        getContext().unregisterReceiver(musicReceiver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        remove();
        System.out.println("onDestroyView ---> mRunnableVideo");
    }

    public void removeCallbacks() {
        mHandler.removeCallbacks(mRunnableVideo);
    }

    //判断服务是否绑定
    private void unbind() {
        //如果绑定了
        if (isBind) {
            //我们的handler发送是定时1000s发送的，如果不关闭，MediaPlayer release掉了还在获取getCurrentPosition就会爆IllegalStateException错误
            mHandler.removeCallbacks(mRunnable);

//            if (mMyBinder != null) {
//                mMyBinder.closeMedia();
//            }
            getContext().unbindService(mServiceConnection);
            isBind = false;
        }
    }

    /**
     * 更新ui的runnable
     */
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            int playPosition = mMyBinder.getPlayPosition();
            sb.setProgress(playPosition);
            tvProgress.setText(time.format(playPosition));

            locate(position, playPosition);

            //只判断这一句和下一句的方法
//            if (playPosition >= sentenceList.get(position + 1).getNode()) {
//                position++;
//                XToastUtils.toast("定位到"+position);
//            }

            mHandler.postDelayed(mRunnable, 500);
        }
    };
    private final Runnable mRunnableVideo = new Runnable() {
        @Override
        public void run() {
            int playPosition = (int) videoFragment.getCurrentPosition();
            sb.setProgress(playPosition);
            tvProgress.setText(time.format(playPosition));

            locate(position, playPosition);

            //只判断这一句和下一句的方法
//            if (playPosition >= sentenceList.get(position + 1).getNode()) {
//                position++;
//                XToastUtils.toast("定位到"+position);
//            }

            mHandler.postDelayed(mRunnableVideo, 500);
        }
    };

    //自定义广播
    public class MusicReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            UIControl(intent.getAction());
        }
    }

    //UI控制
    private void UIControl(String state) {
        switch (state) {
            case ACTION_MUSIC_INIT:
//                position = 0;
                if (mMyBinder != null) {
                    //获取倍数
                    tvSpeed.setText(mMyBinder.getSpeed()+"x");

                    if (mMyBinder.isPlaying()) {
                        ivPlay.setImageResource(R.drawable.pause1_yellow);
                    } else {
                        ivPlay.setImageResource(R.drawable.play1_yellow);
                    }
                    tvTotal.setText(time.format(mMyBinder.getDuration()));
                    sb.setMax(mMyBinder.getDuration());

                    sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            //这里很重要，如果不判断是否来自用户操作进度条，会不断执行下面语句块里面的逻辑，然后就会卡顿卡顿
                            if (fromUser) {
                                mMyBinder.seekToPositon(seekBar.getProgress());
//                    mMediaService.mMediaPlayer.seekTo(seekBar.getProgress());
                                locate(0, progress);
                            }
                            //进当滑动条到末端时，设置播放图标
                            if (progress == seekBar.getMax()/* || !mMyBinder.isPlaying()*/) {
                                ivPlay.setImageResource(R.drawable.play1_yellow);
//                                mMyBinder.seekToPositon(mMyBinder.getDuration());
//                                    mMyBinder.resetMusic();
//                                position = 0;
                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });

                    mHandler.post(mRunnable);
                }
                break;
            case ACTION_MUSIC_START:
                locate(position, mMyBinder.getPlayPosition());
                break;
            case ACTION_MUSIC_COMPLETE:
                ivPlay.setImageResource(R.drawable.play1_yellow);
                mHandler.removeCallbacks(mRunnable);
                break;
            case ACTION_VIDEO_INIT:
                //获取倍数
                tvSpeed.setText(videoFragment.getSpeed()+"x");

                System.out.println(videoFragment);
                if (videoFragment != null) {
                    if (videoFragment.isPlaying()) {
                        ivPlay.setImageResource(R.drawable.pause1_yellow);
                    } else {
                        ivPlay.setImageResource(R.drawable.play1_yellow);
                    }
                    tvTotal.setText(time.format(videoFragment.getDuration()));
                    sb.setMax((int) videoFragment.getDuration());

                    sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            //这里很重要，如果不判断是否来自用户操作进度条，会不断执行下面语句块里面的逻辑，然后就会卡顿卡顿
                            if (fromUser) {
                                videoFragment.seekTo(seekBar.getProgress());
//                    mMediaService.mMediaPlayer.seekTo(seekBar.getProgress());
                                locate(0, progress);
                            }
                            //进当滑动条到末端时，设置播放图标
                            if (progress == seekBar.getMax()/* || !mMyBinder.isPlaying()*/) {
                                ivPlay.setImageResource(R.drawable.play1_yellow);
//                                mMyBinder.seekToPositon(mMyBinder.getDuration());
//                                    mMyBinder.resetMusic();
//                                position = 0;
                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });

                    mHandler.post(mRunnableVideo);
                }
            case ACTION_VIDEO_START:
                ivPlay.setImageResource(R.drawable.pause1_yellow);
                mHandler.post(mRunnableVideo);
                break;
            case ACTION_VIDEO_STOP:
            case ACTION_VIDEO_COMPLETE:
                ivPlay.setImageResource(R.drawable.play1_yellow);
                mHandler.removeCallbacks(mRunnableVideo);
                break;
            default:
                break;
        }
    }

    //判断播放的句子并定位
    private void locate(int start, int playPosition) {
        if(sentenceList==null||sentenceList.size()==0)return;
        boolean isLocated = false;
        for (int i = start; i < sentenceList.size() - 1; i++) {
            if (playPosition >= sentenceList.get(i).getNode() && playPosition < sentenceList.get(i + 1).getNode()) {
                if (i != position) {
                    position = i;
//                    XToastUtils.toast("定位到" + position);
                    locate(1);
                    System.out.println("定位到" + position);
                }
                isLocated = true;
                break;
            }
        }
        //最后一个节点
        if (!isLocated) {
            if (playPosition >= sentenceList.get(sentenceList.size() - 1).getNode()) {
                if (sentenceList.size() - 1 != position) {
                    position = sentenceList.size() - 1;
//                    XToastUtils.toast("定位到" + position);
                    locate(1);
                    System.out.println("定位到" + position);
                }
                isLocated = true;
            }
        }
        if (!isLocated) {
            if (0 != position) {
                position = 0;
//                XToastUtils.toast("定位到初始位置");
                locate(1);
                System.out.println("定位到初始位置");
            }
        }
    }

    private void locate(int type) {
//        type=1;
        switch (type) {
            case 1:
                //慢
                LinearTopSmoothScroller smoothScroller = new LinearTopSmoothScroller(getContext(), true);
                smoothScroller.setTargetPosition(position + 1);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                break;
            case 2:
                //快
                TopSmoothScroller mScroller = new TopSmoothScroller(getContext());
                mScroller.setTargetPosition(position + 1);
                linearLayoutManager.startSmoothScroll(mScroller);
                break;
            case 3:
                //闪现
                linearLayoutManager.scrollToPositionWithOffset(position + 1, 200);
                break;
        }
        sentenceAdapter.setLight(position + 1);
        sentenceAdapter.notifyDataSetChanged();
    }

    public boolean onBackPressed(){
        return videoFragment.onBackPressed();
    }
}