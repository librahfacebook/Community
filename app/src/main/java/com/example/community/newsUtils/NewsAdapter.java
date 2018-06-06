package com.example.community.newsUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.community.R;
import com.example.community.domain.News;

import java.util.ArrayList;

public class NewsAdapter extends BaseAdapter{
    private ArrayList<News> list;
    private Context context;

    public NewsAdapter(ArrayList<News> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.author = (TextView) convertView.findViewById(R.id.author_name);
            // holder.thumbnail_pic_s = (SmartImageView) convertView.findViewById(R.id.imgsrc);
            holder.image = (ImageView) convertView.findViewById(R.id.imgsrc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(list.get(position).getTitle());
        holder.date.setText(list.get(position).getDate());
        holder.author.setText(list.get(position).getAuthor());

        //加载图片方式一：SmartImageView
        //holder.thumbnail_pic_s.setImageUrl(list.get(position).getThumbnail_pic_s());


        //加载图片方式二：开启ImageTask异步加载图片
        // 设置让iv在图片下载等待过程中显示提示图片
        holder.image.setImageResource(R.drawable.loading_throbber);
        //通过setTag方法让iv上存储当前图片的下载网址
        holder.image.setTag(list.get(position).getImage());
        new ImageTask(holder.image).execute(list.get(position).getImage());
        return convertView;
    }

    class ViewHolder{
        TextView title;
        TextView date;
        TextView author;
        ImageView image;
        //SmartImageView thumbnail_pic_s;
    }
}
