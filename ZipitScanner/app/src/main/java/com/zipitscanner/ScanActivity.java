package com.zipitscanner;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

/**
 * Scanner Activity that opens up the camera and scans QRCode
 *Library used is ZXing
 */
public class ScanActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {


    String scanningURL;
    private QRCodeReaderView mydecoderview;

    /**
     * OnCreate that initializes the camera decoder view
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        mydecoderview.setOnQRCodeReadListener(this);
    }

    /**
     * Once the QR has been scanned
     * @param text
     * @param points
     */
    @Override
    public void onQRCodeRead(String text, PointF[] points) {

        scanningURL=text;
        Intent intent = new Intent(ScanActivity.this, CartActivity.class);
        intent.putExtra("cartString",scanningURL);
        startActivity(intent);
    }

    /**
     * If Phone Camera is not found handler
     */
    @Override
    public void cameraNotFound() {

    }

    /**
     * QR Not found (for timeout purposes)
     */
    @Override
    public void QRCodeNotFoundOnCamImage() {

    }


    /**
     * Once this activity is Resumed
     */
    @Override
    protected void onResume() {
        super.onResume();
        mydecoderview.getCameraManager().startPreview();
    }

    /**
     * Resource handling once the activity is paused
     */
    @Override
    protected void onPause() {
        super.onPause();
        mydecoderview.getCameraManager().stopPreview();
    }

    /**
     * OnBackPressed handler
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
