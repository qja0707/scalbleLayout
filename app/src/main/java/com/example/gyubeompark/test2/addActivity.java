package com.example.gyubeompark.test2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class addActivity extends AppCompatActivity {

    final static int UPDATE = 1;
    final static int ADD = 2;

    final static int RESULT_CANCEL=0;

    EditText name;
    EditText phoneNum;
    ImageView snapshot;
    Uri targetUri = null;

    Bundle bundle;

    final static int RESULT_OK = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);



        Button btnOk = findViewById(R.id.btnOk);
        Button btnCancel = findViewById(R.id.btnCancel);
        Button addImgBtn = findViewById(R.id.addImgBtn);


        Intent intent = getIntent();

        bundle = intent.getExtras();

        name = findViewById(R.id.editName);
        phoneNum = findViewById(R.id.editPhoneNum);
        snapshot = findViewById(R.id.addImg);


        final int position = bundle.getInt("position");

        if(position>=0){
            name.setText(bundle.getString("name"));
            phoneNum.setText(bundle.getString("phoneNum"));

            byte[] byteArray = bundle.getByteArray("image");

            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            snapshot.setImageBitmap(bmp);
        }
        else{
            Bitmap bmp = BitmapFactory.decodeResource(getBaseContext().getResources(),R.mipmap.unknown);
            snapshot.setImageBitmap(bmp);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();


            bundle.putByteArray("image",byteArray);
        }


        btnOk.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {


                Intent returnIntent = new Intent();
                Bundle returnBundle = new Bundle();
                if(targetUri!=null){
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), targetUri);
                        int tempInt = (int)(bitmap.getHeight()*(512.0/bitmap.getWidth()));
                        Bitmap scaled = Bitmap.createScaledBitmap(bitmap,512,tempInt,true);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        scaled.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        returnBundle.putByteArray("image",byteArray);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    returnBundle.putByteArray("image",bundle.getByteArray("image"));
                }

                returnBundle.putString("name",name.getText().toString());
                returnBundle.putString("phoneNum",phoneNum.getText().toString());
                returnBundle.putInt("position",position);

                returnIntent.putExtras(returnBundle);

                System.out.println("addActivity ok click : position : "+position);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCEL);
                finish();
            }
        });
        addImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("requestCode:"+requestCode);
        System.out.println("resultCode:"+resultCode);
        if (resultCode == RESULT_OK) {
            targetUri = data.getData();
            Bitmap bitmap;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), targetUri);
                int temp = (int)(bitmap.getHeight()*(512.0/bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap,512,temp,true);

                snapshot.setImageBitmap(scaled);

                System.out.println("change image");



            } catch (FileNotFoundException e) {
                System.out.println("in addActivity, when load image from gallery");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        setResult(RESULT_CANCEL);
        finish();
    }
}
