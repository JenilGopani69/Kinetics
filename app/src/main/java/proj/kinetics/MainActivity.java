package proj.kinetics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import proj.kinetics.BroadcastReceivers.ConnectivityReceiver;
import proj.kinetics.Database.DBHelper;
import proj.kinetics.Database.MyDbHelper;
import proj.kinetics.Utils.ApiClient;
import proj.kinetics.Utils.ApiInterface;
import proj.kinetics.Utils.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
Button loginBtn;

    SessionManagement session;
    EditText txtUsername, txtPassword;
CheckBox show_hide_password;

MyDbHelper myDbHelper;
    DBHelper dbHelper;

ProgressDialog progressdialog;
    CardView cd1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
myDbHelper=new MyDbHelper(getApplicationContext());
dbHelper=new DBHelper(MainActivity.this);
        // Session Manager
        session = new SessionManagement(getApplicationContext());
progressdialog=new ProgressDialog(MainActivity.this);

       if (session.isLoggedIn()==true){
            Intent intent=new Intent(MainActivity.this,UserProfileActivity.class);
            startActivity(intent);

        }



        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        show_hide_password = (CheckBox) findViewById(R.id.show_hide_password);
        show_hide_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    txtPassword.setTransformationMethod(null);
                }
                else {

                    txtPassword.setTransformationMethod(new PasswordTransformationMethod());

                }
            }
        });
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


                    boolean isconnected=ConnectivityReceiver.isConnected();
                    if (isconnected){
                        getUserLogin(username, password);
                    }
                    else {
                        progressdialog.dismiss();
                        Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                    }





                        }






            }
        });



    }



    private void getUserLogin(final String username, final String password) {
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> responseBodyCall=apiInterface.getTaskList(username,password);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {

                        String data=response.body().string();
                        JSONObject jsonObject=new JSONObject(data);
                        String dataresponse=jsonObject.getString("message");
                        if (dataresponse.equalsIgnoreCase("success")) {
                            deleteDatabase("offline.db");

                            String task_name, user_id, project_id, project_name, priority_id, task_id, estimated_time, required_time, status, total_qty, done_qty, task_details, pdf_link = null, dependent_task_id = null, video_link = null;


                            user_id = jsonObject.getString("user_id");
                            JSONArray jsonArray = jsonObject.getJSONArray("task");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jobj = jsonArray.getJSONObject(i);

                                task_id = jobj.getString("task_id");
                                project_id = jobj.getString("project_id");
                                project_name = jobj.getString("project_name");
                                task_name = jobj.getString("task_name");
                                estimated_time = jobj.getString("estimated_time");
                                required_time = jobj.getString("required_time");
                                status = jobj.getString("status");
                                total_qty = jobj.getString("total_qty");
                                done_qty = jobj.getString("done_qty");
                                task_details = jobj.getString("task_details");
                                priority_id = jobj.getString("priority");


                                if (dbHelper.isProjectExisting(project_id)){
                                    Log.e("errr",""+project_name+" "+user_id);
                                    dbHelper.updateProject(project_id,project_name,user_id);


                                }
                                else {
                                    Log.e("errr2",""+project_name+" "+user_id);

                                    dbHelper.addProject(project_id, project_name, user_id);

                                }
                                if (dbHelper.istaskExisting(task_id)){
                                    dbHelper.updateTask(task_id, task_name,project_id,priority_id, estimated_time, required_time, status, total_qty, done_qty, task_details, pdf_link, dependent_task_id, video_link, user_id);

                                    getTaskDetailsOffline(user_id, task_id);
                                }
                                else {
                                    dbHelper.addTask(task_id, task_name,project_id,priority_id, estimated_time, required_time, status, total_qty, done_qty, task_details, pdf_link, dependent_task_id, video_link, user_id);

                                    getTaskDetailsOffline(user_id,task_id);
                                }

                                if (progressdialog.isShowing()) {
                                    progressdialog.dismiss();
                                    session.createLoginSession(username, password, user_id);

                                } else {
                                    if (progressdialog.isShowing()) {
                                        progressdialog.dismiss();
                                    }
                                }


                            }


                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Log.d("response","no response");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("response",""+t.getMessage());
            }
        });
    }

    public void getTaskDetailsOffline(final String user_id, final String id) {
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> response = api.getTaskDetailsOffline(id);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String data = response.body().string();
                    JSONObject jsonObject = new JSONObject(data);
                    String dataresponse = jsonObject.getString("message");
                    if (dataresponse.equalsIgnoreCase("success"))
                    {
                        String taskid,taskname,taskdescription,quantity,estimated_time,duration,amount,pdf_link,video_link;

                        JSONArray depjsonarray=jsonObject.getJSONArray("dependenttask");

                        if (depjsonarray.length()>0) {

                            for (int i = 0; i < depjsonarray.length(); i++) {

                                JSONObject childobj=depjsonarray.getJSONObject(i);



                                    if (dbHelper.istaskExisting(id)){
                                     //   Toast.makeText(MainActivity.this,id+ "exits"+childobj.getString("id"), Toast.LENGTH_SHORT).show();
                                        dbHelper.updateDTask(id,childobj.getString("id"));
                                        Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                    }
                                    else {
                                        Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                    }



                            }
                        }



                        JSONArray qcjsonarray=jsonObject.getJSONArray("qualitycheck");
                        if (qcjsonarray.length()>0) {

                            for (int i = 0; i < qcjsonarray.length(); i++) {

                                JSONObject childobj=qcjsonarray.getJSONObject(i);

                                Log.d("qcdata",""+childobj.getString("descripton")+" "+id);

                                if (dbHelper.isQCExisting(childobj.getString("id"))){

                                    dbHelper.updateQC(childobj.getString("id"),childobj.getString("status"),childobj.getString("description"),childobj.getString("video_link"),childobj.getString("image_link"),user_id,id);
                                    Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                }
                                else {
                                    dbHelper.addQC(childobj.getString("id"),childobj.getString("status"),childobj.getString("description"),childobj.getString("video_link"),childobj.getString("image_link"),user_id,id);
                                    Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                }




                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
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
        }
    }


}
