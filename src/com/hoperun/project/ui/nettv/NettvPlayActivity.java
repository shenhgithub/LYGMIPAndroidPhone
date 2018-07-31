package com.hoperun.project.ui.nettv;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.hoperun.manager.components.MyVideoView;
import com.hoperun.manager.components.MyVideoView.onTouchSchreenListen;
import com.hoperun.mip.GlobalState;
import com.hoperun.mip.utils.LogUtil;
import com.hoperun.mip.utils.OnClickUtil;
import com.hoperun.miplygphone.R;

/**
 * 
 * 电视新闻，一级菜单
 * 
 * @Description<功能详细描述>
 * 
 * @author shen_feng
 * @Version [版本号, 2013-9-12]
 */
/**
 * <一句话功能简述>
 * 
 * @Description<功能详细描述>
 * 
 * @author ren_qiujing
 * @Version [版本号, 2013-12-19]
 */
public class NettvPlayActivity extends Activity implements OnClickListener
{
    /**
     * MainActivity实例
     */
    private Activity             mActivity;
    
    /**
     * 整体布局Rl
     */
    private RelativeLayout       mainRl;
    
    /**
     * 中间布局，包括 播放视频页面、电视图标
     */
    private RelativeLayout       mMid_rl;
    
    /**
     * 可以播放视频的区域布局
     */
    private RelativeLayout       mVideoRl;
    
    /**
     * 可以播放视频的区域布局,最小宽
     */
    private int                  mVideoRl_w            = 0;
    
    /**
     * 可以播放视频的区域布局，最小高
     */
    private int                  mVideoRl_h            = 0;
    
    /**
     * 播放视频实例
     */
    private MyVideoView          mVideoView;
    
    /**
     * 进度框布局
     */
    private RelativeLayout       mTvProgress;
    
    /**
     * 视频开始播放的thread
     */
    private Thread               videoT;
    
    /**
     * 是否执行够onDestory
     */
    boolean                      isDestroy             = false;
    
    /**
     * 播放视频的URI
     */
    private Uri                  mUri                  = null;
    
    /**
     * 播放进程滚动条
     */
    private SeekBar              seekbar;
    
    /**
     * 播放开始时间/已经播放的时间
     */
    private TextView             mstartTime;
    
    /**
     * 视频的总时间
     */
    private TextView             mendTime;
    
    /**
     * 音量进度条
     */
    private SeekBar              soundbar;
    
    /**
     * 播放的当前时间
     */
    private int                  currentPosition       = 0;
    
    /**
     * 开始、暂停按钮
     */
    private ImageView            mPlayImageView;
    
    /**
     * 返回按钮
     */
    private ImageView            mBackImageView;
    
    /**
     * 返回布局
     */
    private RelativeLayout       mBackLayout;
    
    // private RelativeLayout mFullScreenRl;
    
    /**
     * 开始展亭按钮
     */
    private ImageButton          mStopStart;
    
    /**
     * 是否全屏
     */
    private boolean              isFullScreen          = false;
    
    /**
     * 视频开始
     */
    public static final int      STARTVIDEO            = 0;
    
    /**
     * 滚动条滚动
     */
    public static final int      SEEKBAR               = 1;
    
    /**
     * 滚动条滚动
     */
    public static final int      VIDEOBEGIN            = 2;
    
    private AudioManager         mAudioManager         = null;
    
    private boolean              mVideoPause           = false;
    
    private String               url                   = "";
    
    /**
     * 播放结束监听
     */
    private OnCompletionListener mOnCompletionListener = new OnCompletionListener()
                                                       {
                                                           
                                                           @Override
                                                           public void onCompletion(MediaPlayer mp)
                                                           {
                                                               // TODO Auto-generated method stub
                                                               // mPlayButton.setBackgroundResource(R.drawable.video_play_selector);
                                                               seekbar.setProgress(0);
                                                               onSchreenListener.onSchreenTouchedListener();
                                                           }
                                                           
                                                       };
    
    @SuppressLint({"HandlerLeak", "HandlerLeak"})
    Handler                      mNetHandler           = new Handler()
                                                       {
                                                           
                                                           @Override
                                                           public void handleMessage(Message msg)
                                                           {
                                                               // TODO Auto-generated method stub
                                                               switch (msg.what)
                                                               {
                                                                   case STARTVIDEO:
                                                                       mUri = (Uri)msg.obj;
                                                                       
                                                                       if (isFullScreen)
                                                                       {
                                                                           // mGridViewTop.setVisibility(View.GONE);
                                                                       }
                                                                       else
                                                                       {
                                                                           // mGridViewTop.setVisibility(View.VISIBLE);
                                                                       }
                                                                       
                                                                       mVideoView.setVisibility(View.VISIBLE);
                                                                       mMid_rl.setBackgroundColor(mActivity.getResources()
                                                                           .getColor(R.color.black));
                                                                       
                                                                       startVideo(mUri);
                                                                       mTvProgress.setVisibility(View.VISIBLE);
                                                                       
                                                                       break;
                                                                   
                                                                   // 因为是网络，没有进度条
                                                                   case SEEKBAR:
                                                                       if (mVideoView != null)
                                                                           currentPosition =
                                                                               mVideoView.getCurrentPosition();
                                                                       
                                                                       if (seekbar.isEnabled())
                                                                       {
                                                                           seekbar.setProgress(currentPosition);
                                                                       }
                                                                       mstartTime.setText(toTime(currentPosition));
                                                                       
                                                                       if (mVideoView != null && mVideoView.isPlaying())
                                                                       {
                                                                           mNetHandler.sendEmptyMessageDelayed(SEEKBAR,
                                                                               1000);
                                                                       }
                                                                       break;
                                                                   
                                                                   case VIDEOBEGIN:
                                                                       mTvProgress.setVisibility(View.INVISIBLE);
                                                                       mMid_rl.setBackgroundColor(mActivity.getResources()
                                                                           .getColor(R.color.black));
                                                                       
                                                                       if (mVideoView.getDuration() > 0)
                                                                       {
                                                                           seekbar.setEnabled(true);
                                                                           seekbar.setMax(mVideoView.getDuration());
                                                                           
                                                                           mstartTime.setText(toTime(mVideoView.getCurrentPosition()));
                                                                           mendTime.setText(toTime(mVideoView.getDuration()));
                                                                           
                                                                           mVideoView.setOnCompletionListener(mOnCompletionListener);
                                                                           
                                                                           mNetHandler.sendEmptyMessage(SEEKBAR);
                                                                       }
                                                                       else
                                                                       {
                                                                           seekbar.setEnabled(false);
                                                                       }
                                                                       
                                                                       Animation mbottomtOut =
                                                                           AnimationUtils.loadAnimation(getApplicationContext(),
                                                                               R.anim.fromtopout);
                                                                       mBackLayout.setAnimation(mbottomtOut);
                                                                       mBackLayout.setVisibility(View.GONE);
                                                                       setVideoScale();
                                                                       
                                                                       break;
                                                                   default:
                                                                       break;
                                                               }
                                                               
                                                               super.handleMessage(msg);
                                                           }
                                                           
                                                       };
    
    private onTouchSchreenListen onSchreenListener     = new onTouchSchreenListen()
                                                       {
                                                           
                                                           @Override
                                                           public void onSchreenTouchedListener()
                                                           {
                                                               if (mVideoView != null)
                                                               {
                                                                   if (mVideoView.isPlaying())
                                                                   {
                                                                       mVideoView.pause();
                                                                       mVideoPause = true;
                                                                       mPlayImageView.setVisibility(View.VISIBLE);
                                                                       mPlayImageView.setBackgroundResource(R.drawable.nettv_pause);
                                                                       Animation mbottomtOut =
                                                                           AnimationUtils.loadAnimation(getApplicationContext(),
                                                                               R.anim.fromtopin);
                                                                       mBackLayout.setAnimation(mbottomtOut);
                                                                       mBackLayout.setVisibility(View.VISIBLE);
                                                                       mStopStart.setBackgroundResource(R.drawable.playerstop);
                                                                   }
                                                                   else
                                                                   {
                                                                       mVideoView.start();
                                                                       mVideoPause = false;
                                                                       mPlayImageView.setVisibility(View.INVISIBLE);
                                                                       mNetHandler.sendEmptyMessage(SEEKBAR);
                                                                       Animation mbottomtOut =
                                                                           AnimationUtils.loadAnimation(getApplicationContext(),
                                                                               R.anim.fromtopout);
                                                                       mBackLayout.setAnimation(mbottomtOut);
                                                                       mBackLayout.setVisibility(View.GONE);
                                                                       mStopStart.setBackgroundResource(R.drawable.playerstart);
                                                                   }
                                                               }
                                                           }
                                                       };
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View v = inflater.inflate(R.layout.nettv_play_layout, null);
        // 设置全屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(v);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        mActivity = NettvPlayActivity.this;
        mainRl = (RelativeLayout)v.findViewById(R.id.net_mainRl);
        
        mMid_rl = (RelativeLayout)v.findViewById(R.id.mid_rl);
        
        mVideoRl = (RelativeLayout)v.findViewById(R.id.video_rl);
        
        mVideoView = (MyVideoView)v.findViewById(R.id.videoview);
        mVideoView.setOnTouchSchreenListener(onSchreenListener);
        
        mTvProgress = (RelativeLayout)v.findViewById(R.id.myprogressbar_tv);
        mTvProgress.setVisibility(View.INVISIBLE);
        
        mPlayImageView = (ImageView)v.findViewById(R.id.im_play);
        mBackImageView = (ImageView)v.findViewById(R.id.back);
        mBackLayout = (RelativeLayout)v.findViewById(R.id.back_layout);
        // 进度条
        seekbar = (SeekBar)v.findViewById(R.id.video_seekbar);
        // 不让拖动
        seekbar.setOnTouchListener(new OnTouchListener()
        {
            
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });
        seekbar.setEnabled(false);
        
        soundbar = (SeekBar)v.findViewById(R.id.video_sound);
        
        mstartTime = (TextView)v.findViewById(R.id.video_playtime);
        mendTime = (TextView)v.findViewById(R.id.video_duration);
        
        mStopStart = (ImageButton)v.findViewById(R.id.stop_start);
        
        // mFullScreenRl = (RelativeLayout)v.findViewById(R.id.full_screen_rl);
        initData();
        initListener();
    }
    
    /**
     * 
     * 初始化数据
     * 
     * @Description 初始化数据
     * 
     * @LastModifiedDate：2013-10-18
     * @author wang_ling
     * @EditHistory：<修改内容><修改人>
     */
    private void initData()
    {
        mAudioManager = (AudioManager)mActivity.getSystemService(mActivity.AUDIO_SERVICE);
        int maxSound = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        /* 获得当前音量 */
        int currentSound = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        
        LogUtil.i("", "======maxSound =" + maxSound + ";currentSound=" + currentSound);
        
        soundbar.setMax(maxSound);
        soundbar.setProgress(currentSound);
        Message msg = new Message();
        msg.what = STARTVIDEO;
        msg.obj = Uri.parse(url);
        
        // dismissScheduleView(true);
        mNetHandler.sendMessage(msg);
    }
    
    private void initListener()
    {
        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if (fromUser)
                {
                    mVideoView.seekTo(progress);
                }
                
            }
        });
        
        soundbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if (fromUser)
                {
                    int ScurrentPosition = progress;
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, ScurrentPosition, 0);
                    LogUtil.i("", "======ScurrentPosition=" + ScurrentPosition);
                }
                
            }
        });
        
        // mFullScreenRl.setOnClickListener(new OnClickListener()
        // {
        //
        // @Override
        // public void onClick(View v)
        // {
        // if (!isFullScreen)
        // {
        // mainRl.setBackgroundColor(mActivity.getResources().getColor(R.color.black));
        // mFullScreen.setBackgroundResource(R.drawable.nettv_half);
        // isFullScreen = true;
        // setVideoScale();
        // }
        // else
        // {
        // mFullScreen.setBackgroundResource(R.drawable.nettv_full);
        // isFullScreen = false;
        // setVideoScale();
        // }
        //
        // }
        // });
        
        mStopStart.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                onSchreenListener.onSchreenTouchedListener();
            }
        });
        
        mBackImageView.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                closeThisFragment();
            }
        });
    }
    
    private void setVideoScale()
    {
        int width = 0;
        int height = 0;
        int videoWidth = 0;
        int videoHeight = 0;
        
        width = GlobalState.getInstance().getmScreen_With();
        height = GlobalState.getInstance().getmScreen_Height();
        
        if (mVideoRl_w <= 0 || mVideoRl_h <= 0)
        {
            mVideoRl_w = mVideoRl.getWidth();
            mVideoRl_h = mVideoRl.getHeight();
            width = (int)((float)mVideoRl_h / width * height);
            height = mVideoRl_h;
        }
        // if (isFullScreen)
        // {
        // width = GlobalState.getInstance().getmScreen_With();
        // height = GlobalState.getInstance().getmScreen_Height();
        // }
        // else
        // {
        // if (mVideoRl_w <= 0 || mVideoRl_h <= 0)
        // {
        // mVideoRl_w = mVideoRl.getWidth();
        // mVideoRl_h = mVideoRl.getHeight();
        // }
        //
        // if (mVideoRl_w > 0 && mVideoRl_h > 0)
        // {
        // width = mVideoRl_w;
        // height = mVideoRl_h;
        // }
        // else
        // {
        // return;
        // }
        // }
        //
        // if (mVideoView != null)
        // {
        // videoWidth = mVideoView.getVideoWidth();
        // videoHeight = mVideoView.getVideoHeight();
        // }
        //
        // if (videoWidth > 0 && videoHeight > 0)
        // {
        // if (videoWidth * height > width * videoHeight)
        // {
        //
        // height = width * videoHeight / videoWidth;
        // }
        // else if (videoWidth * height < width * videoHeight)
        // {
        // width = height * videoWidth / videoHeight;
        // }
        // }
        //
        mVideoView.setVideoScale(width, height);
        
    }
    
    /**
     * 重载方法
     * 
     * @author shen_feng
     */
    @Override
    public void onResume()
    {
        super.onResume();
        
        if (mUri != null)
        {
            
            if (mVideoView != null)
            {
                if (mVideoView.getIsPause() && !mVideoPause)
                {
                    mVideoView.start();
                }
            }
            else
            {
                startVideo(mUri);
                mTvProgress.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            mVideoView.setVisibility(View.INVISIBLE);
            mMid_rl.setBackgroundResource(R.drawable.nettv_backgroud);
            
        }
    }
    
    private void startVideo(Uri uri)
    {
        if (uri == null)
        {
            return;
        }
        
        Log.i("hoperun", "****startVideo uri= " + uri);
        
        mUri = uri;
        setMediaScale(false);
        mVideoView.setVideoURI(uri);
        mVideoView.requestFocus();
        mVideoView.start();
        
        mPlayImageView.setVisibility(View.INVISIBLE);
        mNetHandler.sendEmptyMessage(SEEKBAR);
        
        videoT = new Thread(videoR);
        videoT.start();
    }
    
    private Thread videoR = new Thread(new Runnable()
                          {
                              
                              @Override
                              public void run()
                              {
                                  boolean flag = true;
                                  while (flag)
                                  {
                                      if (isDestroy || mVideoView.getCurrentPosition() > 0)
                                      {
                                          flag = false;
                                          mNetHandler.sendEmptyMessage(VIDEOBEGIN);
                                      }
                                  }
                              }
                          });
    
    @Override
    public void onClick(View v)
    {
        if (OnClickUtil.isMostPost())
        {
            return;
        }
        
        switch (v.getId())
        {
            default:
                break;
        }
    }
    
    /**
     * 二级布局关闭
     * 
     * @author ren_qiujing
     */
    public void closeThisFragment()
    {
        // TODO Auto-generated method stub
        finish();
        if (mVideoView != null)
        {
            mVideoView.stopPlayback();
            mVideoView.setVisibility(View.GONE);
        }
    }
    
    int currentMusicPosition = 0;
    
    /**
     * 点击back键
     * 
     * @author ren_qiujing
     */
    
    public boolean onKeyDown(int keyId)
    {
        // TODO Auto-generated method stub
        if (keyId == KeyEvent.KEYCODE_BACK)
        {
            closeThisFragment();
            return true;
        }
        else if (keyId == KeyEvent.KEYCODE_VOLUME_DOWN)
        {
            if (mVideoView != null && mVideoView.isPlaying())
            {
                currentMusicPosition = soundbar.getProgress();
                currentMusicPosition--;
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentMusicPosition, 0);
                soundbar.setProgress(currentMusicPosition);
            }
            return true;
        }
        else if (keyId == KeyEvent.KEYCODE_VOLUME_UP)
        {
            if (mVideoView != null && mVideoView.isPlaying())
            {
                currentMusicPosition = soundbar.getProgress();
                currentMusicPosition++;
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentMusicPosition, 0);
                soundbar.setProgress(currentMusicPosition);
            }
            return true;
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public void onPause()
    {
        // TODO Auto-generated method stub
        if (mVideoView != null && mVideoView.isPlaying())
        {
            mVideoView.pause();
        }
        super.onPause();
    }
    
    /**
     * 重载方法
     * 
     * @author shen_feng
     */
    @Override
    public void onDestroy()
    {
        isDestroy = true;
        super.onDestroy();
    }
    
    /**
     * 转换时间
     * 
     * @Description<功能详细描述>
     * 
     * @param time
     * @return
     * @LastModifiedDate：2013-12-19
     * @author ren_qiujing
     * @EditHistory：<修改内容><修改人>
     */
    public String toTime(int time)
    {
        
        time /= 1000;
        int minute = time / 60;
        int hour = minute / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }
    
    private void setMediaScale(boolean flag)
    {
        if (mVideoView != null)
        {
            mVideoView.setHandScale(flag);
        }
    }
    
}
