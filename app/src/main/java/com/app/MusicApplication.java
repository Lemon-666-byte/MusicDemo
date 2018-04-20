package com.app;

import android.app.Application;
import android.content.Intent;

import com.service.PlayService;
import com.utils.AppCache;


/**
 * 自定义Application
 * Created by wcy on 2015/11/27.
 */
public class MusicApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppCache.getInstance().init(this);
        Intent intent = new Intent(this, PlayService.class);
        startService(intent);
    }
}
