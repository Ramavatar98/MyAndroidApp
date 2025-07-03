package com.voiceavtar.extended;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class CommandHandler {
    public static void processCommand(Context context, String command, String assistantName) {
        command = command.toLowerCase();
        if (!command.contains(assistantName.toLowerCase())) return;

        try {
            Log.d("VoiceAvtar", "Received command: " + command);
            Toast.makeText(context, "Command: " + command, Toast.LENGTH_SHORT).show();

            if (command.matches(".*(कैमरा|camera|camra|open camera|कैमरा खोलो|कैमरा ओपन).*")) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // ✅ जरूरी
                context.startActivity(intent);

            } else if (command.matches(".*(कॉल|call|डायल|फोन).*")) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:1234567890"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // ✅ जरूरी
                context.startActivity(intent);

            } else {
                Toast.makeText(context, "कोई कार्य मेल नहीं खाया", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e("VoiceAvtar", "Error processing command: ", e);
            Toast.makeText(context, "त्रुटि: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
