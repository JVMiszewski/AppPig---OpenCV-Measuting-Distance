package com.example.opencvtesting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {

    private final String RIZ_TAG = "Riz: CamActivity: ";
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.activity = this;

        Log.i(RIZ_TAG, "Tentando conectar-se com as bibliotecas do OpenCV");
        if (!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_2, this, mOpenCVCallBack))
        {
            Log.e(RIZ_TAG, "Não foi possível se conectar com o OpenCV Manager");
        }

        setContentView(R.layout.activity_main);
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction().replace(R.id.container, Camera2BasicFragment.newInstance()).commit();
        }
    }

    private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            super.onManagerConnected(status);

            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(RIZ_TAG, "OpenCV carregada com sucesso");
                    setContentView(R.layout.activity_main);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, Camera2BasicFragment.newInstance())
                            .commit();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
}
