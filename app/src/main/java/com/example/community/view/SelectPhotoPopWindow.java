package com.example.community.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.community.R;

/**
 * 向上滑出现选择图片的界面
 */
public class SelectPhotoPopWindow extends PopupWindow{

    private Button takePhotoButton,pickPhotoButton,cancelButton;
    private View mMenuView;
    private PopupWindow popupWindow;
    public SelectPhotoPopWindow(Activity context, View.OnClickListener itemOnClick){
        super(context);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView=inflater.inflate(R.layout.layout_picture_selector,null);
        takePhotoButton=mMenuView.findViewById(R.id.takePhotoButton);
        pickPhotoButton=mMenuView.findViewById(R.id.pickPhotoButton);
        cancelButton=mMenuView.findViewById(R.id.cancelButton);
        //取消按钮
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        //设置按钮监听
        takePhotoButton.setOnClickListener(itemOnClick);
        pickPhotoButton.setOnClickListener(itemOnClick);
        //设置弹出窗口的view
        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(android.R.style.Animation_InputMethod);
        ColorDrawable dw=new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height=mMenuView.findViewById(R.id.pop_layout).getTop();
                int y=(int)event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height)
                        dismiss();
                }
                return true;
            }
        });
    }



}

