package com.example.nobulijava.model;

import java.io.Serializable;

public class QuizObj implements Serializable {
    private String question;
    private Boolean answer;
    private String quizID;

    public QuizObj(String question, Boolean answer) {
        this.question = question;
        this.answer = answer;
    }

    public QuizObj(){

    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Boolean getAnswer() {
        return answer;
    }

    public void setAnswer(Boolean answer) {
        this.answer = answer;
    }

    public String getQuizID() {
        return quizID;
    }

    public void setQuizID(String quizID) {
        this.quizID = quizID;
    }

    @Override
    public String toString() {
        return "QuizObj{" +
                "question='" + question + '\'' +
                ", answer=" + answer +
                ", quizID='" + quizID + '\'' +
                '}';
    }
}
