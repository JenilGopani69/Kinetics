package proj.kinetics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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

import proj.kinetics.Utils.ApiClient;
import proj.kinetics.Utils.ApiInterface;
import proj.kinetics.Utils.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Button loginBtn;
    int i=0,j=0;
    String task_name="", user_id, project_id, project_name, priority_id, task_id="", estimated_time,duration,due_date ,quantity="0", status, amount="0", task_details, pdf_link = null, dependent_task_id = null, video_link = null;
    String taskname="";
    String taskdescription="";
    String d_id="";
    SessionManagement session;
    EditText txtUsername, txtPassword;
    CheckBox show_hide_password;
    SQLiteDatabase sqLiteDatabase;
    DBHelper dbHelper;
SharedPreferences firstTime;
SharedPreferences.Editor firstTimeeditor;
    ProgressDialog progressdialog;
    CardView cd1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Session Manager
        dbHelper=new DBHelper(MainActivity.this);


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
                deleteDatabase("offline.db");

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
                if (response.isSuccessful()) {
                    try {

                        String data = response.body().string();
                        Log.d("mydata",data);
                        JSONObject jsonObject = new JSONObject(data);
                        String dataresponse = jsonObject.getString("message");
                        if (dataresponse.equalsIgnoreCase("success")) {

                            //  Toast.makeText(MainActivity.this, ""+response.body().string(), Toast.LENGTH_SHORT).show();

                            //get task detail   add and update task
                            user_id=jsonObject.getString("user_id");
                            if (jsonObject.has("task")) {

                                JSONArray taskjsnarray = jsonObject.getJSONArray("task");


                                for (int i = 0; i < taskjsnarray.length(); i++) {
                                    JSONObject jsonObject1 = taskjsnarray.getJSONObject(i);
                                    task_id = jsonObject1.getString("id");
                                    task_name = jsonObject1.getString("taskname");
                                    task_details = jsonObject1.getString("taskdescription");
                                    status = jsonObject1.getString("status");
                                    project_name = jsonObject1.getString("project_name");

                                    //totasl required
                                    quantity = jsonObject1.getString("quantity");
                                    estimated_time = jsonObject1.getString("estimated_time");
                                    duration = jsonObject1.getString("duration");
                                    due_date = jsonObject1.getString("due_date");
                                    amount = jsonObject1.getString("amount");
                                    pdf_link = jsonObject1.getString("pdf_link");
                                    video_link = jsonObject1.getString("video_link");

                                    if (dbHelper.istaskExisting(task_id))
                                    {
                                        dbHelper.updateTask(due_date,task_id,task_name,project_name,priority_id,estimated_time,duration,status,quantity,amount,task_details,pdf_link,"",video_link,user_id);
                                    }
                                    else
                                    {
                                        dbHelper.addTask(due_date,task_id,task_name,project_name,priority_id,estimated_time,duration,status,quantity,amount,task_details,pdf_link,"",video_link,user_id);
                                    }
                                    //add task or update task
                                    if (jsonObject1.has("qc")) {


                                        JSONArray qcarray = jsonObject1.getJSONArray("qc");
                                        for (int j = 0; j < qcarray.length(); j++) {
                                            JSONObject qcObject = qcarray.getJSONObject(j);
                                            String qcstatus, qcid, qcdescrip, qcimg, qcvideo;
                                            qcstatus = qcObject.getString("status");
                                            qcid = qcObject.getString("id");
                                            qcdescrip = qcObject.getString("descripton");
                                            qcimg = qcObject.getString("image_link");
                                            qcvideo = qcObject.getString("video_link");

                                            //add or update qc
                                            if (dbHelper.isQCExisting(qcid))
                                            {
                                                dbHelper.updateQC(qcid,qcstatus,qcdescrip,qcvideo,qcimg,user_id,task_id);
                                            }
                                            else
                                            {
                                                dbHelper.addQC(qcid,qcstatus,qcdescrip,qcvideo,qcimg,user_id,task_id);
                                            }

                                        }
                                    }
                                    if (jsonObject1.has("dependenttask")) {




                                            JSONObject maintask = jsonObject1.getJSONObject("dependenttask");
                                            //Toast.makeText(MainActivity.this, ""+maintask.length(), Toast.LENGTH_SHORT).show();

                                            String maintaskid = "", main_task_status, maintask_name;
                                            maintaskid = maintask.getString("id");
                                            // Toast.makeText(MainActivity.this, ""+maintaskid, Toast.LENGTH_SHORT).show();

                                            main_task_status = maintask.getString("status");
                                            maintask_name = maintask.getString("taskname");

                                            //add or update taskmapping
                                            if (main_task_status.equalsIgnoreCase("7")) {

                                            } else {
                                                if (dbHelper.isTaskMapId(task_id)) {
                                                    dbHelper.updateTaskMapping(task_id, maintaskid, maintask_name, main_task_status);

                                                } else {
                                                    dbHelper.insertTaskMapping(task_id, maintaskid, maintask_name, main_task_status);
                                                }


                                            }

                                    }

                                }
                            }
                            if (progressdialog.isShowing()){
                                progressdialog.dismiss();
                                Intent intent =new Intent(MainActivity.this,UserProfileActivity.class);
                                session.createLoginSession(username,password,user_id);

                                    // TODO: Use the current user's information
                                    // You can call any combination of these three methods



                                startActivity(intent);

                            }



                        }
                        else {
                            if (progressdialog.isShowing())
                            {
                                progressdialog.dismiss();
                                Toast.makeText(MainActivity.this, "Please Enter Valid Credentials", Toast.LENGTH_SHORT).show();
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressdialog.dismiss();
                Log.d("response",""+t.getMessage());
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
