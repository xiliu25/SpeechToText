package com.sriyanksiddhartha.speechtotext;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import java.util.Calendar;
import java.util.Date;



public class MainActivity extends AppCompatActivity {

	private TextView txvResult;
	private TextToSpeech mTTS;
	private String mText;
	private String askTime = "what is the time";
	private String askDate = "what is the date";
	private String response = "please ask me what is the time or what is the date";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txvResult = (TextView) findViewById(R.id.txvResult);


		mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				if (status == TextToSpeech.SUCCESS) {
					int result = mTTS.setLanguage(Locale.getDefault());
					if (result == TextToSpeech.LANG_MISSING_DATA
							|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
						Log.e("TTS", "Language not supported");
					} else {
						// TODO
					}
				} else {
					Log.e("TTS", "Initialization failed");
				}
			}
		});
	}

	public void getSpeechInput(View view) {

		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

		if (intent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(intent, 10);
		} else {
			Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case 10:
				if (resultCode == RESULT_OK && data != null) {
					ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
					//Date currentTime = Calendar.getInstance().getTime();
					txvResult.setText(result.get(0));
					//txvResult.setText(currentTime.toString());
					mText = result.get(0);
					speak();

				}
				break;
		}
	}

	private void speak() {
		Date currentTime = Calendar.getInstance().getTime();
		String [] splitStr = currentTime.toString().split("\\s+");
		String date = splitStr[0]+" "+splitStr[1]+" "+splitStr[2]+" "+splitStr[5];
		String time = splitStr[3];
		if(mText.equals(askTime)){
			mTTS.speak(time, TextToSpeech.QUEUE_FLUSH, null);
		}else if(mText.equals(askDate)){
			mTTS.speak(date, TextToSpeech.QUEUE_FLUSH, null);
		}else{
		mTTS.speak(response, TextToSpeech.QUEUE_FLUSH, null);
		}
	}
}
