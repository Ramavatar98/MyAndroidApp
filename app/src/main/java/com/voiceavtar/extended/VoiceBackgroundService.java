package com.voiceavtar.extended;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import java.util.ArrayList;

public class VoiceBackgroundService extends Service {

    private SpeechRecognizer recognizer;
    private Intent speechIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = new NotificationCompat.Builder(this, "voice_channel")
                .setContentTitle("Voice Avtar चालू है")
                .setContentText("आपका वॉयस असिस्टेंट सुन रहा है...")
                .setSmallIcon(android.R.drawable.ic_btn_speak_now)
                .build();

        startForeground(1, notification);
        startListening();
        return START_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "voice_channel",
                    "Voice Listener Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void startListening() {
        recognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        recognizer.setRecognitionListener(new RecognitionListener() {
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null) {
                    String command = matches.get(0).toLowerCase();
                    SharedPreferences prefs = getSharedPreferences("VoiceAvtarPrefs", MODE_PRIVATE);
                    String name = prefs.getString("name", "वॉइस अवतार");
                    CommandHandler.processCommand(getApplicationContext(), command, name);
                }
                new Handler(Looper.getMainLooper()).postDelayed(() ->
                        recognizer.startListening(speechIntent), 1000);
            }

            public void onError(int error) {
                new Handler(Looper.getMainLooper()).postDelayed(() ->
                        recognizer.startListening(speechIntent), 1000);
            }

            public void onReadyForSpeech(Bundle params) {}
            public void onBeginningOfSpeech() {}
            public void onRmsChanged(float rmsdB) {}
            public void onBufferReceived(byte[] buffer) {}
            public void onEndOfSpeech() {}
            public void onPartialResults(Bundle partialResults) {}
            public void onEvent(int eventType, Bundle params) {}
        });

        recognizer.startListening(speechIntent);
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
