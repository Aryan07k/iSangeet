package com.example.isangeet;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ListView listView ;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listview);
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        Toast.makeText(MainActivity.this, "Runtime Permission Given", Toast.LENGTH_SHORT).show();
                        Log.w(TAG,"hello");
                        ArrayList<File> mysongs = fetchSongs(new File(Environment.getExternalStorageDirectory().getAbsolutePath()));
                        String [] items = new String[mysongs.size()];

                        for(int i=0; i<mysongs.size(); i++ ){
                            items[i] = mysongs.get(i).getName().replace(".mp3", "");
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, items);
                        listView.setAdapter(adapter);

                        
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(!Utils.isPermissionGranted(this)){
//            new AlertDialog.Builder(this)
//                    .setTitle("All Permission")
//                    .setMessage("Due to Android 11 restrictions, this app requires all fils permission")
//                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                        takePermission();
//                        }
//                    })
//                    .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                        }
//                    })
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .show();
//        }
//        else{
//            Toast.makeText(this, "Permission already Granted", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void takePermission() {
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
//            try {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                intent.addCategory("android.intent.category.DEFAULT");
//                Uri uri = Uri.fromParts("package", getPackageName(), null);
//                intent.setData(uri);
//                startActivityForResult(intent, 101);
//            } catch (Exception e) {
//                e.printStackTrace();
//                Intent intent =new Intent();
//                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                startActivityForResult(intent, 101);
//            }
//        }
//        else{
//            ActivityCompat.requestPermissions(this, new String[]{
//                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
//            }, 101);
//        }
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(grantResults.length>0){
//            if(requestCode==101){
//                boolean readExt = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                if(!readExt){
//                    takePermission();
//                }
//            }
//        }
//    }

    public ArrayList<File> fetchSongs(File file){
        ArrayList<File> arrayList =new ArrayList<File>();
        File [] songs = file.listFiles();
        Log.w(TAG,"hello");
        if(songs !=null){
         for (File myfile: songs){
             if (!myfile.isHidden() && myfile.isDirectory()){
                 arrayList.addAll(fetchSongs(myfile));
             }
             else{
                 if(myfile.getName().endsWith(".mp3") && !myfile.getName().startsWith(".")){
                     arrayList.add(myfile);
                 }
             }
         }
        }
        return arrayList;
    }
}