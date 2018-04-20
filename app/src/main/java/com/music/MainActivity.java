package com.music;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.bean.Music;
import com.service.AudioPlayer;
import com.service.OnPlayerEventListener;
import com.utils.NetworkUtils;
import com.utils.Preferences;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MusicActivity.class);
                startActivity(intent);
            }
        });

    }

    private void checkNetwork() {
        boolean mobileNetworkPlay = Preferences.enableMobileNetworkPlay();
        if (NetworkUtils.isActiveNetworkMobile(this) && !mobileNetworkPlay) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.tips);
            builder.setMessage(R.string.play_tips);
            builder.setPositiveButton(R.string.play_tips_sure, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Preferences.saveMobileNetworkPlay(true);
//                    getPlayInfoWrapper();
                }
            });
            builder.setNegativeButton(R.string.cancel, null);
            Dialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else {
//            getPlayInfoWrapper();
        }
    }

}
