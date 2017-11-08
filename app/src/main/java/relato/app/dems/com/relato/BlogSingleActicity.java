package relato.app.dems.com.relato;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BlogSingleActicity extends AppCompatActivity {
    private String mPost_key = null;
    private DatabaseReference mDatabase;
    private ImageView mImage_paralax;
    private TextView mPostTitleDetails,mPostDescDetails;
    private Button mPostRemoveDetails;
    private ProgressDialog mProgresDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_single_acticity);
        mPostTitleDetails = (TextView) findViewById(R.id.postTitleDetails);
        mPostDescDetails = (TextView) findViewById(R.id.postDescDetails);
        mImage_paralax = (ImageView) findViewById(R.id.image_paralax);
        mPostRemoveDetails = (Button) findViewById(R.id.postRemoveDetails);
        mProgresDialog= new ProgressDialog(this);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
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
                Glide.with(BlogSingleActicity.this)
                        .load(post_image)
                        .into(mImage_paralax);


                setToolbar(post_title);
                if (getSupportActionBar() != null) // Habilitar up button
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mPostRemoveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgresDialog.setMessage("Posteando al Blog");
                mProgresDialog.show();
                mDatabase.child(mPost_key).removeValue();
                startActivity(new Intent(BlogSingleActicity.this,Feed.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                mProgresDialog.dismiss();
                finish();

            }
        });

        /*
        Toast.makeText(BlogSingleActicity.this,""+mPost_key,Toast.LENGTH_SHORT).show();
        Log.v("clicks","click"+mPost_key);*/
    }



    private void setToolbar(String title) {
        // Añadir la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
    }
}
