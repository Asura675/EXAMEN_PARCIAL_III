package com.example.examenparcialiii;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.ResponseDelivery;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.ImageLoader.ImageCache;

import java.io.InputStream;
import java.net.ResponseCache;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private ImageView imageView;
    private ProgressBar progressBar;
    private Button colorbutton;

    private boolean isImageLoaded = false;
    private Button alarmaButton;
    private Ringtone ringtone;
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);
        try {
            Thread.sleep(4000);
            setTheme(R.style.Theme_EXAMENPARCIALIII);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hacer el contenido de TextView desplazable
        textView = findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());

        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);


        progressBar.setVisibility(View.INVISIBLE);




        // Configurar OnClickListener para el botón
        Button button = findViewById(R.id.FetchImageButton);
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

                    TareaAsincrona task = new TareaAsincrona();
                    task.execute(8);

                    ImageRequest imageRequest = new ImageRequest(
                            imageUrl,
                            new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap bitmap) {
                                    textView.setVisibility(View.INVISIBLE);
                                    textView.setText("Imagen Descargada");
                                    imageView.setVisibility(View.INVISIBLE);
                                    imageView.setImageBitmap(bitmap);
                                    // Ocultar la barra de progreso cuando la descarga se completa
                                    //progressBar.setVisibility(View.INVISIBLE);
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
        colorbutton= findViewById(R.id.ColorButton);
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

                if(player == null)
                {
                    player = MediaPlayer.create(MainActivity.this, R.raw.santaclause);
                }
                player.start();

                // Mostrar el mensaje de Toast
                Toast.makeText(MainActivity.this, "Nuevo botón presionado", Toast.LENGTH_SHORT).show();
            }
        });
        alarmaButton = findViewById(R.id.AlarmButton);
        alarmaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Configurar OnClickListener para el botón de la alarma

                // Reproducir el sonido de la alarma
                playAlarm();

                // Mostrar el mensaje de Toast
                Toast.makeText(MainActivity.this, "Alarma activada", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void playAlarm() {
        try {
            // Configurar el sonido de la alarma
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            ringtone = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);

            // Reproducir el sonido
            ringtone.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        // Detener la alarma al salir de la actividad
        if (ringtone != null && ringtone.isPlaying()) {
            ringtone.stop();
        }

        super.onDestroy();
    }

    private class TareaAsincrona extends AsyncTask<Integer,Integer,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(Integer... integers) {
            for(int i=0 ;i< integers[0]; i++){
                publishProgress((i*100 / integers[0]));
                try {
                    Thread.sleep(25);
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
            progressBar.setVisibility(View.INVISIBLE);
        }


    }
}
