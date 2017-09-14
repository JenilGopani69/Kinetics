package proj.kinetics;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import proj.kinetics.Adapters.QCAdapter;
import proj.kinetics.Adapters.QCAdapter_;
import proj.kinetics.Adapters.UnitsAdapter;
import proj.kinetics.Adapters.UnitsAdapter2;
import proj.kinetics.BroadcastReceivers.ConnectivityReceiver;
import proj.kinetics.Database.DBHelper;
import proj.kinetics.Model.Dependenttask;
import proj.kinetics.Model.Mapping;
import proj.kinetics.Model.Qualitycheck_;
import proj.kinetics.Model.TaskDetails;
import proj.kinetics.Session.TimerSession;
import proj.kinetics.TimerWidget.TimeService;
import proj.kinetics.Utils.ApiClient;
import proj.kinetics.Utils.ApiInterface;
import proj.kinetics.Utils.MySpannable;
import proj.kinetics.Utils.PDFTools;
import proj.kinetics.Utils.RecyclerTouchListener;
import proj.kinetics.Utils.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.enabled;

public class TaskActivity extends AppCompatActivity implements PropertyChangeListener, View.OnClickListener {
    public static Button finishtask;
    QCAdapter_ qcAd;
    String taskname,taskselectedid;
    DBHelper dbHelper;
String totalunit2;
int selection=1;
    String taskid,attachmenturl2,videolink2,userId;
    ArrayList<String> al = new ArrayList<>();
    ImageButton videoattach,videoattach2, attachment,attachment2, undobtn,undobtn2;
    Toolbar toolbar;
    String string1="",string2="";
     Animation myAnim;
    String d_taskdescription="",d_taskname="",d_taskquantity="",d_task_id;
    TextView requiredunit2;
    SharedPreferences sharedPreferences;
    String attachmenturl,videolink;


    SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat("hh:mm:ss aa");
    View openDialog;
    int counts=1;
    FrameLayout pausehide;
    String[] items;
    int getpauseselection=1;
    TextView task2;
    CoordinatorLayout coordinate;
    LinearLayout unitsdata2,unitsdata, nextqcbtn,qcrecylerdependent;
    QCAdapter myAdapter;
    RecyclerView units,units2, recyclerView;
    EditText unitsproduced,unitsproduced2;
    int count = 0;
    LinearLayout linqc,taskd;
    LinearLayout  lintask;
    ArrayList arrayList = new ArrayList();
    UnitsAdapter unitsAdapter;
    UnitsAdapter2 unitsAdapter2;
    int recent = 0;
    List<CharSequence> list = new ArrayList<CharSequence>();
    private Button btnStart, btnReset, btnSubmit, btnComplete,btnSubmit2;
    private Handler h;
    private TextView tvtask,tvTime, totalunits, requiredunits, taskdescrip, unitsleft, startedtime,taskdescrip2,unitsleft2,recordedtym,breaktym;
    private final Runnable updateTextRunnable = new Runnable() {
        @Override
        public void run() {
            updateTimeText();
        }
    };
    private Timer t;
    private SessionManagement session;
    TimerSession timerSession;

SharedPreferences.Editor editor;
    private String data;
    private String pdf_link2;
    private String video_link2;

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new MySpannable(false) {
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "View Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, "View More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        taskid=getIntent().getStringExtra("taskid");
        sharedPreferences=getSharedPreferences("tasktimer",MODE_PRIVATE);
dbHelper=new DBHelper(TaskActivity.this);
        editor=sharedPreferences.edit();
        pausehide= (FrameLayout) findViewById(R.id.pausehide);

        session = new SessionManagement(getApplicationContext());
        timerSession=new TimerSession(getApplicationContext());
        session.checkLogin();

        // get user data from session
        final HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManagement.KEY_USERNAME);
userId=user.get(SessionManagement.KEY_USERID);
        // email
        String email = user.get(SessionManagement.KEY_PASSWORD);


    //    Toast.makeText(getApplicationContext(), "LoggedIN" +name, Toast.LENGTH_SHORT).show();
        units = (RecyclerView) findViewById(R.id.units);
        units2 = (RecyclerView) findViewById(R.id.units2);
        finishtask = (Button) findViewById(R.id.finishtask);
        startedtime = (TextView) findViewById(R.id.startedtime);
        breaktym = (TextView) findViewById(R.id.breaktym);
        recordedtym = (TextView) findViewById(R.id.recordedtym);
        taskdescrip2 = (TextView) findViewById(R.id.taskdescrip2);
        requiredunit2 = (TextView) findViewById(R.id.requiredunit2);
        task2 = (TextView) findViewById(R.id.task2);
        //taskd = (LinearLayout) findViewById(R.id.taskd);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        coordinate = (CoordinatorLayout) findViewById(R.id.coordinate);
        myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        linqc = (LinearLayout) findViewById(R.id.lineqc);
        unitsdata = (LinearLayout) findViewById(R.id.unitsdata);
        unitsdata2 = (LinearLayout) findViewById(R.id.unitsdata2);
        lintask = (LinearLayout) findViewById(R.id.lintask);
        unitsproduced = (EditText) findViewById(R.id.unitsproduced);
        unitsproduced2 = (EditText) findViewById(R.id.unitsproduced2);
        taskdescrip = (TextView) findViewById(R.id.taskdescrip);
        requiredunits = (TextView) findViewById(R.id.requiredunits);
        requiredunit2 = (TextView) findViewById(R.id.requiredunit2);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvtask = (TextView) findViewById(R.id.task);
        unitsleft = (TextView) findViewById(R.id.unitsleft);
        unitsleft2 = (TextView) findViewById(R.id.unitsleft2);
        totalunits = (TextView) findViewById(R.id.totalunits);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnComplete = (Button) findViewById(R.id.btnComplete);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit2 = (Button) findViewById(R.id.btnSubmit2);
        undobtn = (ImageButton) findViewById(R.id.undobtn);
        undobtn2 = (ImageButton) findViewById(R.id.undobtn2);
        videoattach = (ImageButton) findViewById(R.id.action_video);
        videoattach2 = (ImageButton) findViewById(R.id.action_video2);
        attachment = (ImageButton) findViewById(R.id.action_attach);
        attachment2 = (ImageButton) findViewById(R.id.action_attach2);
        nextqcbtn = (LinearLayout) findViewById(R.id.nextqcbtn);
      //  recyclerView = (RecyclerView) findViewById(R.id.qcrecyler);
        btnReset = (Button) findViewById(R.id.btnReset);
        videoattach.setOnClickListener(this);
        attachment.setOnClickListener(this);
        attachment2.setOnClickListener(this);
        videoattach2.setOnClickListener(this);

taskselectedid=getIntent().getStringExtra("taskselectedid");

        openDialog = (View) findViewById(R.id.openDialog);
        if (taskselectedid != null && !taskselectedid.isEmpty()) {
            openDialog.setVisibility(View.GONE);
        }
        else {
            openDialog.setVisibility(View.VISIBLE);
        }

openDialog.setVisibility(View.GONE);

        openDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskActivity.this, TaskSelection.class);
                intent.putExtra("taskid", taskid);
                startActivity(intent);

               /* // Intialize  readable sequence of char values
                final CharSequence[] dialogList = list.toArray(new CharSequence[list.size()]);
                final AlertDialog.Builder builderDialog = new AlertDialog.Builder(TaskActivity.this);
                builderDialog.setTitle("SELECT TASK");
                int count = dialogList.length;

                boolean[] is_checked = new boolean[count];

                // Creating multiple selection by using setMutliChoiceItem method
                builderDialog.setSingleChoiceItems(dialogList, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                               selection=which;

                            }
                        });

                builderDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


//


                                ListView list = ((AlertDialog) dialog).getListView();
                                // make selected item in the comma seprated string

                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < list.getCount(); i++) {
                                    boolean checked = list.isItemChecked(i);

                                    if (checked) {

                                        Toast.makeText(TaskActivity.this, ""+list.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
                                      *//*  if (stringBuilder.length() > 0) stringBuilder.append(",");
                                        stringBuilder.append(list.getItemAtPosition(i));*//*
                                        coordinate.setVisibility(View.VISIBLE);
                                        taskdescrips = (TextView) findViewById(R.id.taskdescrips);
                                        requiredunit = (TextView) findViewById(R.id.requiredunit);
                                        task = (TextView) findViewById(R.id.task2);
                                        getOfflineSimilar(""+list.getItemAtPosition(i));

                                      *//*  requiredunit.setText(d_taskquantity);
                                        task.setText(d_taskname);
                                        taskdescrips.setText(d_taskdescription);
                                        makeTextViewResizable(taskdescrips, 3, "View More", true);
*//*

Log.d("ggg",d_taskquantity);
                                        //here d_stask is added
                                       timerSession.addStask(d_task_id,d_taskquantity);

                                        openDialog.setVisibility(View.GONE);
                                        //btnStart.setEnabled(true);

                                        getDependentask();
                                    }
                                }

                        *//*Check string builder is empty or not. If string builder is not empty.
                          It will display on the screen.
                         *//*
                                *//*if (stringBuilder.toString().trim().equals("")) {

                                    ((TextView) findViewById(R.id.text)).setText("SELECT SIMILAR TASK");
                                    stringBuilder.setLength(0);

                                } else {

                                    ((TextView) findViewById(R.id.text)).setText(stringBuilder);
                                }*//*
                            }
                        });

                builderDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog alert = builderDialog.create();
                alert.show();*/

            }
        });


        if (tvTime.getText().toString().equalsIgnoreCase("00:00")) {
            unitsdata.setVisibility(View.GONE);
            unitsdata2.setVisibility(View.GONE);
        }

btnSubmit2.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (unitsproduced2.getText().toString().equals("0")) {

            Toast.makeText(TaskActivity.this, "Cannot Submit No Units Produced", Toast.LENGTH_SHORT).show();
        } else {

            new AlertDialog.Builder(TaskActivity.this).setCancelable(false).setMessage("I have produced " + unitsproduced2.getText().toString() + " units").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (requiredunit2.getText().toString().trim().equalsIgnoreCase(unitsproduced2.getText().toString().trim())) {
                        totalunit2=unitsproduced2.getText().toString();

                        //  totalunits.setText(unitsproduced.getText().toString());
                    }
                    if (Integer.parseInt(unitsproduced2.getText().toString().trim()) >= Integer.parseInt(requiredunit2.getText().toString().trim())) {


                        new AlertDialog.Builder(TaskActivity.this).setCancelable(false).setMessage("Producton is complete, you can now proceed to QC").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                totalunit2=unitsproduced2.getText().toString();
                               // totalunits.setText(requiredunit2.getText().toString());

                                Toast.makeText(TaskActivity.this, ""+userId+" "+taskselectedid+" "+tvTime.getText().toString()+" "+totalunit2, Toast.LENGTH_SHORT).show();
                                updateTaskDetails(userId,taskselectedid,tvTime.getText().toString(),totalunit2);

                                timerSession.addStaskDetails(totalunit2,tvTime.getText().toString(),unitsleft2.getText().toString());

                                dialogInterface.dismiss();
                            }
                        }).show();
                        // Toast.makeText(TaskActivity.this, "Production Completed", Toast.LENGTH_SHORT).show();
                        totalunit2=requiredunit2.getText().toString();
                       // totalunits.setText(requiredunits.getText().toString());
                    } else {

                        totalunit2=unitsproduced2.getText().toString();
                        timerSession.addStaskDetails(totalunit2,tvTime.getText().toString(),unitsleft2.getText().toString());

                        Toast.makeText(TaskActivity.this, ""+userId+" "+taskselectedid+" "+tvTime.getText().toString()+" "+totalunit2, Toast.LENGTH_SHORT).show();
                        updateTaskDetails(userId,taskselectedid,tvTime.getText().toString(),totalunit2);
                        Toast.makeText(TaskActivity.this, "Please complete the production", Toast.LENGTH_SHORT).show();
                    }

                }
            }).show();
        }

    }
});
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (unitsproduced.getText().toString().equals("0")) {

                    Toast.makeText(TaskActivity.this, "Cannot Submit No Units Produced", Toast.LENGTH_SHORT).show();
                } else {

                    new AlertDialog.Builder(TaskActivity.this).setCancelable(false).setMessage("I have produced " + unitsproduced.getText().toString() + " units").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (requiredunits.getText().toString().trim().equalsIgnoreCase(unitsproduced.getText().toString().trim())) {
                                totalunits.setText(unitsproduced.getText().toString());
                                updateTaskDetails(userId,taskid,tvTime.getText().toString(),unitsproduced.getText().toString());
                            }
                            if (Integer.parseInt(unitsproduced.getText().toString().trim()) >= Integer.parseInt(requiredunits.getText().toString().trim())) {


                                new AlertDialog.Builder(TaskActivity.this).setCancelable(false).setMessage("Producton is complete, you can now proceed to QC").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {


                                            totalunits.setText(requiredunits.getText().toString());


                                       // updateTaskDetails(userId,taskid,recordedtym.getText().toString(),totalunits.getText().toString(),getpauseselection,breaktym.getText().toString());


                                        dialogInterface.dismiss();
                                    }
                                }).show();
                              // Toast.makeText(TaskActivity.this, "Production Completed", Toast.LENGTH_SHORT).show();

                                totalunits.setText(requiredunits.getText().toString());
                            } else {
                                totalunits.setText(unitsproduced.getText().toString());

                                Toast.makeText(TaskActivity.this, "Please complete the production", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).show();
                }
            }
        });
        al.add("Checklist 1");
        al.add("Checklist 2");
        al.add("Checklist 3");
        if (unitsproduced.getText().toString().equalsIgnoreCase("0")) {
            undobtn.setEnabled(false);
            undobtn.setImageDrawable(getResources().getDrawable(R.mipmap.ic_undodisable));

        } if (unitsproduced2.getText().toString().equalsIgnoreCase("0")) {
            undobtn2.setEnabled(false);
            undobtn2.setImageDrawable(getResources().getDrawable(R.mipmap.ic_undodisable));

        }

        undobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(TaskActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        undobtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /// Toast.makeText(TaskActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
      /*  RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL
                , false);
        recyclerView.setLayoutManager(layoutManager);*/
        /*finishtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog openDialog = new Dialog(TaskActivity.this);
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
                        TimeService.TimeContainer.getInstance().stopAndReset();
                        Intent intent = new Intent(TaskActivity.this, UserProfileActivity.class);
                        startActivity(intent);


                        openDialog.dismiss();
                    }
                });
                openDialog.show();
            }
        });
        finishtask.setBackgroundColor(Color.GRAY);
        finishtask.setEnabled(false);
        finishtask.setClickable(false);*/

        nextqcbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    if((requiredunits.getText().toString().equalsIgnoreCase(totalunits.getText().toString())) ){
                        lintask.setVisibility(View.GONE);
                        Intent intent=new Intent(TaskActivity.this,QCActivity.class);
                        intent.putExtra("taskid",taskid);
                        startActivity(intent);

                        //linqc.setVisibility(View.VISIBLE);
                        nextqcbtn.setVisibility(View.GONE);
                    }
                    else {


                        Toast.makeText(TaskActivity.this, "Production Pending", Toast.LENGTH_SHORT).show();



                }

            }
        });
        h = new Handler();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        arrayList.add("1");
        arrayList.add("10");
        arrayList.add("20");
        arrayList.add("30");
        arrayList.add("80");
        arrayList.add("60");
        arrayList.add("100");
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        units.setLayoutManager(layoutManager3);
        units2.setLayoutManager(layoutManager2);

        //Toast.makeText(this, "sh"+sharedPreferences.getString("task",""), Toast.LENGTH_SHORT).show();
        unitsAdapter = new UnitsAdapter(arrayList, this);
        unitsAdapter2 = new UnitsAdapter2(arrayList, this);
        units.setAdapter(unitsAdapter);
        units2.setAdapter(unitsAdapter2);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (requiredunits.getText().toString().trim().equalsIgnoreCase(totalunits.getText().toString().trim())) {

                    Toast.makeText(TaskActivity.this, "Production Completed Proceed to QC", Toast.LENGTH_SHORT).show();
                } else {


                    unitsdata.setVisibility(View.VISIBLE);
                    unitsdata2.setVisibility(View.VISIBLE);
                    editor.putString("task", taskid);


                    editor.commit();
                    boolean isconnect = ConnectivityReceiver.isConnected();
                    if (isconnect) {
                        updateTaskStatus(userId, taskid, "4");


                        if (taskselectedid != null && !taskselectedid.isEmpty()) {
                            updateTaskStatus(userId, taskselectedid, "4");

                        }

                    }


                    //Toast.makeText(TaskActivity.this, "sh"+sharedPreferences.getString("task",""), Toast.LENGTH_SHORT).show();

                    TimeService.TimeContainer tc = TimeService.TimeContainer.getInstance();
                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                    if (tc.getCurrentState() == TimeService.TimeContainer.STATE_RUNNING) {
                        TimeService.TimeContainer.getInstance().pause();
                        startedtime.setText("" + currentDateTimeString);
                        dbHelper.updateTaskStatus(taskid, "4", unitsproduced.getText().toString().trim(), tvTime.getText().toString());//working
                        if (taskselectedid != null && !taskselectedid.isEmpty()) {
                            dbHelper.updateTaskStatus(taskselectedid, "4", unitsproduced2.getText().toString().trim(), tvTime.getText().toString());//working

                        }
                        boolean isconnected = ConnectivityReceiver.isConnected();
                        if (isconnected) {
                            updateTaskStatus(userId, taskid, "4");

                            if (taskselectedid != null && !taskselectedid.isEmpty()) {
                                updateTaskStatus(userId, taskselectedid, "4");

                            }
                        }

                        btnStart.setText("CONTINUE");
                        btnComplete.setVisibility(View.GONE);
                        // btnReset.setVisibility(View.VISIBLE);
                        showDialog();
                        Log.d("check", "start");
                    } else {
                        btnReset.setVisibility(View.GONE);
                        btnComplete.setVisibility(View.VISIBLE);
                        dbHelper.updateTaskStatus(taskid, "5", "" + unitsproduced.getText().toString().trim(), tvTime.getText().toString());//pause
                        if (taskselectedid != null && !taskselectedid.isEmpty()) {
                            dbHelper.updateTaskStatus(taskselectedid, "5", "" + unitsproduced2.getText().toString().trim(), tvTime.getText().toString());//pause


                        }
                        boolean isconnected = ConnectivityReceiver.isConnected();
                        if (isconnected) {
                            updateTaskStatus(userId, taskid, "5");
                            if (taskselectedid != null && !taskselectedid.isEmpty()) {
                                updateTaskStatus(userId, taskselectedid, "5");

                            }

                        }
                        if (btnStart.getText() == "CONTINUE") {
                            String currentDateTimeString2 = DateFormat.getTimeInstance().format(new Date());

                            // Toast.makeText(TaskActivity.this, "jjj" + currentDateTimeString2, Toast.LENGTH_SHORT).show();
                            string2 = currentDateTimeString2;

                            if (string2.length() > 0 && string1.length() > 0) {

                                try {

                                    Date date1 = simpleDateFormat.parse(string1);
                                    Date date2 = simpleDateFormat.parse(string2);

                                    printDifference(date1, date2);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else {

                            }

                        }
                        TimeService.TimeContainer.getInstance().start();

                        startUpdateTimer();
                        Log.d("check", "stop");
                        btnStart.setText("PAUSE");
                        btnComplete.setVisibility(View.VISIBLE);
                    }
                }
            }

            private void printDifference(Date startDate, Date endDate) {
                //milliseconds
                long different = endDate.getTime() - startDate.getTime();

                System.out.println("startDate : " + startDate);
                System.out.println("endDate : " + endDate);
                System.out.println("different : " + different);

                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;
                long daysInMilli = hoursInMilli * 24;


                long elapsedDays = different / daysInMilli;
                different = different % daysInMilli;

                long elapsedHours = different / hoursInMilli;
                different = different % hoursInMilli;

                long elapsedMinutes = different / minutesInMilli;
                different = different % minutesInMilli;


                long elapsedSeconds = different / secondsInMilli;


                System.out.printf(
                        "%02d days, %02d hours, %02d minutes, %02d seconds%n",
                        elapsedDays,
                        elapsedHours, elapsedMinutes, elapsedSeconds);

                String curtime = String.format("%02d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);


                boolean isConnect = ConnectivityReceiver.isConnected();
                if (isConnect) {
                    //server api
                } else {

                    /*myDbHelper.insertPause(userId,taskid,String.valueOf(getpauseselection),curtime);*/
                }


                breaktym.setText(curtime);
                unitsdata.setVisibility(View.VISIBLE);
                dbHelper.addPauseReason("" + getpauseselection, breaktym.getText().toString(), userId, taskid);

                updatePause(userId, taskid, "" + getpauseselection);


            }


        });
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordedtym.setText(tvTime.getText().toString());
                totalunits.setText(unitsproduced.getText().toString());
                dbHelper.updateTaskStatus(taskid,"6",totalunits.getText().toString().trim(),recordedtym.getText().toString());//stopped
                if (taskselectedid != null && !taskselectedid.isEmpty()) {
                    dbHelper.updateTaskStatus(taskselectedid,"6",totalunit2,recordedtym.getText().toString());//stopped

                }
                boolean isconnect= ConnectivityReceiver.isConnected();
                if (isconnect){
                    updateTaskStatus(userId,taskid,"6");
                    if (taskselectedid != null && !taskselectedid.isEmpty()) {
                        updateTaskStatus(userId, taskselectedid, "6");

                    }

                }


                Cursor c=dbHelper.getTaskStatusUnits(taskid);
                if (c.getCount()>0)
                {
                    if (c.moveToFirst())
                    {
                        do {
                            Log.d("stopped units",c.getString(c.getColumnIndex("status")));
                            Log.d("stopped units",c.getString(c.getColumnIndex("done_qty")));
                            Log.d("stopped units",c.getString(c.getColumnIndex("recordedtime")));
                        }while (c.moveToNext());
                    }
                }


                //updateTaskStatus(userId,taskid,"6");
                Log.d("hhhhhh",recordedtym.getText().toString());
                Log.d("hhhhhh",totalunits.getText().toString());
                Log.d("hhhhhh",taskid);
                Log.d("hhhhhh",userId);
                unitsdata.setVisibility(View.GONE);
                dbHelper.updateTaskData(taskid,totalunits.getText().toString(),recordedtym.getText().toString());

                // updateTaskDetails(userId,taskid,recordedtym.getText().toString(),totalunits.getText().toString(),0,"");
               // Toast.makeText(TaskActivity.this, ""+userId+" "+taskid, Toast.LENGTH_SHORT).show();

editor.clear();
                editor.commit();

                timerSession.clearTimer();
              //  Toast.makeText(TaskActivity.this, "stopped", Toast.LENGTH_SHORT).show();
                TimeService.TimeContainer.getInstance().stopAndReset();
                tvTime.setText(recordedtym.getText().toString());

                btnStart.setText("Start");
                //btnReset.setVisibility(View.VISIBLE);
                btnComplete.setVisibility(View.GONE);




            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeService.TimeContainer.getInstance().reset();
                updateTimeText2();
                btnReset.setVisibility(View.GONE);
                btnStart.setText("START");
              timerSession.clearTimer();
                editor.clear();
                editor.commit();
                unitsproduced.setText("0");
                unitsdata.setVisibility(View.GONE);
                unitsdata2.setVisibility(View.GONE);
            }
        });

        undobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (unitsproduced.getText().toString().equals("0")) {

                    undobtn.setImageDrawable(getResources().getDrawable(R.mipmap.ic_undodisable));


                } else {
                    if ((Integer.parseInt(unitsproduced.getText().toString().trim()))<(Integer.parseInt(requiredunits.getText().toString().trim())))
                    {



                        int minus = Integer.parseInt((unitsproduced.getText().toString())) - recent;

                        unitsproduced.setText(String.valueOf(minus));

                        int leftunits = Integer.parseInt(requiredunits.getText().toString().trim()) - Integer.parseInt(unitsproduced.getText().toString().trim());

                        unitsleft.setText(leftunits+" "+"left");
                        timerSession.createTimerData(String.valueOf(minus),tvTime.getText().toString(),unitsleft.getText().toString());

                        undobtn.setEnabled(false);
                        undobtn.setImageDrawable(getResources().getDrawable(R.mipmap.ic_undodisable));
                    }
                    }

            }
        });
        undobtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (unitsproduced2.getText().toString().equals("0")) {

                    undobtn2.setImageDrawable(getResources().getDrawable(R.mipmap.ic_undodisable));


                } else {





                    if ((Integer.parseInt(unitsproduced2.getText().toString().trim()))<(Integer.parseInt(requiredunit2.getText().toString().trim())))
                    {



                        int minus = Integer.parseInt((unitsproduced2.getText().toString())) - recent;

                        unitsproduced2.setText(String.valueOf(minus));

                        int leftunits = Integer.parseInt(requiredunit2.getText().toString().trim()) - Integer.parseInt(unitsproduced2.getText().toString().trim());

                        unitsleft2.setText(leftunits+" "+"left");
                        Log.d("mylog",""+unitsproduced2.getText().toString()+" "+tvTime.getText().toString()+" "+unitsleft2.getText().toString());
                        timerSession.addStaskDetails(unitsproduced2.getText().toString(),tvTime.getText().toString(),unitsleft2.getText().toString());

                        undobtn2.setEnabled(false);
                        undobtn2.setImageDrawable(getResources().getDrawable(R.mipmap.ic_undodisable));
                    }
                  /*  int minus = Integer.parseInt((unitsproduced2.getText().toString())) - recent;

                    unitsproduced2.setText(String.valueOf(minus));
                    undobtn2.setEnabled(false);
                    undobtn2.setImageDrawable(getResources().getDrawable(R.mipmap.ic_undodisable));*/
                }
            }
        });

        units2.addOnItemTouchListener(new RecyclerTouchListener(this, units, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                if (Integer.parseInt(unitsproduced2.getText().toString().trim()) >= Integer.parseInt(requiredunit2.getText().toString().trim()))
                {

                    if (counts>=2){
                        new AlertDialog.Builder(TaskActivity.this).setCancelable(false).setMessage("Producton is complete, you can now proceed to QC").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (Integer.parseInt(unitsproduced2.getText().toString().trim()) >= Integer.parseInt(requiredunits.getText().toString().trim())) {

                                    Toast.makeText(TaskActivity.this, "Production Completed", Toast.LENGTH_SHORT).show();

                                    btnSubmit2.startAnimation(myAnim);
                                }
                                dialogInterface.dismiss();
                            }
                        }).show();
                    }
                    else {
                        unitsleft2.setText("Production Completed");
                        // Toast.makeText(TaskActivity.this, "Production is Completed Please click to Submit", Toast.LENGTH_SHORT).show();

                        TimeService.TimeContainer.getInstance().pause();
                        btnStart.setText("CONTINUE");
                        btnComplete.setVisibility(View.GONE);
                       // btnReset.setVisibility(View.VISIBLE);
                        new AlertDialog.Builder(TaskActivity.this).setCancelable(false).setMessage("Producton is complete, you can now proceed to QC").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (Integer.parseInt(unitsproduced2.getText().toString().trim()) >= Integer.parseInt(requiredunits.getText().toString().trim())) {

                                    Toast.makeText(TaskActivity.this, "Production Completed", Toast.LENGTH_SHORT).show();

                                    totalunits.setText(requiredunit2.getText().toString());
                                    /*undobtn2.setImageDrawable(getResources().getDrawable(R.mipmap.ic_undodisable));
                                    undobtn2.setEnabled(false);*/
                                }
                                dialogInterface.dismiss();
                            }
                        }).show();
                    }



                }
                else {
                    recent = Integer.parseInt(arrayList.get(position).toString());
                    count = (Integer.parseInt(unitsproduced2.getText().toString())) + (Integer.parseInt(arrayList.get(position).toString()));

                    if (count< (Integer.parseInt(requiredunit2.getText().toString().trim()))){
                        unitsproduced2.setText(String.valueOf(count));

                    }
                    else{
                        unitsproduced2.setText(requiredunit2.getText().toString());

                    }

                    undobtn2.setEnabled(true);
                    counts++;
                    int leftunits = Integer.parseInt(requiredunit2.getText().toString().trim()) - Integer.parseInt(unitsproduced2.getText().toString().trim());
                    unitsleft2.setText(String.valueOf(leftunits + " " + "left"));
                    if(unitsproduced2.getText().toString().equalsIgnoreCase("")) {

                    } else {
                        Log.d("mylog2",""+unitsproduced2.getText().toString()+" "+tvTime.getText().toString()+" "+unitsleft2.getText().toString());


                        timerSession.addStaskDetails(unitsproduced2.getText().toString(),tvTime.getText().toString(),unitsleft2.getText().toString());

                    }

                }

                undobtn2.setImageDrawable(getResources().getDrawable(R.mipmap.ic_undo));


            }


            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        units.addOnItemTouchListener(new RecyclerTouchListener(this, units, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {



                if (Integer.parseInt(unitsproduced.getText().toString().trim()) ==Integer.parseInt(requiredunits.getText().toString().trim()))

                {
                    new AlertDialog.Builder(TaskActivity.this).setCancelable(false).setMessage("Producton is complete, you can now proceed to QC").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (Integer.parseInt(unitsproduced.getText().toString().trim()) >= Integer.parseInt(requiredunits.getText().toString().trim())) {

                                Toast.makeText(TaskActivity.this, "Production Completed", Toast.LENGTH_SHORT).show();

                                btnSubmit.startAnimation(myAnim);


                                   /* totalunits.setText(requiredunits.getText().toString());*/
                            }
                            dialogInterface.dismiss();
                        }
                    }).show();


                }
                if (Integer.parseInt(unitsproduced.getText().toString().trim()) >=Integer.parseInt(requiredunits.getText().toString().trim())) {



                    if (counts>=2){
                        unitsproduced.setText(requiredunits.getText().toString());
                        new AlertDialog.Builder(TaskActivity.this).setCancelable(false).setMessage("Producton is complete, you can now proceed to QC").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (Integer.parseInt(unitsproduced.getText().toString().trim()) >= Integer.parseInt(requiredunits.getText().toString().trim())) {

                                    Toast.makeText(TaskActivity.this, "Production Completed", Toast.LENGTH_SHORT).show();

                                    btnSubmit.startAnimation(myAnim);


                                   /* totalunits.setText(requiredunits.getText().toString());*/
                                }
                                dialogInterface.dismiss();
                            }
                        }).show();
                    }
                    else {
                        unitsleft.setText("Production Completed");
                        // Toast.makeText(TaskActivity.this, "Production is Completed Please click to Submit", Toast.LENGTH_SHORT).show();

                        TimeService.TimeContainer.getInstance().stopAndReset();
                        btnStart.setText("CONTINUE");
                        btnComplete.setVisibility(View.GONE);
                       // btnReset.setVisibility(View.VISIBLE);
                        new AlertDialog.Builder(TaskActivity.this).setCancelable(false).setMessage("Producton is complete, you can now proceed to QC").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (Integer.parseInt(unitsproduced.getText().toString().trim()) >= Integer.parseInt(requiredunits.getText().toString().trim())) {

                                    Toast.makeText(TaskActivity.this, "Production Completed", Toast.LENGTH_SHORT).show();

                                    totalunits.setText(requiredunits.getText().toString());
                                }
                                dialogInterface.dismiss();
                            }
                        }).show();
                    }



                }
                else {

                    recent = Integer.parseInt(arrayList.get(position).toString());
                    count = (Integer.parseInt(unitsproduced.getText().toString())) + (Integer.parseInt(arrayList.get(position).toString()));


                    if (count<Integer.parseInt(requiredunits.getText().toString().trim())){
                        Log.d("mmmmm",""+count);

                        unitsproduced.setText(String.valueOf(count));
                    }
                    else {
                        unitsproduced.setText(requiredunits.getText().toString());
                    }

                   /* if ((Integer.parseInt(unitsproduced.getText().toString().trim())< (Integer.parseInt(requiredunits.getText().toString().trim())))){

                        unitsproduced.setText(String.valueOf(count));

                    }*/

                    undobtn.setEnabled(true);

                    counts++;
                    int leftunits = Integer.parseInt(requiredunits.getText().toString().trim()) - Integer.parseInt(unitsproduced.getText().toString().trim());
                    unitsleft.setText(String.valueOf(leftunits + " " + "left"));
                    if (unitsproduced.getText().toString().equalsIgnoreCase("")){

                    }
                    else {
                        timerSession.createTimerData(unitsproduced.getText().toString(),tvTime.getText().toString(),unitsleft.getText().toString());
                    }

                }
                undobtn.setImageDrawable(getResources().getDrawable(R.mipmap.ic_undo));


            }


            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
           // getTaskDetails();
            getOfflineTaskData();

        }
        else {
            getOfflineTaskData();


            }

        }

    private void updateTaskStatus(String userId, String taskid, String s) {
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> responseBody=apiInterface.updateTaskStatus(userId, taskid,s);
        responseBody.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(TaskActivity.this, "success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(TaskActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    //  }

    private void getOfflineTaskData() {


       Cursor cursor=dbHelper.getTaskDetails(taskid);

        if (cursor.getCount()>0)
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    Log.d("mydatadone",cursor.getString(cursor.getColumnIndex("done_qty")));
                    Log.d("mydatatotal",cursor.getString(cursor.getColumnIndex("total_qty")));
                    String doneunits=cursor.getString(cursor.getColumnIndex("done_qty"));



                        totalunits.setText(cursor.getString(cursor.getColumnIndex("done_qty")));
                        requiredunits.setText(cursor.getString(cursor.getColumnIndex("total_qty")));

                        recordedtym.setText(cursor.getString(cursor.getColumnIndex("recordedtime")));
                    if (doneunits.equalsIgnoreCase(""))
                    {
                        Log.d("mydatatotal",cursor.getString(cursor.getColumnIndex("done_qty")));
                        doneunits="0";
                        unitsproduced.setText(doneunits);

                    }
                    else {
                        int left_units = ((Integer.parseInt(requiredunits.getText().toString()))) - (Integer.parseInt(doneunits));
                        unitsleft.setText(left_units + " left");
                        unitsproduced.setText(cursor.getString(cursor.getColumnIndex("done_qty")));

                    }



                        tvtask.setText(cursor.getString(cursor.getColumnIndex("task_name")));
                        taskdescrip.setText(cursor.getString(cursor.getColumnIndex("task_details")));


                        editor.putString("taskname", tvtask.getText().toString());

                        makeTextViewResizable(taskdescrip, 3, "View More", true);



                }
                while (cursor.moveToNext());
            }
        }



    }


    private void getOfflineSimilar(CharSequence charSequence) {

        Toast.makeText(this, ""+charSequence, Toast.LENGTH_SHORT).show();
        Cursor cursor=dbHelper.getTaskDetails(""+charSequence);

        if (cursor.getCount()>0)
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    Log.d("mydatadone",cursor.getString(cursor.getColumnIndex("done_qty")));
                    Log.d("mydatatotal",cursor.getString(cursor.getColumnIndex("total_qty")));
                    String doneunits=cursor.getString(cursor.getColumnIndex("done_qty"));
unitsproduced2.setVisibility(View.VISIBLE);

                    pdf_link2=cursor.getString(cursor.getColumnIndex("pdf_link"));
                    video_link2=cursor.getString(cursor.getColumnIndex("video_link"));

                    attachmenturl2=pdf_link2;
                 videolink   =video_link2;
                    totalunits.setText(cursor.getString(cursor.getColumnIndex("done_qty")));
                    requiredunit2.setText(cursor.getString(cursor.getColumnIndex("total_qty")));
                    timerSession.addStask(taskselectedid,requiredunit2.getText().toString());


                    task2.setText(cursor.getString(cursor.getColumnIndex("task_name")));
                    recordedtym.setText(cursor.getString(cursor.getColumnIndex("recordedtime")));
                    if (doneunits.equalsIgnoreCase(""))
                    {
                        Log.d("mydatatotal",cursor.getString(cursor.getColumnIndex("done_qty")));
                        doneunits="0";
                        unitsproduced2.setText(doneunits);

                    }
                    else {
                        int left_units = ((Integer.parseInt(requiredunit2.getText().toString()))) - (Integer.parseInt(doneunits));
                        unitsleft2.setText(left_units + " left");
                        unitsproduced2.setText(cursor.getString(cursor.getColumnIndex("done_qty")));

                    }



                   // .setText(cursor.getString(cursor.getColumnIndex("task_name")));
                    taskdescrip2.setText(""+cursor.getString(cursor.getColumnIndex("task_details")));


                   // editor.putString("taskname", tvtask.getText().toString());

/*
                    makeTextViewResizable(taskdescrip2, 3, "View More", true);
*/



                }
                while (cursor.moveToNext());
            }
        }



    }


    private void updateTaskDetails(String userId, String taskid, String duration,String amt) {
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);

     //   Toast.makeText(this, "user-"+userId+"taskid- "+taskid+" "+duration+" "+amt+" "+id+" "+pausetime, Toast.LENGTH_SHORT).show();
        Call<ResponseBody> responseBody=apiInterface.updateTask(userId,(taskid),duration,amt);

            responseBody.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                   //Toast.makeText(TaskActivity.this, "updated", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                   // Toast.makeText(TaskActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void getTaskDetailsOffline(String id){
        ApiInterface api= ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> response=api.getTaskDetailsOffline(id);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String data= null;
                try {
                    data = response.body().string();

                    JSONObject jsonObject = new JSONObject(data);
                    String dataresponse = jsonObject.getString("message");
                    String id = jsonObject.getString("id");
                    if (dataresponse.equalsIgnoreCase("success")) {

                        Log.d("iftask", data);

                        if (dataresponse.equalsIgnoreCase("success")) {

                              /*  if (!(myDbHelper.isTaskDataExists(id))) {

                                    myDbHelper.insertTaskData(data, id);
                                   // Toast.makeText(TaskActivity.this, "No task", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    myDbHelper.updateTaskData(data,id);
                                   // Toast.makeText(TaskActivity.this, "Task updated", Toast.LENGTH_SHORT).show();

                                }
*/

                        }
                    }
                }catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }




            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        } );
    }
    public void getTaskDetailsOffline()
    {


        Cursor cursor=dbHelper.getTaskDetails(taskid);
        if (cursor.getCount()>0)
        {
            if (cursor.moveToFirst()){
                do {
                    String id,taskname,taskdescription,quantity,estimated_time,duration,amount,pdf_link,video_link,dtask_id;


                    id=cursor.getString(cursor.getColumnIndex("task_id"));
                    taskname=cursor.getString(cursor.getColumnIndex("task_name"));
                    taskdescription=cursor.getString(cursor.getColumnIndex("task_details"));
                    quantity=cursor.getString(cursor.getColumnIndex("total_qty"));
                    estimated_time=cursor.getString(cursor.getColumnIndex("estimated_time"));
                    amount=cursor.getString(cursor.getColumnIndex("done_qty"));
                    pdf_link=cursor.getString(cursor.getColumnIndex("pdf_link"));
                    video_link=cursor.getString(cursor.getColumnIndex("video_link"));
                    dtask_id=cursor.getString(cursor.getColumnIndex("dependent_task_id"));

                    tvtask.setText(taskname);

                    taskdescrip.setText(taskdescription);
                    requiredunits.setText(quantity);
                    editor.putString("taskname",tvtask.getText().toString());
                    attachmenturl=pdf_link;
                    videolink=video_link;
                    taskdescrip.setText(taskdescription);
                    makeTextViewResizable(taskdescrip, 3, "View More", true);

                    if (dtask_id != null && !dtask_id.isEmpty()) {
                        //Toast.makeText(this, "has dependent", Toast.LENGTH_SHORT).show();

                    }




                }while (cursor.moveToNext());
            }
        }

    }


    private void getTaskDetails() {
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        Call<TaskDetails> responseBodyCall=apiInterface.getTaskDetails(taskid);
        responseBodyCall.enqueue(new Callback<TaskDetails>() {
            @Override
            public void onResponse(Call<TaskDetails> call, Response<TaskDetails> response) {

                String data=response.body().getMessage();
                Log.d("taskdetail",data);
                if (data.equalsIgnoreCase("success")) {


                }




            }

            @Override
            public void onFailure(Call<TaskDetails> call, Throwable t) {

            }
        });



    }

    private void getDependentask() {



    }


    private void updateTimeText() {


        tvTime.setText(getTimeString(TimeService.TimeContainer.getInstance().getElapsedTime()));
    }

    private void updateTimeText2() {


        tvTime.setText("00:00");
    }

    public void startUpdateTimer() {
        if (t != null) {
            t.cancel();
            t = null;
        }
        t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                h.post(updateTextRunnable);
            }
        }, 0, 16);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.two_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void checkServiceRunning() {
        if (!TimeService.TimeContainer.getInstance().isServiceRunning.get()) {
            startService(new Intent(getApplicationContext(), TimeService.class));
        }
    }

    private String getTimeString(long ms) {
        if (ms == 0) {
            return "00:00:00";
        } else {
            //  long millis = (ms % 1000) / 10;
            long seconds = (ms / 1000) % 60;
            long minutes = (ms / 1000) / 60;
            long hours = minutes / 60;

            StringBuilder sb = new StringBuilder();
            if (hours > 0) {
                sb.append(hours);
                sb.append(':');
            }
            else {
                sb.append('0');
                sb.append('0');
            }
            sb.append(':');
            if (minutes > 0) {
                minutes = minutes % 60;
                if (minutes >= 10) {
                    sb.append(minutes);
                } else {
                    sb.append(0);
                    sb.append(minutes);
                }
            } else {
                sb.append('0');
                sb.append('0');
            }
            sb.append(':');
            if (seconds > 0) {
                if (seconds >= 10) {
                    sb.append(seconds);
                } else {
                    sb.append(0);
                    sb.append(seconds);
                }
            } else {
                sb.append('0');
                sb.append('0');
            }
           /* sb.append(':');
            if (millis > 0) {
                if (millis >= 10) {
                    sb.append(millis);
                } else {
                    sb.append(0);
                    sb.append(millis);
                }
            } else {
                sb.append('0');
                sb.append('0');
            }*/
            return sb.toString();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

        if (propertyChangeEvent.getPropertyName() == TimeService.TimeContainer.STATE_CHANGED) {
            TimeService.TimeContainer t = TimeService.TimeContainer.getInstance();
            if (t.getCurrentState() == TimeService.TimeContainer.STATE_RUNNING) {
                btnStart.setText("PAUSE");

                unitsdata.setVisibility(View.VISIBLE);
                unitsdata2.setVisibility(View.VISIBLE);
                btnComplete.setVisibility(View.VISIBLE);
              // Toast.makeText(this, "running timer", Toast.LENGTH_SHORT).show();
                startUpdateTimer();
                btnReset.setVisibility(View.GONE);
            } else {


                updateTimeText();
            }
            if (t.getCurrentState() == TimeService.TimeContainer.STATE_STOPPED) {
              // Toast.makeText(this, "stopped", Toast.LENGTH_SHORT).show();
                btnStart.setText("START");
                unitsdata.setVisibility(View.GONE);
                unitsdata2.setVisibility(View.GONE);

                btnReset.setVisibility(View.GONE);
            } else {
                unitsdata2.setVisibility(View.VISIBLE);

            }
            if (t.getCurrentState() == TimeService.TimeContainer.STATE_PAUSED) {
              // Toast.makeText(this, "pause", Toast.LENGTH_SHORT).show();

                btnStart.setText("CONTINUE");
unitsdata.setVisibility(View.GONE);
                btnComplete.setVisibility(View.GONE);
               // btnReset.setVisibility(View.VISIBLE);
            }
            checkServiceRunning();
        }
    }

    public void showDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle("Select Reason");
        builder.setCancelable(false);

        //list of items
      items = getResources().getStringArray(R.array.pausereason);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                        getpauseselection=which+1;
                        Toast.makeText(TaskActivity.this, ""+items[which]+" "+getpauseselection, Toast.LENGTH_SHORT).show();


                    }
                });

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                        TimeService.TimeContainer tc = TimeService.TimeContainer.getInstance();
                        String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());

                         string1=currentDateTimeString;
                        int pid=getpauseselection+1;
                        totalunits.setText(unitsproduced.getText().toString());
                        dbHelper.updateTaskStatus(taskid,"5",totalunits.getText().toString().trim(),tvTime.getText().toString());//pause status


                        if (taskselectedid != null && !taskselectedid.isEmpty()) {
                            dbHelper.updateTaskStatus(taskselectedid,"5",totalunit2,tvTime.getText().toString());//pause status

                        }
                        boolean isconnect= ConnectivityReceiver.isConnected();
                        if (isconnect){
                            updateTaskStatus(userId,taskid,"5");
                            if (taskselectedid != null && !taskselectedid.isEmpty()) {
                                updateTaskStatus(userId, taskselectedid, "5");

                            }

                        }
                        unitsdata.setVisibility(View.GONE);

                        // updatePause(userId,taskid,String.valueOf(pid));
                        Toast.makeText(TaskActivity.this, tvTime.getText().toString()+" "+pid, Toast.LENGTH_SHORT).show();

                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                        btnReset.setVisibility(View.GONE);

                        TimeService.TimeContainer.getInstance().start();
                        startUpdateTimer();
                        Log.d("check", "stop");
                        btnStart.setText("PAUSE");
                        btnComplete.setVisibility(View.VISIBLE);
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    private void updatePause(String userId, String taskid, String s) {
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);

        //   Toast.makeText(this, "user-"+userId+"taskid- "+taskid+" "+duration+" "+amt+" "+id+" "+pausetime, Toast.LENGTH_SHORT).show();
        Call<ResponseBody> responseBody=apiInterface.updatePause(userId,taskid,s);
        responseBody.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        /*if (!(taskname.equalsIgnoreCase(sharedPreferences.getString("task","")))){
            Intent intent=new Intent(this,NoTaskActivity.class);
            intent.putExtra("taskname",taskname);
            startActivity(intent);
        }*/
        Toast.makeText(this, "resart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {

        super.onPause();
        if (t != null) {
            t.cancel();
            t = null;
        }
        TimeService.TimeContainer.getInstance().removeObserver(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }



    private void showVideo(String url) {
        if (url != null && !url.isEmpty()) {
            LayoutInflater inflater = getLayoutInflater();
            View dialogLayout = inflater.inflate(R.layout.videolayout, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            final AlertDialog dialog = builder.create();
            dialog.setView(dialogLayout);
            dialog.show();
            // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            final VideoView video_player_view = (VideoView) dialog.findViewById(R.id.video);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.copyFrom(dialog.getWindow().getAttributes());
            dialog.getWindow().setAttributes(lp);

            Uri uri = Uri.parse(url);
            video_player_view.setVideoURI(uri);
            video_player_view.start();
            // doSomething
        }
        else {
            Toast.makeText(this, "No Video", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (taskselectedid != null && !taskselectedid.isEmpty()) {
            openDialog.setVisibility(View.GONE);
            coordinate.setVisibility(View.VISIBLE);
            unitsproduced2.setVisibility(View.VISIBLE);
timerSession.addST(taskselectedid);
            if (timerSession.isLoggedIn()){
                Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
                HashMap<String,String> hashMap2=timerSession.getStaskDetails();
                unitsproduced2.setText(hashMap2.get(TimerSession.KEY_S_TASKAMT));
                unitsleft2.setText(hashMap2.get(TimerSession.KEY_S_TASKLEFT));
            }
else {
                Toast.makeText(this, "false", Toast.LENGTH_SHORT).show();

                coordinate.setVisibility(View.GONE);
            }
            getOfflineSimilar(taskselectedid);




        }
        else {
            coordinate.setVisibility(View.GONE);

            openDialog.setVisibility(View.VISIBLE);
        }


if (taskid.equalsIgnoreCase(sharedPreferences.getString("task",""))) {
    checkServiceRunning();
    TimeService.TimeContainer t = TimeService.TimeContainer.getInstance();
    if (t.getCurrentState() == TimeService.TimeContainer.STATE_RUNNING) {


        startUpdateTimer();
        unitsdata.setVisibility(View.VISIBLE);
        unitsdata2.setVisibility(View.VISIBLE);
        // btnStart.setText("PAUSE");
        btnComplete.setVisibility(View.VISIBLE);
        btnStart.setText("PAUSE");
       // Toast.makeText(this, "resume running timer", Toast.LENGTH_SHORT).show();
    }
    if (t.getCurrentState() == TimeService.TimeContainer.STATE_PAUSED) {
        btnStart.setText("CONTINUE");
        unitsdata.setVisibility(View.VISIBLE);
        unitsdata2.setVisibility(View.VISIBLE);
        btnComplete.setVisibility(View.GONE);
       // btnReset.setVisibility(View.VISIBLE);
        updateTimeText();

    }
    if (t.getCurrentState() == TimeService.TimeContainer.STATE_STOPPED) {
        btnStart.setText("START");

        unitsdata.setVisibility(View.GONE);
        unitsdata2.setVisibility(View.GONE);

        updateTimeText();

    }

    TimeService.TimeContainer.getInstance().addObserver(this);
}
else {
  //  Toast.makeText(this, "Start Your work", Toast.LENGTH_SHORT).show();
}
if (sharedPreferences.getString("task","").length()>0){
    Log.d("dfsf","fsdfsdf"+sharedPreferences.getString("task","")+""+taskid);
    if (!(taskid.matches(sharedPreferences.getString("task","")))){
        Intent intent=new Intent(this,NoTaskActivity.class);
        intent.putExtra("taskid",taskid);
        intent.putExtra("taskname",sharedPreferences.getString("taskname",""));
        startActivity(intent);
    }
    else {

        /*if (timerSession.isLoggedIn()){
            coordinate.setVisibility(View.VISIBLE);
            HashMap<String,String> hashMap2=timerSession.getStaskDetails();
            unitsproduced.setText(hashMap2.get(TimerSession.KEY_AMOUNT));
            unitsleft.setText(hashMap2.get(TimerSession.KEY_LEFT));
            //requiredunit.setText(hashMap2.get(TimerSession.KEY_S_TASKREQ));
           // Toast.makeText(this, "called"+hashMap2.get(TimerSession.KEY_S_TASKREQ), Toast.LENGTH_SHORT).show();

        }
        else {
            HashMap<String,String> hashMap2=timerSession.getTaskDetails();
            unitsproduced.setText(hashMap2.get(TimerSession.KEY_AMOUNT));
            unitsleft.setText(hashMap2.get(TimerSession.KEY_LEFT));
            coordinate.setVisibility(View.GONE);
        }*/
    }

}
/*
if (sharedPreferences.getString("task","").isEmpty()){

}*/


    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, null);
            return d;
        } catch (Exception e) {
            return null;
        }
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.action_attach) {
            if(attachmenturl!=null) {

                if (attachmenturl.contains(".pdf")) {
                    if (attachmenturl.contains("http")) {


                        if (PDFTools.isPDFSupported(TaskActivity.this)) {
                            PDFTools.showPDFUrl(TaskActivity.this, attachmenturl);
                        } else {
                            PDFTools.askToOpenPDFThroughGoogleDrive(TaskActivity.this, attachmenturl);
                        }
                    }
                    else {
                    if (PDFTools.isPDFSupported(TaskActivity.this)) {
                        PDFTools.showPDFUrl(TaskActivity.this, "http://66.201.99.67/~kinetics/"+attachmenturl);
                    } else {
                        PDFTools.askToOpenPDFThroughGoogleDrive(TaskActivity.this, "http://66.201.99.67/~kinetics/"+attachmenturl);
                    }
                    }

                }
            }
            else {
                Toast.makeText(this, "No Attachment", Toast.LENGTH_SHORT).show();
               /* Toast.makeText(this, "d", Toast.LENGTH_SHORT).show();
                LoadImageFromWebOperations(attachmenturl);*/
            }
        }

        if (view.getId() == R.id.action_attach2) {
            if(attachmenturl2!=null) {

                if (attachmenturl2.contains(".pdf")) {
                    if (attachmenturl2.contains("http")) {


                        if (PDFTools.isPDFSupported(TaskActivity.this)) {
                            PDFTools.showPDFUrl(TaskActivity.this, attachmenturl2);
                        } else {
                            PDFTools.askToOpenPDFThroughGoogleDrive(TaskActivity.this, attachmenturl2);
                        }
                    }
                    else {
                        if (PDFTools.isPDFSupported(TaskActivity.this)) {
                            PDFTools.showPDFUrl(TaskActivity.this, "http://66.201.99.67/~kinetics/"+attachmenturl2);
                        } else {
                            PDFTools.askToOpenPDFThroughGoogleDrive(TaskActivity.this, "http://66.201.99.67/~kinetics/"+attachmenturl2);
                        }
                    }

                }
            }
            else {
                Toast.makeText(this, "No Attachment", Toast.LENGTH_SHORT).show();
               /* Toast.makeText(this, "d", Toast.LENGTH_SHORT).show();
                LoadImageFromWebOperations(attachmenturl);*/
            }
        }

        if (view.getId() == R.id.action_video2) {
            showVideo(videolink2);
        }
        if (view.getId() == R.id.action_video) {
            showVideo(videolink);
        }
    }
}