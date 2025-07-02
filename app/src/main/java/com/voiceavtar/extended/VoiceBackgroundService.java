package com.voiceavtar.extended;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class VoiceBackgroundService extends Service {

    private SpeechRecognizer recognizer;

    @Override
    public void onCreate() {
        super.onCreate();
        startListening();
    }

    private void startListening() {
        recognizer = SpeechRecognizer.createSpeechRecognizer(this);
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN");

        recognizer.setRecognitionListener(new RecognitionListener() {
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null) {
                    String command = matches.get(0).toLowerCase();
                    SharedPreferences prefs = getSharedPreferences("VoiceAvtarPrefs", MODE_PRIVATE);
                    String name = prefs.getString("name", "वॉइस अवतार");
                    CommandHandler.processCommand(getApplicationContext(), command, name);
                }
                recognizer.startListening(intent);
            }
            public void onError(int error) {
                recognizer.startListening(intent);
            }
            public void onReadyForSpeech(Bundle params) {}
            public void onBeginningOfSpeech() {}
            public void onRmsChanged(float rmsdB) {}
            public void onBufferReceived(byte[] buffer) {}
            public void onEndOfSpeech() {}
            public void onPartialResults(Bundle partialResults) {}
            public void onEvent(int eventType, Bundle params) {}
        });

        recognizer.startListening(intent);
    }

    @Override
    public void onDestroy() {
        if (recognizer != null) recognizer.destroy();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
