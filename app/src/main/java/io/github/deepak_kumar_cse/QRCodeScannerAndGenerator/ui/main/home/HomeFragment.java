package io.github.deepak_kumar_cse.QRCodeScannerAndGenerator.ui.main.home;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import io.github.deepak_kumar_cse.QRCodeScannerAndGenerator.databinding.FragmentHomeBinding;
import io.github.deepak_kumar_cse.QRCodeScannerAndGenerator.ui.display.CaptureQR;
import io.github.deepak_kumar_cse.QRCodeScannerAndGenerator.ui.display.QRCodeDisplay;
import io.github.deepak_kumar_cse.QRCodeScannerAndGenerator.ui.display.QRCodeDisplay2;
import io.github.deepak_kumar_cse.QRCodeScannerAndGenerator.ui.display.ScanQR;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        binding.generateQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.textlength.getText().toString().matches("") || binding.interval.getText().toString().matches("")){
                    Toast.makeText(getContext(),"enter values",Toast.LENGTH_SHORT).show();
                    return;

                }
                Intent intent=new Intent(getActivity(), QRCodeDisplay.class);
                intent.putExtra("length", Integer.parseInt(binding.textlength.getText().toString()));
                intent.putExtra("interval", Integer.parseInt(binding.interval.getText().toString()));
                startActivity(intent);
            }
        });

        binding.generateQrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), QRCodeDisplay2.class);
                startActivity(intent);
            }
        });

        binding.scanbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ScanQR.class));
            }
        });

        binding.scan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scancode2();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    void scancode2(){
        ScanOptions scanOptions=new ScanOptions();
        scanOptions.setPrompt("use ^ for flashlight");
        scanOptions.setBeepEnabled(true);
//        scanOptions.setTorchEnabled(true);
        scanOptions.setOrientationLocked(true);
        scanOptions.setCaptureActivity(CaptureQR.class);
        barLaucher.launch(scanOptions);
    }
    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() !=null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(result.getContents());
            builder.setCancelable(false);

            String time1=result.toString().substring(0, 12);
            LocalTime lt2=LocalTime.now();
            builder.setTitle("Time: "+ lt2.toString());
            long diff= Long.MAX_VALUE;
            try {
                diff=lt2.until(LocalTime.parse(time1, DateTimeFormatter.ofPattern("HH:mm:ss.SSS")), ChronoUnit.MILLIS);


//                if(Math.abs(diff)>=Long.parseLong(result.toString().substring(13, 18))){
//                    Toast.makeText(getContext(),"invalid qr: "+Long.toString(diff),Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Toast.makeText(getContext(),"valid qr: "+Long.toString(diff),Toast.LENGTH_SHORT).show();
//
//                }
            }
            catch (Exception e){
                Toast.makeText(getContext(),"exception: "+Long.toString(diff),Toast.LENGTH_SHORT).show();

            }




            builder.setNeutralButton("Copy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ((ClipboardManager)getActivity().getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("text", result.getContents()));
                    Toast.makeText(getContext(),"copied",Toast.LENGTH_SHORT).show();

                }
            });


            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    dialogInterface.dismiss();
                }
            }).show();
        }
    });
}