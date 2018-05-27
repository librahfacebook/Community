package com.example.community.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.community.R;
import com.example.community.domain.Config;
import com.example.community.domain.FriendCircle;
import com.example.community.domain.Location;
import com.example.community.utils.ImageUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 朋友圈内容布局
 */
public class CircleAdapter extends RecyclerView.Adapter<CircleAdapter.ViewHolder>{
    private List<FriendCircle> circleList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView personalName;
        ImageView personalImage;
        TextView circleText;
        GridView circleGirdView;
        TextView locationText;

        public ViewHolder(View view){
            super(view);
            personalName=view.findViewById(R.id.personalCircleName);
            personalImage=view.findViewById(R.id.personalCircleImage);
            circleText=view.findViewById(R.id.circleText);
            circleGirdView=view.findViewById(R.id.circleGridView);
            locationText=view.findViewById(R.id.locationText);
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return circleList.size();
    }
}
