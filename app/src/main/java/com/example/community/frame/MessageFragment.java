package com.example.community.frame;
/**
 * 消息内容主界面
 */

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.community.R;
import com.example.community.adapter.FocusAdapter;
import com.example.community.adapter.TodayNewsAdapter;
import com.example.community.domain.News;
import com.example.community.domain.TodayNews;
import com.example.community.newsUtils.NewsAdapter;
import com.example.community.newsUtils.NewsTask;
import com.example.community.service.TodaynewsService;
import com.example.community.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static String URL_NEWS = "http://v.juhe.cn/toutiao/index?type=top&key=6928eda123d4aef596b726b4addadf48";

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private ArrayList<News> list = new ArrayList<>();
    private ArrayList<News> list1 = new ArrayList<>();
    private String citySubing;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MessageFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MessageFragment newInstance(String param1) {
        MessageFragment fragment = new MessageFragment();
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
        View view=inflater.inflate(R.layout.fragment_message,container,false);
        RecyclerView recyclerView=view.findViewById(R.id.newsView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        TodayNewsAdapter adapter=new TodayNewsAdapter(getData());
        recyclerView.setAdapter(adapter);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
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
    public List<TodayNews> getData(){
        List<TodayNews> todayNewsList= Utility.todayNewsExcute();
        return todayNewsList;
    }
}
