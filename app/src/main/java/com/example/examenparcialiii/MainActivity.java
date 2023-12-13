package com.example.examenparcialiii;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private ImageView imageView;
    private ProgressBar progressBar;
    private Button colorbutton;

    private boolean isImageLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hacer el contenido de TextView desplazable
        textView = findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());

        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);

        // Configurar OnClickListener para el botón
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Verificar si ya hay una imagen cargada
                if (isImageLoaded) {
                    // Borrar la imagen si ya está cargada
                    imageView.setImageBitmap(null);
                    textView.setText("");
                    progressBar.setVisibility(View.INVISIBLE);
                    isImageLoaded = false;
                } else {
                    // Si no hay imagen cargada, realizar la descarga
                    final String imageUrl = "https://assets.change.org/photos/0/py/pc/rVpYpCvUfXNsJbp-1600x900-noPad.jpg?1631739802";

                    // Mostrar la barra de progreso al iniciar la descarga
                    progressBar.setVisibility(View.VISIBLE);

                    ImageRequest imageRequest = new ImageRequest(
                            imageUrl,
                            new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap bitmap) {
                                    textView.setText("Imagen Descargada");
                                    imageView.setImageBitmap(bitmap);
                                    // Ocultar la barra de progreso cuando la descarga se completa
                                    progressBar.setVisibility(View.INVISIBLE);
                                    isImageLoaded = true;
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
                                    // Ocultar la barra de progreso en caso de error
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                    );

                    // Agregar la solicitud a la cola de solicitudes de Volley
                    VolleySingleton.getInstance(getApplicationContext())
                            .addToRequestQueue(imageRequest);
                }
            }
        });
        colorbutton= findViewById(R.id.colorbutton);
        int[] colors = {getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_blue_light),
                getResources().getColor(android.R.color.holo_red_light)};
        colorbutton.setOnClickListener(new View.OnClickListener() {
            int colorIndex = 0;

            @Override
            public void onClick(View v) {
                // Cambiar el color del nuevo botón
                colorbutton.setBackgroundColor(colors[colorIndex]);

                // Incrementar el índice para el próximo color
                colorIndex = (colorIndex + 1) % colors.length;

                // Mostrar el mensaje de Toast
                Toast.makeText(MainActivity.this, "Nuevo botón presionado", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private class TareaAsincrona extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            // Simular un proceso largo en doInBackground
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(5000);
                    publishProgress((i + 1) * 20); // Notificar el progreso
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return "Tarea asíncrona completa!";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
            progressBar.setProgress(0);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
