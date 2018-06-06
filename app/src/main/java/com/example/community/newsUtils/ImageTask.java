package com.example.community.newsUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;

public class ImageTask extends AsyncTask<String,Void,Bitmap>{
    public ImageTask(ImageView iv) {
        super();
        this.iv = iv;
    }

    private ImageView iv;

    private String url;  //记录本次图片的下载网址

    @Override
    protected Bitmap doInBackground(String... params) {
        // TODO Auto-generated method stub
        url = params[0];
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(params[0]).openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() == 200) {
                return BitmapFactory.decodeStream(conn.getInputStream());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        //判断本次下载完成的图片网址与iv上存储的tag网址是否相同，相同则显示图片
        if (iv.getTag() !=null && iv.getTag().equals(url)) {
            iv.setImageBitmap(result);
        }

    }
}
