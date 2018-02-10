package relato.app.dems.com.relato.beta.View.Util.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import relato.app.dems.com.relato.beta.R;

/**
 * Created by CORAIMA on 24/11/2017.
 */

public class RelatoViewHolderStructuraAudio extends RecyclerView.ViewHolder {

    public View mViewStructure_audio;
    public TextView mItem_recycler_structure_title_audio;
    public TextView mItem_recycler_structure_category_audio;
    public TextView mItem_recycler_structure_author_audio;
    public ImageView mPost_image_audio;
    public ImageView mPost_image_down;
    public ImageView mPost_image_play;


    public RelatoViewHolderStructuraAudio(View itemView) {
        super(itemView);
        mViewStructure_audio = itemView;
    }

    public void setTitle(String title) {
        //mItem_recycler_structure_title.setTypeface(Pacifico);
        mItem_recycler_structure_title_audio = mViewStructure_audio.findViewById(R.id.item_recycler_structure_title_audio);

        mItem_recycler_structure_title_audio.setText(title);
    }

    public void setCatergory(String category) {
        mItem_recycler_structure_category_audio = mViewStructure_audio.findViewById(R.id.item_recycler_structure_category_audio);
        mItem_recycler_structure_category_audio.setText(category);
    }

    public void setAuthor(String author) {
        mItem_recycler_structure_author_audio = mViewStructure_audio.findViewById(R.id.item_recycler_structure_author_audio);
        mItem_recycler_structure_author_audio.setText(author);
    }


    public void setImage(Context context, String image) {
        mPost_image_audio = mViewStructure_audio.findViewById(R.id.item_recycler_structure_imagen_audio);

        Glide.with(context)
                .load(image)
                .thumbnail(Glide.with(context).load(R.drawable.item_placeholder))
                .into(mPost_image_audio);

    }




}