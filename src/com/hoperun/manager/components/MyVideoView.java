package com.hoperun.manager.components;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;

import com.hoperun.mip.utils.LogUtil;

public class MyVideoView extends SurfaceView implements MediaPlayerControl
{
    
    private String                         TAG           = "VideoView";
    
    private Context                        mContext;
    
    private Uri                            mUri;
    
    private int                            mDuration;
    
    private SurfaceHolder                  surfaceHolder = null;
    
    private MediaPlayer                    mMediaPlayer  = null;
    
    private boolean                        mIsPrepared;
    
    private int                            mVideoWidth;
    
    private int                            mVideoHeight;
    
    private int                            mSurfaceWidth;
    
    private int                            mSurfaceHeight;
    
    private MediaController                mMediaController;
    
    private OnCompletionListener           mOnCompletionListener;
    
    private MediaPlayer.OnPreparedListener mOnPreparedListener;
    
    private int                            mCurrentBufferPercentage;
    
    private OnErrorListener                mOnErrorListener;
    
    private boolean                        mStartWhenPrepared;
    
    private int                            mSeekWhenPrepared;
    
    private MySizeChangeLinstener          mMyChangeLinstener;
    
    private onTouchSchreenListen           onTouchSchreenListener;
    
    private boolean                        isHandScale   = false;
    
    public MediaPlayer getmMediaPlayer()
    {
        return mMediaPlayer;
    }
    
    public void setmMediaPlayer(MediaPlayer mMediaPlayer)
    {
        this.mMediaPlayer = mMediaPlayer;
    }
    
    public MediaController getmMediaController()
    {
        return mMediaController;
    }
    
    public void setmMediaController(MediaController mMediaController)
    {
        this.mMediaController = mMediaController;
    }
    
    public int getVideoWidth()
    {
        return mVideoWidth;
    }
    
    public int getVideoHeight()
    {
        return mVideoHeight;
    }
    
    public void setVideoScale(int width, int height)
    {
        LayoutParams lp = getLayoutParams();
        lp.height = height;
        lp.width = width;
        setLayoutParams(lp);
    }
    
    public interface MySizeChangeLinstener
    {
        public void doMyThings();
    }
    
    public void setMySizeChangeLinstener(MySizeChangeLinstener l)
    {
        mMyChangeLinstener = l;
    }
    
    public MyVideoView(Context context)
    {
        super(context);
        mContext = context;
        initVideoView();
    }
    
    public MyVideoView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
        mContext = context;
        initVideoView();
    }
    
    public MyVideoView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mContext = context;
        initVideoView();
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // Log.i ( "@@@@" , "onMeasure" );
        int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
        int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
        
        // if ( mVideoWidth > 0 && mVideoHeight > 0 ) {
        //
        // if ( mVideoWidth * height > width * mVideoHeight ) {
        // Log.i ( "@@@" , "image too tall, correcting" );
        // height = width * mVideoHeight / mVideoWidth;
        // } else if ( mVideoWidth * height < width * mVideoHeight ) {
        // Log.i ( "@@@" , "image too wide, correcting" );
        // width = height * mVideoWidth / mVideoHeight;
        // } else {
        // Log.i ( "@@@" , "aspect ratio is correct: " + width + "/"
        // + height + "=" + mVideoWidth + "/" + mVideoHeight );
        // }
        // }
        
        // Log.i ( "@@@@@@@@@@" , "setting size: " + width + 'x' + height );
        setMeasuredDimension(width, height);
    }
    
    public int resolveAdjustedSize(int desiredSize, int measureSpec)
    {
        int result = desiredSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        
        switch (specMode)
        {
            case MeasureSpec.UNSPECIFIED:
                result = desiredSize;
                break;
            
            case MeasureSpec.AT_MOST:
                result = Math.min(desiredSize, specSize);
                break;
            
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }
    
    private void initVideoView()
    {
        mVideoWidth = 0;
        mVideoHeight = 0;
        getHolder().addCallback(mSHCallback);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
    }
    
    public void setVideoPath(String path)
    {
        setVideoURI(Uri.parse(path));
    }
    
    public void setVideoURI(Uri uri)
    {
        mUri = uri;
        mStartWhenPrepared = false;
        mSeekWhenPrepared = 0;
        openVideo();
        requestLayout();
        invalidate();
    }
    
    public void stopPlayback()
    {
        if (mMediaPlayer != null)
        {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
    
    private void openVideo()
    {
        if (mUri == null || surfaceHolder == null)
        {
            return;
        }
        Intent i = new Intent("com.android.music.musicservicecommand");
        i.putExtra("command", "pause");
        mContext.sendBroadcast(i);
        
        if (mMediaPlayer != null)
        {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        
        try
        {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnPreparedListener(mPreparedListener);
            mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
            mIsPrepared = false;
            Log.v(TAG, "reset duration to -1 in openVideo");
            mDuration = -1;
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
            mMediaPlayer.setOnErrorListener(mErrorListener);
            mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            mCurrentBufferPercentage = 0;
            mMediaPlayer.setDataSource(mContext, mUri);
            mMediaPlayer.setDisplay(surfaceHolder);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.prepareAsync();
            attachMediaController();
        }
        catch (IOException ex)
        {
            Log.w(TAG, "Unable to open content: " + mUri, ex);
            return;
        }
        catch (IllegalArgumentException ex)
        {
            Log.w(TAG, "Unable to open content: " + mUri, ex);
            return;
        }
    }
    
    public MediaController getMediaController()
    {
        return mMediaController;
    }
    
    public void setMediaController(MediaController controller)
    {
        if (mMediaController != null)
        {
            mMediaController.hide();
        }
        mMediaController = controller;
        attachMediaController();
    }
    
    private void attachMediaController()
    {
        if (mMediaPlayer != null && mMediaController != null)
        {
            mMediaController.setMediaPlayer(this);
            
            mMediaController.setEnabled(mIsPrepared);
            
        }
    }
    
    MediaPlayer.OnVideoSizeChangedListener        mSizeChangedListener     =
                                                                               new MediaPlayer.OnVideoSizeChangedListener()
                                                                               {
                                                                                   public void onVideoSizeChanged(
                                                                                       MediaPlayer mp, int width,
                                                                                       int height)
                                                                                   {
                                                                                       mVideoWidth = mp.getVideoWidth();
                                                                                       mVideoHeight =
                                                                                           mp.getVideoHeight();
                                                                                       
                                                                                       if (mMyChangeLinstener != null)
                                                                                       {
                                                                                           mMyChangeLinstener.doMyThings();
                                                                                       }
                                                                                       
                                                                                       if (mVideoWidth != 0
                                                                                           && mVideoHeight != 0)
                                                                                       {
                                                                                           getHolder().setFixedSize(mVideoWidth,
                                                                                               mVideoHeight);
                                                                                       }
                                                                                   }
                                                                               };
    
    MediaPlayer.OnPreparedListener                mPreparedListener        = new MediaPlayer.OnPreparedListener()
                                                                           {
                                                                               public void onPrepared(MediaPlayer mp)
                                                                               {
                                                                                   mIsPrepared = true;
                                                                                   if (mOnPreparedListener != null)
                                                                                   {
                                                                                       mOnPreparedListener.onPrepared(mMediaPlayer);
                                                                                   }
                                                                                   if (mMediaController != null)
                                                                                   {
                                                                                       mMediaController.setEnabled(true);
                                                                                   }
                                                                                   mVideoWidth = mp.getVideoWidth();
                                                                                   mVideoHeight = mp.getVideoHeight();
                                                                                   if (mVideoWidth != 0
                                                                                       && mVideoHeight != 0)
                                                                                   {
                                                                                       
                                                                                       Log.i("video size: ",
                                                                                           +mVideoWidth + "/"
                                                                                               + mVideoHeight);
                                                                                       getHolder().setFixedSize(mVideoWidth,
                                                                                           mVideoHeight);
                                                                                       
                                                                                       if (mSurfaceWidth == mVideoWidth
                                                                                           && mSurfaceHeight == mVideoHeight)
                                                                                       {
                                                                                           if (mSeekWhenPrepared != 0)
                                                                                           {
                                                                                               mMediaPlayer.seekTo(mSeekWhenPrepared);
                                                                                               mSeekWhenPrepared = 0;
                                                                                           }
                                                                                           if (mStartWhenPrepared)
                                                                                           {
                                                                                               mMediaPlayer.start();
                                                                                               mStartWhenPrepared =
                                                                                                   false;
                                                                                               if (mMediaController != null)
                                                                                               {
                                                                                                   mMediaController.show();
                                                                                               }
                                                                                           }
                                                                                           else if (!isPlaying()
                                                                                               && (mSeekWhenPrepared != 0 || getCurrentPosition() > 0))
                                                                                           {
                                                                                               if (mMediaController != null)
                                                                                               {
                                                                                                   mMediaController.show(0);
                                                                                               }
                                                                                           }
                                                                                       }
                                                                                   }
                                                                                   else
                                                                                   {
                                                                                       if (mSeekWhenPrepared != 0)
                                                                                       {
                                                                                           mMediaPlayer.seekTo(mSeekWhenPrepared);
                                                                                           mSeekWhenPrepared = 0;
                                                                                       }
                                                                                       if (mStartWhenPrepared)
                                                                                       {
                                                                                           mMediaPlayer.start();
                                                                                           mStartWhenPrepared = false;
                                                                                       }
                                                                                   }
                                                                               }
                                                                           };
    
    private MediaPlayer.OnCompletionListener      mCompletionListener      = new MediaPlayer.OnCompletionListener()
                                                                           {
                                                                               public void onCompletion(MediaPlayer mp)
                                                                               {
                                                                                   if (mMediaController != null)
                                                                                   {
                                                                                       mMediaController.hide();
                                                                                   }
                                                                                   if (mOnCompletionListener != null)
                                                                                   {
                                                                                       mOnCompletionListener.onCompletion(mMediaPlayer);
                                                                                   }
                                                                               }
                                                                           };
    
    private MediaPlayer.OnErrorListener           mErrorListener           = new MediaPlayer.OnErrorListener()
                                                                           {
                                                                               public boolean onError(MediaPlayer mp,
                                                                                   int framework_err, int impl_err)
                                                                               {
                                                                                   Log.d(TAG, "Error: " + framework_err
                                                                                       + "," + impl_err);
                                                                                   if (mMediaController != null)
                                                                                   {
                                                                                       mMediaController.hide();
                                                                                   }
                                                                                   // 如果捕捉到错误，错误监听启动
                                                                                   if (mOnErrorListener != null)
                                                                                   {
                                                                                       if (mOnErrorListener.onError(mMediaPlayer,
                                                                                           framework_err,
                                                                                           impl_err))
                                                                                       {
                                                                                           return true;
                                                                                       }
                                                                                   }
                                                                                   
                                                                                   return true;
                                                                               }
                                                                           };
    
    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener =
                                                                               new MediaPlayer.OnBufferingUpdateListener()
                                                                               {
                                                                                   public void onBufferingUpdate(
                                                                                       MediaPlayer mp, int percent)
                                                                                   {
                                                                                       mCurrentBufferPercentage =
                                                                                           percent;
                                                                                       LogUtil.i("",
                                                                                           "onBufferingUpdate ="
                                                                                               + percent
                                                                                               + ";mp.getDuration()="
                                                                                               + mp.getDuration());
                                                                                   }
                                                                               };
    
    public void setOnPreparedListener(MediaPlayer.OnPreparedListener l)
    {
        mOnPreparedListener = l;
    }
    
    public void setOnCompletionListener(OnCompletionListener l)
    {
        mOnCompletionListener = l;
    }
    
    public void setOnErrorListener(OnErrorListener l)
    {
        mOnErrorListener = l;
    }
    
    SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback()
                                       {
                                           public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
                                           {
                                               mSurfaceWidth = w;
                                               mSurfaceHeight = h;
                                               if (mMediaPlayer != null && mIsPrepared && mVideoWidth == w
                                                   && mVideoHeight == h)
                                               {
                                                   if (mSeekWhenPrepared != 0)
                                                   {
                                                       mMediaPlayer.seekTo(mSeekWhenPrepared);
                                                       mSeekWhenPrepared = 0;
                                                   }
                                                   
                                                   LogUtil.i("", "******************mSHCallback ");
                                                   if (!isHandScale)
                                                   {
                                                       mMediaPlayer.start();
                                                   }
                                                   else
                                                   {
                                                       isHandScale = false;
                                                   }
                                                   
                                                   if (mMediaController != null)
                                                   {
                                                       mMediaController.show();
                                                   }
                                               }
                                           }
                                           
                                           public void surfaceCreated(SurfaceHolder holder)
                                           {
                                               surfaceHolder = holder;
                                               openVideo();
                                           }
                                           
                                           public void surfaceDestroyed(SurfaceHolder holder)
                                           {
                                               // after we return from this we
                                               // can't use the surface any more
                                               surfaceHolder = null;
                                               if (mMediaController != null)
                                                   mMediaController.hide();
                                               if (mMediaPlayer != null)
                                               {
                                                   mMediaPlayer.reset();
                                                   mMediaPlayer.release();
                                                   mMediaPlayer = null;
                                               }
                                           }
                                       };
    
    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        if (mIsPrepared && mMediaPlayer != null && (mMediaController != null || mOnCompletionListener != null))
        {
            toggleMediaControlsVisiblity();
        }
        return false;
    }
    
    @Override
    public boolean onTrackballEvent(MotionEvent ev)
    {
        if (mIsPrepared && mMediaPlayer != null && mMediaController != null)
        {
            toggleMediaControlsVisiblity();
        }
        return false;
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (mIsPrepared && keyCode != KeyEvent.KEYCODE_BACK && keyCode != KeyEvent.KEYCODE_VOLUME_UP
            && keyCode != KeyEvent.KEYCODE_VOLUME_DOWN && keyCode != KeyEvent.KEYCODE_MENU
            && keyCode != KeyEvent.KEYCODE_CALL && keyCode != KeyEvent.KEYCODE_ENDCALL && mMediaPlayer != null
            && mMediaController != null)
        {
            if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
            {
                if (mMediaPlayer.isPlaying())
                {
                    pause();
                    mMediaController.show();
                }
                else
                {
                    start();
                    mMediaController.hide();
                }
                return true;
            }
            else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP && mMediaPlayer.isPlaying())
            {
                pause();
                mMediaController.show();
            }
            else
            {
                toggleMediaControlsVisiblity();
            }
        }
        
        return super.onKeyDown(keyCode, event);
    }
    
    private void toggleMediaControlsVisiblity()
    {
        if (mMediaController != null)
        {
            if (mMediaController.isShowing())
            {
                mMediaController.hide();
            }
            else
            {
                mMediaController.show();
            }
        }
        
        if (onTouchSchreenListener != null)
        {
            onTouchSchreenListener.onSchreenTouchedListener();
        }
    }
    
    public void start()
    {
        if (mMediaPlayer != null && mIsPrepared)
        {
            mMediaPlayer.start();
            mStartWhenPrepared = false;
        }
        else
        {
            mStartWhenPrepared = true;
        }
        
    }
    
    public void pause()
    {
        if (mMediaPlayer != null && mIsPrepared)
        {
            if (mMediaPlayer.isPlaying())
            {
                mMediaPlayer.pause();
            }
        }
        mStartWhenPrepared = false;
    }
    
    public int getDuration()
    {
        if (mMediaPlayer != null && mIsPrepared)
        {
            if (mDuration > 0)
            {
                return mDuration;
            }
            mDuration = mMediaPlayer.getDuration();
            return mDuration;
        }
        mDuration = -1;
        return mDuration;
    }
    
    public int getCurrentPosition()
    {
        if (mMediaPlayer != null && mIsPrepared)
        {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }
    
    public void seekTo(int msec)
    {
        if (mMediaPlayer != null && mIsPrepared)
        {
            mMediaPlayer.seekTo(msec);
        }
        else
        {
            mSeekWhenPrepared = msec;
        }
    }
    
    public boolean isPlaying()
    {
        if (mMediaPlayer != null && mIsPrepared)
        {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }
    
    public int getBufferPercentage()
    {
        if (mMediaPlayer != null)
        {
            return mCurrentBufferPercentage;
        }
        return 0;
    }
    
    @Override
    public boolean canPause()
    {
        return true;
    }
    
    @Override
    public boolean canSeekBackward()
    {
        return true;
    }
    
    @Override
    public boolean canSeekForward()
    {
        return true;
    }
    
    public boolean getIsPause()
    {
        if (mMediaPlayer != null)
        {
            return mMediaPlayer.getDuration() > 0 ? true : false;
        }
        else
        {
            return false;
        }
    }
    
    public interface onTouchSchreenListen
    {
        public void onSchreenTouchedListener();
    }
    
    public onTouchSchreenListen getOnTouchSchreenListener()
    {
        return onTouchSchreenListener;
    }
    
    public void setOnTouchSchreenListener(onTouchSchreenListen onTouchSchreenListener)
    {
        this.onTouchSchreenListener = onTouchSchreenListener;
    }
    
    public boolean isHandScale()
    {
        return isHandScale;
    }
    
    public void setHandScale(boolean isHandScale)
    {
        this.isHandScale = isHandScale;
    }
    
}
