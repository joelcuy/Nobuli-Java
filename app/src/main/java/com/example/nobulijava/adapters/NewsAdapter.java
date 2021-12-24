package com.example.nobulijava.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nobulijava.R;
import com.example.nobulijava.activity.AdminNewsEditActivity;
import com.example.nobulijava.activity.AdminQuizEditActivity;
import com.example.nobulijava.model.NewsObj;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{
    private ArrayList<NewsObj> newsDataSet;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private final Context context;

    public NewsAdapter(ArrayList<NewsObj> newsDataSet, Context context) {
        this.newsDataSet = newsDataSet;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_news, parent, false);

        return new NewsAdapter.NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int position) {
        newsViewHolder.bind(newsDataSet.get(position), position);
    }

    @Override
    public int getItemCount() {
        return newsDataSet.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{
        private final TextView textViewTitle;
        private final TextView textViewContent;
        private final TextView textViewDatePosted;
        private final ImageView imageViewNews;

        String FOLDER_NAME = "news_image";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        Uri uriSelectedImage;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = (TextView) itemView.findViewById(R.id.textView_newsList_title);
            textViewContent = (TextView) itemView.findViewById(R.id.textView_newsList_content);
            textViewDatePosted = (TextView) itemView.findViewById(R.id.textView_newsList_datePosted);
            imageViewNews = (ImageView) itemView.findViewById(R.id.imageView_newsList_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO launch update screen
                    Intent editNewsIntent = new Intent(context, AdminNewsEditActivity.class);
                    editNewsIntent.putExtra("SelectedNews", newsDataSet.get(getAdapterPosition()));
                    context.startActivity(editNewsIntent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Delete");
                    alert.setMessage("Do you wanna delete this item?");
                    alert.setCancelable(true);

                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            mDatabase.child("News").child(newsDataSet.get(getAdapterPosition()).getNewsID()).removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    //TODO notify deleted news
                                }
                            });
                        }
                    });

                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                    return true;
                }
            });
        }

        public void bind(NewsObj newsObj, Integer position){
            textViewTitle.setText(newsObj.getTitle());
            textViewContent.setText(newsObj.getContent().substring(0, 105) + "...");
            textViewDatePosted.setText(newsObj.getDatePosted());

            StorageReference gsReference = storage.getReferenceFromUrl("gs://nobulibot-ysta.appspot.com/" + FOLDER_NAME + "/" + newsObj.getNewsID());
            gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context).load(uri).into(imageViewNews);
                }
            });
        }
    }
}
