package com.example.vibecitypanel;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;

public class SeguimientoDemoActivity extends Activity {

    private SeguimientoDemoView demo;
    private boolean activo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(18, 18, 28));

        demo = new SeguimientoDemoView(this);

        root.addView(demo, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1
        ));

        Button boton = new Button(this);
        boton.setText("AUTO APUNTADO");

        boton.setOnClickListener(v -> {
            activo = !activo;
            demo.activarSeguimiento(activo);

            boton.setText(activo
                    ? "AUTO APUNTADO: ACTIVADO"
                    : "AUTO APUNTADO");
        });

        root.addView(boton, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        setContentView(root);
    }
}
