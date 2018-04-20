package com.utils;

import android.app.Application;
import android.content.Context;
import android.support.v4.util.LongSparseArray;


import com.bean.Music;
import com.bean.SheetInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hzwangchenyan on 2016/11/23.
 */
public class AppCache {
    private Context mContext;
    private final List<Music> mLocalMusicList = new ArrayList<>();
    private final List<SheetInfo> mSheetList = new ArrayList<>();

    private AppCache() {
    }

    private static class SingletonHolder {
        private static AppCache instance = new AppCache();
    }

    public static AppCache getInstance() {
        return SingletonHolder.instance;
    }

    public void init(Application application) {
        mContext = application.getApplicationContext();
        ToastUtils.init(mContext);
        Preferences.init(mContext);
        ScreenUtils.init(mContext);
        CoverLoader.get().init(mContext);
    }

    public Context getContext() {
        return mContext;
    }

    public List<Music> getLocalMusicList() {
        return mLocalMusicList;
    }

    public List<SheetInfo> getSheetList() {
        return mSheetList;
    }

}
