package com.example.nobulijava.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nobulijava.R;
import com.example.nobulijava.adapters.MessageAdapter;
import com.example.nobulijava.interfaces.BotReply;
import com.example.nobulijava.model.MessageObj;
import com.example.nobulijava.utils.SendMessageInBg;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.common.collect.Lists;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class UserChatBotActivity extends AppCompatActivity implements BotReply {

    RecyclerView recyclerViewChat;
    EditText editTextMessageBox;
    ImageButton btnSend;
    TextView textViewDisclaimer;

    MessageAdapter messageAdapter;
    List<MessageObj> messageObjArrayList = new ArrayList<>();

    private DatabaseReference mDatabase;

    //dialogFlow
    private SessionsClient sessionsClient;
    private SessionName sessionName;
    private String uuid = UUID.randomUUID().toString();
    private String TAG = "mainactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chatbot);

        getSupportActionBar().setTitle("Nobuli Chatbot");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        recyclerViewChat = findViewById(R.id.chatRecyclerView);
        editTextMessageBox = findViewById(R.id.messageBoxEditText);
        btnSend = findViewById(R.id.sendButton);
        textViewDisclaimer = findViewById(R.id.textView_userChatBot_textDisclaimer);

        messageAdapter = new MessageAdapter(messageObjArrayList, this);
        recyclerViewChat.setAdapter(messageAdapter);

//        mDatabase.child("Message").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                for (DataSnapshot msgSnapshot: task.getResult().getChildren()){
//                    MessageObj msgObj = msgSnapshot.getValue(MessageObj.class);
//                    messageObjArrayList.add(msgObj);
//                }
//                Objects.requireNonNull(recyclerViewChat.getAdapter()).notifyDataSetChanged();
//            }
//        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewDisclaimer.setVisibility(View.GONE);
                String message = editTextMessageBox.getText().toString().trim();
                if (!message.isEmpty()) {
                    MessageObj newMessage = new MessageObj(message, false);
                    messageObjArrayList.add(newMessage);
                    mDatabase.child("Message").push().setValue(newMessage);
                    editTextMessageBox.setText("");
                    sendMessageToBot(message);
                    Objects.requireNonNull(recyclerViewChat.getAdapter()).notifyDataSetChanged();
                    Objects.requireNonNull(recyclerViewChat.getLayoutManager())
                            .scrollToPosition(messageObjArrayList.size() - 1);
                } else {
                    Toast.makeText(UserChatBotActivity.this, "Please enter text!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setUpBot();
    }

    private void setUpBot() {
        try {
            InputStream stream = this.getResources().openRawResource(R.raw.credential);
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
            String projectId = ((ServiceAccountCredentials) credentials).getProjectId();

            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(
                    FixedCredentialsProvider.create(credentials)).build();
            sessionsClient = SessionsClient.create(sessionsSettings);
            sessionName = SessionName.of(projectId, uuid);

            Log.d(TAG, "projectId : " + projectId);
        } catch (Exception e) {
            Log.d(TAG, "setUpBot: " + e.getMessage());
        }
    }

    private void sendMessageToBot(String message) {
        QueryInput input = QueryInput.newBuilder()
                .setText(TextInput.newBuilder().setText(message).setLanguageCode("en-US")).build();
        new SendMessageInBg(this, sessionName, sessionsClient, input).execute();
    }

    @Override
    public void callback(DetectIntentResponse returnResponse) {
        if (returnResponse != null) {
            String botReply = returnResponse.getQueryResult().getFulfillmentText();
            if (!botReply.isEmpty()) {
                MessageObj newBotMsg = new MessageObj(botReply, true, returnResponse.getQueryResult().getIntent().getDisplayName());
                mDatabase.child("Message").push().setValue(newBotMsg);
                messageObjArrayList.add(newBotMsg);
                messageAdapter.notifyDataSetChanged();
                if (returnResponse.getQueryResult().getIntent().getDisplayName().equals("Identify Cyberbully - report")){
                    messageObjArrayList.add(new MessageObj("Is there anything I can help you with?", true, "null"));
                }
                messageAdapter.notifyDataSetChanged();
                Objects.requireNonNull(recyclerViewChat.getLayoutManager()).scrollToPosition(messageObjArrayList.size() - 1);
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Failed to connect!", Toast.LENGTH_SHORT).show();
        }
    }
}