package com.example.nobulijava.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nobulijava.R;
import com.example.nobulijava.model.QuizObj;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder>{
    private ArrayList<QuizObj> quizDataSet;
    /**
     * Initialize the dataset of the Adapter.
     *
     * @param quizDataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public QuizAdapter(ArrayList<QuizObj> quizDataSet) {
        this.quizDataSet = quizDataSet;
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
    public static class QuizViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewQuizNo;
        private final TextView textViewQuestion;
        private final TextView textViewAnswer;

        public QuizViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textViewQuizNo = (TextView) view.findViewById(R.id.textView_quizList_quizNo);
            textViewQuestion = (TextView) view.findViewById(R.id.textView_quizList_question);
            textViewAnswer = (TextView) view.findViewById(R.id.textView_quizList_answer);
        }

        public void bind(QuizObj quizObj, Integer position){
            Integer quizNo = position + 1;
            textViewQuizNo.setText(quizNo.toString());
            textViewQuestion.setText(quizObj.getQuestion());
            textViewAnswer.setText(WordUtils.capitalizeFully(quizObj.getAnswer().toString()));
        }
    }
}
