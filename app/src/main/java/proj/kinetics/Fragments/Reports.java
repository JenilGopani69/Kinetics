package proj.kinetics.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;

import proj.kinetics.BroadcastReceivers.ConnectivityReceiver;
import proj.kinetics.MainActivity;
import proj.kinetics.R;
import proj.kinetics.MyApplication;
import proj.kinetics.Utils.SessionManagement;

/**
 * A simple {@link Fragment} subclass.
 */
public class Reports extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    String url="http://66.201.99.67/~kinetics/users/reports.html";
    Document doc=null;

    WebView webView;
    ViewGroup container;
    TextView internetid;
    private Activity mActivity;
    public Reports() {
        // Required empty public constructor
    }
    SessionManagement session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.container=container;
        session = new SessionManagement(getActivity());
        session.checkLogin();
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManagement.KEY_USERNAME);

        // email
        String email = user.get(SessionManagement.KEY_PASSWORD);
        Toast.makeText(getActivity(), "LoggedIN"+name, Toast.LENGTH_SHORT).show();

        return inflater.inflate(R.layout.fragment_reports, container, false);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.one_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.fragment_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.app_name);
            builder.setIcon(R.mipmap.ic_alert);
            builder.setMessage("Do you want to logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            session.logoutUser();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();



        }

        return super.onOptionsItemSelected(item);
    }

    private void checkConnection(ViewGroup container) {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected,container);
    }
    private void showSnack(boolean isConnected,ViewGroup container) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
            webView.setVisibility(View.VISIBLE);

            internetid.setVisibility(View.GONE);

            loadWebViewdata();
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
            webView.setVisibility(View.GONE);
            internetid.setVisibility(View.VISIBLE);


        }

        Snackbar snackbar = Snackbar
                .make(container, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView=view.findViewById(R.id.webreports);
        internetid=view.findViewById(R.id.internetid);

        checkConnection(container);

    }

    public void loadWebViewdata(){
        new AsyncTask<Void, Void, Document>() {
            @Override
            protected Document doInBackground(Void... voids) {
                Document document = null;
                try {
                    document= Jsoup.connect(url).get();
                    document.getElementsByClass("footer").remove();
                    document.getElementsByClass("navbar").remove();
                    document.getElementsByClass("main_title").remove();
                    Log.d("htmlcntent",""+document.getElementById("reportrange"));




                } catch (IOException e) {
                    e.printStackTrace();
                }
                return document;
            }

            @Override
            protected void onPostExecute(Document s) {
                webView.loadDataWithBaseURL(url,s.toString(),"text/html","utf-8","");
                webView.setWebViewClient(new MyBrowser());
                webView.getSettings().setLoadWithOverviewMode(true);
                webView.getSettings().setUseWideViewPort(true);
                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                webView.getSettings().setAppCacheMaxSize( 5 * 1024 * 1024 ); // 5MB
                webView.getSettings().setAppCachePath( mActivity.getCacheDir().getAbsolutePath() );
                webView.getSettings().setAllowFileAccess( true );
                webView.getSettings().setAppCacheEnabled( true );
                webView.getSettings().setJavaScriptEnabled( true );
                webView.getSettings().setCacheMode( WebSettings.LOAD_DEFAULT );
                webView.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK );
                super.onPostExecute(s);
            }
        }.execute();

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected,container);

    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, final String url) {
            Log.d("mydata",url);
            new AsyncTask<Void, Void, Document>() {
                @Override
                protected Document doInBackground(Void... voids) {
                    Document document=null;
                    try {
                        document=Jsoup.connect(url).get();
                        document.getElementsByClass("footer").remove();
                        document.getElementsByClass("navbar").remove();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return document;
                }

                @Override
                protected void onPostExecute(Document s) {
                    doc=s;
                    webView.loadDataWithBaseURL(url,s.toString(),"text/html","utf-8","");
                    //webView.setWebViewClient(new CompletedProjects.MyBrowser());
                    webView.getSettings().setLoadsImagesAutomatically(true);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                    webView.getSettings().setAppCacheMaxSize( 5 * 1024 * 1024 ); // 5MB
                    webView.getSettings().setAppCachePath( getActivity().getApplicationContext().getCacheDir().getAbsolutePath() );
                    webView.getSettings().setAllowFileAccess( true );
                    webView.getSettings().setAppCacheEnabled( true );
                    webView.getSettings().setJavaScriptEnabled( true );
                    webView.getSettings().setCacheMode( WebSettings.LOAD_DEFAULT );
                    webView.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK );
                    webView.getSettings().setLoadWithOverviewMode(true);
                    webView.getSettings().setUseWideViewPort(true);
                    super.onPostExecute(s);
                }
            }.execute();
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webView.loadUrl("javascript:document.body.style.padding=\"0%\"; void 0");

            webView.loadUrl("javascript:document.body.style.margin=\"0%\"; void 0");

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this,container);


    }

}
