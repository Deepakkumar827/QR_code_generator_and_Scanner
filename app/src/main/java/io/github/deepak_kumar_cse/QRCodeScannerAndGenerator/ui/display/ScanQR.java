package io.github.deepak_kumar_cse.QRCodeScannerAndGenerator.ui.display;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import io.github.deepak_kumar_cse.QRCodeScannerAndGenerator.R;

public class ScanQR extends AppCompatActivity {
    TextView resulttext;
    Button rescan, share;
    ConstraintLayout result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                Toast.makeText(getApplicationContext(),"camera permission granted",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(getApplicationContext(),"please give camera permission",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(),"camera permission is not granted",Toast.LENGTH_SHORT).show();
            onBackPressed();
        }


        resulttext=findViewById(R.id.resulttext);
        rescan=findViewById(R.id.rescan);
        share=findViewById(R.id.share);
        result=findViewById(R.id.result);


        //////////////////////////////////
        resulttext.setText(scanCode());
        rescan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resulttext.setText(scanCode());
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(screenShot(findViewById(R.id.shareview)));
            }
        });

        resulttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resulttext.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(),"nothing here",Toast.LENGTH_SHORT).show();
                    return;
                }


            }
        });








        resulttext.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(resulttext.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(),"nothing to copy"+resulttext.getText().toString(),Toast.LENGTH_SHORT).show();
                    return false;
                };
                ((ClipboardManager)getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("text", resulttext.getText().toString()));
                Toast.makeText(getApplicationContext(),"copied to clipboard:"+resulttext.getText().toString(),Toast.LENGTH_SHORT).show();
                return true;
            }
        });





    }



    public String  scanCode(){
        resulttext.setText("");
        return null;
    }
    private Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private void share(Bitmap bitmap) {

        String pathofBmp =
                MediaStore.Images.Media.insertImage(getContentResolver(),
                        bitmap,  "QRCode result" + System.currentTimeMillis(), null);
        Uri uri = Uri.parse(pathofBmp);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "QR code");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "this the my new app");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(shareIntent, "start"));


    }
}