package com.example.community.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.community.R;
import com.example.community.domain.News;
import com.example.community.newsUtils.NewsAdapter;
import com.example.community.newsUtils.NewsTask;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements NewsTask.NewsCallBack{

    private static String URL_NEWS="http://v.juhe.cn/toutiao/index?type=top&key=c9651fbe718340b4d3f343ba53132338";
    private ListView lv;
    private ArrayList<News> list=new ArrayList<>();
    private ArrayList<News> list1=new ArrayList<>();
    private String citySubing;
    private Toolbar toolbar;
    private NewsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        setContentView(R.layout.activity_news);
        toolbar=findViewById(R.id.newsToolbar);
        setSupportActionBar(toolbar);


        initView();
        initData(URL_NEWS);
        initAdapter();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = list.get(position).getUrl();
                Intent intent = new Intent(NewsActivity.this, DetailsActivity.class);
                intent.putExtra("news_url",url);
                startActivity(intent);
            }
        });
    }
    private void initAdapter() {
        adapter = new NewsAdapter(list, NewsActivity.this);
        lv.setAdapter(adapter);
    }

    private void initData(String Url_News) {
        new NewsTask(this, NewsActivity.this).execute(Url_News);
    }

    private void initView() {
        lv=findViewById(R.id.newsView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        citySubing = URL_NEWS.substring(URL_NEWS.indexOf("type=")+5,URL_NEWS.indexOf("&key"));
        switch (item.getItemId()) {
            case R.id.action_top:
                URL_NEWS = URL_NEWS.replaceAll(citySubing,"top");
                initData(URL_NEWS);
                Toast.makeText(NewsActivity.this, R.string.top, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_yule:
                URL_NEWS = URL_NEWS.replaceAll(citySubing,"yule");
                initData(URL_NEWS);
                Toast.makeText(NewsActivity.this, R.string.yule, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_tiyu:
                URL_NEWS = URL_NEWS.replaceAll(citySubing,"tiyu");
                initData(URL_NEWS);
                Toast.makeText(NewsActivity.this, R.string.tiyu, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_caijing:
                URL_NEWS = URL_NEWS.replaceAll(citySubing,"caijing");
                initData(URL_NEWS);
                Toast.makeText(NewsActivity.this, R.string.caijing, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_guoji:
                URL_NEWS = URL_NEWS.replaceAll(citySubing,"guoji");
                initData(URL_NEWS);
                Toast.makeText(NewsActivity.this, R.string.guoji, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_guonei:
                URL_NEWS = URL_NEWS.replaceAll(citySubing,"guonei");
                initData(URL_NEWS);
                Toast.makeText(NewsActivity.this, R.string.guonei, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_junshi:
                URL_NEWS = URL_NEWS.replaceAll(citySubing,"junshi");
                initData(URL_NEWS);
                Toast.makeText(NewsActivity.this, R.string.junshi, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_keji:
                URL_NEWS = URL_NEWS.replaceAll(citySubing,"keji");
                initData(URL_NEWS);
                Toast.makeText(NewsActivity.this, R.string.keji, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_shehui:
                URL_NEWS = URL_NEWS.replaceAll(citySubing,"shehui");
                initData(URL_NEWS);
                Toast.makeText(NewsActivity.this, R.string.shehui, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_shishang:
                URL_NEWS = URL_NEWS.replaceAll(citySubing,"shishang");
                initData(URL_NEWS);
                Toast.makeText(NewsActivity.this, R.string.shishang, Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getNews(ArrayList<News> result) {
        list.clear();
        list.addAll(result);
        adapter.notifyDataSetChanged();
    }
}
