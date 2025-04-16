package com.example.flashlightapp1;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.flashlightapp1.R;

public class MainActivity extends AppCompatActivity {

    private CameraManager cameraManager;
    private String cameraId;
    private boolean isFlashOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 🔒 Request camera permission if not already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 100);
        }

        // 📷 Initialize camera manager
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];  // Get back camera
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        // 💡 Toggle flashlight on button click
        Button btnToggleFlash =findViewById(R.id.btnToggleFlash);
        btnToggleFlash.setOnClickListener(v -> {
            try {
                if (isFlashOn) {
                    cameraManager.setTorchMode(cameraId, false);
                    isFlashOn = false;
                    Toast.makeText(this, "Flashlight OFF", Toast.LENGTH_SHORT).show();
                } else {
                    cameraManager.setTorchMode(cameraId, true);
                    isFlashOn = true;
                    Toast.makeText(this, "Flashlight ON", Toast.LENGTH_SHORT).show();
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        });
    }

    // 🔁 Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied! Flashlight won’t work.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
