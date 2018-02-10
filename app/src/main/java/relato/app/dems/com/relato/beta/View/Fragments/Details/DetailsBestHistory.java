package relato.app.dems.com.relato.beta.View.Fragments.Details;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import relato.app.dems.com.relato.beta.R;

public class DetailsBestHistory extends AppCompatActivity {

    private DatabaseReference mDatabaseHeader,mMensajeShare;
    private TextView mPostTitleDetails,mPostDescDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_best_history);

        mPostTitleDetails = (TextView) findViewById(R.id.postTitleDetails);
        mPostDescDetails = (TextView) findViewById(R.id.postDescDetails);

        mDatabaseHeader = FirebaseDatabase.getInstance().getReference();
        mDatabaseHeader.keepSynced(true);

        mDatabaseHeader.child("Portada").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("images").getValue();

                ImageView image_paralax = (ImageView) findViewById(R.id.front_image_paralax);
                Log.v("imagen","imagen"+post_image);
                mPostTitleDetails.setText(post_title);
                mPostDescDetails.setText(post_desc);
                Glide.with(DetailsBestHistory.this)
                        .load(post_image)
                        .into(image_paralax);

                setToolbar(post_title);
                if (getSupportActionBar() != null) // Habilitar up button
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setToolbar(String titulo) {
        // Añadir la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(titulo);
        setSupportActionBar(toolbar);
    }

}
