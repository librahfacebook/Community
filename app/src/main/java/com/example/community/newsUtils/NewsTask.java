package com.example.community.newsUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;

import com.example.community.domain.News;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NewsTask extends AsyncTask<String,Void,ArrayList<News>>{
    public interface NewsCallBack{
        void getNews(ArrayList<News> list);
    }

    private NewsCallBack news;
    private ProgressDialog pd;

    public NewsTask(NewsCallBack news, Context context) {
        this.news = news;
        pd = new ProgressDialog(context);
        pd.setMessage("loading...");
        pd.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd.show();
    }

    @Override
    protected ArrayList<News> doInBackground(String... params) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(params[0]).openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                byte[] b = new byte[1024];
                int num = -1;
                StringBuffer sb = new StringBuffer();
                while ((num = is.read(b)) != -1) {
                    sb.append(new String(b,0,num));
                }
                SystemClock.sleep(1000);
                String json = sb.toString();
                ArrayList<News> list = new ArrayList<>();
                News news;
                JSONObject jo = new JSONObject(json);
                if (jo.getString("reason").equals("成功的返回")) {
                    JSONObject jo1 = jo.getJSONObject("result");
                    JSONArray ja = jo1.getJSONArray("data");
                    for (int i = 0; i < ja.length(); i++) {
                        news = new News();
                        JSONObject obj = ja.getJSONObject(i);
                        news.setTitle(obj.getString("title"));
                        news.setDate(obj.getString("date"));
                        news.setAuthor(obj.getString("author_name"));
                        news.setImage(obj.getString("thumbnail_pic_s"));
                        news.setUrl(obj.getString("url"));
                        list.add(news);
                    }
                    return list;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<News> newses) {
        super.onPostExecute(newses);
        pd.dismiss();
        if (news != null) {
            news.getNews(newses);
        }
    }
}
