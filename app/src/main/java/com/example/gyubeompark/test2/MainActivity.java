package com.example.gyubeompark.test2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final static int UPDATE = 1;
    final static int ADD = -2;
    final static int RESULT_OK = -1;
    final static int RESULT_CANCEL=0;

    private ListView listView = null;
    private MyAdapter adapter = null;
    ArrayList<ListData> list = null;

    Activity mainActivity = null;

    Button addBtn = null;
    Button escBtn = null;

    Bitmap img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
//        getActionBar().hide();
        mainActivity = this;

        addBtn = findViewById(R.id.addPerson);
        escBtn = findViewById(R.id.terminate);

        img = BitmapFactory.decodeResource(getBaseContext().getResources(),R.mipmap.unknown);

        ListData temp = new ListData();
        temp.name="aaa";
        temp.personImg = img;


        list = new ArrayList<>();

        for(int i=0;i<30;i++){
            temp.phoneNum = "010-1234-1234 "+i;
            list.add(temp);
        }


        adapter = new MyAdapter(this,list);

        listView = findViewById(R.id.lists);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(myLisntener);
        listView.setOnItemLongClickListener(LongClick);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, addActivity.class);
                intent.putExtra("position",ADD);
                startActivityForResult(intent, 0);
            }
        });

        escBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    AdapterView.OnItemLongClickListener LongClick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(final AdapterView<?> adapterView, View view, int i, long l) {
            System.out.println("adapterView: "+adapterView);
            System.out.println("view: "+view);
            System.out.println("i: "+i);
            System.out.println("l: "+l);

            final int target = i;
            final CustomDialogue dialog = new CustomDialogue(mainActivity); // Context, this, etc.

            dialog.show();

            Button deleteOk = dialog.findViewById(R.id.deleteOk);
            Button deleteCancel = dialog.findViewById(R.id.deleteCancel);

            deleteOk.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    list.remove(target);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Removed from list", Toast.LENGTH_SHORT).show();
                    dialog.hide();
                }
            });

            deleteCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
//            alertBuilder.setM
            return false;
        }
    };
    AdapterView.OnItemClickListener myLisntener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            System.out.println("adapterView: "+adapterView);
            System.out.println("view: "+view);
            System.out.println("i: "+i);
            System.out.println("l: "+l);

//            Drawable personImg = ((ImageView)view.findViewById(R.id.personImg)).getDrawable();
//            String name = ((TextView)view.findViewById(R.id.name)).getText().toString();
//            String phoneNum = ((TextView)view.findViewById(R.id.phoneNum)).getText().toString();

            Intent intent = new Intent(mainActivity,addActivity.class);

//            intent.putExtra("personImg",personImg);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            list.get(i).personImg.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Bundle bundle = new Bundle();
            bundle.putByteArray("image",byteArray);
            bundle.putInt("position",i);
            bundle.putString("name",list.get(i).name);
            bundle.putString("phoneNum",list.get(i).phoneNum);

            intent.putExtras(bundle);

//            startActivity(intent);
            startActivityForResult(intent,UPDATE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        int position=-1;

        System.out.println("resultCode:"+resultCode);


        System.out.println("onAcResult");
        if(resultCode==RESULT_OK){
            Bundle bundle = data.getExtras();

            byte[] byteArray = bundle.getByteArray("image");

            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

            position = bundle.getInt("position");
            String name = bundle.getString("name");
            String phoneNum = bundle.getString("phoneNum");

            ListData temp = new ListData();
//            temp.personImg = getResources().getDrawable(R.drawable.comingsoon);

            temp.personImg = bmp;
            temp.phoneNum = phoneNum;
            temp.name = name;


            System.out.println("position : "+position);
            if(position>=0)
                list.set(position,temp);
            else
                list.add(temp);

            adapter.notifyDataSetChanged();
        }

        else if(resultCode==RESULT_CANCEL){
            System.out.println("canceled");
        }

        else{
            System.out.println("Main onActivityResult else position : "+position);
        }
    }

}
