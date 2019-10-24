package com.juniv.pmscsnotice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import java.util.ArrayList;
import java.util.List;

public class NoticeListAdapter extends RecyclerView.Adapter<NoticeListAdapter.CustomHolder> {
    private List<Notice> mDataset;
    private MainActivity _context;
    JUService api;
    private boolean isChecked = false;
    View _root;

    SwipeRefreshLayout refreshLayout;

    public NoticeListAdapter(List<Notice> myDataset, MainActivity context, JUService api, View root ,
                             SwipeRefreshLayout refreshLayout ) {
        mDataset = myDataset;
        _context = context;
        this.api = api;
        _root=root;
        this.refreshLayout= refreshLayout;

    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public List<Notice> getDataset() {
        return mDataset;
    }

    public void setDataset(List<Notice> mDataset) {
        this.mDataset = mDataset;
    }

    public static class CustomHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public  RelativeLayout rv;
        View view;
        //public  sml;
        public CustomHolder(View v) {
            super(v);
            tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            rv=(RelativeLayout)v.findViewById(R.id.rlapproval);
            view = v;
        }
    }

    @Override
    public NoticeListAdapter.CustomHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
// create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        CustomHolder vh = new CustomHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(NoticeListAdapter.CustomHolder holder, int position) {
        final NoticeListAdapter adapter = this;
        final NoticeListAdapter.CustomHolder myViewHolder = (NoticeListAdapter.CustomHolder) holder;
        final Notice model = mDataset.get(position);
        holder.tvTitle.setText(model.getTitle());


        if (position % 2 == 0) {
            holder.view.setBackgroundResource(R.color.colorAccent);
        }
        if (position % 2 != 0) {
            holder.view.setBackgroundResource(R.color.colorPrimary);
        }


        bindEvent(myViewHolder, myViewHolder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        if (mDataset == null)
            return 0;
        return mDataset.size();
    }

    private void bindEvent(final CustomHolder holder, final int position) {
        final Notice model = mDataset.get(position);
        final NoticeListAdapter adapter = this;

        holder.rv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String url="https://docs.google.com/gview?embedded=true&url=http://juniv.edu.bd/discussion/"+model.getDiscussion()+"/file/"+model.getFile();
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                _context.startActivity(browserIntent);
                WebView webView= _root.findViewById(R.id.loadPDF);

                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setAllowFileAccess(true);
                webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
                webView.getSettings().setBuiltInZoomControls(true);
               // _context.findViewById(R.id.progressBar_cyclic).setVisibility(View.VISIBLE);
                refreshLayout.setRefreshing(true);
                webView.setWebChromeClient(new WebChromeClient());
                webView.loadUrl(url);

                webView.setWebViewClient(new WebViewClient() {

                    public void onPageFinished(WebView view, String url){
                        _root.findViewById(R.id.rlLoadPDF).setVisibility(View.VISIBLE);
                       // _context.findViewById(R.id.progressBar_cyclic).setVisibility(View.GONE);
                        refreshLayout.setRefreshing(false);
                    }
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });

            }
        });

    }




    public void applyFilter(String q, List<Notice> dataSet) {

        List<Notice> Match = new ArrayList<Notice>();
        if (q.length() != 0) {
            for (Notice d : dataSet) {
                if (d.getTitle().toLowerCase().trim().contains(q.toLowerCase().trim())) {
                    Match.add(d);
                }
            }
            this.setDataset(Match);
        } else {
            this.setDataset(dataSet);
        }
        notifyDataSetChanged();
    }


}

