package io.github.deepak_kumar_cse.QRCodeScannerAndGenerator.ui.display;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.time.LocalTime;
import java.util.UUID;

import io.github.deepak_kumar_cse.QRCodeScannerAndGenerator.R;

public class QRCodeDisplay extends AppCompatActivity {
    ImageView qrcodeimage;
    TextView qrcodetext;
    Thread qRGeneratorThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_display);
        qrcodeimage=findViewById(R.id.qrcodeimage);
        qrcodetext=findViewById(R.id.qrcodevalue);
        qrcodeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"qr image",Toast.LENGTH_SHORT).show();

            }
        });
        qrcodetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ClipboardManager)getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("text", qrcodetext.toString()));


                Toast.makeText(getApplicationContext(), "copied to clipboard",Toast.LENGTH_SHORT).show();

            }
        });
        generateRandomQRCode(getIntent().getExtras().getInt("length"), getIntent().getExtras().getInt("interval"));

    }
    void generateRandomQRCode(int strlength, int interval){// milli second

        qRGeneratorThread=new Thread(){
            @Override
            public void run(){
                final int[] counttest = {0};
                try {
                    while (!isInterrupted()){
                        Thread.sleep(interval);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String text=generateRandomString(strlength);
                                text= LocalTime.now().toString()+"+"+String.format("%05d", interval)+"+"+text;
                                generateQRCode(text);
                                Log.i("abcde", Thread.currentThread().getId()+" inside thread "+ counttest[0]++);

                            }
                        });
                    }
                } catch (InterruptedException e) {
                     Log.d("abced", "interruptInThread");
                }

            }
        };
        qRGeneratorThread.start();;


    }
    void generateQRCode(String text){
        qrcodetext.setText(text);
        MultiFormatWriter writer=new MultiFormatWriter();
        try {
            BitMatrix matrix =writer.encode(text, BarcodeFormat.QR_CODE, 600, 600);
            BarcodeEncoder encoder=new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            qrcodeimage.setImageBitmap(bitmap);
        }
        catch (WriterException e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

        }



    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

        if(qRGeneratorThread.isAlive()){
            Log.d("abcde", "thread is still  alive");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        qRGeneratorThread.interrupt();
    }

    String generateRandomString(int size){
        return UUID.randomUUID().toString();


    }
}