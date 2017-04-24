package io.github.zhaomy6.lab4;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

public class StaticReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("StaticFilter")) {
            Bundle bundle = intent.getExtras();
            Log.d("SR", bundle.getString("name"));
            Log.d("SR", bundle.getInt("imgId") + "");
            Notification.Builder builder = new Notification.Builder(context);
            Intent intent1 = new Intent(context, MainActivity.class);
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), bundle.getInt("imgId"));
            builder.setContentTitle("静态广播")
                    .setContentText(bundle.getString("name"))
                    .setLargeIcon(bm)
                    .setSmallIcon(bundle.getInt("imgId"))
                    .setAutoCancel(true)
                    .setContentIntent(PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_ONE_SHOT));
            NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = builder.build();
            nManager.notify(0, notification);
        }
    }
}
