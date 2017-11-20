package proj.kinetics.Fragments;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.jsoup.nodes.Document;

import java.util.HashMap;

import proj.kinetics.BroadcastReceivers.ConnectivityReceiver;
import proj.kinetics.MyApplication;
import proj.kinetics.R;
import proj.kinetics.Utils.SessionManagement;

/**
 * A simple {@link Fragment} subclass.
 */
public class Reports extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    String url="";
    Document doc=null;

    WebView webView;
    ViewGroup container;
    TextView internetid;
    SessionManagement session;
    private Activity mActivity;
    public Reports() {
        // Required empty public constructor
    }

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
        String userid=user.get(SessionManagement.KEY_USERID);

        url="http://66.201.99.67/~kinetics/reports.php?user_id="+userid+"&viewreport=&key=9yESZ6XB4ey5afG9";
        // email
        String email = user.get(SessionManagement.KEY_PASSWORD);
     //   Toast.makeText(getActivity(), "LoggedIN"+name, Toast.LENGTH_SHORT).show();

        return inflater.inflate(R.layout.fragment_reports, container, false);
    }
  /*  @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.one_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.fragment_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("");
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
*/
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


        CookieSyncManager.createInstance(getActivity().getBaseContext());
        CookieSyncManager.getInstance().startSync();
        CookieSyncManager.getInstance().sync();
        webView.loadUrl(url);
        checkConnection(container);

    }

    public void loadWebViewdata(){



                    webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setLoadWithOverviewMode(true);
                    webView.getSettings().setUseWideViewPort(true);
                    webView.getSettings().setLoadsImagesAutomatically(true);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                    webView.getSettings().setAppCacheMaxSize(5 * 1024 * 1024); // 5MB
                    webView.getSettings().setAppCachePath(mActivity.getCacheDir().getAbsolutePath());
                    webView.getSettings().setAllowFileAccess(true);
                    webView.getSettings().setAppCacheEnabled(true);
                    webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);


    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected,container);

    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, final String url) {

            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {


            CookieSyncManager.getInstance().sync();
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

                            
    /*@Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this,container);


    }*/

}
