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
            Log.d("VoiceAvtar", "Received command: " + command);

            if (command.matches(".*(कैमरा|camera|camra|open camera|कैमरा खोलो|कैमरा ओपन).*")) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            } else if (command.matches(".*(कॉल|call|डायल|फोन).*")) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:1234567890"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            } else if (command.matches(".*(संदेश|sms|message|मेसेज).*")) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setData(Uri.parse("smsto:1234567890"));
                smsIntent.putExtra("sms_body", "Hello from Voice Avtar");
                smsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(smsIntent);

            } else if (command.matches(".*(गाना|music|स्पॉटिफाई|play music).*")) {
                Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.spotify.music");
                if (intent != null) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            } else if (command.matches(".*(youtube|यूट्यूब|यूटयूब).*")) {
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
