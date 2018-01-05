package com.example.a.userinfo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static com.example.a.userinfo.DatabaseHelper.PHOTO;
import static com.example.a.userinfo.DatabaseHelper.STUD_ADDRESS;
import static com.example.a.userinfo.DatabaseHelper.STUD_HOBBIES;
import static com.example.a.userinfo.DatabaseHelper.STUD_ID;
import static com.example.a.userinfo.DatabaseHelper.STUD_NAME;
import static com.example.a.userinfo.DatabaseHelper.STUD_PHONE;
import static com.example.a.userinfo.DatabaseHelper.TABLE_STUDENT;
import static com.example.a.userinfo.MyContentProvider.CONTENT_URI;


public class ViewFragment extends Fragment {

    private List<Student> cartList;
    private UserAdapter mAdapter;

    DatabaseHelper db;
    RecyclerView recyclerView;
    public ViewFragment() {
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
        View view=inflater.inflate(R.layout.fragment_view, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        cartList = new ArrayList<>(getAllEmployees());

        mAdapter = new UserAdapter(getActivity(), cartList, new ClickListener() {

            @Override
            public void onPositionClicked(int position) {
                Student student=cartList.get(position);

                Intent intent=new Intent(getContext(),ViewSingleUserActivity.class);
                intent.putExtra("id",student.getStudId());

                startActivity(intent);
            }

        });

        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        return view;
    }


    public List<Student> getAllEmployees() {
        List<Student> employeeList = new ArrayList<Student>();
        Cursor cursor=getActivity().getContentResolver().query(CONTENT_URI,null,null,null,null);

        if (cursor.moveToFirst()) {
            do {
                Student employee = new Student();
                employee.setStudId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(STUD_ID))));
                employee.setStudName(cursor.getString(cursor.getColumnIndex(STUD_NAME)));
                employee.setStudPhone(cursor.getString(cursor.getColumnIndex(STUD_PHONE)));
                employee.setStudAddress(cursor.getString(cursor.getColumnIndex(STUD_ADDRESS)));
                employee.setStudHobbies(cursor.getString(cursor.getColumnIndex(STUD_HOBBIES)));
                employee.setImage(cursor.getString(cursor.getColumnIndex(PHOTO)));
                // Adding contact to list
                employeeList.add(employee);
            } while (cursor.moveToNext());
        }

        // return contact list
        return employeeList;
    }

}
