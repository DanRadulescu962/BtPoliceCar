package com.example.btpolicecar;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter btAd;

    private BluetoothDevice car;

    private BluetoothSocket carSocket;

    private OutputStream carChannel;

    private static final String macAddr = "98:D3:61:FD:37:D2";

    private static final UUID appUUID = UUID.fromString("966dc3b6-69be-11e9-a923-1681be663d3e");

    public void initBT() {

        int expected_enable = 0;

        btAd = BluetoothAdapter.getDefaultAdapter();
        if (btAd == null) {
            // Unable to use BT
            return;
        }

        if (!btAd.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, expected_enable);
        }

    }

    public BluetoothDevice getCarDevice() {

        Set<BluetoothDevice> collec = btAd.getBondedDevices();

        for (BluetoothDevice it : collec) {
            String btName = it.getName();
            String btAddr = it.getAddress();

            //Log.d("Dan", btName + " " + btAddr);

            if (btAddr.equals(macAddr))
                return it;
        }

        return null;
    }

    // Connect to HC-05 as client because MCU is a server
    public void connectToCar() {

        try {
            Method createRfcommSocket = car.getClass().getMethod("createRfcommSocket", int.class);
            carSocket = (BluetoothSocket) createRfcommSocket.invoke(car, 1);
            if (carSocket == null) {
                Toast.makeText(MainActivity.this, "Invalid socket!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            carSocket.connect();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IOException e) {
            e.printStackTrace();
        }

        // Get comm channel
        try {
            carChannel = carSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBT();

        Button b = findViewById(R.id.button);
        Button bF = findViewById(R.id.button5);
        Button bS = findViewById(R.id.button4);
        Button bL = findViewById(R.id.button2);
        Button bR = findViewById(R.id.button3);
        Button bH = findViewById(R.id.button7);
        Button bG = findViewById(R.id.button6);

        SeekBar speed = findViewById(R.id.seekBar);

        b.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        car = getCarDevice();

                        if (car == null) {
                            Toast.makeText(MainActivity.this, "Error when connecting!",
                                    Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        connectToCar();

                        if (carChannel == null) {
                            Toast.makeText(MainActivity.this, "Invalid stream!",
                                    Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        Toast.makeText(MainActivity.this, "Connected!",
                                Toast.LENGTH_SHORT).show();

                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }

                return true;
            }
        });

        bF.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                String a;
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        a = "W";
                        try {
                            carChannel.write(a.getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d("Dan", a);
                        break;
                    case MotionEvent.ACTION_UP:
                        a = "w";
                        try {
                            carChannel.write(a.getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d("Dan", a);
                        break;
                }

                return true;
            }
        });

        bS.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                String a;
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        a = "S";
                        try {
                            carChannel.write(a.getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        a = "s";
                        try {
                            carChannel.write(a.getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }

                return true;
            }
        });

        bL.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                String a;
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        a = "L";
                        try {
                            carChannel.write(a.getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        a = "l";
                        try {
                            carChannel.write(a.getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }

                return true;
            }
        });

        bR.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                String a;
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        a = "R";
                        try {
                            carChannel.write(a.getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        a = "r";
                        try {
                            carChannel.write(a.getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }

                return true;
            }
        });

        bH.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                String a;
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        a = "H";
                        try {
                            carChannel.write(a.getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        a = "h";
                        try {
                            carChannel.write(a.getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }

                return true;
            }
        });

        bG.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                String a;
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        a = "G";
                        try {
                            carChannel.write(a.getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        a = "g";
                        try {
                            carChannel.write(a.getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }

                return true;
            }
        });

        speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 0 to 150, 100 to 220
                int aP = 1, bP = 150;

                Integer val = aP * progress + bP;
                if (val >= 220)
                    val = 220;
                String a = "z" + val.toString();
                Log.d("Dan", a);
                try {
                    carChannel.write(a.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Unused
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Unused
            }
        });
    }
}
