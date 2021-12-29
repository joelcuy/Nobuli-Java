package com.example.nobulijava.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nobulijava.R;
import com.example.nobulijava.model.MessageObj;
import com.example.nobulijava.model.ReportRecipientObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{

    private List<MessageObj> messageList;
    private Activity activity;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private ArrayList<String> recipientEmailArrayList = new ArrayList<>();
    String[] recipientEmailArray;

    public MessageAdapter(List<MessageObj> messageList, Activity activity) {
        this.messageList = messageList;
        this.activity = activity;
    }

    @NonNull @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.template_message, parent, false);
        return new MyViewHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String text = messageList.get(position).getText();
        boolean isBot = messageList.get(position).isBot();
        String intent = messageList.get(position).getIntent();
        if(isBot){
            holder.messageReceive.setVisibility(View.VISIBLE);
            holder.messageSend.setVisibility(View.GONE);
            holder.action1.setVisibility(View.GONE);
            holder.action2.setVisibility(View.GONE);
            holder.action3.setVisibility(View.GONE);
            holder.action4.setVisibility(View.GONE);
            holder.messageReceive.setText(text);

            mDatabase.child("Report").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    recipientEmailArrayList.clear();
                    for (DataSnapshot reportRecipientSnapshot: snapshot.getChildren()){
                        ReportRecipientObj reportRecipientObj = reportRecipientSnapshot.getValue(ReportRecipientObj.class);
                        recipientEmailArrayList.add(reportRecipientObj.getEmail());
                    }
                    recipientEmailArray = new String[recipientEmailArrayList.size()];
                    for (int i = 0; i < recipientEmailArrayList.size(); i++) {
                        recipientEmailArray[i] = recipientEmailArrayList.get(i);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            if (intent.equals("Identify Cyberbully - report")){
                holder.action1.setVisibility(View.VISIBLE);
                holder.action1.setText("Report");
                holder.action1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                        intent.putExtra(Intent.EXTRA_EMAIL, recipientEmailArray);
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Reporting Cyberbullying Case");
                        intent.putExtra(Intent.EXTRA_TEXT, "Hi, I would like to report a cyberbullying case that I am experiencing right now.\n <Insert further elaboration here>");
                        activity.startActivity(intent);
                    }
                });
            }
        }else {
            holder.messageSend.setVisibility(View.VISIBLE);
            holder.messageReceive.setVisibility(View.GONE);
            holder.messageSend.setText(text);
        }
    }

    @Override public int getItemCount() {
        return messageList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView messageSend;
        TextView messageReceive;
        Button action1;
        Button action2;
        Button action3;
        Button action4;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            messageSend = itemView.findViewById(R.id.sendMessageTextView);
            messageReceive = itemView.findViewById(R.id.receiveMessageTextView);
            action1 = itemView.findViewById((R.id.button_message_action1));
            action2 = itemView.findViewById((R.id.button_message_action2));
            action3 = itemView.findViewById((R.id.button_message_action3));
            action4 = itemView.findViewById((R.id.button_message_action4));
        }
    }
}
