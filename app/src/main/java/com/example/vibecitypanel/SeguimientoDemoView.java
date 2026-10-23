package com.example.vibecitypanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class SeguimientoDemoView extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean seguimientoActivo = false;
    private float objetivoX = 180;
    private float objetivoY = 300;
    private float tiempo = 0;

    public SeguimientoDemoView(Context context) {
        super(context);
        setBackgroundColor(Color.rgb(18, 18, 28));
    }

    public void activarSeguimiento(boolean activo) {
        seguimientoActivo = activo;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float w = getWidth();
        float h = getHeight();

        paint.setColor(Color.WHITE);
        paint.setTextSize(42);
        canvas.drawText("DEMO DE SEGUIMIENTO", 30, 55, paint);

        // Objetivo de prueba que se mueve.
        tiempo += 0.04f;
        objetivoX = w / 2f + (float) Math.sin(tiempo) * w * 0.3f;
        objetivoY = h / 2f + (float) Math.cos(tiempo * 0.7f) * h * 0.2f;

        paint.setColor(Color.RED);
        canvas.drawCircle(objetivoX, objetivoY, 24, paint);

        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        canvas.drawText("OBJETIVO DE PRUEBA",
                objetivoX - 120, objetivoY - 38, paint);

        if (seguimientoActivo) {
            paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);

            canvas.drawRoundRect(
                    new RectF(objetivoX - 45, objetivoY - 45,
                            objetivoX + 45, objetivoY + 45),
                    8, 8, paint);

            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(32);
            canvas.drawText("SEGUIMIENTO ACTIVO", 30, h - 45, paint);
        } else {
            paint.setColor(Color.LTGRAY);
            paint.setTextSize(32);
            canvas.drawText("SEGUIMIENTO DESACTIVADO",
                    30, h - 45, paint);
        }

        postInvalidateDelayed(33);
    }
}
