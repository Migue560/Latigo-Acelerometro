package ud.example.latigo;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import static android.media.AudioManager.STREAM_MUSIC;
import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager Sensores, Sensora;
    private Sensor SensorAce, SensorGra;
    private float X, Y, Z, E, O, P;
    private TextView ValorX, Valory, Valorz, LogText, ValorO, ValorE, ValorP;
    private android.widget.ScrollView ScrollView;

    private SoundPool sPool;
    private int sound1, sound2, sound3, sound4;
    private MediaPlayer player;
    private AudioManager audioManager;
    private Button boton03, boton04;
    private SeekBar volumen, Valor;
    private TextView total, asonado, Info;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogText = findViewById(R.id.textView2);
        ValorX = findViewById(R.id.textView3);
        Valory = findViewById(R.id.textView4);
        Valorz = findViewById(R.id.textView5);
        ScrollView = findViewById(R.id.scrollView);
        ValorX.setText("0");
        Valory.setText("0");
        Valorz.setText("0");
        X = 0;
        Y = 0;
        Z = 0;

        Sensores = (SensorManager) getSystemService(SENSOR_SERVICE);
        SensorAce = Sensores.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensores.registerListener((SensorEventListener) this, SensorAce, SensorManager.SENSOR_DELAY_NORMAL);

        List<Sensor> listSensors = Sensores.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : listSensors) {
            log("Sensor:" + sensor.getName().toString());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
            sPool = new SoundPool.Builder().setAudioAttributes(attributes).setMaxStreams(10).build();
        } else {
            sPool = new SoundPool(6, STREAM_MUSIC, 0);
        }

        sound1 = sPool.load(this, R.raw.latigo, 1);
    }

    private void log(String s) {
        LogText.append("\n" + s);
        ScrollView.post((new Runnable() {
            @Override
            public void run() {
                ScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }));
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            try {
                float Xa = sensorEvent.values[0];
                float Ya = sensorEvent.values[1];
                float Za = sensorEvent.values[2];
                if (abs(Xa - X) >= 4 || abs(Ya - Y) >= 5 || abs(Za - Z) >= 4) {
                    ValorX.setText(String.valueOf((sensorEvent.values[0])));
                    Valory.setText(String.valueOf((sensorEvent.values[1])));
                    Valorz.setText(String.valueOf((sensorEvent.values[2])));
                    sPool.play(sound1, 1, 1, 1, 0, 1);
                }
                X = sensorEvent.values[0];
                Y = sensorEvent.values[1];
                Z = sensorEvent.values[2];
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        sPool.release();
        sPool = null;
        player.release();
    }
}