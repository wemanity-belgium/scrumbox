package com.wemanity.scrumbox.android.gui.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import com.wemanity.scrumbox.android.R;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageReplacerView extends FrameLayout implements CameraView.PictureTakenListener{
    private ImageView avatarImageView;

    private ImageView cameraButton;
    private ViewFlipper flipper;
    private Camera mCamera;
    private Drawable cameraDrawable;
    private Drawable noCameraDrawable;
    private Drawable defaultAvater;
    private boolean cameraOn = false;
    private CameraView cameraView;
    public ImageReplacerView(Context context) {
        super(context);
        init(context);
    }

    public ImageReplacerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImageReplacerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        cameraDrawable = context.getResources().getDrawable(R.drawable.member_camera);
        noCameraDrawable = context.getResources().getDrawable(R.drawable.member_no_camera);
        defaultAvater = context.getResources().getDrawable(R.drawable.default_profile_avatar);

        flipper = new ViewFlipper(context);
        flipper.showNext();
        this.addView(flipper, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER));

        avatarImageView = new ImageView(context);
        avatarImageView.setImageDrawable(defaultAvater);
        flipper.addView(avatarImageView, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER));

        cameraView = new CameraView(context);
        cameraView.setPictureTakenListener(this);
        flipper.addView(cameraView, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER));
        cameraView.setKeepScreenOn(true);


        cameraButton = new ImageView(context);
        cameraButton.setImageDrawable(cameraDrawable);
        cameraButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cameraOn){
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = null;
                    cameraView.setCamera(null);
                    cameraButton.setImageDrawable(cameraDrawable);
                } else {
                    mCamera = Camera.open(0);
                    cameraView.setCamera(mCamera);
                    cameraButton.setImageDrawable(noCameraDrawable);
                }
                flipper.showNext();
                cameraOn = !cameraOn;
            }
        });
        this.addView(cameraButton, new FrameLayout.LayoutParams(70, 70, Gravity.TOP | Gravity.RIGHT));
    }

    @Override
    public void onPictureTaken(byte[] data) {
        new DownloadImageTask(avatarImageView){
            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                cameraButton.callOnClick();
            }
        }.execute(data);
        //new SaveImageTask().execute(data);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mCamera != null){
            mCamera.stopPreview();
            mCamera.release();
        }
    }

    private class SaveImageTask extends AsyncTask<String, Void, Void> {
        ImageView bmImage;
        private SaveImageTask(ImageView bmImage){
            this.bmImage = bmImage;
        }

        @Override
        protected Void doInBackground(String... data) {
            Drawable drawable = bmImage.getDrawable();
            if (drawable instanceof BitmapDrawable){
                Bitmap bmp = ((BitmapDrawable)drawable).getBitmap();
                FileOutputStream out = null;
                try {
                    File file = new File(data[0]);
                    file.mkdirs();
                    out = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }

    private class DownloadImageTask extends AsyncTask<byte[], Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(byte[]... datas) {
            Bitmap mIcon11 = null;
            if (datas == null){
                return null;
            }
            byte[] data = datas[0];
            InputStream ims = null;
            try {
                ims = new ByteArrayInputStream(data);
                // load image as Drawable
                mIcon11 = BitmapFactory.decodeStream(ims);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            } finally {
                if (ims != null) {
                    try {
                        ims.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void saveDrawable(String path){
        new SaveImageTask(avatarImageView).execute(path);
    }
}
