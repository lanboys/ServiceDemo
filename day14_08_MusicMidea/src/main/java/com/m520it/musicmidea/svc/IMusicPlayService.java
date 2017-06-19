package com.m520it.musicmidea.svc;

import java.util.ArrayList;

/**
 * Created by 520 on 2016/11/28.
 */
public interface IMusicPlayService {

     void callPlayMusic(ArrayList<String> musicDatas,int position);

     void callStopMusic();

}
