package droidrocks.com.writepdfusingjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button button;
    private static final int STORAGE_CODE =1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.textEdit);
        button = findViewById(R.id.saveBtn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
                                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                                        PackageManager.PERMISSION_DENIED){

                            String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permission,STORAGE_CODE);

                                }else {
                                    //Permission granted
                                    savePDF();
                                }

                }else {
                        // system OS < M , no need to ask permission Call pdf method
                    savePDF();
                }

            }
        });









    }

    private void savePDF() {
            // create object
        Document mDoc = new Document();
        // pdf file name
        String mFileName = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis());
        // pdf file path
        String mFilePath = Environment.getExternalStorageDirectory()+"/"+mFileName+".pdf";

        try {
            // create instance of pdfWriter class
            PdfWriter.getInstance(mDoc,new FileOutputStream(mFilePath));
            // open the document for writing
            mDoc.open();
            // get text from EditText
            String mText = editText.getText().toString();
            // add author of the document (optional)
            mDoc.addAuthor("MD TAYOBUR RAHMAN");
            // add paragraphs to the document
            mDoc.add(new Paragraph(mText));
            // close the document
            mDoc.close();
            // show message that file is saved
            Toast.makeText(this, "File Saved Successfully", Toast.LENGTH_SHORT).show();


        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("TAG", "savePDF: "+e.getMessage());
        }


    }

    // handle permission result

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       switch (requestCode){
           case STORAGE_CODE:{
               if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // permission was granted from popup
                   savePDF();
               }else {
                   // permission was denied from popup , show error message 
                   Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
               }
           }
       }
    }
}