package com.wushengqi.androiddemo.hotfix;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
    private FileWriteListener fileWriteListener;

    public void setFileWriteListener(FileWriteListener fileWriteListener){
        this.fileWriteListener = fileWriteListener;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                if (fileWriteListener != null){
                    fileWriteListener.onFinish();
                }
            }
        }
    };


    public void writeFile(final InputStream inputStream, final String filePath){

        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(filePath);
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(file);
                    int length;
                    byte[] data = new byte[1024];
                    while ((length = inputStream.read(data)) != -1) {
                        fileOutputStream.write(data, 0, length);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (fileOutputStream != null){
                        try {
                            if (inputStream != null){
                                inputStream.close();
                            }
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    handler.sendEmptyMessage(1);


                }
            }
        }).start();
    }


    public interface FileWriteListener{
         void onFinish();
    }
}
