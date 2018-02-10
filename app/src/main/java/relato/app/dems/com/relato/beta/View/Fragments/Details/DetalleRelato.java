package relato.app.dems.com.relato.beta.View.Fragments.Details;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import relato.app.dems.com.relato.beta.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetalleRelato extends Fragment {
    private String mPost_key = null;
    private DatabaseReference mDatabase;
    private ImageView mImage_paralax;
    private TextView mPostTitleDetails,mPostDescDetails;

    public DetalleRelato() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_detalle_relato, container, false);




        mPostTitleDetails = (TextView) v.findViewById(R.id.postTitleDetails);
        mPostDescDetails = (TextView) v.findViewById(R.id.postDescDetails);
        mImage_paralax = (ImageView) v.findViewById(R.id.image_paralax);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Historias");
     // mPost_key = getIntent().getExtras().getString("blog_id");
        mPost_key = getArguments().getString("blog_id");




        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("image").getValue();

                mPostTitleDetails.setText(post_title);
                mPostDescDetails.setText(post_desc);

                //validarVisibilidadAudio();
                Glide.with(getActivity().getApplicationContext())
                        .load(post_image)
                        .into(mImage_paralax);

                Log.v("post_all",""+post_image+post_title+post_desc);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Log.v("blog_id",""+mPost_key);


        return v;




    }


}
