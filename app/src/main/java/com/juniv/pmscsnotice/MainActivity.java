package com.juniv.pmscsnotice;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener
        {
          public  iMessage mainFrag;
    public  List<Notice> noticeList=new ArrayList<>();

    NoticeListAdapter adapter;
    JUService service;
    List<Notice> NoticeList;
    androidx.appcompat.widget.SearchView searchView;
            FloatingActionButton btnAction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchView = (androidx.appcompat.widget.SearchView) findViewById(R.id.searchView);
        btnAction=findViewById(R.id.btnDownLoad);
        searchView.setQueryHint("JU Notice Board");
        searchView.setOnQueryTextListener(this);

        searchView.setIconified(false);
        searchView.clearFocus();
        List<Notice> NoticeList=new ArrayList<Notice>();
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainFrag.NextPage();
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }
    @Override
    public boolean onQueryTextChange(String s) {
        mainFrag.SearchText(s);
        return true;
    }

}



