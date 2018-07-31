package com.artifex.mupdf;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.artifex.handWriteEditor.CustomHandWriteEditor;
import com.artifex.handWriteEditor.HandWriteTool;
import com.artifex.handWriteEditor.SpenView;
import com.hoperun.miplygphone.R;

// Make our ImageViews opaque to optimize redraw
class OpaqueImageView extends ImageView
{
    
    public OpaqueImageView(Context context)
    {
        super(context);
    }
    
    @Override
    protected void onDraw(Canvas canvas)
    {
        // TODO Auto-generated method stub
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        
        super.onDraw(canvas);
    }
    
}

public abstract class PageView extends ViewGroup
{
    private static final int                      HIGHLIGHT_COLOR       = 0x805555FF;
    
    private static final int                      LINK_COLOR            = 0x80FFCC88;
    
    private static final int                      BACKGROUND_COLOR      = 0xFFFFFFFF;
    
    private static final int                      PROGRESS_DIALOG_DELAY = 200;
    
    private static final int                      BORDER_SIZE           = 48;
    
    private static final int                      SPEN_BORDER_SIZE      = 60;
    
    private final Context                         mContext;
    
    protected int                                 mPageNumber;
    
    private Point                                 mParentSize;
    
    protected Point                               mSize;                                // Size of page at minimum zoom
                                                                                         
    protected float                               mSourceScale;
    
    private ImageView                             mEntire;                              // Image rendered at minimum
                                                                                         // zoom
                                                                                         
    private Bitmap                                mEntireBm;
    
    private AsyncTask<Void, Void, LinkInfo[]>     mDrawEntire;
    
    private Point                                 mPatchViewSize;                       // View size on the basis of
                                                                                         // which the patch was created
                                                                                         
    private Rect                                  mPatchArea;
    
    private OpaqueImageView                       mPatch;
    
    private AsyncTask<PatchInfo, Void, PatchInfo> mDrawPatch;
    
    private RectF                                 mSearchBoxes[];
    
    private LinkInfo                              mLinks[];
    
    private View                                  mSearchView;
    
    private boolean                               mIsBlank;
    
    private boolean                               mUsingHardwareAcceleration;
    
    private boolean                               mHighlightLinks;
    
    private float                                 mScale                = 1f;
    
    private ProgressBar                           mBusyIndicator;
    
    private final Handler                         mHandler              = new Handler();
    
    public boolean setHandWriteSign(boolean flag)
    {
        if (HandWriteTool.mEditor[mPageNumber] != null)
        {
            if (HandWriteTool.type == 0)
            {
                ((CustomHandWriteEditor)HandWriteTool.mEditor[mPageNumber]).setSign(flag);
                
            }
            else if (HandWriteTool.type == 1)
            {
                ((SpenView)HandWriteTool.mEditor[mPageNumber]).setSign(flag);
            }
            return true;
        }
        
        return false;
    }
    
    public boolean getHandWriteSignEnable()
    {
        if (HandWriteTool.mEditor[mPageNumber] != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public void setPaintColor(int color)
    {
        HandWriteTool.setmSignColor(color);
        if (HandWriteTool.type == 0)
        {
            ((CustomHandWriteEditor)HandWriteTool.mEditor[mPageNumber]).setPaintColor(HandWriteTool.mSignColor);
            
        }
        else if (HandWriteTool.type == 1)
        {
            ((SpenView)HandWriteTool.mEditor[mPageNumber]).setPaintColor(HandWriteTool.mSignColor);
        }
        
    }
    
    public void setStress(int stress)
    {
        HandWriteTool.setmSignStress(stress);
        // HandWriteTool.mEditor[mPageNumber].setStress(HandWriteTool.mSignStress);
        if (HandWriteTool.type == 0)
        {
            ((CustomHandWriteEditor)HandWriteTool.mEditor[mPageNumber]).setStress(HandWriteTool.mSignStress);
            
        }
        else if (HandWriteTool.type == 1)
        {
            ((SpenView)HandWriteTool.mEditor[mPageNumber]).setStress(HandWriteTool.mSignStress);
        }
    }
    
    public void setErase()
    {
        // HandWriteTool.mEditor[mPageNumber].setErase();
        if (HandWriteTool.type == 0)
        {
            ((CustomHandWriteEditor)HandWriteTool.mEditor[mPageNumber]).setErase();
            
        }
        else if (HandWriteTool.type == 1)
        {
            ((SpenView)HandWriteTool.mEditor[mPageNumber]).setErase();
        }
    }
    
    public void setPen()
    {
        // HandWriteTool.mEditor[mPageNumber].setPen();
        if (HandWriteTool.type == 0)
        {
            ((CustomHandWriteEditor)HandWriteTool.mEditor[mPageNumber]).setPen();
            
        }
        else if (HandWriteTool.type == 1)
        {
            ((SpenView)HandWriteTool.mEditor[mPageNumber]).setPen();
        }
    }
    
    public void setEmpty()
    {
        // HandWriteTool.mEditor[mPageNumber].clearAll();
        if (HandWriteTool.type == 0)
        {
            ((CustomHandWriteEditor)HandWriteTool.mEditor[mPageNumber]).clearAll();
            
        }
        else if (HandWriteTool.type == 1)
        {
            ((SpenView)HandWriteTool.mEditor[mPageNumber]).setEmpty();
        }
    }
    
    public void setRedo()
    {
        // HandWriteTool.mEditor[mPageNumber].redo();
        if (HandWriteTool.type == 0)
        {
            ((CustomHandWriteEditor)HandWriteTool.mEditor[mPageNumber]).redo();
            
        }
        else if (HandWriteTool.type == 1)
        {
            ((SpenView)HandWriteTool.mEditor[mPageNumber]).setRedo();
        }
    }
    
    public void setUndo()
    {
        // HandWriteTool.mEditor[mPageNumber].undo();
        if (HandWriteTool.type == 0)
        {
            ((CustomHandWriteEditor)HandWriteTool.mEditor[mPageNumber]).undo();
            
        }
        else if (HandWriteTool.type == 1)
        {
            ((SpenView)HandWriteTool.mEditor[mPageNumber]).setUndo();
        }
    }
    
    @SuppressWarnings("deprecation")
    public PageView(Context c, Point parentSize)
    {
        super(c);
        mContext = c;
        mParentSize = parentSize;
        setBackgroundColor(BACKGROUND_COLOR);
        mUsingHardwareAcceleration = Build.VERSION.SDK != null && Integer.valueOf(Build.VERSION.SDK) >= 14;
        
    }
    
    protected abstract void drawPage(Bitmap bm, int sizeX, int sizeY, int patchX, int patchY, int patchWidth,
        int patchHeight);
    
    protected abstract LinkInfo[] getLinkInfo();
    
    public void blank(int page)
    {
        // Cancel pending render task
        if (mDrawEntire != null)
        {
            mDrawEntire.cancel(true);
            mDrawEntire = null;
        }
        
        mIsBlank = true;
        mPageNumber = page;
        
        if (mSize == null)
        {
            mSize = mParentSize;
        }
        
        if (mEntire != null)
        {
            mEntire.setImageBitmap(null);
        }
        
        if (mPatch != null)
        {
            mPatch.setImageBitmap(null);
        }
        
        if (mBusyIndicator == null)
        {
            mBusyIndicator = new ProgressBar(mContext);
            mBusyIndicator.setIndeterminate(true);
            mBusyIndicator.setBackgroundResource(R.drawable.busy);
            addView(mBusyIndicator);
        }
        
    }
    
    float oX = 0f;
    
    float oY = 0f;
    
    public void setPage(int page, PointF size)
    {
        // Cancel pending render task
        if (mDrawEntire != null)
        {
            mDrawEntire.cancel(true);
            mDrawEntire = null;
        }
        
        oX = size.x;
        oY = size.y;
        
        mIsBlank = false;
        
        mPageNumber = page;
        
        if (mEntire == null)
        {
            mEntire = new OpaqueImageView(mContext);
            mEntire.setScaleType(ImageView.ScaleType.FIT_CENTER);
            addView(mEntire);
        }
        
        // Calculate scaled size that fits within the screen limits
        // This is the size at minimum zoom
        mSourceScale = Math.min(mParentSize.x / size.x, mParentSize.y / size.y);
        Point newSize = new Point((int)(size.x * mSourceScale), (int)(size.y * mSourceScale));
        mSize = newSize;
        
        if (mSize.x <= 0 || mSize.y <= 0)
        {
            final Activity activity = (Activity)mContext;
            new AlertDialog.Builder(activity).setTitle("警告")
                .setMessage("打开文件时出现错误，请尝试重新打开文件!")
                .setCancelable(false)
                .setPositiveButton("关闭", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        activity.finish();
                    }
                })
                .show();
        }
        
        if (MuPDFAttr.s_PageSizes != null)
        {
            
            if (MuPDFAttr.s_PageSizes[page + 1] != null)
            {
                MuPDFAttr.s_PageSizes[page + 1] = null;
            }
            
            MuPDFAttr.s_PageSizes[page + 1] = mSize;
        }
        
        if (mUsingHardwareAcceleration)
        {
            // When hardware accelerated, updates to the bitmap seem to be
            // ignored, so we recreate it. There may be another way around this
            // that we are yet to find.
            mEntire.setImageBitmap(null);
            mEntireBm = null;
        }
        
        if (mEntireBm == null || mEntireBm.getWidth() != newSize.x || mEntireBm.getHeight() != newSize.y)
        {
            mEntireBm = Bitmap.createBitmap(mSize.x, mSize.y, Bitmap.Config.ARGB_8888);
        }
        
        // Render the page in the background
        mDrawEntire = new AsyncTask<Void, Void, LinkInfo[]>()
        {
            protected LinkInfo[] doInBackground(Void... v)
            {
                drawPage(mEntireBm, mSize.x, mSize.y, 0, 0, mSize.x, mSize.y);
                
                return getLinkInfo();
            }
            
            protected void onPreExecute()
            {
                mEntire.setImageBitmap(null);
                
                if (mBusyIndicator == null)
                {
                    mBusyIndicator = new ProgressBar(mContext);
                    mBusyIndicator.setIndeterminate(true);
                    mBusyIndicator.setBackgroundResource(R.drawable.busy);
                    addView(mBusyIndicator);
                    mBusyIndicator.setVisibility(INVISIBLE);
                    mHandler.postDelayed(new Runnable()
                    {
                        public void run()
                        {
                            if (mBusyIndicator != null)
                            {
                                mBusyIndicator.setVisibility(VISIBLE);
                            }
                        }
                    }, PROGRESS_DIALOG_DELAY);
                }
            }
            
            protected void onPostExecute(LinkInfo[] v)
            {
                removeView(mBusyIndicator);
                mBusyIndicator = null;
                
                mEntire.setImageBitmap(mEntireBm);
                mLinks = v;
                
                invalidate();
                base = false;
                
                if (HandWriteTool.mEditor != null)
                {
                    for (int i = 0; i < HandWriteTool.mEditor.length; i++)
                    {
                        if (HandWriteTool.mEditor[i] != null)
                        {
                            removeView((View)HandWriteTool.mEditor[i]);
                            if (HandWriteTool.type == 0)
                            {
                                removeView(((CustomHandWriteEditor)HandWriteTool.mEditor[i]));
                            }
                            else if (HandWriteTool.type == 1)
                            {
                                removeView(((SpenView)HandWriteTool.mEditor[i]));
                            }
                            
                        }
                    }
                    
                    if (HandWriteTool.mEditor[mPageNumber] == null)
                    {
                        if (HandWriteTool.type == 0)
                        {
                            HandWriteTool.mEditor[mPageNumber] = new CustomHandWriteEditor(mContext);
                            ((CustomHandWriteEditor)HandWriteTool.mEditor[mPageNumber]).setBaseW(mSize.x);
                            ((CustomHandWriteEditor)HandWriteTool.mEditor[mPageNumber]).setBaseH(mSize.y);
                            
                            ((CustomHandWriteEditor)HandWriteTool.mEditor[mPageNumber]).setPaintColor(HandWriteTool.mSignColor);
                            ((CustomHandWriteEditor)HandWriteTool.mEditor[mPageNumber]).setStress(HandWriteTool.mSignStress);
                        }
                        else if (HandWriteTool.type == 1)
                        {
                            HandWriteTool.mEditor[mPageNumber] = new SpenView(mContext);
                            ((SpenView)HandWriteTool.mEditor[mPageNumber]).setBaseW(mSize.x);
                            ((SpenView)HandWriteTool.mEditor[mPageNumber]).setBaseH(mSize.y);
                        }
                    }
                    
                    try
                    {
                        if (HandWriteTool.type == 0)
                        {
                            addView(((CustomHandWriteEditor)HandWriteTool.mEditor[mPageNumber]));
                        }
                        else if (HandWriteTool.type == 1)
                        {
                            addView(((SpenView)HandWriteTool.mEditor[mPageNumber]));
                        }
                    }
                    catch (IllegalStateException e)
                    {
                        // TODO: handle exception
                        if (HandWriteTool.type == 0)
                        {
                            HandWriteTool.mEditor[mPageNumber] = null;
                            
                            HandWriteTool.mEditor[mPageNumber] = new CustomHandWriteEditor(mContext);
                            ((CustomHandWriteEditor)HandWriteTool.mEditor[mPageNumber]).setBaseW(mSize.x);
                            ((CustomHandWriteEditor)HandWriteTool.mEditor[mPageNumber]).setBaseH(mSize.y);
                            
                            ((CustomHandWriteEditor)HandWriteTool.mEditor[mPageNumber]).setPaintColor(HandWriteTool.mSignColor);
                            ((CustomHandWriteEditor)HandWriteTool.mEditor[mPageNumber]).setStress(HandWriteTool.mSignStress);
                            addView(((CustomHandWriteEditor)HandWriteTool.mEditor[mPageNumber]));
                        }
                        else if (HandWriteTool.type == 1)
                        {
                            HandWriteTool.mEditor[mPageNumber] = null;
                            HandWriteTool.mEditor[mPageNumber] = new SpenView(mContext);
                            
                            addView(((SpenView)HandWriteTool.mEditor[mPageNumber]));
                        }
                        
                    }
                    
                    if (HandWriteTool.type == 0)
                    {
                        bringChildToFront((CustomHandWriteEditor)HandWriteTool.mEditor[mPageNumber]);
                    }
                    else if (HandWriteTool.type == 1)
                    {
                        bringChildToFront((SpenView)HandWriteTool.mEditor[mPageNumber]);
                    }
                    
                }
            }
        };
        
        mDrawEntire.execute();
        
        if (mSearchView == null)
        {
            mSearchView = new View(mContext)
            {
                @Override
                protected void onDraw(Canvas canvas)
                {
                    super.onDraw(canvas);
                    float scale = mSourceScale * (float)getWidth() / (float)mSize.x;
                    Paint paint = new Paint();
                    
                    if (!mIsBlank && mSearchBoxes != null)
                    {
                        // Work out current total scale factor
                        // from source to view
                        paint.setColor(HIGHLIGHT_COLOR);
                        for (RectF rect : mSearchBoxes)
                            canvas.drawRect(rect.left * scale, rect.top * scale, rect.right * scale, rect.bottom
                                * scale, paint);
                    }
                    
                    if (!mIsBlank && mLinks != null && mHighlightLinks)
                    {
                        // Work out current total scale factor
                        // from source to view
                        paint.setColor(LINK_COLOR);
                        for (RectF rect : mLinks)
                            canvas.drawRect(rect.left * scale, rect.top * scale, rect.right * scale, rect.bottom
                                * scale, paint);
                    }
                }
            };
            
            addView(mSearchView);
        }
        
        requestLayout();
    }
    
    public void setSearchBoxes(RectF searchBoxes[])
    {
        mSearchBoxes = searchBoxes;
        if (mSearchView != null)
        {
            mSearchView.invalidate();
        }
    }
    
    public void setLinkHighlighting(boolean f)
    {
        mHighlightLinks = f;
        if (mSearchView != null)
        {
            mSearchView.invalidate();
        }
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int x, y;
        switch (View.MeasureSpec.getMode(widthMeasureSpec))
        {
            case View.MeasureSpec.UNSPECIFIED:
                x = mSize.x;
                break;
            default:
                x = View.MeasureSpec.getSize(widthMeasureSpec);
        }
        switch (View.MeasureSpec.getMode(heightMeasureSpec))
        {
            case View.MeasureSpec.UNSPECIFIED:
                y = mSize.y;
                break;
            default:
                y = View.MeasureSpec.getSize(heightMeasureSpec);
        }
        
        setMeasuredDimension(x, y);
        
        if (mBusyIndicator != null)
        {
            int limit = Math.min(mParentSize.x, mParentSize.y) / 2;
            mBusyIndicator.measure(View.MeasureSpec.AT_MOST | limit, View.MeasureSpec.AT_MOST | limit);
        }
    }
    
    private boolean base = true;
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        int w = right - left;
        int h = bottom - top;
        
        if (mEntire != null)
        {
            mEntire.layout(0, 0, w, h);
        }
        if (mSearchView != null)
        {
            mSearchView.layout(0, 0, w, h);
        }
        
        if (mPatchViewSize != null)
        {
            // LogUtil.i("hoperun", "changed=" + changed + ";mPatchViewSize.x=" + mPatchViewSize.x +
            // ";mPatchViewSize.y="
            // + mPatchViewSize.y);
            if (mPatchViewSize.x != w || mPatchViewSize.y != h)
            {
                // Zoomed since patch was created
                mPatchViewSize = null;
                mPatchArea = null;
                if (mPatch != null)
                {
                    mPatch.setImageBitmap(null);
                }
            }
            else if (mPatch != null && mPatchArea != null)
            {
                mPatch.layout(mPatchArea.left, mPatchArea.top, mPatchArea.right, mPatchArea.bottom);
                // LogUtil.i("hoperun", "changed=" + changed + ";mPatchArea.left=" + mPatchArea.left +
                // ";mPatchArea.top="
                // + mPatchArea.top + ";mPatchArea.right=" + mPatchArea.right + ";mPatchArea.bottom="
                // + mPatchArea.bottom);
            }
        }
        if (mBusyIndicator != null)
        {
            int bw = mBusyIndicator.getMeasuredWidth();
            int bh = mBusyIndicator.getMeasuredHeight();
            
            mBusyIndicator.layout((w - bw) / 2, (h - bh) / 2, (w + bw) / 2, (h + bh) / 2);
        }
        
        if (HandWriteTool.mEditor[mPageNumber] != null)
        {
            if (HandWriteTool.type == 0)
            {
                ((CustomHandWriteEditor)HandWriteTool.mEditor[mPageNumber]).layout(0, 0, w, h);
                bringChildToFront((CustomHandWriteEditor)HandWriteTool.mEditor[mPageNumber]);
                
            }
            else if (HandWriteTool.type == 1)
            {
                ((SpenView)HandWriteTool.mEditor[mPageNumber]).layout(0, 0, w, h);
                bringChildToFront((SpenView)HandWriteTool.mEditor[mPageNumber]);
            }
        }
    }
    
    public void addHq()
    {
        Rect viewArea = new Rect(getLeft(), getTop(), getRight(), getBottom());
        // If the viewArea's size matches the unzoomed size, there is no need for an hq patch
        if (viewArea.width() != mSize.x || viewArea.height() != mSize.y)
        {
            Point patchViewSize = new Point(viewArea.width(), viewArea.height());
            Rect patchArea = new Rect(0, 0, mParentSize.x, mParentSize.y);
            
            // Intersect and test that there is an intersection
            if (!patchArea.intersect(viewArea))
            {
                return;
            }
            // Offset patch area to be relative to the view top left
            patchArea.offset(-viewArea.left, -viewArea.top);
            
            // If being asked for the same area as last time, nothing to do
            if (patchArea.equals(mPatchArea) && patchViewSize.equals(mPatchViewSize))
            {
                return;
            }
            
            // Stop the drawing of previous patch if still going
            if (mDrawPatch != null)
            {
                mDrawPatch.cancel(true);
                mDrawPatch = null;
            }
            
            // Create and add the image view if not already done
            if (mPatch == null)
            {
                mPatch = new OpaqueImageView(mContext);
                mPatch.setScaleType(ImageView.ScaleType.FIT_CENTER);
                addView(mPatch);
                mSearchView.bringToFront();
            }
            
            Bitmap bm = Bitmap.createBitmap(patchArea.width(), patchArea.height(), Bitmap.Config.ARGB_8888);
            
            mDrawPatch = new AsyncTask<PatchInfo, Void, PatchInfo>()
            {
                protected PatchInfo doInBackground(PatchInfo... v)
                {
                    drawPage(v[0].bm,
                        v[0].patchViewSize.x,
                        v[0].patchViewSize.y,
                        v[0].patchArea.left,
                        v[0].patchArea.top,
                        v[0].patchArea.width(),
                        v[0].patchArea.height());
                    
                    return v[0];
                }
                
                @SuppressWarnings("deprecation")
                protected void onPostExecute(PatchInfo v)
                {
                    mPatchViewSize = v.patchViewSize;
                    mPatchArea = v.patchArea;
                    
                    mPatch.setImageBitmap(v.bm);
                    // requestLayout();
                    // Calling requestLayout here doesn't lead to a later call to layout. No idea
                    // why, but apparently others have run into the problem.
                    mPatch.layout(mPatchArea.left, mPatchArea.top, mPatchArea.right, mPatchArea.bottom);
                    
                    invalidate();
                    
                }
            };
            mDrawPatch.execute(new PatchInfo(bm, patchViewSize, patchArea));
        }
    }
    
    public void removeHq()
    {
        // Stop the drawing of the patch if still going
        if (mDrawPatch != null)
        {
            mDrawPatch.cancel(true);
            mDrawPatch = null;
        }
        
        // And get rid of it
        mPatchViewSize = null;
        mPatchArea = null;
        if (mPatch != null)
        {
            mPatch.setImageBitmap(null);
        }
    }
    
    public int getPage()
    {
        return mPageNumber;
    }
    
    @Override
    public void removeAllViews()
    {
        // TODO Auto-generated method stub
        if (mDrawEntire != null)
        {
            mDrawEntire.cancel(true);
            mDrawEntire = null;
        }
        super.removeAllViews();
    }
    
}
