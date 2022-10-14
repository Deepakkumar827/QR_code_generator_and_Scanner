package io.github.deepak_kumar_cse.QRCodeScannerAndGenerator.ui.display;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import io.github.deepak_kumar_cse.QRCodeScannerAndGenerator.R;

public class QRCodeDisplay2 extends AppCompatActivity {

    EditText editText;
    ImageView qrcodeImage;
    Button submit;
    Button share2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_display2);
        editText=findViewById(R.id.qrcodeedittext2);
        qrcodeImage=findViewById(R.id.qrcodeimage2);
        submit=findViewById(R.id.submit2);
        share2=findViewById(R.id.share2);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(),"input string",Toast.LENGTH_SHORT).show();
                    return;
                }
                setQR(editText.getText().toString());
            }
        });

        share2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(QRCodeDisplay2.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) && PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(QRCodeDisplay2.this, Manifest.permission.READ_EXTERNAL_STORAGE))) {
                    ActivityCompat.requestPermissions(QRCodeDisplay2.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

                    return;
                }
                share(screenShot(findViewById(R.id.qrscreen2)));

            }
        });
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
                        bitmap,  "QRCode" + System.currentTimeMillis(), null);
        Uri uri = Uri.parse(pathofBmp);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Attendance App");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "try this qr code");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(shareIntent, "start"));


    }
    void setQR(String text){
        MultiFormatWriter writer=new MultiFormatWriter();
        try {
            BitMatrix matrix =writer.encode(text, BarcodeFormat.QR_CODE, 600, 600);
            BarcodeEncoder encoder=new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            qrcodeImage.setImageBitmap(bitmap);
        }
        catch (WriterException e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

        }

    }
}