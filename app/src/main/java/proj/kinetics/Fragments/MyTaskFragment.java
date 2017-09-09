package proj.kinetics.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;
import proj.kinetics.Adapters.ProjectsAdapter;
import proj.kinetics.BroadcastReceivers.ConnectivityReceiver;
import proj.kinetics.Database.DBHelper;
import proj.kinetics.Database.MyDbHelper;
import proj.kinetics.MainActivity;
import proj.kinetics.Model.DTask;
import proj.kinetics.Model.Dependenttask;
import proj.kinetics.Model.Example;
import proj.kinetics.Model.ProjectItem;
import proj.kinetics.Model.Task;
import proj.kinetics.Model.TaskDetails;
import proj.kinetics.TaskActivity;
import proj.kinetics.R;
import proj.kinetics.UserProfileActivity;
import proj.kinetics.Utils.ApiClient;
import proj.kinetics.Utils.ApiInterface;
import proj.kinetics.Utils.LinearLayoutMangerWithSmoothScroll;
import proj.kinetics.Utils.RecyclerTouchListener;
import proj.kinetics.Utils.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTaskFragment extends Fragment {
    private View view;
    String username,password;
    MyDbHelper myDbHelper;
    DBHelper dbHelper;

SessionManagement session;
    public MyTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    ArrayList<DTask> arraylist=new ArrayList<>();
    Cursor cursor;
    RecyclerView tasklist;
    ProjectsAdapter projadapter;
    String data,userId;
    ProjectItem p1,p2,p3,p4,p5,p6;
    ArrayList<ProjectItem> al=new ArrayList();
    private LinearLayoutManager linearLayout;
    List<Task> list=new ArrayList<>();
    GridLayoutManager gridLayoutManager;
SwipeRefreshLayout swipeRefresh;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dbHelper=new DBHelper(getActivity());




        view = inflater.inflate(R.layout.fragment_my_task, container, false);
        session = new SessionManagement(getActivity());
        session.checkLogin();
        myDbHelper=new MyDbHelper(getActivity());
        swipeRefresh=view.findViewById(R.id.swipeRefresh);
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
         username = user.get(SessionManagement.KEY_USERNAME);
         password = user.get(SessionManagement.KEY_PASSWORD);
        userId = user.get(SessionManagement.KEY_USERID);
        cursor=dbHelper.getTask(userId);



        //Toast.makeText(getActivity(), "LoggedIN"+username, Toast.LENGTH_SHORT).show();
        // password

        tasklist=view.findViewById(R.id.tasklist);


       linearLayout = new LinearLayoutMangerWithSmoothScroll(getActivity());
       gridLayoutManager = new GridLayoutManager(getActivity(),2);
        tasklist.setLayoutManager(linearLayout);
        p1=new ProjectItem("Task A","","","1");
        p2=new ProjectItem("Task B","","","2");
        p3=new ProjectItem("Task C","","","6");
        p4=new ProjectItem("Task D","","","5");
        p5=new ProjectItem("Task E","","","4");
        p6=new ProjectItem("Task F","","","3");
        al.add(p1);
        al.add(p2);
        al.add(p3);
        al.add(p4);
        al.add(p5);
        al.add(p6);
        /*projadapter=new ProjectsAdapter(al,getActivity());
        tasklist.setAdapter(projadapter);*/
        tasklist.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), tasklist, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Task task=list.get(position);

                String data =dbHelper.getTaskMap(task.getTaskId());

if (data!=null){
    new AlertDialog.Builder(getActivity()).setPositiveButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

        }
    }).setCancelable(false).setMessage("Please Complete Task: "+dbHelper.getTaskMapName(task.getTaskId())).show();
}
else {
    Intent intent=new Intent(getActivity(),TaskActivity.class);
    intent.putExtra("taskid",task.getTaskId());
    startActivity(intent);
    //getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
}



               /* DTask dtask=arraylist.get(position);
                Toast.makeText(getActivity(), task.getTaskId()+""+dtask.getDtaskid(), Toast.LENGTH_SHORT).show();
                //getTaskDetails(task.getTaskId());

                if (task.getTaskId().equalsIgnoreCase(dtask.getDtaskid())){
                    new AlertDialog.Builder(getActivity()).setMessage("Please Complete Task: " +dtask.getTaskname()).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();


                }
                else {
                Intent intent=new Intent(getActivity(),TaskActivity.class);
                intent.putExtra("taskid",task.getTaskId());
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);


                }*/
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

                }
                else {
                    if (list==null){
                        getOfflineTask();
                    }
                    else {
                        list.clear();
                        getOfflineTask();
                    }

                }
            }
        });
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
          //  getTaskDetail(username, password);
            getOfflineTask();


        }
        else {
            getOfflineTask();
        }
        return view;
    }



    private void getOfflineTask() {
        swipeRefresh.setRefreshing(false);
        if (cursor.getCount()>0)
        {
            if (cursor.moveToFirst())
            {
                do {
                    String task_id,project_id,project_name = null,task_name,estimated_time,required_time,status,total_qty,done_qtytask_details,priority,task_details;

                    task_id=cursor.getString(cursor.getColumnIndex("task_id"));
                    project_id=cursor.getString(cursor.getColumnIndex("projects_id"));
                    task_name=cursor.getString(cursor.getColumnIndex("task_name"));



                    project_name = dbHelper.getProjectName(project_id);

                    estimated_time=cursor.getString(cursor.getColumnIndex("estimated_time"));
                    required_time=cursor.getString(cursor.getColumnIndex("required_time"));
                    status=cursor.getString(cursor.getColumnIndex("status"));
                    total_qty=cursor.getString(cursor.getColumnIndex("total_qty"));
                    done_qtytask_details=cursor.getString(cursor.getColumnIndex("done_qty"));
                    task_details=cursor.getString(cursor.getColumnIndex("task_details"));
                    priority=cursor.getString(cursor.getColumnIndex("priority_id"));

                     Task task=new Task(task_id,project_id,project_name,task_name,estimated_time,required_time,status,total_qty,done_qtytask_details,task_details,priority);
                    getTaskDetails(task_id);



                    list.add(task);

                }

                while (cursor.moveToNext());
                projadapter=new ProjectsAdapter(list,getActivity());
                tasklist.setAdapter(projadapter);

            }
        }
    }


    private void gettaskName(final String id){
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        Call<TaskDetails> taskDetailsCall=apiInterface.getTaskDetails(id);
        taskDetailsCall.enqueue(new Callback<TaskDetails>() {
            @Override
            public void onResponse(Call<TaskDetails> call, retrofit2.Response<TaskDetails> response) {



                List<Dependenttask> dependent= response.body().getDependenttask();

                if (dependent==null){

                   /* Intent intent=new Intent(getActivity(),TaskActivity.class);
                    intent.putExtra("taskid",id);
                    startActivity(intent);
                    getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);*/
                    Log.d("rrrrr",id+ " ");

                }
                else {
                    Dependenttask dep = dependent.get(0);

                    Toast.makeText(getActivity(), ""+dep.getTaskname(), Toast.LENGTH_SHORT).show();
                        /*Intent intent=new Intent(getActivity(),TaskActivity.class);
                        intent.putExtra("taskid",id);
                        startActivity(intent);
                        getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);*/

                }


            }

            @Override
            public void onFailure(Call<TaskDetails> call, Throwable t) {

            }
        });

    }





    private void getTaskDetails(final String id){
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        Call<TaskDetails> taskDetailsCall=apiInterface.getTaskDetails(id);
        taskDetailsCall.enqueue(new Callback<TaskDetails>() {
            @Override
            public void onResponse(Call<TaskDetails> call, retrofit2.Response<TaskDetails> response) {



                List<Dependenttask> dependent= response.body().getDependenttask();

                if (dependent==null){

                   /* Intent intent=new Intent(getActivity(),TaskActivity.class);
                    intent.putExtra("taskid",id);
                    startActivity(intent);
                    getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);*/
                    Log.d("rrrrr",id+ " ");

                }
                else {
                    Dependenttask dep = dependent.get(0);


                    Log.d("rrrrr",id+ " "+dep.getId());
                    dbHelper.updateDTask(id,dep.getId());

                    if ((dbHelper.isTaskMapId(id)==true)){

                        dbHelper.updateTaskMapping(id,dep.getId(),dep.getTaskname());
                    }
                    else {
                        dbHelper.insertTaskMapping(id,dep.getId(),dep.getTaskname());

                    }

                        /*Intent intent=new Intent(getActivity(),TaskActivity.class);
                        intent.putExtra("taskid",id);
                        startActivity(intent);
                        getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);*/

                }


            }

            @Override
            public void onFailure(Call<TaskDetails> call, Throwable t) {

            }
        });

    }

    private void getTaskDetail(String username, String password) {
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
                        String task_name, user_id, project_id, project_name, priority_id, task_id, estimated_time, required_time, status, total_qty, done_qty, task_details, pdf_link = "", dependent_task_id = "", video_link = "";

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
                                dbHelper.updateProject(project_id,project_name,user_id);

                            }
                            else {
                                dbHelper.addProject(project_id, project_name, user_id);

                            }
                            if (dbHelper.istaskExisting(task_id)){
                                dbHelper.updateTask(task_id, task_name,project_id,priority_id, estimated_time, required_time, status, total_qty, done_qty, task_details, pdf_link, dependent_task_id, video_link, user_id);

                            }
                            else {
                                dbHelper.addTask(task_id, task_name,project_id,priority_id, estimated_time, required_time, status, total_qty, done_qty, task_details, pdf_link, dependent_task_id, video_link, user_id);

                            }swipeRefresh.setRefreshing(false);
                            if (list!=null)
                            {
                                list.clear();
                                getOfflineTask();
                            }
                            else {
                                getOfflineTask();
                            }



                        }
                    }

                  //  Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {

        }
    });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.three_menu, menu);


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getActivity(), "refreshed", Toast.LENGTH_SHORT).show();

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            getTaskDetail(username, password);
            if (list!=null){
                list.clear();
                getOfflineTask();
            }
            else {
                getOfflineTask();
            }


        }
        else {
            if (list==null){
                getOfflineTask();
            }
            else {
                list.clear();
                getOfflineTask();
            }

        }
       /* if (projadapter.getItemCount()>0) {
            al.clear();

            al.add(p1);
            al.add(p2);
            al.add(p3);
            al.add(p4);
            al.add(p5);
            al.add(p6);
            projadapter.notifyDataSetChanged();*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
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
       /* if (id==R.id.action_grid){

            if (tasklist.getLayoutManager()==linearLayout){
                item.setIcon(R.mipmap.ic_list);

                tasklist.setLayoutManager(gridLayoutManager);

            }
            else {
                item.setIcon(R.mipmap.ic_grid);

                tasklist.setLayoutManager(linearLayout);
            }
        }*/


        return super.onOptionsItemSelected(item);
    }

   /* @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (tasklist.getLayoutManager()==linearLayout){
            menu.findItem(R.id.action_grid).setIcon(R.mipmap.ic_grid);


        }
        else
            menu.findItem(R.id.action_grid).setIcon(R.mipmap.ic_list);

    }*/
}
