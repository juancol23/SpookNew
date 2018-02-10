package relato.app.dems.com.relato.beta.View.Fragments.Details;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import relato.app.dems.com.relato.beta.R;
import relato.app.dems.com.relato.beta.View.Details.DetailsRelato;
import relato.app.dems.com.relato.beta.View.FeedRelatos;

/**
 * Created by CORAIMA on 30/11/2017.
 */

public class DetalleHistoria extends AppCompatActivity {

    private String mPost_key = null;
    private DatabaseReference mDatabase;
    private ImageView mImage_paralax;
    private TextView mPostTitleDetails,mPostDescDetails;
    private Button mPostRemoveDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_single_acticity);


        mPostTitleDetails = (TextView) findViewById(R.id.postTitleDetails);
        mPostDescDetails = (TextView) findViewById(R.id.postDescDetails);
        mImage_paralax = (ImageView) findViewById(R.id.image_paralax);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Historias");
        mPost_key = getIntent().getExtras().getString("blog_id");

        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("image").getValue();
                //String post_uid = (String) dataSnapshot.child("uid").getValue();
                // Toast.makeText(BlogSingleActicity.this,""+post_title+post_desc+post_image,Toast.LENGTH_SHORT).show();

                mPostTitleDetails.setText(post_title);
                mPostDescDetails.setText(post_desc);

                //validarVisibilidadAudio();
                Glide.with(getApplicationContext())
                        .load(post_image)
                        .into(mImage_paralax);

                Log.v("post_all",""+post_image+post_title+post_desc);
                //setToolbar(post_title);
                if (getSupportActionBar() != null) // Habilitar up button
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


}
