package relato.app.dems.com.relato.beta.Service.Sounds;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;

import relato.app.dems.com.relato.beta.R;

/**
 * Created by CORAIMA on 10/11/2017.
 */

public class Random_start_game extends Service {
    private static final String TAG = null;
    private MediaPlayer player;
    private SharedPreferences prefs = null;

    public IBinder onBind(Intent arg0) {

        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this, R.raw.coins_start);
        player.setLooping(true); // Set looping
        player.setVolume(100,100);

        prefs = getSharedPreferences("relato.app.dems.com.relato.beta", MODE_PRIVATE);

        prefs.edit().putBoolean("firstrun", true).commit();

    }
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (prefs.getBoolean("firstrun", true)) {

            player.start();

        }

        return 1;
    }


    public void onStart(Intent intent, int startId) {
        // TO DO
    }
    public IBinder onUnBind(Intent arg0) {
        // TO DO Auto-generated method
        return null;

    }

    public void onStop() {

    }
    public void onPause() {
        player.pause();

    }
    @Override
    public void onDestroy() {
        player.stop();
        player.release();
    }

    @Override
    public void onLowMemory() {

    }
}