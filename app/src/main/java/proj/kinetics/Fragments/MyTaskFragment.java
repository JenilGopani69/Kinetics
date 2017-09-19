package proj.kinetics.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import proj.kinetics.Adapters.ProjectsAdapter;
import proj.kinetics.BroadcastReceivers.ConnectivityReceiver;
import proj.kinetics.Database.DBHelper;
import proj.kinetics.Model.DTask;
import proj.kinetics.Model.Mapping;
import proj.kinetics.Model.Task;
import proj.kinetics.R;
import proj.kinetics.TaskActivity;
import proj.kinetics.TaskSelection;
import proj.kinetics.Utils.ApiClient;
import proj.kinetics.Utils.ApiInterface;
import proj.kinetics.Utils.LinearLayoutMangerWithSmoothScroll;
import proj.kinetics.Utils.RecyclerTouchListener;
import proj.kinetics.Utils.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTaskFragment extends Fragment {
    String username, password;
    DBHelper dbHelper;
    ImageButton syncc;
    String task_name = "", project_id, project_name = "", priority_id, task_id = "", estimated_time, duration, quantity = "0", status, amount = "0", task_details, pdf_link = null, dependent_task_id = null, video_link = null;
    SessionManagement session;
    ArrayList<DTask> arraylist = new ArrayList<>();
    Cursor cursor;
    RecyclerView tasklist;
    ProjectsAdapter projadapter;
    String data, userId;
    List<Task> list = new ArrayList<>();
    GridLayoutManager gridLayoutManager;
    SwipeRefreshLayout swipeRefresh;
    private View view;
    private LinearLayoutManager linearLayout;
    private String due_date;

    public MyTaskFragment() {
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
        dbHelper = new DBHelper(getActivity());
        session = new SessionManagement(getActivity());
        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();

        // name
        username = user.get(SessionManagement.KEY_USERNAME);
        password = user.get(SessionManagement.KEY_PASSWORD);
        userId = user.get(SessionManagement.KEY_USERID);

        view = inflater.inflate(R.layout.fragment_my_task, container, false);

        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        // get user data from session


        tasklist = view.findViewById(R.id.tasklist);
        syncc = view.findViewById(R.id.syncc);


        linearLayout = new LinearLayoutMangerWithSmoothScroll(getActivity());
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        tasklist.setLayoutManager(linearLayout);

        syncc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTaskDetail(username,password);
            }
        });
        tasklist.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), tasklist, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Task task = list.get(position);


                String datas = dbHelper.getTaskMap(task.getTaskId());

                if (datas != null) {
                    String status = dbHelper.getTaskStatus(datas);
                    //  Toast.makeText(getActivity(), "a"+status, Toast.LENGTH_SHORT).show();
                    if (status.equalsIgnoreCase("7")) {
                        Intent intent = new Intent(getActivity(), TaskActivity.class);
                        intent.putExtra("taskid", task.getTaskId());
                        startActivity(intent);
                    } else {
                        new AlertDialog.Builder(getActivity()).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setCancelable(false).setMessage("Please Complete Task: " + dbHelper.getTaskMapName(task.getTaskId())).show();
                    }
                } else {
                    Intent intent = new Intent(getActivity(), TaskActivity.class);
                    intent.putExtra("taskid", task.getTaskId());
                    startActivity(intent);

                }


            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                boolean isConnected = ConnectivityReceiver.isConnected();
                if (isConnected) {
                    getTaskDetail(username, password);
                    if (list == null) {
                        getOfflineTask();
                    } else {
                        list.clear();
                        getOfflineTask();
                    }

                } else {
                    if (list == null) {
                        getOfflineTask();
                    } else {
                        list.clear();
                        getOfflineTask();
                    }

                }
            }
        });
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            //getTaskDetail(username, password);
            getOfflineTask();


        } else {
            getOfflineTask();
        }
        return view;
    }


    private void getOfflineTask() {
        swipeRefresh.setRefreshing(false);
        cursor = dbHelper.getTask(userId);

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    String task_id, due, project_name = null, task_name, estimated_time, required_time, status, total_qty, done_qtytask_details, priority, task_details;

                    task_id = cursor.getString(cursor.getColumnIndex("task_id"));
                    project_name = cursor.getString(cursor.getColumnIndex("project_name"));
                    task_name = cursor.getString(cursor.getColumnIndex("task_name"));



                    estimated_time = cursor.getString(cursor.getColumnIndex("estimated_time"));
                    Log.d("estimated_time", estimated_time);
                    required_time = cursor.getString(cursor.getColumnIndex("required_time"));
                    status = cursor.getString(cursor.getColumnIndex("status"));
                    total_qty = cursor.getString(cursor.getColumnIndex("total_qty"));
                    done_qtytask_details = cursor.getString(cursor.getColumnIndex("done_qty"));
                    task_details = cursor.getString(cursor.getColumnIndex("task_details"));
                    due = cursor.getString(cursor.getColumnIndex("due_date"));
                    priority = cursor.getString(cursor.getColumnIndex("priority_id"));

                    Task task = new Task(task_id, "", project_name, task_name, estimated_time, required_time, status, total_qty, done_qtytask_details, task_details, "");
task.setDue_date(due);

                    list.add(task);

                }

                while (cursor.moveToNext());
                projadapter = new ProjectsAdapter(list, getActivity());
                tasklist.setAdapter(projadapter);

            }
        }
    }


    private void getTaskDetail(String username, String password) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> responseBodyCall = apiInterface.getTaskList(username, password);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {

                        String data = response.body().string();
                        Log.d("MyFragment", data);
                        JSONObject jsonObject = new JSONObject(data);
                        String dataresponse = jsonObject.getString("message");
                        if (dataresponse.equalsIgnoreCase("success")) {
                            if (jsonObject.getString("task").equalsIgnoreCase("null")) {

                            }
                            //  Toast.makeText(MainActivity.this, ""+response.body().string(), Toast.LENGTH_SHORT).show();
                            swipeRefresh.setRefreshing(false);
                            //get task detail   add and update task
                            userId = jsonObject.getString("user_id");


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
                                    amount = jsonObject1.getString("amount");
                                    pdf_link = jsonObject1.getString("pdf_link");
                                    video_link = jsonObject1.getString("video_link");
                                    due_date = jsonObject1.getString("due_date");

                                    Log.d("new data", pdf_link);
                                    if (dbHelper.istaskExisting(task_id)) {
                                        Log.d("update data", task_id + " " + estimated_time);

                                        dbHelper.updateTask(due_date,task_id, task_name, project_name, priority_id, estimated_time, duration, status, quantity, amount, task_details, pdf_link, "", video_link, userId);
                                    } else {
                                        Log.d("add data", task_id);
                                        dbHelper.addTask(due_date,task_id, task_name, project_name, priority_id, estimated_time, duration, status, quantity, amount, task_details, pdf_link, "", video_link, userId);
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
                                            if (dbHelper.isQCExisting(qcid)) {
                                                dbHelper.updateQC(qcid, qcstatus, qcdescrip, qcvideo, qcimg, userId, task_id);
                                            } else {
                                                dbHelper.addQC(qcid, qcstatus, qcdescrip, qcvideo, qcimg, userId, task_id);
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
                                if (list != null) {
                                    list.clear();
                                    getOfflineTask();
                                } else {
                                    getOfflineTask();
                                }
                                syncc.setVisibility(View.GONE);

                            }else {
                                syncc.setVisibility(View.VISIBLE);
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
                Log.d("response", "" + t.getMessage());
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        //  Toast.makeText(getActivity(), "refreshed", Toast.LENGTH_SHORT).show();

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {

            //getTaskDetail(username, password);
            if (list != null) {
                list.clear();
                getOfflineTask();
            } else {


                getOfflineTask();
            }


        } else {
            if (list == null) {
                getOfflineTask();
            } else {
                list.clear();
                getOfflineTask();
            }

        }

        if (list.isEmpty()){
            syncc.setVisibility(View.VISIBLE);

        }
        else {
            syncc.setVisibility(View.GONE);
        }
    }

}
