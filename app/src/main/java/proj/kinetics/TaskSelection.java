package proj.kinetics;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import proj.kinetics.Adapters.ProjectsAdapter;
import proj.kinetics.Database.DBHelper;
import proj.kinetics.Model.Mapping;
import proj.kinetics.Model.Task;
import proj.kinetics.Model.TaskName;
import proj.kinetics.Utils.SessionManagement;

public class TaskSelection extends Activity {


    DBHelper dbHelper;
    SessionManagement session;
    boolean pass = false;

    ArrayList<TaskName> al = new ArrayList<>();
    String taskid = "", selection = "";
    ListView listView;
    Button canceldialog, adddialog;
    ArrayList<CharSequence> list = new ArrayList<>();
    private String user_id, datas, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_selection);
        taskid = getIntent().getStringExtra("taskid");
        dbHelper = new DBHelper(TaskSelection.this);
        session = new SessionManagement(TaskSelection.this);
        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();

        // name

        user_id = user.get(SessionManagement.KEY_USERID);


        // Getting object reference to listview of main.xml
        listView = (ListView) findViewById(R.id.listview);
        canceldialog = (Button) findViewById(R.id.canceldialog);
        adddialog = (Button) findViewById(R.id.adddialog);


        canceldialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskSelection.this, TaskActivity.class);
                intent.putExtra("taskid", taskid);
                startActivity(intent);
                overridePendingTransition(0, 0);


            }
        });
        adddialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pass == true) {
                    Toast.makeText(TaskSelection.this, "OK", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TaskSelection.this, TaskActivity.class);
                    intent.putExtra("taskselectedid", selection);
                    intent.putExtra("taskid", taskid);
                    startActivity(intent);
                    overridePendingTransition(0, 0);

                } else {
                    Toast.makeText(TaskSelection.this, "NO", Toast.LENGTH_SHORT).show();
                }


            }
        });
        getOfflineTask();
        // Instantiating array adapter to populate the listView
        // The layout android.R.layout.simple_list_item_single_choice creates radio button for each listview item


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TaskName t = al.get(i);
                selection = "" + t.getId();
                datas = dbHelper.getTaskMap(selection);
                if (datas != null) {
                    status = dbHelper.getTaskStatus(datas);
                    //  Toast.makeText(getActivity(), "a"+status, Toast.LENGTH_SHORT).show();
                    if (status.equalsIgnoreCase("7")) {
                        pass = true;
                        Toast.makeText(TaskSelection.this, "" + status, Toast.LENGTH_SHORT).show();
                    } else {
                        pass = false;
                        Toast.makeText(TaskSelection.this, "" + "Please Complete Task: " + dbHelper.getTaskMapName(selection), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    pass = true;

                }

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    private void getOfflineTask() {
        // Toast.makeText(this, ""+taskid, Toast.LENGTH_SHORT).show();


        Cursor cursor = dbHelper.getAllTask(taskid);

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    String task_id, project_id, project_name = null, task_name, estimated_time, required_time, status, total_qty, done_qtytask_details, priority, task_details;

                    task_id = cursor.getString(cursor.getColumnIndex("task_id"));


                    task_name = cursor.getString(cursor.getColumnIndex("task_name"));


                    if (dbHelper.isTaskCompleted(task_id)) {
                        //    Toast.makeText(this, "com"+task_id, Toast.LENGTH_SHORT).show();
                    } else {
                        // Toast.makeText(this, "notcom"+task_id, Toast.LENGTH_SHORT).show();
                        TaskName taskname = new TaskName(task_name, task_id);
                        al.add(taskname);

                        list.add(task_name);
                    }


                }

                while (cursor.moveToNext());
                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, list);

                listView.setAdapter(adapter);

            }
        }
    }
}
