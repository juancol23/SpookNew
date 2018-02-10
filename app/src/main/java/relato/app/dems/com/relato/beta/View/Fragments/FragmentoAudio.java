package relato.app.dems.com.relato.beta.View.Fragments;


import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

import relato.app.dems.com.relato.beta.Model.ItemFeed;
import relato.app.dems.com.relato.beta.R;
import relato.app.dems.com.relato.beta.Service.Sounds.BackgroundSoundService;
import relato.app.dems.com.relato.beta.View.Details.BlogSingleActicity;
import relato.app.dems.com.relato.beta.View.Details.DetailsRelato;
import relato.app.dems.com.relato.beta.View.FeedRelatos;
import relato.app.dems.com.relato.beta.View.Util.DownLoadTask.DownloadTask;
import relato.app.dems.com.relato.beta.View.Util.ViewHolder.RelatoViewHolderStructuraAudio;
import relato.app.dems.com.relato.beta.View.Util.ViewHolder.RelatoViewHolderStructure;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentoAudio extends Fragment {
    private RecyclerView mRecyclerAudio;
    private DatabaseReference mDatabaseAudio;
    private ProgressDialog mProgress;

    private Button mBtnPausa;

    private Dialog MyDialog;


    public FragmentoAudio() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fragmento_audio, container, false);
        mBtnPausa = v.findViewById(R.id.btnPausa);
        //detener musica
        Intent svc = new Intent(getContext(), BackgroundSoundService.class);
        getActivity().stopService(svc);


        mDatabaseAudio = FirebaseDatabase.getInstance().getReference().child("Audios");
        mDatabaseAudio.keepSynced(true);

        LinearLayoutManager layoutManagerTrending
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        layoutManagerTrending.setReverseLayout(true);
        layoutManagerTrending.setStackFromEnd(true);

        mRecyclerAudio = (RecyclerView) v.findViewById(R.id.recycler_fragment_audio_);
        mRecyclerAudio.setHasFixedSize(true);

        mRecyclerAudio.setLayoutManager(layoutManagerTrending);

        Query queryRef = mDatabaseAudio.orderByChild("category").equalTo("Terror");

        FirebaseRecyclerAdapter<ItemFeed, RelatoViewHolderStructuraAudio> firebaseRecyclerAdapterAudio = new FirebaseRecyclerAdapter<ItemFeed, RelatoViewHolderStructuraAudio>(
                ItemFeed.class,
                R.layout.design_structure_relato_menu_audio,
                RelatoViewHolderStructuraAudio.class,
                mDatabaseAudio.limitToLast(10)

        ) {
            @Override
            protected void populateViewHolder(RelatoViewHolderStructuraAudio viewHolder, ItemFeed model, final int position) {
                final String post_key = getRef(position).getKey();
                viewHolder.setTitle(model.getTitle());
                viewHolder.setCatergory(model.getCategory());
                viewHolder.setAuthor(model.getAuthor());

                viewHolder.setImage(getContext(), model.getImage());

                viewHolder.mViewStructure_audio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {

                        mDatabaseAudio.child(post_key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String url_audio = (String) dataSnapshot.child("url_audio").getValue();
                                String name_audio = (String) dataSnapshot.child("name_audio").getValue();
                                String name_extension= (String) dataSnapshot.child("name_extension").getValue();

                                File file = new File (Environment
                                        .getExternalStorageDirectory()
                                        .getPath()+"/Android/data/"
                                        +getActivity().getPackageName()
                                        +"/files/sdcard/DirName/"
                                        +name_audio+"/"
                                        +name_extension);

                                if (!file.exists()) {

                                    MyCustomAlertDialog(post_key,url_audio,name_audio,name_extension);
                                }else{

                                    Intent i = new Intent(getContext(), BlogSingleActicity.class);
                                    i.putExtra("blog_id", post_key);
                                    startActivity(i);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        Log.v("id","id"+post_key);
                    }
                });
            }
        };

        mRecyclerAudio.setAdapter(firebaseRecyclerAdapterAudio);

        return v;

    }

    public void downloadImage(String url,String name_space, String name_extention) {
        DownloadTask downloadTask = new DownloadTask(getActivity());
        downloadTask.execute(url,name_space,name_extention);
    }


    public void download(){
        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        // Uri uri = Uri.parse("https://legalmovil.com/Codigos/c_de_comercio.html");
        Uri uri = Uri.parse("http://sentir-terror.com/media/audios/ninop.mp3");
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("Sincronizando");
        request.setDescription("Sincronizado");

        request.setDestinationInExternalFilesDir(getApplicationContext(),"/sdcard/DirName/pro_penales","index.html");
        Long reference = downloadManager.enqueue(request);
    }

    public void loadSource(String name_audio, String name_extension) {

        File folder = new  File( Environment
                .getExternalStorageDirectory()
                .getPath()+"/Android/data/"
                +getApplicationContext()
                .getPackageName()
                +"/files/sdcard/DirName/"+name_audio+"/");

        File output_file = new File(folder, name_extension);
        String path = output_file.toString();
        //imageView.setImageDrawable(Drawable.createFromPath(path));
        final MediaPlayer player;
        player = MediaPlayer.create(getApplicationContext(), Uri.parse(path));
        Log.i("playy", "Path: " + path);


        player.start();



        mBtnPausa.setVisibility(View.VISIBLE);
        mBtnPausa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.pause();
            }
        });

    }


    //agregamos una alerta tipo bootstrap o materialize
    public void MyCustomAlertDialog(final String post_key, final String url_audio, final String name_audio, final String name_extension){

        MyDialog = new Dialog(getActivity());
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.activity_alert_popup);


    /*    mDatabaseTreding = FirebaseDatabase.getInstance().getReference().child("Audios");
        mDatabaseTreding.keepSynced(true);



        mDatabaseTreding.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String popup_title = (String) dataSnapshot.child("title").getValue();
                String popup_image = (String) dataSnapshot.child("image").getValue();
                String popup_resumen = (String) dataSnapshot.child("desc").getValue();

                ImageView mImage_popup = MyDialog.findViewById(R.id.popup_image);
                TextView mTitle_popup = MyDialog.findViewById(R.id.popup_title);
                TextView mResumen_popup = MyDialog.findViewById(R.id.popup_resumen);


                mTitle_popup.setText(popup_title);

                //mResumen_popup.setText(popup_resumen.substring(0,100));
                mResumen_popup.setText("asd");

                Glide.with(getActivity())
                        .load(popup_image)
                        .thumbnail(Glide.with(getActivity()).load(R.drawable.item_placeholder))
                        .into(mImage_popup);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
*/

    Button mPopup_read = MyDialog.findViewById(R.id.popup_read);
        mPopup_read.setEnabled(true);


        mPopup_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.dismiss();
                download();
                downloadImage(url_audio,name_audio,name_extension);
            }
        });



        MyDialog.show();

    }



    public void onClickCerrarPopup(View v){
        //v.setEnabled(true);
        MyDialog.dismiss();
    }

}
