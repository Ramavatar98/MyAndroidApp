package com.voiceavtar.extended;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE = 101;
    private static final int PERMISSION_REQUEST_CODE = 1001;
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

        SharedPreferences prefs = getSharedPreferences("VoiceAvtarPrefs", MODE_PRIVATE);
        assistantName = prefs.getString("name", "वॉइस अवतार");
        nameInput.setText(assistantName);

        saveNameBtn.setOnClickListener(v -> {
            assistantName = nameInput.getText().toString().trim();
            prefs.edit().putString("name", assistantName).apply();
        });

        micBtn.setOnClickListener(v -> startVoiceInput());

        if (checkMicrophonePermission()) {
            startVoiceService();
        } else {
            requestMicrophonePermission();
        }
    }

    private boolean checkMicrophonePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestMicrophonePermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                PERMISSION_REQUEST_CODE);
    }

    private void startVoiceService() {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE &&
            grantResults.length > 0 &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startVoiceService();
        } else {
            Toast.makeText(this, "Microphone permission is required", Toast.LENGTH_SHORT).show();
        }
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
