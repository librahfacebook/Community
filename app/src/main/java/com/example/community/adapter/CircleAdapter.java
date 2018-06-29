package com.example.community.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.community.R;
import com.example.community.activity.FriendCircleActivity;
import com.example.community.custom.MyGridView;
import com.example.community.domain.Config;
import com.example.community.domain.FriendCircle;
import com.example.community.domain.Location;
import com.example.community.utils.ImageUtils;
import com.example.community.view.HeartHonorLayout;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;

/**
 * 朋友圈内容布局
 */
public class CircleAdapter extends RecyclerView.Adapter<CircleAdapter.ViewHolder>{
    private List<FriendCircle> circleList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView personalName;
        ImageView personalImage;
        TextView circleText;
        MyGridView circleGirdView;
        TextView locationText;
        Random mRandom=new Random();
        HeartHonorLayout heartHonorLayout;
        ImageView heartClick;
        ImageView commentClick;
        TextView commentText;
        public ViewHolder(View view){
            super(view);
            personalName=view.findViewById(R.id.personalCircleName);
            personalImage=view.findViewById(R.id.personalCircleImage);
            circleText=view.findViewById(R.id.circleText);
            circleGirdView=view.findViewById(R.id.circleGridView);
            locationText=view.findViewById(R.id.locationText);

            heartClick=view.findViewById(R.id.heartClick);
            heartHonorLayout=view.findViewById(R.id.heartLayout);
            commentClick=view.findViewById(R.id.commentClick);
            commentText=view.findViewById(R.id.commentText);
        }
    }
    public CircleAdapter(List<FriendCircle> circleList){
        this.circleList=circleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friendcircle,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        FriendCircle friendCircle=circleList.get(position);
        holder.personalName.setText(friendCircle.getName());
        /*SharedPreferences prefs=holder.itemView.getContext().getSharedPreferences("PersonalData",Context.MODE_PRIVATE);
        String images = prefs.getString("image", "");
        Bitmap bitmap = ImageUtils.convertToBitmap(images);
        friendCircle.setBitmap(bitmap);*/
        holder.personalImage.setImageBitmap(friendCircle.getBitmap());
        holder.circleText.setText(friendCircle.getCircleText());
        List<HashMap<String,Object>> imageItem=new ArrayList<>();
        List<String> imageUrlList=friendCircle.getCircleImageUrlList();
        for(String imageUrl:imageUrlList){
            Log.d("朋友圈图片网址", "onBindViewHolder: "+imageUrl);
            Config.image=null;
            ImageUtils.getHttpBitmap(imageUrl);
            try{
                Thread.sleep(200);
            }catch (Exception e){
                e.printStackTrace();
            }
            Bitmap image= Config.image;
            HashMap<String,Object> map=new HashMap<>();
            map.put("itemImage",image);
            imageItem.add(map);
        }
        SimpleAdapter adapter=new SimpleAdapter(holder.itemView.getContext(),imageItem,R.layout.gridview_add,
                new String[]{"itemImage"},new int[]{R.id.imageView1});
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if(view instanceof ImageView && data instanceof Bitmap){
                    ImageView i=(ImageView) view;
                    i.setImageBitmap((Bitmap)data);
                    return true;
                }
                return false;
            }

        });
        holder.circleGirdView.setAdapter(adapter);
        //显示动态发布地理位置
        String account=friendCircle.getAccount();
        Location location= DataSupport.select("address").where("account=?",account).findFirst(Location.class);
        String address=location.getAddress();
        holder.locationText.setText("地理位置："+address);
        holder.heartClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加点赞特效
                holder.heartHonorLayout.addHeart(Color.rgb(holder.mRandom.nextInt(255),
                        holder.mRandom.nextInt(255),holder.mRandom.nextInt(255)));
                holder.heartClick.setImageResource(R.drawable.heart3);
            }
        });
        StringBuilder comment=new StringBuilder();
        comment.append("李浩：生如夏花之绚烂，\n" +
                "                                   死如秋叶之静美。\n\n");
        comment.append("成天：众里寻他千百度。蓦然回首，\n" +
                "                           那人却在，灯火阑珊处。\n\n");
        comment.append("libarh: 谁念西风独自凉，萧萧黄叶\n" +
                "                         闭疏窗，沉思往事立残阳。\n");

        holder.commentText.setText(comment);
        //评论设置
        holder.commentClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.itemView.getContext(),"评论区",Toast.LENGTH_SHORT).show();
                if(holder.commentText.getVisibility()==View.VISIBLE){
                    holder.commentText.setVisibility(View.GONE);
                }else{
                    holder.commentText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return circleList.size();
    }

}
