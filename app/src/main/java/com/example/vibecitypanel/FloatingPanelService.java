
package com.example.vibecitypanel;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FloatingPanelService extends Service {
    private WindowManager windowManager;
    private View panel;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mostrarPanel();
    }

    private void mostrarPanel() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(18, 14, 18, 14);
        layout.setBackgroundColor(Color.rgb(25, 25, 40));

        TextView title = new TextView(this);
        title.setText("VIBE CITY");
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);
        title.setGravity(Gravity.CENTER);
        layout.addView(title);

        Button apuntado = new Button(this);
        apuntado.setText("AUTO APUNTADO (DEMO)");
        apuntado.setOnClickListener(v ->
            Toast.makeText(this, "Botón demostrativo", Toast.LENGTH_SHORT).show());
        layout.addView(apuntado);

        Button trabajo = new Button(this);
        trabajo.setText("TRABAJO AUTOMÁTICO (DEMO)");
        trabajo.setOnClickListener(v ->
            Toast.makeText(this, "Botón demostrativo", Toast.LENGTH_SHORT).show());
        layout.addView(trabajo);

        Button ocultar = new Button(this);
        ocultar.setText("OCULTAR PANEL");
        ocultar.setOnClickListener(v -> {
            if (panel != null) {
                windowManager.removeView(panel);
                panel = null;
                stopSelf();
            }
        });
        layout.addView(ocultar);

        int tipo = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                : WindowManager.LayoutParams.TYPE_PHONE;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                tipo,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 20;
        params.y = 120;

        panel = layout;
        windowManager.addView(panel, params);
    }

    @Override
    public void onDestroy() {
        if (panel != null) {
            windowManager.removeView(panel);
            panel = null;
        }
        super.onDestroy();
    }
                  }
