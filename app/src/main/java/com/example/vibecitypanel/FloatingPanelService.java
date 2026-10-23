
package com.example.vibecitypanel;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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

    private static final String CHANNEL_ID = "vibe_city_panel";
    private static final int NOTIFICATION_ID = 1001;

    private WindowManager wm;
    private View panel;
    private View burbuja;

    private WindowManager.LayoutParams panelParams;
    private WindowManager.LayoutParams burbujaParams;

    private boolean autoApuntadoActivo = false;

    @Override
    public void onCreate() {
        super.onCreate();

        iniciarNotificacion();

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        crearBurbuja();
        mostrarPanel();
    }

    private void iniciarNotificacion() {
        NotificationManager nm =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Vibe City Panel",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Mantiene activo el panel flotante");
            nm.createNotificationChannel(channel);
        }

        Notification.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, CHANNEL_ID);
        } else {
            builder = new Notification.Builder(this);
        }

        Notification notification = builder
                .setSmallIcon(android.R.drawable.ic_menu_view)
                .setContentTitle("Vibe City Panel")
                .setContentText("Panel flotante activo")
                .setOngoing(true)
                .build();

        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private int tipoVentana() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        return WindowManager.LayoutParams.TYPE_PHONE;
    }

    private WindowManager.LayoutParams parametros(int ancho, int alto) {
        WindowManager.LayoutParams p =
                new WindowManager.LayoutParams(
                        ancho,
                        alto,
                        tipoVentana(),
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT
                );

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
        if (burbuja != null) return;

        TextView b = new TextView(this);
        b.setText("VC");
        b.setTextColor(Color.WHITE);
        b.setTextSize(16);
        b.setGravity(Gravity.CENTER);
        b.setElevation(10);
        b.setBackground(fondo(Color.rgb(90, 40, 200), 100));

        burbuja = b;
        burbujaParams = parametros(58, 58);

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

                        try {
                            if (burbuja != null
                                    && burbuja.isAttachedToWindow()) {
                                wm.updateViewLayout(
                                        burbuja, burbujaParams);
                            }
                        } catch (Exception ignored) {
                        }
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

                return true;
            }
        });

        try {
            wm.addView(burbuja, burbujaParams);
        } catch (Exception e) {
            burbuja = null;
            Toast.makeText(this,
                    "No se pudo mostrar la burbuja",
                    Toast.LENGTH_SHORT).show();
        }
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

            apuntado.setText(autoApuntadoActivo
                    ? "AUTO APUNTADO: ON"
                    : "AUTO APUNTADO");

            Toast.makeText(this,
                    autoApuntadoActivo
                            ? "Activado"
                            : "Desactivado",
                    Toast.LENGTH_SHORT).show();
        });

        layout.addView(apuntado);

        panel = layout;
        panelParams = parametros(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        );
        panelParams.x = 90;

        try {
            wm.addView(panel, panelParams);
        } catch (Exception e) {
            panel = null;
            Toast.makeText(this,
                    "No se pudo abrir el panel",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void ocultarPanel() {
        if (panel != null) {
            try {
                wm.removeView(panel);
            } catch (Exception ignored) {
            }
            panel = null;
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        // START_STICKY permite a Android intentar recrear el servicio.
    }

    @Override
    public void onDestroy() {
        ocultarPanel();

        if (burbuja != null) {
            try {
                wm.removeView(burbuja);
            } catch (Exception ignored) {
            }
            burbuja = null;
        }

        super.onDestroy();
    }
}
