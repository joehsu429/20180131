package com.example.student.a20180131;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = findViewById(R.id.imageView);
    }
    public void click1(View v)//1
    {
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(it, 123);
    }

    public void click2(View v)//2
    {
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(getExternalFilesDir("PHOTO"), "myphoto.jpg");
        it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(it, 456);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123)//1
        {
            if (resultCode == RESULT_OK)
            {
                Bundle pBundle = data.getExtras();
                Bitmap bmp = (Bitmap) pBundle.get("data");
                img.setImageBitmap(bmp);
            }
        }
        if (requestCode == 456)//2
        {
            if (resultCode == RESULT_OK)
            {
                File f = new File(getExternalFilesDir("PHOTO"), "myphoto.jpg");
                //Bitmap bitmap=BitmapFactory.decodeFile(f.getAbsolutePath());//2
                try {
                    InputStream is = new FileInputStream(f);
                    Log.d("BMP", "Can READ:" + is.available());
                    Bitmap bmp = getFitImage(is);
                    img.setImageBitmap(bmp);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static Bitmap getFitImage(InputStream is)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        byte[] bytes = new byte[0];
        try {
            bytes = readStream(is);
            //BitmapFactory.decodeStream(inputStream, null, options);
            Log.d("BMP", "byte length:" + bytes.length);
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            System.gc();
            // Log.d("BMP", "Size:" + bmp.getByteCount());
            return bmp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] readStream(InputStream inStream)  throws Exception
    {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }
}

