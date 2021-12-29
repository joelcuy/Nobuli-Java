package com.example.nobulijava.activity;

import static android.view.View.GONE;

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
    List<MessageObj> messageList = new ArrayList<>();

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

        recyclerViewChat = findViewById(R.id.chatRecyclerView);
        editTextMessageBox = findViewById(R.id.messageBoxEditText);
        btnSend = findViewById(R.id.sendButton);
        textViewDisclaimer = findViewById(R.id.textView_userChatBot_textDisclaimer);

        messageAdapter = new MessageAdapter(messageList, this);
        recyclerViewChat.setAdapter(messageAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewDisclaimer.setVisibility(GONE);
                String message = editTextMessageBox.getText().toString();
                if (!message.isEmpty()) {

                    messageList.add(new MessageObj(message, false));
                    editTextMessageBox.setText("");
                    sendMessageToBot(message);
                    Objects.requireNonNull(recyclerViewChat.getAdapter()).notifyDataSetChanged();
                    Objects.requireNonNull(recyclerViewChat.getLayoutManager())
                            .scrollToPosition(messageList.size() - 1);
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
                messageList.add(new MessageObj(botReply, true, returnResponse.getQueryResult().getIntent().getDisplayName()));
                messageAdapter.notifyDataSetChanged();
                if (returnResponse.getQueryResult().getIntent().getDisplayName().equals("Identify Cyberbully - report")){
                    messageList.add(new MessageObj("Is there anything I can help you with?", true, "null"));
                }
                messageAdapter.notifyDataSetChanged();
                Objects.requireNonNull(recyclerViewChat.getLayoutManager()).scrollToPosition(messageList.size() - 1);
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Failed to connect!", Toast.LENGTH_SHORT).show();
        }
    }
}