package com.tieutech.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//ABOUT: The third activity that opens up in the app.
//       Where results of the quiz are displayed
public class ResultActivity extends AppCompatActivity {

    //========== Declare variables ===============================================================
    //----- View variables -----
    TextView congratulatoryMessageTextView; //Display for congratulatory text
    TextView scoreTextView; //Display for score
    Button takeNewQuizButton; //Button to retake the quiz
    Button finishButton; //Button to quit the application

    //----- Keys for retrieving data from QuestionActivity and passing data to MainActivity -----
    static final String FINAL_SCORE = "final_score"; //Key for final score
    static final String ENTERED_NAME = "entered_name"; //Key for entered name
    static final String MAX_NUM_QUESTIONS = "max_num_questions"; //Key for max no. of questions

    //----- Other variables -----
    int finalScore; //Final score obtained
    String enteredName; //Name entered
    int maxNumQuestions; //Max. number of questions

    //========== Define Methods ==================================================================
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //Link variables to views
        congratulatoryMessageTextView = (TextView) findViewById(R.id.congratulatoryMessageTextView);
        scoreTextView = (TextView) findViewById(R.id.scoreTextView);
        takeNewQuizButton = (Button) findViewById(R.id.takeNewQuizButton);
        finishButton = (Button) findViewById(R.id.finishButton);

        //RETRIEVE data from QuestionActivity
        Intent intent = getIntent(); //Create intent
        enteredName = intent.getStringExtra(ENTERED_NAME); //Name entered
        finalScore = intent.getIntExtra(FINAL_SCORE, 0); //Final score obtained
        maxNumQuestions = intent.getIntExtra(MAX_NUM_QUESTIONS, 0); //Max number of questions

        //Update all the TextView views
        congratulatoryMessageTextView.setText("Congratulations " + enteredName + "!");
        scoreTextView.setText(finalScore + "/" + maxNumQuestions);
    }

    //Onclick listener for the "Take New Quiz" Button
    public void takeNewQuiz(View view) {
        //START the QuestionActivity and send data across
        Intent intent = new Intent(ResultActivity.this, MainActivity.class); //Create intent to start MainActivity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //Clear the activities stack
        intent.putExtra(ENTERED_NAME, enteredName); //Entered name
        startActivity((intent));
    }

    //Onclick listener for the "Finish" Button
    public void finish(View view) {
        System.exit(0); //Exit the application
    }
}