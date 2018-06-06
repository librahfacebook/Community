package com.example.community.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.community.R;
import com.example.community.domain.TodayNews;
import com.example.community.utils.ImageUtils;

import java.util.List;

public class TodayNewsAdapter extends RecyclerView.Adapter<TodayNewsAdapter.ViewHolder>{
    private List<TodayNews> todayNewsList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView today_image;
        TextView today_title;
        TextView today_date;

        public ViewHolder(View itemView) {
            super(itemView);
            today_image=itemView.findViewById(R.id.toady_iamge);
            today_title=itemView.findViewById(R.id.today_title);
            today_date=itemView.findViewById(R.id.today_date);
        }
    }

    public TodayNewsAdapter(List<TodayNews> todayNewsList) {
        this.todayNewsList = todayNewsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.history_today,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TodayNews todayNews=todayNewsList.get(position);
        Glide.with(holder.itemView.getContext()).load(todayNews.getImage()).
                placeholder(R.drawable.loading_throbber).error(R.drawable.error).into(holder.today_image);
        holder.today_title.setText(todayNews.getTitle());
        holder.today_date.setText(todayNews.getYear()+"年"+todayNews.getMonth()
            +"月"+todayNews.getDay()+"日");
    }

    @Override
    public int getItemCount() {
        return todayNewsList.size();
    }
}
