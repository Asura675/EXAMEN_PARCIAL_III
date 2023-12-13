package com.example.examenparcialiii;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hacer el contenido de TextView desplazable
        TextView textView = findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());

        // URL para descargar la imagen
        final String imageUrl = "https://images.pexels.com/photos/1085551/" +
                "pexels-photo-1085551.jpeg?" +
                "auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260";

        // Referencias a las vistas
        final ImageView imageView = findViewById(R.id.imageView);
        final ProgressBar progressBar = findViewById(R.id.progressBar);

        // Configurar OnClickListener para el botón
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                // Solicitud de imagen desde la URL
                ImageRequest imageRequest = new ImageRequest(
                        imageUrl,
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                textView.setText("Image downloaded successfully!");
                                imageView.setImageBitmap(bitmap);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        },
                        0, // max width
                        0, // max height
                        ImageView.ScaleType.CENTER_CROP, // image scale type
                        Bitmap.Config.ARGB_8888, // decode config
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                textView.setText(error.getMessage());
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                );

                // Agregar la solicitud a la cola de solicitudes de Volley
                VolleySingleton.getInstance(getApplicationContext())
                        .addToRequestQueue(imageRequest);
            }
        });
    }
}
