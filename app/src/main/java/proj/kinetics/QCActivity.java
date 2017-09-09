package proj.kinetics;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import proj.kinetics.Adapters.QCAdapter;
import proj.kinetics.Adapters.QCAdapter_;
import proj.kinetics.Database.DBHelper;
import proj.kinetics.Model.Dependenttask;
import proj.kinetics.Model.Qualitycheck;
import proj.kinetics.Model.Qualitycheck_;
import proj.kinetics.Model.TaskDetails;
import proj.kinetics.Session.TimerSession;
import proj.kinetics.TimerWidget.TimeService;
import proj.kinetics.Utils.ApiClient;
import proj.kinetics.Utils.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QCActivity extends AppCompatActivity {


   RecyclerView recyclerView,d_qc_recycler;
public static     Button finishtask;

    QCAdapter_ qcAd;
    QCAdapter myAdapter;
    String taskid="";
    TimerSession timerSession;
SharedPreferences sharedPreferences;
DBHelper dbHelper;
    SharedPreferences.Editor editor;
Toolbar toolbar;

    LinearLayout taskd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qc);
        toolbar = (Toolbar) findViewById(R.id.qctoolbar);
        timerSession=new TimerSession(getApplicationContext());
dbHelper=new DBHelper(getApplicationContext());
        taskid=getIntent().getStringExtra("taskid");
        recyclerView = (RecyclerView) findViewById(R.id.qcrecyler);
        d_qc_recycler = (RecyclerView) findViewById(R.id.qcrecylerdependent);
        taskd = (LinearLayout) findViewById(R.id.taskd);
        finishtask = (Button) findViewById(R.id.finishtask);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL
                , false);


        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL
                , false);
        recyclerView.setLayoutManager(layoutManager);
        d_qc_recycler.setLayoutManager(layoutManager2);
        getTaskDetails();

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
                        sharedPreferences=getSharedPreferences("tasktimer",MODE_PRIVATE);
                        editor=sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        Toast.makeText(QCActivity.this, taskid, Toast.LENGTH_SHORT).show();
                        dbHelper.updateTaskStatus(taskid,"7");
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


    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    private void getTaskDetails() {
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        Call<TaskDetails> responseBodyCall=apiInterface.getTaskDetails(taskid);
        responseBodyCall.enqueue(new Callback<TaskDetails>() {
            @Override
            public void onResponse(Call<TaskDetails> call, Response<TaskDetails> response) {

                String data=response.body().getMessage();
                Log.d("taskdetail",data);
                if (data.equalsIgnoreCase("success")){
                    List<Dependenttask> dependent= response.body().getDependenttask();


                    List<Qualitycheck> qualitychecks=response.body().getQualitycheck();
                    if (qualitychecks==null){

                    }else {


                        myAdapter = new QCAdapter(qualitychecks, getApplicationContext());
                        recyclerView.setAdapter(myAdapter);

                    }
                    if (dependent==null){


                    }else {
                        if (timerSession.isLoggedIn()){
                            HashMap<String,String> hashMap2=timerSession.getStaskDetails();

                            String id=hashMap2.get(TimerSession.KEY_S_TASKID);
                            if (id.equalsIgnoreCase("")){



                            }
                            else {

                                getDependentask();
                            }

                        }


                    }
                }






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
                String data=response.body().getMessage();

                if (data.equalsIgnoreCase("success"))
                {
                    List<Dependenttask> dependenttask=response.body().getDependenttask();
                    if (dependenttask==null){

                    }else {
                        Dependenttask dep=dependenttask.get(0);
                        List<Qualitycheck_> qualitycheck_s=dep.getQualitycheck();
                        if (qualitycheck_s==null){

                        }
                        else {
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
