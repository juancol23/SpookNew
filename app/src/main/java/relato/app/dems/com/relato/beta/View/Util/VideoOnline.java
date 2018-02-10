package relato.app.dems.com.relato.beta.View.Util;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import relato.app.dems.com.relato.beta.R;

public class VideoOnline extends AppCompatActivity {
    private VideoView mVideoOnline;
    private Button mBtnStart;
    private Button mBtnPaused;

    private DatabaseReference mDatabaseVersionApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_online);

        mVideoOnline = (VideoView) findViewById(R.id.videoOnline);
        mBtnStart = (Button) findViewById(R.id.mBtnStart);
        mBtnPaused = (Button) findViewById(R.id.mBtnPaused);

        mDatabaseVersionApp = FirebaseDatabase.getInstance().getReference().child("VideoUrl");
        mDatabaseVersionApp.keepSynced(true);
        mDatabaseVersionApp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String videoUrl = (String) dataSnapshot.child("url").getValue();

                String urlVideo = videoUrl;
                Uri uri = Uri.parse(urlVideo);

                mVideoOnline.setVideoURI(uri);
                mVideoOnline.requestFocus();

                mBtnStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mVideoOnline.start();
                    }
                });
                mBtnPaused.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mVideoOnline.pause();
                    }
                });

                Log.v("videoUrl",""+videoUrl);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
