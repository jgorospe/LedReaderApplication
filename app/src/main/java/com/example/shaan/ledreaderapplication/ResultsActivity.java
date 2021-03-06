package com.example.shaan.ledreaderapplication;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.FFmpegFrameGrabber;

import java.io.FileOutputStream;
import java.io.IOException;

import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;

public class ResultsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        final TextView testBin = (TextView) findViewById(R.id.test_bin);
        final TextView testStr = (TextView) findViewById(R.id.test_str);
        final TextView loadingTxt = (TextView) findViewById(R.id.loading_txt);
        loadingTxt.setVisibility(View.GONE);
        Button button = (Button) findViewById(R.id.gen_results_btn);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadingTxt.setVisibility(View.VISIBLE);
                try {

                    analyzeFrames();
                    loadingTxt.setVisibility(View.GONE);
                    testBin.setText("010010010111010000100000011010010111001100100000011001100110100101101110011010010111001101101000011001010110010000100001");
                    testStr.setText("It is finished!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void analyzeFrames() throws Exception,IOException{
        int counter = 0;
        int frameLength = 0;
        opencv_core.IplImage imgs;
        opencv_core.IplImage _imgs;
        Bitmap bitmap = null;
        FileOutputStream out = null;

        try {
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/led/LEDAnalysis.mp4");
            grabber.setFormat("mp4");
            grabber.start();

            frameLength = grabber.getLengthInFrames();

            for (int i = 0; i<frameLength ; i++){
                //ImageIO.write(g.grab().getBufferedImage(), "png", new File("video-frame-" + System.currentTimeMillis() + ".png"));
                imgs = grabber.grab();
                int height = imgs.height();
                int width = imgs.width();
                _imgs = opencv_core.IplImage.create(width, height, opencv_core.IPL_DEPTH_8U , 4);

                bitmap = Bitmap.createBitmap(width, height,
                        Bitmap.Config.ARGB_8888);
                cvCvtColor(imgs,_imgs, opencv_imgproc.CV_BGR2RGBA);
                bitmap.copyPixelsFromBuffer(_imgs.getByteBuffer());
                out = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/led/i-"+ counter + ".png");
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.close();
                counter++;
            }


            grabber.stop();
            grabber.release();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    /*
       int counter = 0;
       int frameLength = 0;


            FFmpegFrameGrabber g = new FFmpegFrameGrabber(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/LEDAnalysis.mp4");
        opencv_core.IplImage imgs;
        Bitmap bitmap = null;
        FileOutputStream out = null;

            g.start();


            frameLength = g.getLengthInFrames();

            for (int i = 0; i<frameLength ; i++){
                //ImageIO.write(g.grab().getBufferedImage(), "png", new File("video-frame-" + System.currentTimeMillis() + ".png"));
                counter++;
                imgs = g.grab();
                bitmap.copyPixelsToBuffer(imgs.getByteBuffer());
                out = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/i-"+ counter + ".png");
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.close();
            }

            g.stop();
            g.release();


			Manipulate the Images
		Bitmap img = MediaStore.Images.Media.getBitmap();
		int w = img.getWidth()/2;
		int h = img.getHeight()/2;
		int rgb = img.getPixel(w, h);
		int red = (rgb >> 16) & 0x000000FF;
		int green = (rgb >> 8) & 0x000000FF;
		int blue = (rgb) & 0x000000FF;
		System.out.println("RGB: " + rgb);
		System.out.println("Red: " + red);
		System.out.println("Green: " + green);
		System.out.println("Blue: " + blue);*/

    }

}
