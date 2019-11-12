package com.hungviet.contenprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView lvList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvList = findViewById(R.id.listview);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode ==888){
            Toast.makeText(this,"Lệnh Xin Quyền" + grantResults[0],Toast.LENGTH_SHORT).show();

        }
    }

    public void loadContacts(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            Toast.makeText(this,"Chưa có quyền",Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_CALL_LOG},
                    888);
        }
        run();

    }
    public void run() {Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        String data = "";
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    //String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    data =  name + "-" + number ;
                    list.add(data);
                    cursor.moveToNext();

                }
                cursor.close();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, list);
                lvList.setAdapter(adapter);

            }
        }
    }
    public void accessTheCallLog()
    {

        try {
            String[] projection = new String[]{
                    CallLog.Calls.DATE,
                    CallLog.Calls.NUMBER,
                    CallLog.Calls.DURATION

            };
            ArrayList<String> list = new ArrayList<>();
            Cursor c = getContentResolver().query(
                    CallLog.Calls.CONTENT_URI,
                    projection,
                    CallLog.Calls.DURATION + "<?", new String[]{"30"},
                    CallLog.Calls.DATE + " Asc");
            c.moveToFirst();
            String s = "";
            while (c.isAfterLast() == false) {
                for (int i = 0; i < c.getColumnCount(); i++) {
                    s += c.getString(i) + " - ";
                }

                c.moveToNext();

                list.add(s);

            }
            c.close();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,list);
            lvList.setAdapter(adapter);
            list.remove(s);
        }catch(Exception e) {

        }



    }

    public void loadContacts2(View view) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                Toast.makeText(this,"Chưa có quyền",Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        898);
            }
            accessTheCallLog();
    }
}
