package proj.kinetics.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
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
import java.net.CookieHandler;
import java.util.HashMap;

import proj.kinetics.BroadcastReceivers.ConnectivityReceiver;
import proj.kinetics.MainActivity;
import proj.kinetics.R;
import proj.kinetics.Utils.SessionManagement;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompletedTask extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener{
    WebView webView;
    ViewGroup container;
    Document doc=null;
    TextView internetid;
    String url;
    SessionManagement session;
   // String url="http://66.201.99.67/~kinetics/users/completed.html";
  // String url="http://kinetics.local/completedprojects.php?user_id=2&viewreport=&key=9yESZ6XB4ey5afG9";
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
        session = new SessionManagement(getActivity());
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManagement.KEY_USERNAME);
String userid=user.get(SessionManagement.KEY_USERID);
        url="http://66.201.99.67/~kinetics/completedprojects.php?user_id="+userid+"&viewreport=&key=9yESZ6XB4ey5afG9";
        // password
        String password = user.get(SessionManagement.KEY_PASSWORD);
        //Toast.makeText(getActivity(), "LoggedIN"+name, Toast.LENGTH_SHORT).show();
        return inflater.inflate(R.layout.fragment_completed_task, container, false);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.one_menu, menu);



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

        CookieSyncManager.createInstance(getActivity().getBaseContext());
        CookieSyncManager.getInstance().startSync();
        CookieSyncManager.getInstance().sync();
        webView.loadUrl(url);
        checkConnection(container);

    }

    public void loadWebViewdata() {




                webView.setWebViewClient(new MyBrowser());
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





    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected, container);

    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, final String url) {


            view.loadUrl(url);

            return true;
        }



        @Override
        public void onPageFinished(WebView view, String url) {



           webView.loadUrl("javascript:document.body.style.padding=\"0%\"; void 0");
            webView.loadUrl("javascript:document.body.style.margin=\"0%\"; void 0");
            CookieSyncManager.getInstance().sync();


        }
        public String getCookie(String siteName,String CookieName){
            String CookieValue = null;

            CookieManager cookieManager = CookieManager.getInstance();
            String cookies = cookieManager.getCookie(siteName);
            String[] temp=cookies.split(";");
            for (String ar1 : temp ){
                if(ar1.contains(CookieName)){
                    String[] temp1=ar1.split("=");
                    CookieValue = temp1[1];
                    break;
                }
            }
            return CookieValue;
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
        if (id == R.id.fragment_logout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.app_name);

            builder.setIcon(R.mipmap.ic_alert);
            builder.setMessage("Do you want to exit?")
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
}
