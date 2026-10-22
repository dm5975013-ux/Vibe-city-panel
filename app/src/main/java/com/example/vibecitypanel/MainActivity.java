package com.example.vibecitypanel;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final int OVERLAY_REQUEST = 701;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(36, 42, 36, 36);
        root.setBackgroundColor(Color.rgb(20, 20, 31));
        root.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText("VIBE CITY PANEL");
        title.setTextColor(Color.WHITE);
        title.setTextSize(26);
        title.setGravity(Gravity.CENTER);
        root.addView(title, matchWrap());

        TextView subtitle = new TextView(this);
        subtitle.setText("Panel flotante · versión inicial");
        subtitle.setTextColor(Color.LTGRAY);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, 12, 0, 28);
        root.addView(subtitle, matchWrap());

        Button permission = new Button(this);
        permission.setText("1. Permitir panel flotante");
        permission.setOnClickListener(v -> requestOverlayPermission());
        root.addView(permission, matchWrap());

        Button start = new Button(this);
        start.setText("2. Iniciar panel");
        start.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "Primero permite mostrar sobre otras apps.", Toast.LENGTH_LONG).show();
                requestOverlayPermission();
                return;
            }
            startService(new Intent(this, FloatingPanelService.class));
            Toast.makeText(this, "Panel iniciado. Abre el juego para probar la superposición.", Toast.LENGTH_LONG).show();
        });
        root.addView(start, matchWrap());

        TextView note = new TextView(this);
        note.setText("\nNota: los interruptores son demostrativos. Esta versión no controla el juego.");
        note.setTextColor(Color.rgb(210, 190, 255));
        note.setTextSize(14);
        note.setPadding(4, 24, 4, 0);
        root.addView(note, matchWrap());

        setContentView(root);
    }

    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_REQUEST);
        } else {
            Toast.makeText(this, "El permiso de panel flotante ya está habilitado.", Toast.LENGTH_SHORT).show();
        }
    }

    private LinearLayout.LayoutParams matchWrap() {
        return new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }
}
