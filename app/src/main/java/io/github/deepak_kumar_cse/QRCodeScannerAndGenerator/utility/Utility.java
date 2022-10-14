package io.github.deepak_kumar_cse.QRCodeScannerAndGenerator.utility;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
//////////TODO: incomplete
class Utility {
    public static void share(Context context, Bitmap bitmap) {

        String pathofBmp =
                MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        bitmap,  "QRCode" + System.currentTimeMillis(), null);
        Uri uri = Uri.parse(pathofBmp);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Attendance App");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "try this qr code");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(shareIntent, "start"));


    }
    public static Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
}
