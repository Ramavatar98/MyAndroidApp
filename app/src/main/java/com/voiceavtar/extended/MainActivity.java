package com.voiceavtar.extended;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE = 101;
    private TextView resultView;
    private EditText nameInput;
    private String assistantName = "वॉइस अवतार";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultView = findViewById(R.id.resultText);
        nameInput = findViewById(R.id.nameInput);
        Button micBtn = findViewById(R.id.voiceButton);
        Button saveNameBtn = findViewById(R.id.saveNameButton);

        // नाम लोड करें
        SharedPreferences prefs = getSharedPreferences("VoiceAvtarPrefs", MODE_PRIVATE);
        assistantName = prefs.getString("name", "वॉइस अवतार");
        nameInput.setText(assistantName);

        // नाम सेव करें
        saveNameBtn.setOnClickListener(v -> {
            assistantName = nameInput.getText().toString().trim();
            prefs.edit().putString("name", assistantName).apply();
        });

        // माइक्रोफ़ोन बटन
        micBtn.setOnClickListener(v -> startVoiceInput());

        // 🔄 Start Background Voice Listener Service
        Intent serviceIntent = new Intent(this, VoiceBackgroundService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN");
        startActivityForResult(intent, REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String spokenText = result.get(0);
            resultView.setText(spokenText);
            CommandHandler.processCommand(this, spokenText, assistantName);
        }
    }
}
