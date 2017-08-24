package proj.kinetics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.os.StrictMode;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import proj.kinetics.BroadcastReceivers.ConnectivityReceiver;
import proj.kinetics.Database.MyDbHelper;
import proj.kinetics.Utils.ApiClient;
import proj.kinetics.Utils.ApiInterface;
import proj.kinetics.Utils.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
Button loginBtn;

    SessionManagement session;
    EditText txtUsername, txtPassword;


MyDbHelper myDbHelper;

ProgressDialog progressdialog;
    CardView cd1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
myDbHelper=new MyDbHelper(getApplicationContext());

        // Session Manager
        session = new SessionManagement(getApplicationContext());
progressdialog=new ProgressDialog(MainActivity.this);

       if (session.isLoggedIn()==true){
            Intent intent=new Intent(MainActivity.this,UserProfileActivity.class);
            startActivity(intent);

        }



        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        loginBtn= (Button) findViewById(R.id.loginBtn);
        cd1= (CardView) findViewById(R.id.card_view);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                if(username.trim().length() > 0 && password.trim().length() > 0) {
                    // For testing puspose username, password is checked with sample data
                    // username = test
                    // password = test
                    progressdialog.show();

                            getUserLogin(username, password);




                        }






            }
        });



    }

    private void getUserLogin(final String username, final String password) {
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> responseBodyCall=apiInterface.getTaskList(username);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {

String data=response.body().string();

                        Toast.makeText(MainActivity.this, "data inserted"+response.body().string(), Toast.LENGTH_SHORT).show();
                            if (progressdialog.isShowing()){
                                progressdialog.dismiss();
                                session.createLoginSession(username, password);
                                Intent intent=new Intent(MainActivity.this,UserProfileActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);


                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(MainActivity.this, "no respnse", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (session.isLoggedIn()==true){
            Intent intent=new Intent(MainActivity.this,UserProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        }
    }


}
