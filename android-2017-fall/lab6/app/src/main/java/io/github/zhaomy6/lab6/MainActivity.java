package io.github.zhaomy6.lab6;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity implements View.OnClickListener {
    private MusicService ms;
    private ServiceConnection connection;
    private Handler handler;

    //  缓存常用界面元素
    private SeekBar bar = null;
    private TextView usedTime = null;
    private TextView remainedTime = null;

    private double coverAngle = 0.0;
    private int counter = 0;
    private Runnable rotateThread;
    private SimpleDateFormat sdf = new SimpleDateFormat("mm:ss", Locale.CHINA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  动态初始化界面元素
        this.initUIElement();
        this.initBufferedUIElements();
        this.setButtonClickListeners();

        //  绑定MusicService
        this.initConnection();
        this.connectService();
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        unbindService(connection);
    }

    //  初始化一些UI元素
    private void initUIElement() {
        LinearLayout l = (LinearLayout) findViewById(R.id.activity_main);
        l.setBackgroundResource(R.mipmap.background);
    }

    //  设置连接到服务的回调函数
    private void initConnection() {
        this.connection = new ServiceConnection() {
            @Override  //  连接建立，获取提供服务的引用
            public void onServiceConnected(ComponentName name, IBinder service) {
                ms = ((MusicService.MusicBinder) service).getService();
                //  进度条初始化
                bar.setMax(ms.getMusicDuration());
                bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            ms.setPosition(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        usedTime.setText(sdf.format(new Date(ms.getMusicCurrentPosition())));
                        remainedTime.setText(sdf.format(new Date(ms.getMusicDuration() - ms.getMusicCurrentPosition())));
                    }
                });

                //  时间显示初始化配置
                usedTime.setText(sdf.format(new Date(ms.getMusicCurrentPosition())));
                remainedTime.setText(sdf.format(new Date(ms.getMusicDuration() - ms.getMusicCurrentPosition())));
            }

            @Override  //  此时断开连接，解除引用
            public void onServiceDisconnected(ComponentName name) {
                ms = null;
            }
        };
    }

    //  连接到对应服务
    private void connectService() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    //  绑定按钮点击事件
    private void setButtonClickListeners() {
        Button playButton = (Button) findViewById(R.id.play_button);
        playButton.setOnClickListener(this);
        Button stopButton = (Button) findViewById(R.id.stop_button);
        stopButton.setOnClickListener(this);
        Button quitButton = (Button) findViewById(R.id.quit_button);
        quitButton.setOnClickListener(this);
    }

    //  初始化缓存的界面元素
    private void initBufferedUIElements() {
        this.bar = (SeekBar) findViewById(R.id.seek_bar);
        this.usedTime = (TextView) findViewById(R.id.time_used);
        this.remainedTime = (TextView) findViewById(R.id.time_remain);
    }

    //  i == 0 注册线程
    //  i == 1 注销线程
    private void dealWithHandle(int i) {
        final ImageView imageView = (ImageView) findViewById(R.id.cover);

        if (this.handler == null) {
            this.handler = new Handler();
        }
        if (this.rotateThread == null) {
            this.rotateThread = new Runnable() {
                @Override
                public void run() {
                    //  更新UI
                    //  coverAngle计数器每0.025秒会更新一次
                    handler.postDelayed(this, 25);
                    coverAngle += 0.50;
                    ++counter;
                    //  避免计数器溢出
                    if (coverAngle > 3600) coverAngle -= 3600;
                    imageView.setRotation((float)(coverAngle));

                    int duration = ms.getMusicDuration();
                    int pos = ms.getMusicCurrentPosition();
                    //  设置进度条更新，用计数器拖慢其刷新次数
                    if (counter > 10) {
                        bar.setProgress(pos);
                        counter -= 10;
                    }
                    //  设置时间显示的更新
                    usedTime.setText(sdf.format(new Date(pos)));
                    remainedTime.setText(sdf.format(new Date(duration - pos)));
                }
            };
        }

        //  设置旋转动画
//        ImageView imageView = (ImageView) findViewById(R.id.cover);
//        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
//        LinearInterpolator lin = new LinearInterpolator();
//        operatingAnim.setInterpolator(lin);

        if (i == 0) {
            handler.post(this.rotateThread);
        } else if (i == 1) {
            handler.removeCallbacks(this.rotateThread);
        }
    }

    //  注册按钮点击触发的不同事件
    @Override
    public void onClick(View v) {
        final TextView statusView = (TextView) findViewById(R.id.status);
        final Button pb = (Button) findViewById(R.id.play_button);
        switch (v.getId()) {
            case R.id.play_button:
                if ("PLAYING".equals(ms.getStatus())) {
                    dealWithHandle(1);
                    statusView.setText(R.string.pause);
                    pb.setText(R.string.play);
                } else {
                    bar.setEnabled(true);
                    dealWithHandle(0);
                    statusView.setText(R.string.play);
                    pb.setText(R.string.pause);
                }
                ms.play();
                break;
            case R.id.stop_button:
                bar.setEnabled(false);
                dealWithHandle(1);
                statusView.setText(R.string.stop);
                pb.setText(R.string.play);
                ms.stop();
                break;
            case R.id.quit_button:
                dealWithHandle(1);
                try {
                    MainActivity.this.finish();
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("MainActivity", "Service Quit");
                break;
        }
    }
}
