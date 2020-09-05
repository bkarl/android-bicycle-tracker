package com.bk.bicycletracker;

import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button m_startLogButton;
    private Button m_tracksButton;
    private boolean m_bServiceRunning = false;

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnStartLog)
            {
                if (!m_bServiceRunning)
                {
                    startLoggingService();
                    m_startLogButton.setText("Stop Tracking");
                }
                else
                {
                    stopLoggingService();
                    m_startLogButton.setText("Start Tracking");

                }
            }

            if (v.getId() == R.id.btnTracks)
            {
               // Intent i = new Intent(getApplicationContext(), ActivityTracks.class);
                Intent i = new Intent(getApplicationContext(), TracksTabbed.class);
                startActivityForResult(i, 0);
            }
        }
    };

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if("com.bk.bicycletracker.LoggingService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void startLoggingService()
    {
        Intent loggingService = new Intent(this, LoggingService.class);
        startService(loggingService);

        m_bServiceRunning = true;
        createNotificationChannel();
        //addTrackingNotification();
    }

    private void stopLoggingService()
    {
        Intent loggingService = new Intent(this, LoggingService.class);
        stopService(loggingService);

        removeNotification();
        m_bServiceRunning = false;
    }

    private void removeNotification()
    {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.cancelAll();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.channel_name), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    String[] PERMISSIONS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.FOREGROUND_SERVICE
    };

    public void hasPermissions() {
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(PERMISSIONS, 0);
                return;
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getPreferences(MODE_PRIVATE).edit().remove("weeklyGoal");
        //getPreferences(MODE_PRIVATE).edit().commit();
        m_startLogButton = (Button)this.findViewById(R.id.btnStartLog);
        m_startLogButton.setOnClickListener(this.listener);

        m_tracksButton = (Button)this.findViewById(R.id.btnTracks);
        m_tracksButton.setOnClickListener(this.listener);

        m_bServiceRunning = isServiceRunning();

        if (m_bServiceRunning)
            m_startLogButton.setText("Stop Tracking");

        hasPermissions();
    }


}
