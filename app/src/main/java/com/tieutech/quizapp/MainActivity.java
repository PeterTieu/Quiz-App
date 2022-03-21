package com.tieutech.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

//ABOUT: The first activity that opens up in the app.
//       Where the user enters a name prior to starting the quiz
public class MainActivity extends AppCompatActivity {

    //========== Declare variables ===============================================================
    EditText nameEditText; //EditText for the user to enter a name
    static final String ENTERED_NAME = "entered_name"; //Key for the name entered
    String enteredName; //Name entered by the user

    //========== Define Methods ==================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Link nameEditText variable to nameEditText view
        nameEditText = (EditText) findViewById(R.id.nameEditText);

        //RETRIEVE enteredName data from ResultActivity if available
        Intent intent = getIntent();
        enteredName = intent.getStringExtra(ENTERED_NAME);

        //Set text for enteredName if exists
        if (enteredName != null) {
            nameEditText.setText(enteredName);
        }
    }

    //Make a Toast with a specified text
    public void makeToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();;
    }

    //Onclick listener for startQuizButton
    public void startQuiz(View view) {
        enteredName = nameEditText.getText().toString(); //Obtain text from namedEditText

        //If namedEditText has NO entry (i.e. enteredName IS empty)
        if (enteredName.isEmpty()) {
            makeToast("Please enter your name"); //Prompt the user to enter a name
        }
        //If nameEditText HAS an entry (i.e. enteredName is NOT empty)
        else {
            //START the QuestionActivity and send data across
            Intent intent = new Intent(MainActivity.this, QuestionActivity.class); //Create Intent to start QuestionActivity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //Clear the activity stack
            intent.putExtra(ENTERED_NAME, enteredName); //Name entered by the user
            startActivity((intent)); //Start the QuestionActivity
        }
    }
}