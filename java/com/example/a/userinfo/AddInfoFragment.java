package com.example.a.userinfo;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import static android.app.Activity.RESULT_OK;
import static com.example.a.userinfo.DatabaseHelper.PHOTO;
import static com.example.a.userinfo.DatabaseHelper.STUD_ADDRESS;
import static com.example.a.userinfo.DatabaseHelper.STUD_HOBBIES;
import static com.example.a.userinfo.DatabaseHelper.STUD_NAME;
import static com.example.a.userinfo.DatabaseHelper.STUD_PHONE;
import static com.example.a.userinfo.MyContentProvider.CONTENT_URI;


public class AddInfoFragment extends Fragment implements View.OnClickListener {

    private static final int PERMISSION_REQUEST_CODE = 1000;
    ImageView imageView;
    Button select,save;
    EditText name,phone,address,hobbies;
    String Name, Phone,Address,Hobbies;
    Bitmap bp;
    private byte[] photo;
    DatabaseHelper db;
    String imsgePath;
    String picPath;

    public AddInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_info, container, false);
        db=new DatabaseHelper(getContext());
        imageView=(ImageView)view.findViewById(R.id.img);
        select=(Button)view.findViewById(R.id.select);
        save=(Button)view.findViewById(R.id.save);

        name=(EditText) view.findViewById(R.id.name);
        phone=(EditText) view.findViewById(R.id.phone);
        address=(EditText) view.findViewById(R.id.addr);
        hobbies=(EditText)view.findViewById(R.id.hobbies);

        select.setOnClickListener(this);
        save.setOnClickListener(this);

        askPermission();
        return view;
    }

    private void askPermission() {

        int hasgetPhotoPermission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (hasgetPhotoPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            //Toast.makeText(AddContactsActivity.this, "Contact Permission is already granted", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.select:
                selectImage();
                break;
            case R.id.save:
                addInfo();
                break;
        }
    }
    public void selectImage(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //  photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 2);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode ,data);
        switch(requestCode) {
            case 2:
                if(resultCode == RESULT_OK){
                    Uri choosenImage = data.getData();

                    picPath=choosenImage.toString();

                    Glide.with(getContext()).load(picPath).into(imageView);


                }
        }
    }


    private void getValues(){

        Name=name.getText().toString();
        Phone=phone.getText().toString();
        Address=address.getText().toString();
        Hobbies=hobbies.getText().toString();

    }

    private void addInfo() {

        getValues();
        Student student=new Student(Name,Phone,Address,Hobbies, picPath);
        addData(student);
      //  db.addStudent(new Student(Name,Phone,Address,Hobbies, picPath));
        Toast.makeText(getContext(),"Add info successfully.......",Toast.LENGTH_LONG).show();

        name.setText("");
        phone.setText("");
        address.setText("");
        hobbies.setText("");


    }

    public void addData(Student student)
    {
        ContentValues values=new ContentValues();
        values.put(STUD_NAME,student.getStudName());
        values.put(STUD_PHONE,student.getStudPhone());
        values.put(STUD_ADDRESS,student.getStudAddress());
        values.put(STUD_HOBBIES,student.getStudHobbies());
        values.put(PHOTO,student.getImage());

       getActivity().getContentResolver().insert(CONTENT_URI,values);
    }

}
