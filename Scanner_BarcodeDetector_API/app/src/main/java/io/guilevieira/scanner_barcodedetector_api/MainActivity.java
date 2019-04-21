package io.guilevieira.scanner_barcodedetector_api;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;


public class MainActivity extends AppCompatActivity {

    /*Componentes visuais*/
    private SurfaceView surfaceVw_leitura;
    private TextView txtVw_resultado;

    /*Recursos*/
    private CameraSource cameraSource;
    private BarcodeDetector detector;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        surfaceVw_leitura = findViewById(R.id.surfaceVw_leitura);
        txtVw_resultado = findViewById(R.id.txtVw_resultado);


        /*Cria obejto que ira fazer a leitura*/
        detector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build();
       /*Cria objeto para imagens da camera*/
        cameraSource = new CameraSource.Builder(this, detector)
                                .setRequestedPreviewSize(640, 480)
                                .setAutoFocusEnabled(Boolean.TRUE).build();

        /*Iniciar solicitando a permissão para camera e vibração*/
        requisitar_permicao(this);



        /*callback que vai "jogar" as imagens no surfaceView */
        surfaceVw_leitura.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {


                if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                } else {
                    try {
                        cameraSource.start(holder);
                    } catch (Exception e) {
                        Log.e("ERRO AO ABRIR CAMERA: ", e.getStackTrace().toString());
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop(); //Fecha camera ao destruir a surfaceView
            }
        });

        detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> result = detections.getDetectedItems();
                Log.e("SIZE ARRRAY" , "Tamanhho: " + result.size());
                if(result != null && result.size()>0 ){
                    txtVw_resultado.post(new Runnable() {
                        @Override
                        public void run() {
                            if(!result.valueAt(0).displayValue.contentEquals(txtVw_resultado.getText())) {

                                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
                                vibrator.vibrate(1000);
                            }
                            txtVw_resultado.setText(result.valueAt(0).displayValue);


                        }
                    });
                }
            }
        });
    }

    /*Metodo para solicitar a permissao ao usuário*/
    public void requisitar_permicao(Activity activity){
       int MY_PERMISSIONS = 0;
       ActivityCompat.requestPermissions( activity,
                new String[]{android.Manifest.permission.CAMERA, Manifest.permission.VIBRATE},MY_PERMISSIONS);
    }
}
