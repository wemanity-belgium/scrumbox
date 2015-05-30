package com.wemanity.scrumbox.android.gui.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wemanity.scrumbox.android.R;

import java.io.IOException;
import java.util.List;

class CameraView extends FrameLayout implements SurfaceHolder.Callback, Camera.PictureCallback {

    public interface PictureTakenListener {
        void onPictureTaken(byte[] data);
    }

    private ImageView valideView;
    private ImageView cancelView;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private Size mPreviewSize;
    private List<Size> mSupportedPreviewSizes;
    private Camera mCamera;
    private byte[] lastPreview;
    private PictureTakenListener pictureTakenListener;
    private OnClickListener surfaceOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            if (mCamera != null) {
                mCamera.takePicture(shutterCallback, rawCallback, CameraView.this);
            }
        }
    };

    public CameraView(Context context) {
        super(context);
        mSurfaceView = new SurfaceView(context);
        mSurfaceView.setOnClickListener(surfaceOnClickListener);
        addView(mSurfaceView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER));

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(linearLayout, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(80, 80);
        layoutParams.setMargins(10,20,10,20);

        cancelView = new ImageView(context);
        cancelView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        cancelView.setImageResource(R.drawable.member_cancel);
        cancelView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCam();
            }
        });
        linearLayout.addView(cancelView, layoutParams);

        valideView = new ImageView(context);
        valideView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        valideView.setImageResource(R.drawable.member_valide);
        valideView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pictureTakenListener != null) {
                    pictureTakenListener.onPictureTaken(lastPreview);
                }
                lastPreview = null;
                setButtonVisibility(View.INVISIBLE);
            }
        });
        linearLayout.addView(valideView, layoutParams);

        setButtonVisibility(View.INVISIBLE);
        invalidate();
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
    }

    public void setCamera(Camera camera) {
        mCamera = camera;
        if (mCamera != null) {
            mCamera.setDisplayOrientation(90);
            mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
            requestLayout();

            // get Camera parameters
            Camera.Parameters params = mCamera.getParameters();

            List<String> focusModes = params.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                // set the focus mode
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                // set Camera parameters
                mCamera.setParameters(params);
            }
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the camera and tell it where
        // to draw.
        try {
            mSurfaceView.setOnClickListener(surfaceOnClickListener);
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
            }
        } catch (IOException exception) {
            Log.e("CameraView", "IOException caused by setPreviewDisplay()", exception);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }


    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            if (mSupportedPreviewSizes != null && mPreviewSize == null) {
                mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, w, h);
            }
            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
            requestLayout();

            mCamera.setParameters(parameters);
            mCamera.startPreview();
        }
    }


    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            //			 Log.d(TAG, "onShutter'd");
        }
    };

    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d("TAG", "onPictureTaken - jpeg");
        }
    };

    private void resetCam() {
        mCamera.startPreview();
        lastPreview = null;
        setButtonVisibility(View.INVISIBLE);
        mSurfaceView.setOnClickListener(surfaceOnClickListener);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        lastPreview = data;
        setButtonVisibility(View.VISIBLE);
        mSurfaceView.setOnClickListener(null);
        Log.d("TAG", "onPictureTaken - jpeg");
    }

    private void setButtonVisibility(int visibility){
        cancelView.setVisibility(visibility);
        valideView.setVisibility(visibility);
    }

    public PictureTakenListener getPictureTakenListener() {
        return pictureTakenListener;
    }

    public void setPictureTakenListener(PictureTakenListener listener) {
        this.pictureTakenListener = listener;
    }
}
