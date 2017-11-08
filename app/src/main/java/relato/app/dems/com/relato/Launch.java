package relato.app.dems.com.relato;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

public class Launch extends AppCompatActivity {
    private VideoView mVideoViewLaunch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);



        mVideoViewLaunch= findViewById(R.id.videoViewLaunch);

//https://www.youtube.com/watch?v=MFbTNKOisqM

        Uri uri  = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.b);
        mVideoViewLaunch.setVideoURI(uri);
        mVideoViewLaunch.start();

        mVideoViewLaunch.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });


    }
}
