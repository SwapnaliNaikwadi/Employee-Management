package com.example.a.userinfo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import static android.app.Activity.RESULT_OK;
import static com.example.a.userinfo.DatabaseHelper.PHOTO;
import static com.example.a.userinfo.DatabaseHelper.STUD_ADDRESS;
import static com.example.a.userinfo.DatabaseHelper.STUD_HOBBIES;
import static com.example.a.userinfo.DatabaseHelper.STUD_ID;
import static com.example.a.userinfo.DatabaseHelper.STUD_NAME;
import static com.example.a.userinfo.DatabaseHelper.STUD_PHONE;
import static com.example.a.userinfo.MyContentProvider.CONTENT_URI;


public class UpdateFragment extends Fragment implements View.OnClickListener {
    EditText name, phone, address, hobbies;
    EditText search_name;
    Button search, update, select;
    DatabaseHelper db;
    ImageView imageView;
    String Name, Phone, Address, Hobbies, picPath;
    LinearLayout linearLayout;

    public UpdateFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db=new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update, container, false);
        search_name = (EditText) view.findViewById(R.id.search_name);
        search = (Button) view.findViewById(R.id.search);
        select = (Button) view.findViewById(R.id.select);
        name = (EditText) view.findViewById(R.id.name);
        phone = (EditText) view.findViewById(R.id.phone);
        address = (EditText) view.findViewById(R.id.addr);
        hobbies = (EditText) view.findViewById(R.id.hobbies);
        imageView = (ImageView) view.findViewById(R.id.img);

        update = (Button) view.findViewById(R.id.update);

        linearLayout = (LinearLayout) view.findViewById(R.id.layout);
        search.setOnClickListener(this);
        select.setOnClickListener(this);
        update.setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.update:
                String s_name1=search_name.getText().toString();

                Name = name.getText().toString().trim();
                Phone = phone.getText().toString().trim();
                Address = address.getText().toString().trim();
                Hobbies = hobbies.getText().toString().trim();
                Student employee = new Student(Name, Phone, Address, Hobbies, picPath);
                //db.update(employee,s_name1);
                UpdateData(employee,s_name1);
                name.setText("");
                phone.setText("");
                address.setText("");
                hobbies.setText("");
                break;
            case R.id.select:
                selectImage();
                break;
            case R.id.search:
                String s_name=search_name.getText().toString();

                Cursor c =  db.getStud(s_name);
                if (c.moveToFirst())
                    DisplayEmp(c);
                else
                    Toast.makeText(getContext(), "No Record found", Toast.LENGTH_LONG).show();

                break;
        }

    }

    public void DisplayEmp(Cursor c) {
        linearLayout.setVisibility(View.VISIBLE);
        // TODO Auto-generated method stub
        name.setText(c.getString(0));
        phone.setText(c.getString(1));
        address.setText(c.getString(2));
        hobbies.setText(c.getString(3));
        Glide.with(getContext()).load(c.getString(4)).into(imageView);


    }

    public void selectImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //  photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                if (resultCode == RESULT_OK) {
                    Uri choosenImage = data.getData();
                    picPath =choosenImage.toString();
                  /*  String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(choosenImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    picPath = c.getString(columnIndex);
                    c.close();
                    Bitmap thumbnail = (BitmapFactory.decodeFile(picPath));*/
                   // imageView.setImageBitmap(thumbnail);
                    Glide.with(getContext()).load(picPath).into(imageView);

                }
        }
    }
    public void UpdateData(Student student,String name)
    {
       // int id=1;
        ContentValues values=new ContentValues();
        values.put(STUD_NAME,student.getStudName());
        values.put(STUD_PHONE,student.getStudPhone());
        values.put(STUD_ADDRESS,student.getStudAddress());
        values.put(STUD_HOBBIES,student.getStudHobbies());
        values.put(PHOTO,student.getImage());
        getActivity().getContentResolver().update(CONTENT_URI,values,STUD_NAME + " = ?",
                new String[]{name});
    }

}
