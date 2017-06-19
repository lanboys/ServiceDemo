package com.m520it.musicmidea.adpter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.m520it.musicmidea.fileUtils.FileUtil;

import java.util.ArrayList;

/**
 * Created by 520 on 2016/11/28.
 */
public class MusicAdapter extends BaseAdapter {

    private ArrayList<String> mDatas;

    public ArrayList<String> getmDatas() {
        return mDatas;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView tv = null;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.simple_list_item_1, viewGroup, false);
            tv = (TextView) view.findViewById(android.R.id.text1);
            view.setTag(tv);
        } else {
            tv = (TextView) view.getTag();
        }

        String filePath = mDatas.get(i);
        String musicFileName = FileUtil.getMusicFileName(filePath);
        tv.setText(musicFileName);


        return view;
    }

    public void setDatas(ArrayList<String> datas) {
        mDatas = datas;
    }
}
