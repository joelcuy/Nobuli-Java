package com.example.nobulijava.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nobulijava.R;
import com.example.nobulijava.activity.AdminQuizEditActivity;
import com.example.nobulijava.model.QuizObj;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder>{
    private ArrayList<QuizObj> quizDataSet;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private final Context context;
    /**
     * Initialize the dataset of the Adapter.
     *
     * @param quizDataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public QuizAdapter(ArrayList<QuizObj> quizDataSet, Context context) {
        this.quizDataSet = quizDataSet;
        this.context = context;
    }

    // Create new views (invoked by the layout manager
    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.template_quiz, viewGroup, false);

        return new QuizViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder quizViewHolder, int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        quizViewHolder.bind(quizDataSet.get(position), position);
    }

    @Override
    public int getItemCount() {
        return quizDataSet.size();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class QuizViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewQuizNo;
        private final TextView textViewQuestion;
        private final TextView textViewAnswer;

        public QuizViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textViewQuizNo = (TextView) view.findViewById(R.id.textView_quizList_quizNo);
            textViewQuestion = (TextView) view.findViewById(R.id.textView_quizList_question);
            textViewAnswer = (TextView) view.findViewById(R.id.textView_quizList_answer);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO launch update screen
                    Intent editQuizIntent = new Intent(context, AdminQuizEditActivity.class);
                    editQuizIntent.putExtra("SelectedQuiz", quizDataSet.get(getAdapterPosition()));
                    context.startActivity(editQuizIntent);
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
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

                            mDatabase.child("Quiz").child(quizDataSet.get(getAdapterPosition()).getQuizID()).removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    //TODO notify deleted
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

        public void bind(QuizObj quizObj, Integer position){
            Integer quizNo = position + 1;
            textViewQuizNo.setText(quizNo.toString());
            textViewQuestion.setText(quizObj.getQuestion());
            textViewAnswer.setText(WordUtils.capitalizeFully(quizObj.getAnswer().toString()));
        }
    }
}
