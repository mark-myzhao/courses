package io.github.zhaomy6.lab6;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

public class MusicService extends Service {
    private MediaPlayer mp;
    private final IBinder binder = new MusicBinder();

    //  status: IDLE / PLAYING / PAUSE / STOP
    private String status;

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override  //  创建时调用
    public void onCreate() {
        super.onCreate();
        this.status = "IDLE";
        this.mp = new MediaPlayer();
        this.mp.setLooping(true);
        this.mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            this.mp.setDataSource("/data/BlankSpace.mp3");
            this.mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override  //  绑定时调用，返回代理对象
    public IBinder onBind(Intent intent) {
        return this.binder;
    }

    //  播放器功能
    public void play() {
        if ("IDLE".equals(this.status)) {
            Log.d("MusicService", "play");
            this.mp.start();
            this.status = "PLAYING";
        } else if ("PAUSE".equals(this.status)) {
            Log.d("MusicService", "play");
            if (this.mp != null) {
                this.mp.start();
                this.status = "PLAYING";
            }
        } else if ("PLAYING".equals(this.status)) {
            Log.d("MusicService", "pause");
            if (this.mp != null) {
                this.mp.pause();
                this.status = "PAUSE";
            }
        } else if ("STOP".equals(this.status)) {
            this.mp.start();
            this.status = "PLAYING";
        }
    }

    public void stop() {
        Log.d("MusicService", "stop");
        if (mp != null) {
            mp.stop();
            try {
                mp.stop();
                mp.prepare();
                mp.seekTo(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.status = "STOP";
        }
    }

    public String getStatus() {
        return this.status;
    }

    public int getMusicDuration() {
        if (this.mp != null)
            return this.mp.getDuration();
        else
            return -1;
    }

    public int getMusicCurrentPosition() {
        if (this.mp != null)
            return mp.getCurrentPosition();
        else
            return -1;
    }

    public void setPosition(int pos) {
        if (this.mp != null) {
            mp.seekTo(pos);
        }
    }
}
