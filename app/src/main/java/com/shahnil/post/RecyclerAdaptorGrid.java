package com.shahnil.post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdaptorGrid extends RecyclerView.Adapter<RecyclerAdaptorGrid.ViewHolder> {

    private ArrayList<userinformation> userlist;
    private Context mcontext;
    private Onitemclicklistener mlistener;

    public interface Onitemclicklistener{
        void onitemclick(int position);
    }

    public void setOnitemclicklistener(Onitemclicklistener listener){
        mlistener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mPost;
        public ImageView imageView;
        public ViewHolder(@NonNull View itemView, final Onitemclicklistener listener) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageidGrid);

           // mPost = itemView.findViewById(R.id.textViewPost);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            listener.onitemclick(position);
                        }
                    }

                }
            });
        }
    }

    public RecyclerAdaptorGrid(Context context, ArrayList<userinformation> userlist) {
        mcontext = context;
        this.userlist = userlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_grid_data_layout, parent ,false);
        ViewHolder vh = new ViewHolder(view,mlistener);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        userinformation user = userlist.get(position);


       // holder.mPost.setText(user.getmText1());

        Picasso.get().load(user.getImageUrl())
                .fit()
                .centerCrop()
                .into(holder.imageView);






    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }
}
