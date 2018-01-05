package com.example.a.userinfo;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ViewSingleUserActivity extends AppCompatActivity {
    DatabaseHelper db;
    ImageView imageView;
    TextView name,phone,address,hobbies;
    int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_user);

        getSupportActionBar().setTitle("View Information ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db=new DatabaseHelper(this);
        Intent intent=getIntent();
        pos=intent.getIntExtra("id",1);


        imageView=(ImageView)findViewById(R.id.view_img);
        name=(TextView)findViewById(R.id.txtName);
        phone=(TextView)findViewById(R.id.txtPhone);
        address=(TextView)findViewById(R.id.txtAddress);
        hobbies=(TextView)findViewById(R.id.txtHobbies);

        Cursor c =  db.getStud1(pos);
        if (c.moveToFirst())
            DisplayEmp(c);

    }
    public void DisplayEmp(Cursor c) {
        // TODO Auto-generated method stub
        name.setText("Name : "+ c.getString(0));
        phone.setText("Phone No. : "+c.getString(1));
        address.setText("Address : "+c.getString(2));
        hobbies.setText("Hobbies : "+c.getString(3));
        Glide.with(getApplicationContext()).load(c.getString(4)).into(imageView);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
