package proj.kinetics.Fragments;


import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import proj.kinetics.BroadcastReceivers.ConnectivityReceiver;
import proj.kinetics.MainActivity;
import proj.kinetics.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompletedTask extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener{
    WebView webView;
    ViewGroup container;
    Document doc=null;
    TextView internetid;
    String url="http://66.201.99.67/~kinetics/users/completed.html";
    private Activity mActivity;

    public CompletedTask() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.container = container;

        return inflater.inflate(R.layout.fragment_completed_task, container, false);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.three_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void checkConnection(ViewGroup container) {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected, container);
    } @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    private void showSnack(boolean isConnected, ViewGroup container) {
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
        webView = view.findViewById(R.id.webcompleted);
        internetid = view.findViewById(R.id.internetid2);

        checkConnection(container);

    }

    public void loadWebViewdata() {



        new AsyncTask<Void, Void, Document>() {
            @Override
            protected Document doInBackground(Void... voids) {
                Document document=null;
                try {
                    document= Jsoup.connect(url).get();
                    document.getElementsByClass("footer").remove();
                    document.getElementsByClass("navbar").remove();
                    document.getElementsByClass("main_title").remove();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return document;
            }

            @Override
            protected void onPostExecute(Document s) {
                doc=s;
                webView.loadDataWithBaseURL(url,s.toString(),"text/html","utf-8","");
                webView.setWebViewClient(new MyBrowser());
                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                webView.getSettings().setAppCacheMaxSize( 5 * 1024 * 1024 ); // 5MB
                webView.getSettings().setAppCachePath( mActivity.getCacheDir().getAbsolutePath() );
                webView.getSettings().setAllowFileAccess( true );
                webView.getSettings().setAppCacheEnabled( true );
                webView.getSettings().setCacheMode( WebSettings.LOAD_DEFAULT );
                webView.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK );
                super.onPostExecute(s);
            }
        }.execute();




    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected, container);

    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, final String url) {

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
                    webView.getSettings().setCacheMode( WebSettings.LOAD_DEFAULT );
                    webView.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK );

                    super.onPostExecute(s);
                }
            }.execute();
            view.loadUrl(url);

            return true;
        }



        @Override
        public void onPageFinished(WebView view, String url) {
            webView.loadUrl("javascript:document.body.style.padding=\"0%\"; void 0");
            webView.loadUrl("javascript:document.body.style.margin=\"0%\"; void 0");


        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            if (view.getSettings().getCacheMode()==WebSettings.LOAD_NO_CACHE){
                webView.setVisibility(View.GONE);
                internetid.setVisibility(View.VISIBLE);
            }
            super.onReceivedError(view, errorCode, description, failingUrl);

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Intent intent=new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);


        }
        return super.onOptionsItemSelected(item);
    }
}
