package io.guilevieira.scanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.client.android.Intents;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import com.google.zxing.oned.Code39Reader;
import com.google.zxing.oned.MultiFormatOneDReader;
import com.google.zxing.oned.MultiFormatUPCEANReader;
import com.google.zxing.qrcode.QRCodeReader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private Button btn_scan;
    private EditText edtTxt_resultado;
    private SurfaceView surfaceVw_camera;

    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Instancia dos objetos visuais*/
        btn_scan          =  findViewById(R.id.btn_scan);
        edtTxt_resultado  =  findViewById(R.id.edtTxt_resultados);

        /*abre a camera ou fecha*/
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*metodo que chama a tela de leitura*/
                scan_codigo();
            }
        });

    }

    /*Chama a tela de leitura *****CORE DA APLICAÇÃO*****/
    public void scan_codigo(){
        IntentIntegrator intentIntegrator = new IntentIntegrator( this);
        intentIntegrator.setCameraId(0);  //Seleciona a camera traseira para a leitura (0: camera traseira, 1:camera frontal)
        intentIntegrator.addExtra(Intents.Scan.SCAN_TYPE, Intents.Scan.MIXED_SCAN);  //Indica que o leitor irá encontar varios tipos de codigos
        intentIntegrator.setTimeout(15000);   //Define tempo limite para caso a leitura nao dê certo
        intentIntegrator.initiateScan(); //Inicia o scan
    }

    @Override /*O resultado da leitura é retornado aqui */
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null) {
            if(result.getContents() != null) {

                edtTxt_resultado.setText(result.getContents());
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
