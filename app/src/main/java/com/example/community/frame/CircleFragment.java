package com.example.community.frame;
/**
 * 朋友圈以及动态界面
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.community.R;
import com.example.community.activity.FriendCircleActivity;
import com.example.community.activity.NearPeopleActivity;
import com.example.community.activity.NewsActivity;
import com.example.community.activity.WeatherActivity;
import com.example.community.view.CircleMenuLayout;


public class CircleFragment extends Fragment{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private CircleMenuLayout circleMenuLayout;
    private String[] mItemTexts=new String[]{"朋友圈","附近用户","新闻头条",
    "今日天气","今日天气","摇一摇"};
    private int[] mItemImgs=new int[]{R.drawable.ico_circle,R.drawable.ico_near,R.drawable.ico_news,
            R.drawable.ico_weather,R.drawable.snowman,R.drawable.snowman};
    private OnFragmentInteractionListener mListener;
    public CircleFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CircleFragment newInstance(String param1) {
        CircleFragment fragment = new CircleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_circle, container, false);
        circleMenuLayout=view.findViewById(R.id.id_menulayout);
        circleMenuLayout.setMenuItemIconsAndTexts(mItemImgs,mItemTexts);
        circleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                Toast.makeText(getContext(), mItemTexts[pos],
                        Toast.LENGTH_SHORT).show();
                if(mItemTexts[pos].equals("朋友圈")){
                    Intent intent=new Intent(getContext(), FriendCircleActivity.class);
                    intent.putExtra("account","");
                    startActivity(intent);
                }else if(mItemTexts[pos].equals("附近用户")){
                    Intent intent_near=new Intent(getContext(), NearPeopleActivity.class);
                    startActivity(intent_near);
                }else if(mItemTexts[pos].equals("新闻头条")){
                    Intent intent_news=new Intent(getContext(), NewsActivity.class);
                    startActivity(intent_news);
                }else if(mItemTexts[pos].equals("今日天气")){
                    Intent intent_weather=new Intent(getContext(), WeatherActivity.class);
                    startActivity(intent_weather);
                }
            }

            @Override
            public void itemCenterClick(View view) {
                Toast.makeText(getContext(),
                        "you can do something just like ccb  ",
                        Toast.LENGTH_SHORT).show();
            }
        });
        /* friendCircle=view.findViewById(R.id.friendCircle);
        friendCircle.setOnClickListener(this);
        nearPeople=view.findViewById(R.id.nearPeople);
        nearPeople.setOnClickListener(this);*/
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /*@Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.friendCircle:
                Toast.makeText(getContext(),"朋友圈",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getContext(), FriendCircleActivity.class);
                intent.putExtra("account","");
                startActivity(intent);
                break;
            case R.id.nearPeople:
                Toast.makeText(getContext(),"附近的人",Toast.LENGTH_SHORT).show();
                Intent intent_near=new Intent(getContext(), NearPeopleActivity.class);
                startActivity(intent_near);
                break;
        }
    }*/
}
