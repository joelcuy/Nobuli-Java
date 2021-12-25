package com.example.nobulijava.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.nobulijava.R;
import com.example.nobulijava.model.ReportRecipientObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminReportEditActivity extends AppCompatActivity {
    private EditText editTextRecipientEmail;
    private EditText editTextRecipientName;
    private Button buttonAddEmail;
    private ListView listViewEmail;

    private TextView listViewText1;
    private TextView listViewText2;


    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private ArrayList<ReportRecipientObj> recipientObjArrayList = new ArrayList<>();
    private ArrayAdapter<ReportRecipientObj> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_report_edit);

        findViewById();

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, recipientObjArrayList);
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View view = super.getView(position, convertView, parent);
//                listViewText1 = (TextView) view.findViewById(android.R.id.text1);
//                listViewText2 = (TextView) view.findViewById(android.R.id.text2);
//
//                listViewText1.setText(persons.get(position).getName());
//                listViewText2.setText(persons.get(position).getAge());
//
//                return view;
//            }
//        };
        listViewEmail.setAdapter(arrayAdapter);

        mDatabase.child("Report").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recipientObjArrayList.clear();
                for (DataSnapshot recipientSnapshot : snapshot.getChildren()) {
                    ReportRecipientObj recipientObj = recipientSnapshot.getValue(ReportRecipientObj.class);
                    recipientObjArrayList.add(recipientObj);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buttonAddEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recipientNameString = editTextRecipientName.getText().toString().trim();
                String recipientEmailString = editTextRecipientEmail.getText().toString().trim();

                ReportRecipientObj recipientObj = new ReportRecipientObj(recipientNameString, recipientEmailString);
                mDatabase.child("Report").push().setValue(recipientObj);

                editTextRecipientName.setText("");
                editTextRecipientEmail.setText("");

            }
        });
    }

    public void findViewById() {
        editTextRecipientEmail = findViewById(R.id.editText_adminReport_recipientEmail);
        editTextRecipientName = findViewById(R.id.editText_adminReport_recipientName);
        buttonAddEmail = findViewById(R.id.button_adminReport_addEmail);
        listViewEmail = findViewById(R.id.listView_adminReport_emailList);
    }
}