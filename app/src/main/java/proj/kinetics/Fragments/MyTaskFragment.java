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
import proj.kinetics.Database.MyDbHelper;
import proj.kinetics.MainActivity;
import proj.kinetics.Model.Example;
import proj.kinetics.Model.ProjectItem;
import proj.kinetics.Model.Task;
import proj.kinetics.TaskActivity;
import proj.kinetics.R;
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
SessionManagement session;
    public MyTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
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

                Intent intent=new Intent(getActivity(),TaskActivity.class);
                intent.putExtra("taskid",task.getTaskId());
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                Toast.makeText(getActivity(), ""+task.getTaskId(), Toast.LENGTH_SHORT).show();
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
                        getOfflineData();
                    }
                    else {
                        list.clear();
                        getOfflineData();
                    }

                }
            }
        });
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            getTaskDetail(username, password);

        }
        else {
            getOfflineData();
        }
        return view;
    }

    private void getOfflineData() {

        Cursor c=myDbHelper.getData(userId);

        if (c.getCount()>0){
            if (c.moveToFirst()){
                do {
                     data=c.getString(1);
                }while (c.moveToNext());

                try {
                    JSONObject jsonobject=new JSONObject(data);
                    String message=jsonobject.getString("message");
                    Log.d("offline",message);
                    swipeRefresh.setRefreshing(false);

                    JSONArray jsonArray=jsonobject.getJSONArray("task");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject childobj=jsonArray.getJSONObject(i);
                            String task_id,project_id,project_name,task_name,estimated_time,required_time,status,total_qty,done_qtytask_details,priority,task_details;
                            task_id=childobj.getString("task_id");
                            Log.d("checkoffline",""+jsonArray.length());

                            task_details=childobj.getString("task_details");
                            project_id=childobj.getString("project_id");
                            project_name=childobj.getString("project_name");
                            task_name=childobj.getString("task_name");
                            estimated_time=childobj.getString("estimated_time");
                            required_time=childobj.getString("required_time");
                            status=childobj.getString("status");
                            done_qtytask_details=childobj.getString("done_qty");
                            total_qty=childobj.getString("total_qty");
                            priority=childobj.getString("priority");
                                Task task=new Task(task_id,project_id,project_name,task_name,estimated_time,required_time,status,total_qty,done_qtytask_details,task_details,priority);
                            list.add(task);

                        }projadapter=new ProjectsAdapter(list,getActivity());
                        tasklist.setAdapter(projadapter);




                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }



    }

    private void getTaskDetail(String username, String password) {
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        Call<Example> responseBodyCall=apiInterface.getTaskLists(username,password);
        responseBodyCall.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, retrofit2.Response<Example> response) {
                String data=response.body().getMessage();
               
                if (data.equalsIgnoreCase("success"))
                {
                    swipeRefresh.setRefreshing(false);
                    Log.d("hhhh",""+response.body().getTask());
                    list=response.body().getTask();
                    projadapter=new ProjectsAdapter(list,getActivity());
                    tasklist.setAdapter(projadapter);
                }
                else {
                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    
                }
               
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

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

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            getTaskDetail(username, password);

        }
        else {
            if (list==null){
                getOfflineData();
            }
            else {
                list.clear();
                getOfflineData();
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
        if (id==R.id.action_grid){

            if (tasklist.getLayoutManager()==linearLayout){
                item.setIcon(R.mipmap.ic_list);

                tasklist.setLayoutManager(gridLayoutManager);

            }
            else {
                item.setIcon(R.mipmap.ic_grid);

                tasklist.setLayoutManager(linearLayout);
            }
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (tasklist.getLayoutManager()==linearLayout){
            menu.findItem(R.id.action_grid).setIcon(R.mipmap.ic_grid);


        }
        else
            menu.findItem(R.id.action_grid).setIcon(R.mipmap.ic_list);

    }
}
