package proj.kinetics;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import proj.kinetics.Adapters.QCAdapter;
import proj.kinetics.Adapters.QCAdapter_;
import proj.kinetics.BroadcastReceivers.ConnectivityReceiver;
import proj.kinetics.Database.DBHelper;
import proj.kinetics.Model.Dependenttask;
import proj.kinetics.Model.Qualitycheck;
import proj.kinetics.Model.Qualitycheck_;
import proj.kinetics.Model.TaskDetails;
import proj.kinetics.Session.TimerSession;
import proj.kinetics.Session.TimerSessionForAddTask;
import proj.kinetics.TimerWidget.TimeService;
import proj.kinetics.Utils.ApiClient;
import proj.kinetics.Utils.ApiInterface;
import proj.kinetics.Utils.RecyclerTouchListener;
import proj.kinetics.Utils.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QCActivity extends AppCompatActivity {


    public static Button finishtask;
    RecyclerView recyclerView, d_qc_recycler;
    TextView taskqcname;
    QCAdapter_ qcAd;
    QCAdapter myAdapter;
    String taskid = "", userId,taskselectedid;
    TimerSession timerSession;
    SharedPreferences sharedPreferences;
    DBHelper dbHelper;
    SharedPreferences.Editor editor;
    Toolbar toolbar;
    List<Qualitycheck> list = new ArrayList<>();
    LinearLayout taskd;
    private SessionManagement session;

TimerSessionForAddTask timerSessionForAddTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qc);
        toolbar = (Toolbar) findViewById(R.id.qctoolbar);
        timerSession = new TimerSession(getApplicationContext());
        dbHelper = new DBHelper(getApplicationContext());
        taskid = getIntent().getStringExtra("taskid");
        recyclerView = (RecyclerView) findViewById(R.id.qcrecyler);
        taskqcname = (TextView) findViewById(R.id.taskqcname);
        timerSessionForAddTask=new TimerSessionForAddTask(QCActivity.this);
        d_qc_recycler = (RecyclerView) findViewById(R.id.qcrecylerdependent);
        taskd = (LinearLayout) findViewById(R.id.taskd);
        finishtask = (Button) findViewById(R.id.finishtask);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL
                , false);
        session = new SessionManagement(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        userId = user.get(SessionManagement.KEY_USERID);

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL
                , false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(QCActivity.this, recyclerView, new RecyclerTouchListener.OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(QCActivity.this, ""+QCAdapter.arrayListQc, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        d_qc_recycler.setLayoutManager(layoutManager2);
        //  getTaskDetails();

        getTaskQC(taskid);




        finishtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog openDialog = new Dialog(QCActivity.this);
                openDialog.setContentView(R.layout.customdialog);
                timerSession.clearTimer();
                TextView dialogTextContent = (TextView) openDialog.findViewById(R.id.dialog_text);
                Button dialogCloseButton = (Button) openDialog.findViewById(R.id.dialog_button);

                dialogCloseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sharedPreferences = getSharedPreferences("tasktimer", MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        // Toast.makeText(QCActivity.this, taskid, Toast.LENGTH_SHORT).show();
                        dbHelper.updateTaskStatusQC(taskid, "7");

                        boolean isconnect = ConnectivityReceiver.isConnected();
                        if (isconnect) {
                            updateTaskStatus(userId, taskid, "7");

                        }
                        TimeService.TimeContainer.getInstance().stopAndReset();
                        Intent intent = new Intent(QCActivity.this, UserProfileActivity.class);
                        startActivity(intent);


                        openDialog.dismiss();
                    }
                });
                openDialog.show();
            }
        });
        finishtask.setBackgroundColor(Color.GRAY);
        finishtask.setEnabled(false);
        finishtask.setClickable(false);
HashMap<String,String> hm=timerSessionForAddTask.getTaskDetails(taskid);

        taskselectedid=hm.get(taskid);
        if (taskselectedid.equalsIgnoreCase("0")){
            Toast.makeText(this, "has sub task", Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(this, "has sub task", Toast.LENGTH_SHORT).show();
        }

    }

    private void getTaskQC(String taskid) {
        Cursor c = dbHelper.getQcByTask(taskid);

        String task_name, id, status, descripton, image_link, video_link;
        task_name = "";

        if (c.getCount() > 0) {
            if (c.moveToFirst()) {
                do {
                    id = c.getString(c.getColumnIndex("id"));
                    task_name = c.getString(c.getColumnIndex("task_name"));
                    taskqcname.setText(task_name);
                    status = c.getString(c.getColumnIndex("status"));
                    descripton = c.getString(c.getColumnIndex("description"));
                    image_link = c.getString(c.getColumnIndex("image_link"));
                    video_link = c.getString(c.getColumnIndex("video_link"));
                    Qualitycheck qualitycheck = new Qualitycheck(status, id, descripton, image_link, video_link);
                    qualitycheck.setUserid(userId);
                    qualitycheck.setTaskid(taskid);

                    list.add(qualitycheck);

                } while (c.moveToNext());
            }
        }


        myAdapter = new QCAdapter(list, getApplicationContext(), QCActivity.this);
        recyclerView.setAdapter(myAdapter);
    }

    private void updateTaskStatus(String userId, String taskid, String s) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> responseBody = apiInterface.updateTaskStatus(userId, taskid, s);
        responseBody.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(QCActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    private void getTaskDetails() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<TaskDetails> responseBodyCall = apiInterface.getTaskDetails(taskid);
        responseBodyCall.enqueue(new Callback<TaskDetails>() {
            @Override
            public void onResponse(Call<TaskDetails> call, Response<TaskDetails> response) {

                String data = response.body().getMessage();
                Log.d("taskdetail", data);
                taskqcname.setText(response.body().getTaskname());
                if (data.equalsIgnoreCase("success")) {
                    List<Dependenttask> dependent = response.body().getDependenttask();


                    List<Qualitycheck> qualitychecks = response.body().getQualitycheck();
                    if (qualitychecks == null) {

                    } else {


                        myAdapter = new QCAdapter(qualitychecks, getApplicationContext(), QCActivity.this);
                        recyclerView.setAdapter(myAdapter);

                    }
                    if (dependent == null) {


                    } /*else {
                        if (timerSession.isLoggedIn()) {
                            HashMap<String, String> hashMap2 = timerSession.getStaskDetails();

                            String id = hashMap2.get(TimerSession.KEY_S_TASKID);
                            if (id.equalsIgnoreCase("")) {


                            } else {

                                getDependentask();
                            }

                        }


                    }
*/                }


            }

            @Override
            public void onFailure(Call<TaskDetails> call, Throwable t) {

            }
        });


    }


    private void getDependentask() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<TaskDetails> responseBodyCall = apiInterface.getTaskDetails(taskid);
        responseBodyCall.enqueue(new Callback<TaskDetails>() {
            @Override
            public void onResponse(Call<TaskDetails> call, Response<TaskDetails> response) {
                String data = response.body().getMessage();

                if (data.equalsIgnoreCase("success")) {
                    List<Dependenttask> dependenttask = response.body().getDependenttask();
                    if (dependenttask == null) {

                    } else {
                        Dependenttask dep = dependenttask.get(0);
                        List<Qualitycheck_> qualitycheck_s = dep.getQualitycheck();
                        if (qualitycheck_s == null) {

                        } else {
                            taskd.setVisibility(View.VISIBLE);
                            qcAd = new QCAdapter_(qualitycheck_s, getApplicationContext());
                            d_qc_recycler.setAdapter(qcAd);


                        }
                    }


                }


            }

            @Override
            public void onFailure(Call<TaskDetails> call, Throwable t) {

            }
        });

    }

}
