package relato.app.dems.com.relato.Front;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import relato.app.dems.com.relato.R;






public class FrontDetails extends AppCompatActivity {
    private DatabaseReference mDatabaseHeader;
    private TextView mPostTitleDetails,mPostDescDetails;
    private FloatingActionButton m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_details);

        mPostTitleDetails = (TextView) findViewById(R.id.postTitleDetails);
        mPostDescDetails = (TextView) findViewById(R.id.postDescDetails);


        mDatabaseHeader = FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabaseHeader.keepSynced(true);

        mDatabaseHeader.child("portada").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("images").getValue();

                ImageView image_paralax = findViewById(R.id.front_image_paralax);
                Log.v("imagen","imagen"+post_image);
                mPostTitleDetails.setText(post_title);
                mPostDescDetails.setText(post_desc);

                Glide.with(FrontDetails.this)
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

    public void share(View v) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Esta app te hará sufrir un infarto con sus Sangrientas Lecturas, Descargala YA!!: https://play.google.com/store/apps/details?id=relato.app.dems.com.relato&rdid=relato.app.dems.com.relato");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void letraMas(View v) {
        mPostTitleDetails = (TextView) findViewById(R.id.postTitleDetails);
        mPostDescDetails = (TextView) findViewById(R.id.postDescDetails);

        int valorLetraDes = (int) mPostDescDetails.getTextSize();

        if(valorLetraDes < 38){
            int aumentar = valorLetraDes +10;
            Log.v("tts","tt"+valorLetraDes);
            mPostTitleDetails.setTextSize(aumentar);
            mPostDescDetails.setTextSize(TypedValue.COMPLEX_UNIT_SP,mPostDescDetails.getTextSize()+1);
         }
    }

    public void letraMenos(View v){
       /* mPostTitleDetails = (TextView) findViewById(R.id.postTitleDetails);
        mPostDescDetails = (TextView) findViewById(R.id.postDescDetails);

        int valorLetra = (int) mPostDescDetails.getTextSize();

        if(valorLetra > 14) {

            Log.v("tts", "tt" + valorLetra);
            mPostDescDetails.setTextSize(TypedValue.COMPLEX_UNIT_SP,mPostDescDetails.getTextSize()-1);
        }*/
    }
    private void setToolbar(String titulo) {
        // Añadir la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(titulo);
        setSupportActionBar(toolbar);
    }




}
