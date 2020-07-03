package com.wushengqi.camera.view;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by sqwu on 2020/1/16
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "CameraPreview";

    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mHolder = getHolder();
        mHolder.addCallback(this);

        mCamera = camera;

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Log.d(TAG, "surfaceCreated");
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "预览失败");
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged");
        if (mHolder.getSurface() == null){
            return;
        }

        mCamera.stopPreview();

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");
    }
}
