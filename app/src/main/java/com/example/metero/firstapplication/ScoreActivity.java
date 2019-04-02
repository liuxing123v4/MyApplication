package com.example.metero.firstapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    TextView score;
    Button bnt_three;
    Button bnt_two;
    Button bnt_one;
    Button bnt_reset;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        score = (TextView) findViewById(R.id.score);
        bnt_one = (Button) findViewById(R.id.bnt_one);
        bnt_two = (Button) findViewById(R.id.bnt_two);
        bnt_three = (Button) findViewById(R.id.bnt_three);
        bnt_reset = (Button) findViewById(R.id.bnt_reset);
    }
    public void plus_three(View v){
        String str = score.getText().toString();
        int value = Integer.parseInt(str.toString());
        value +=3;
        String three = String.valueOf(value);
        score.setText(three);
    }
    public void plus_two(View v){
        String str = score.getText().toString();
        int value = Integer.parseInt(str.toString());
        value +=2;
        String two = String.valueOf(value);
        score.setText(two);
    }
    public void plus_one(View v){
        String str = score.getText().toString();
        int value = Integer.parseInt(str.toString());
        value +=1;
        String one = String.valueOf(value);
        score.setText(one);
    }
    public void reset(View v){
        String str = score.getText().toString();
        int value = Integer.parseInt(str.toString());
        value = 0;
        String reset = String.valueOf(value);
        score.setText(reset);
    }
}
