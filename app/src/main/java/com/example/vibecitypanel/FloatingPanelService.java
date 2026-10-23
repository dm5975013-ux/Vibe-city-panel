package com.example.vibecitypanel;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FloatingPanelService extends Service {

    private WindowManager wm;
    private View panel;
    private View burbuja;
    private WindowManager.LayoutParams panelParams;
    private WindowManager.LayoutParams burbujaParams;
    private boolean autoApuntadoActivo = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        crearBurbuja();
        mostrarPanel();
    }

    private int tipoVentana() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                : WindowManager.LayoutParams.TYPE_PHONE;
    }

    private WindowManager.LayoutParams parametros(int ancho, int alto) {
        WindowManager.LayoutParams p = new WindowManager.LayoutParams(
                ancho, alto, tipoVentana(),
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        p.gravity = Gravity.TOP | Gravity.START;
        p.x = 20;
        p.y = 160;
        return p;
    }

    private GradientDrawable fondo(int color, int radio) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(radio);
        return d;
    }

    private void crearBurbuja() {
        TextView b = new TextView(this);
        b.setText("VC");
        b.setTextColor(Color.WHITE);
        b.setTextSize(16);
        b.setGravity(Gravity.CENTER);
        b.setBackground(fondo(Color.rgb(90, 40, 200), 100));

        burbuja = b;
        burbujaParams = parametros(58, 58);

        b.setOnClickListener(v -> {
            if (panel == null) {
                mostrarPanel();
            } else {
                ocultarPanel();
            }
        });

        b.setOnTouchListener(new View.OnTouchListener() {
            private int inicioX, inicioY;
            private float toqueX, toqueY;
            private boolean movido;

            @Override
            public boolean onTouch(View v, MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        inicioX = burbujaParams.x;
                        inicioY = burbujaParams.y;
                        toqueX = e.getRawX();
                        toqueY = e.getRawY();
                        movido = false;
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) (e.getRawX() - toqueX);
                        int dy = (int) (e.getRawY() - toqueY);

                        if (Math.abs(dx) > 8 || Math.abs(dy) > 8) {
                            movido = true;
                        }

                        burbujaParams.x = inicioX + dx;
                        burbujaParams.y = inicioY + dy;
                        wm.updateViewLayout(burbuja, burbujaParams);
                        return true;

                    case MotionEvent.ACTION_UP:
                        if (!movido) {
                            if (panel == null) {
                                mostrarPanel();
                            } else {
                                ocultarPanel();
                            }
                        }
                        return true;
                }
                return false;
            }
        });

        wm.addView(burbuja, burbujaParams);
    }

    private void mostrarPanel() {
        if (panel != null) return;

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16, 12, 16, 12);
        layout.setBackground(fondo(Color.rgb(25, 25, 40), 24));

        TextView titulo = new TextView(this);
        titulo.setText("VIBE CITY");
        titulo.setTextColor(Color.WHITE);
        titulo.setTextSize(20);
        titulo.setGravity(Gravity.CENTER);
        layout.addView(titulo);

        Button apuntado = new Button(this);
        apuntado.setText("AUTO APUNTADO");
        apuntado.setOnClickListener(v -> {
            autoApuntadoActivo = !autoApuntadoActivo;
            Toast.makeText(this,
                    autoApuntadoActivo
                            ? "Auto apuntado activado"
                            : "Auto apuntado desactivado",
                    Toast.LENGTH_SHORT).show();
        });
        layout.addView(apuntado);

        panel = layout;
        panelParams = parametros(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        panelParams.x = 90;
        wm.addView(panel, panelParams);
    }

    private void ocultarPanel() {
        if (panel != null) {
            wm.removeView(panel);
            panel = null;
        }
    }

    @Override
    public void onDestroy() {
        if (panel != null) {
            wm.removeView(panel);
            panel = null;
        }
        if (burbuja != null) {
            wm.removeView(burbuja);
            burbuja = null;
        }
        super.onDestroy();
    }
}
