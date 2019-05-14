package com.example.metero.firstapplication;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class RateCalActivity extends AppCompatActivity {
	float rate = 0f;
	String TAG = "RateCalActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rate_cal);
		String title = getIntent().getStringExtra("title");
		rate = getIntent().getFloatExtra("rate",0f);
		Log.i(TAG, "onCreate: "+rate);
		Log.i(TAG, "onCreate: "+title);
		((TextView)findViewById(R.id.cal_name)).setText(title);
		EditText cal_inp = (EditText)findViewById(R.id.cal_inp);
		cal_inp.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@SuppressLint("SetTextI18n")
			@Override
			public void afterTextChanged(Editable s) {
				TextView show = (TextView)findViewById(R.id.cal_value);
				if(s.length()>0){
					float val = Float.parseFloat(s.toString());
					show.setText(val+"===>"+(100/rate*val));
				}
				else{
					show.setText("");
				}
			}
		});
	}
}
