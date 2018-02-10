package relato.app.dems.com.relato.beta.View.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import relato.app.dems.com.relato.beta.View.Details.BlogSingleActicity;
import relato.app.dems.com.relato.beta.Model.ItemFeed;
import relato.app.dems.com.relato.beta.R;
import relato.app.dems.com.relato.beta.View.Details.DetailsRelato;
import relato.app.dems.com.relato.beta.View.Util.ViewHolder.RelatoViewHolderStructure;

/**
 * A simple {@link Fragment} subclass.
 */
public class Misticas extends Fragment {
    private RecyclerView mRecyclerMisticas;
    private DatabaseReference mDatabaseMisticas;
    private ProgressDialog mProgress;


    public Misticas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragmento_misticas, container, false);

        mProgress = new ProgressDialog(getContext());

        mDatabaseMisticas = FirebaseDatabase.getInstance().getReference().child("Historias");
        mDatabaseMisticas.keepSynced(true);

        LinearLayoutManager layoutManagerTrending
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        layoutManagerTrending.setReverseLayout(true);
        layoutManagerTrending.setStackFromEnd(true);

        mRecyclerMisticas = (RecyclerView) v.findViewById(R.id.fragmento_misticas_);
        mRecyclerMisticas.setHasFixedSize(true);

        mRecyclerMisticas.setLayoutManager(layoutManagerTrending);

        Query queryRef = mDatabaseMisticas.orderByChild("category").equalTo("Misticas");

        FirebaseRecyclerAdapter<ItemFeed, RelatoViewHolderStructure> firebaseRecyclerAdapterMisticas = new FirebaseRecyclerAdapter<ItemFeed, RelatoViewHolderStructure>(
                ItemFeed.class,
                R.layout.design_structure_relato_menu,
                RelatoViewHolderStructure.class,
                queryRef.limitToLast(30)

        ) {
            @Override
            protected void populateViewHolder(RelatoViewHolderStructure viewHolder, ItemFeed model, final int position) {
                final String post_key = getRef(position).getKey();
                viewHolder.setTitle(model.getTitle());
                viewHolder.setCatergory(model.getCategory());
                viewHolder.setAuthor(model.getAuthor());

                viewHolder.setImage(getContext(), model.getImage());

                //viewHolder.setTitle("masacre");

                viewHolder.mViewStructure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProgress.setMessage("Accediendo...");
                        mProgress.show();

                        //Toast.makeText(getContext(),"dale "+post_key,Toast.LENGTH_SHORT).show();
                        Intent singleBlogIntent = new Intent(getContext(), DetailsRelato.class);
                        singleBlogIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        singleBlogIntent.putExtra("blog_id", post_key);
                        startActivity(singleBlogIntent);

                        Log.v("id","id"+post_key);
                    }
                });
            }
        };

        mRecyclerMisticas.setAdapter(firebaseRecyclerAdapterMisticas);





        return v;
    }
    @Override
    public void onStop() {
        mProgress.dismiss();
        super.onStop();
    }
}
