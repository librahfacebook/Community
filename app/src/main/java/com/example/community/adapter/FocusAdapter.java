package com.example.community.adapter;
/**
 * 实现关注用户的列表管理
 */

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.community.R;
import com.example.community.activity.NearPeopleActivity;
import com.example.community.activity.UserDataActivity;
import com.example.community.domain.FocusUser;
import com.example.community.utils.ImageUtils;

import java.util.List;

public class FocusAdapter extends RecyclerView.Adapter<FocusAdapter.ViewHolder>{
    private List<FocusUser> focusList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView focusHeadImage;
        TextView focusName;
        TextView focusIntroduce;

        public ViewHolder(View view) {
            super(view);
            focusHeadImage=view.findViewById(R.id.focusHeadImageView);
            focusName=view.findViewById(R.id.focusNameText);
            focusIntroduce=view.findViewById(R.id.focusIntroduceText);
        }
    }

    public FocusAdapter(List<FocusUser> focusList) {
        this.focusList=focusList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.focus_list,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.focusHeadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                FocusUser user=focusList.get(position);
                Intent intent=new Intent(v.getContext(),UserDataActivity.class);
                intent.putExtra("account",user.getAccount());
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final FocusUser focusUser=focusList.get(position);
        holder.focusHeadImage.setImageBitmap(ImageUtils.toRoundBitmap(ImageUtils.convertToBitmap(focusUser.getHeadImage())));
        holder.focusName.setText(focusUser.getName());
        holder.focusIntroduce.setText(focusUser.getIntroduce());
    }

    @Override
    public int getItemCount() {
        return focusList.size();
    }
}
