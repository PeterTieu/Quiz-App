package com.tieutech.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

//ABOUT: The second activity the app.
//       Where the user answers a series of multiple choice questions
public class QuestionActivity extends AppCompatActivity {

    //========== Declare variables ===============================================================
    //----- View variables -----
    TextView welcomeMessageTextView; //Display for welcome message for the user
    TextView questionProgressTextView; //Display for question progress (e.g. 3/7)
    ProgressBar questionProgressProgressBar; //Progress Bar of the entire quiz

    TextView questionTitleTextView; //Display of the Question Title
    TextView questionDetailsTextView; //Display of the Question Details

    Button possibleAnswer1Button; //Button for the FIRST Possible Answer
    Button possibleAnswer2Button; //Button for the SECOND Possible Answer
    Button possibleAnswer3Button; //Button for the THIRD Possible Answer

    Button submitOrNextButton; //Button the submit the answer for the question

    Button possibleAnswerButtonSelected; //Button for the MOST RECENTLY selected Possible Answer

    //----- Keys for retrieving data from MainActivity and passing data to ResultActivity -----
    static final String ENTERED_NAME = "entered_name"; //Key for name entered
    static final String FINAL_SCORE = "final_score"; //Key for final score
    static final String MAX_NUM_QUESTIONS = "max_num_questions"; //Key for max. no. of questions

    //----- Arrays/ArrayLists to store the data of the questions -----
    String[] questionsAndAnswers; //'Root' Array to contain ALL data
    ArrayList<String> questionTitles; //ArrayList for all Question Titles
    ArrayList<String> questionDetails; //ArrayList for all Question Details
    ArrayList<String> possibleAnswers; //ArrayList for all Possible Answers
    int[] correctAnswerPositions = {1, 3, 2, 3, 1, 2, 2}; //Array to store the positions of the correct Possible Answer of each question of the quiz

    //----- Other variables -----
    String enteredName; //Name entered (passed from MainActivity)
    int maxNumQuestions; //Maximum number of questions
    int score = 0; //Score of quiz
    int questionIndex = 0; //Index to process the arrays (above)
    int questionNumberCounter = 1; //Counter for the question number
    int possibleAnswerPositionSelected; //Position of the most recently selected Possible Answer prior to the user selecting the "Submit" button (e.g. 1, 2, or 3)
    boolean isSubmitButtonActive = true; //Switch to indicate whether the state of the "Submit / Next" button is "Submit" or "Next"

    //========== Define Methods ==================================================================
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        //Link variables to views
        welcomeMessageTextView = (TextView) findViewById(R.id.welcomeMessageTextView);
        questionProgressTextView = (TextView) findViewById(R.id.questionProgressTextView);
        questionProgressProgressBar = (ProgressBar) findViewById(R.id.questionProgressProgressBar);
        questionTitleTextView = (TextView) findViewById(R.id.questionTitleTextView);
        questionDetailsTextView = (TextView) findViewById(R.id.questionDetailsTextView);
        possibleAnswer1Button = (Button) findViewById(R.id.possibleAnswer1Button);
        possibleAnswer2Button = (Button) findViewById(R.id.possibleAnswer2Button);
        possibleAnswer3Button = (Button) findViewById(R.id.possibleAnswer3Button);
        submitOrNextButton = (Button) findViewById(R.id.submitOrNextButton);

        //RETRIEVE data from MainActivity
        Intent intent = getIntent(); //Create intent
        enteredName = intent.getStringExtra(ENTERED_NAME); //Name entered

        welcomeMessageTextView.setText("Welcome " + enteredName + "!"); //Set the text of the welcomeMessageTextView

        resetPossibleAnswersColors(); //Reset the colors of all the Possible Answers to grey

        //Read all text from the file: 'questions_and_answers.txt' (saved in the folder: src->res->raw)
        // and store them into the questionsAndAnswers array
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.questions_and_answers); //Open the InputStream for the .txt file
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); //BufferReader linked to the InputStream
            StringBuilder stringBuilder = new StringBuilder(); //StringBuilder to store data of the .txt file

            //Loop through the txt file and store all String data to the StringBuilder
            for (String line; (line = bufferedReader.readLine()) != null; ) {
                stringBuilder.append(line).append('\n');
            }

            String result = stringBuilder.toString(); //Obtain the StringBuilder in String form

            //VERIFICATION CHECKPOINT
            Log.i("result String", result); //Verify that 'result' contains all the text from the .txt file

            //Store all result values (i.e. text from the .txt file) into the questionsAndAnswers array.
            // All elements are split by newline in the result variable
            questionsAndAnswers = result.split("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        maxNumQuestions = questionsAndAnswers.length / 5; //Obtain the maximum number of questions

        //Declare ArrayLists to store question data
        questionTitles = new ArrayList<String>();
        questionDetails = new ArrayList<String>();
        possibleAnswers = new ArrayList<String>();

        //Form to the ArrayLists of question data based on the 'root' array, questionsAndAnswers
        for (int i = 0; i < questionsAndAnswers.length - 1; i++) {

            //Obtain ArrayList for containing all Question Titles
            if (i % 5 == 0) {
                questionTitles.add(questionsAndAnswers[i]);
            }

            //Obtain ArrayList containing all Question Details
            if ((i - 1) % 5 == 0) {
                questionDetails.add(questionsAndAnswers[i]);
            }

            //Obtain ArrayList containing all Possible Answers
            if (!(i % 5 == 0) && !((i - 1) % 5 == 0)) {
                possibleAnswers.add(questionsAndAnswers[i]);
            }
        }

        //VERIFICATION CHECKPOINT - verify the contents of the array and all ArrayLists
        //Verify questionsAndAnswers array contains all question data
        for (int i = 0; i < questionsAndAnswers.length - 1; i++) {
            Log.i("questionsAndAnswers", questionsAndAnswers[i]);
        }

        //Verify questionsAndAnswers array contains all Question Titles
        for (int i = 0; i < questionTitles.size(); i++) {
            Log.i("questionTitles", questionTitles.get(i));
        }

        //Verify questionsAndAnswers array contains all Question Details
        for (int i = 0; i < questionDetails.size(); i++) {
            Log.i("questionDetails", questionDetails.get(i));
        }

        //Verify questionsAndAnswers array contains all Possible Answers
        for (int i = 0; i < possibleAnswers.size(); i++) {
            Log.i("possibleAnswers", possibleAnswers.get(i));
        }

        //Define essential values for the Progress Bar
        questionProgressProgressBar.setProgress(questionNumberCounter); //Set the progress of questionProgressProgressBar
        questionProgressProgressBar.setMax(maxNumQuestions); //Set the max value of questionProgressProgressBar

        //Update all the TextView views
        questionProgressTextView.setText(questionNumberCounter + "/" + maxNumQuestions);
        questionTitleTextView.setText(questionTitles.get(questionIndex));
        questionDetailsTextView.setText(questionDetails.get(questionIndex));
        possibleAnswer1Button.setText(possibleAnswers.get(questionIndex));
        possibleAnswer2Button.setText(possibleAnswers.get(questionIndex + 1));
        possibleAnswer3Button.setText(possibleAnswers.get(questionIndex + 2));
    }

    //Make a Toast with a specified text
    public void makeToast(String text) {
        Toast.makeText(QuestionActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    //Reset the colors of all the Possible Answers to grey
    public void resetPossibleAnswersColors() {
        possibleAnswer1Button.setBackgroundColor(Color.WHITE);
        possibleAnswer2Button.setBackgroundColor(Color.WHITE);
        possibleAnswer3Button.setBackgroundColor(Color.WHITE);
    }

    //Onclick listener for each of the Possible Answer Buttons
    // (i.e. possibleAnswer1Button, possibleAnswer2Button, possibleAnswer3Button)
    public void possibleAnswer(View view) {
        possibleAnswerButtonSelected = (Button) view;

        //Obtain the position (tag) of the selected Possible Answer in int form
        String possibleAnswerSelectedPositionString = possibleAnswerButtonSelected.getTag().toString(); //Position (tag) of the selected Possible Answer in string form
        possibleAnswerPositionSelected = Integer.parseInt(possibleAnswerSelectedPositionString); //Position (tag) of the selected Possible Answer in string form

        //VERIFICATION CHECKPOINT
        Log.i("possibleAnswerPosition", possibleAnswerPositionSelected + ""); //Verify the position of the selected Possible Answer is correctly registered
    }

    //Onclick listener for the "Submit / Next" Button
    @SuppressLint("SetTextI18n")
    public void submitOrNext(View view) {
        //If there is NO Possible Answer selection made (i.e. no Possible Answer Buttons are pressed for the current question)
        if (possibleAnswerPositionSelected == 0) {
            makeToast("Select an answer"); //Notify the user to make a Possible Answer selection
        }
        //If Possible Answer selection(s) have been made (i.e. no Possible Answer Buttons are pressed for the current question)
        else {

            isSubmitButtonActive = !isSubmitButtonActive; //Invert the "Submit / Next" Button switch

            //If the selected Possible Answer is CORRECT
            if (correctAnswerPositions[questionIndex] == possibleAnswerPositionSelected) {
                possibleAnswerButtonSelected.setBackgroundColor(Color.GREEN); //Make the background colour of the Possible Answer GREEN

                //If the Submit button is ACTIVE
                if (isSubmitButtonActive) {
                    score++; //Increment the score by 1
                }
            }
            //If the selected Possible answer is INCORRECT
            else {
                possibleAnswerButtonSelected.setBackgroundColor(Color.RED); //Make the background colour of the Possible Answer RED

                //Set the background colour of the correct Possible Answer to GREEN
                if (correctAnswerPositions[questionIndex] == 1) {
                    possibleAnswer1Button.setBackgroundColor(Color.GREEN);
                } else if (correctAnswerPositions[questionIndex] == 2) {
                    possibleAnswer2Button.setBackgroundColor(Color.GREEN);
                } else if (correctAnswerPositions[questionIndex] == 3) {
                    possibleAnswer3Button.setBackgroundColor(Color.GREEN);
                }
            }

            //If the "Submit" Button is ACTIVE (i.e. the user has not submitted the answer for the question yet)
            if (isSubmitButtonActive) {
                questionNumberCounter++; //
                submitOrNextButton.setText("Submit"); //Set the text of the button to "Submit"
                welcomeMessageTextView.setVisibility(View.INVISIBLE); //Make the welcomeMessageTextView invisible
                possibleAnswerPositionSelected = 0; //Reset the possibleAnswerPositionSelected variable to indicate no Possible Answer has been selected
                resetPossibleAnswersColors(); //Reset the colors of all the Possible Answers to grey

                //If the counter for the question number has NOT exceeded the maximum number of questions yet
                if (questionNumberCounter <= maxNumQuestions) {
                    questionIndex++; //Increment the questionIndex
                }
                //If the counter for the question number HAS exceeded the maximum number of questions
                else {
                    //START the ResultActivity and send data across
                    Intent intent = new Intent(QuestionActivity.this, ResultActivity.class); //Create Intent to start ResultActivity
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //Clear the activities stack
                    intent.putExtra(ENTERED_NAME, enteredName); //Name entered by the user
                    intent.putExtra(FINAL_SCORE, score);  //Score of the quiz
                    intent.putExtra(MAX_NUM_QUESTIONS, maxNumQuestions); //Maximum number of questions of the quiz
                    startActivity((intent));
                }

                //Update the questionProgressProgressBar
                questionProgressProgressBar.setProgress(questionNumberCounter);

                //Update all the TextView views
                questionProgressTextView.setText(questionNumberCounter + "/" + maxNumQuestions);
                questionDetailsTextView.setText(questionDetails.get(questionIndex));
                questionTitleTextView.setText(questionTitles.get(questionIndex));
                possibleAnswer1Button.setText(possibleAnswers.get(questionIndex * 3));
                possibleAnswer2Button.setText(possibleAnswers.get(questionIndex * 3 + 1));
                possibleAnswer3Button.setText(possibleAnswers.get(questionIndex * 3 + 2));

            }
            //If the "Next" Button is ACTIVE (i.e. the user has submitted the answer for the question)
            else {
                submitOrNextButton.setText("Next"); //
            }
        }
    }
}