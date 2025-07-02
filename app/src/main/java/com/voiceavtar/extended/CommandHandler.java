package com.voiceavtar.extended;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class CommandHandler {
    public static void processCommand(Context context, String command, String assistantName) {
        command = command.toLowerCase();
        if (!command.contains(assistantName.toLowerCase())) return;

        try {
            if (command.contains("कैमरा") || command.contains("camera")) {
                Intent i = new Intent(Intent.ACTION_MAIN);
                i.setClassName("com.android.camera", "com.android.camera.Camera");
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            } else if (command.contains("कॉल") || command.contains("call")) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:1234567890"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            } else if (command.contains("संदेश") || command.contains("sms")) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setData(Uri.parse("smsto:"));
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", "1234567890");
                smsIntent.putExtra("sms_body", "Hello from Voice Avtar");
                smsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(smsIntent);

            } else if (command.contains("गाना") || command.contains("music")) {
                Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.spotify.music");
                if (intent != null) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            } else if (command.contains("youtube")) {
                Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
                if (intent != null) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }

        } catch (Exception e) {
            Log.e("VoiceAvtar", "Error executing command: " + e.getMessage());
        }
    }
}
