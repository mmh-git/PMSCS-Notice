package com.juniv.pmscsnotice.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.juniv.pmscsnotice.JUService;
import com.juniv.pmscsnotice.MainActivity;
import com.juniv.pmscsnotice.Notice;
import com.juniv.pmscsnotice.NoticeListAdapter;
import com.juniv.pmscsnotice.PushNotification;
import com.juniv.pmscsnotice.R;
import com.juniv.pmscsnotice.Utility;
import com.juniv.pmscsnotice.iMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements iMessage {

    public  List<Notice> noticeList=new ArrayList<>();
    RecyclerView rv;
    NoticeListAdapter adapter;
    JUService service;
    Button close;
      View root;
    SwipeRefreshLayout refreshLayout;
    iMessage mCallback;
    Context mActivity;
    private RecyclerView.LayoutManager layoutManager;
    public static int CurrentPage=0;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        close=root.findViewById(R.id.close);
        rv=root.findViewById(R.id.my_recycler_view);
        refreshLayout=root.findViewById(R.id.swipeRefreshLayout);
        rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getContext());
        rv.setLayoutManager(layoutManager);
        service =new JUService();
        adapter=new NoticeListAdapter(noticeList,(MainActivity) this.getContext(),service,root,refreshLayout);
        rv.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Refresh(CurrentPage,new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                root.findViewById(R.id.rlLoadPDF).setVisibility(View.GONE);
            }
        });
        return root;
    }

    @Override
    public void onStart() {

        super.onStart();
        Refresh(CurrentPage,new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(HomeFragment.this.getContext(), PushNotification.class);
                HomeFragment.this.getContext().startService(intent);
            }
        });
    }


    void Refresh(final int currentPage, final Runnable runnable)
    {
        Call<String> callback=service.get(currentPage);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String msg=response.body();
                if(msg!="") {
                    noticeList.clear();
                    new Utility().PopulateList(noticeList, msg);

                    /*Comparator<Notice> compareById = new Comparator<Notice>() {
                        @Override
                        public int compare(Notice o1, Notice o2) {
                            return o1.getFile()-o2.getFile();
                        }
                    };

                    Collections.sort(noticeList, compareById);*/

                    adapter.setDataset(noticeList);
                    adapter.notifyDataSetChanged();
                    root.findViewById(R.id.progressBar_cyclic).setVisibility(View.GONE);
                    new Utility().setNoticeID(getContext(), noticeList.get(0).getFile());
                    if (runnable != null)
                        runnable.run();
                }
                else
                {
                    Toast.makeText(getContext(), "You have reached last page", Toast.LENGTH_SHORT).show();
                    CurrentPage--;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        mActivity=context;
        super.onAttach(context);
        ( (MainActivity) mActivity).mainFrag =this ;
        if(adapter==null)
            adapter=new NoticeListAdapter(noticeList,(MainActivity) this.getContext(),service,root,refreshLayout);
    }

    @Override
    public void SearchText(String s) {
        adapter.applyFilter(s,noticeList);
    }
    public  void NextPage()
    {
        CurrentPage++;
        refreshLayout.setRefreshing(true);
        Refresh(CurrentPage,new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        });
    }
}