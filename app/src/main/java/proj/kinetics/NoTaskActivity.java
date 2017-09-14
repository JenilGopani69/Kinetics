package proj.kinetics;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import proj.kinetics.Adapters.QCAdapter;
import proj.kinetics.Adapters.QCAdapter_;
import proj.kinetics.Adapters.UnitsAdapter;
import proj.kinetics.Adapters.UnitsAdapter2;
import proj.kinetics.Model.TaskDetails;
import proj.kinetics.Utils.ApiClient;
import proj.kinetics.Utils.ApiInterface;
import proj.kinetics.Utils.MySpannable;
import proj.kinetics.Utils.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoTaskActivity extends AppCompatActivity {
    public static Button finishtask;
    String taskname;
    String taskid;

    QCAdapter_ qcAd;

    ArrayList<String> al = new ArrayList<>();
    ImageButton videoattach, attachment, undobtn, undobtn2;
    Toolbar toolbar;
    String string1 = "", string2 = "";
    Animation myAnim;
    SharedPreferences sharedPreferences;
    SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat("hh:mm:ss aa");
     View openDialog;

    String d_taskdescription="",d_taskname="",d_taskquantity="";
    int counts = 1;
    CoordinatorLayout coordinate;
    LinearLayout unitsdatas, unitsdata, nextqcbtn;
    QCAdapter myAdapter;
    RecyclerView units, units2, recyclerView;
    EditText unitsproduced, unitsproduced2;
    int count = 0;
    LinearLayout linqc,taskd;
    LinearLayout  lintask;
    ArrayList arrayList = new ArrayList();
    UnitsAdapter unitsAdapter;
    UnitsAdapter2 unitsAdapter2;
    int recent = 0;
    List<CharSequence> list = new ArrayList<CharSequence>();
    private Button btnStart, btnReset, btnSubmit, btnComplete, btnSubmit2;
    private Handler h;
    private TextView tvtask,tvTime, totalunits, requiredunits, taskdescrip, unitsleft, startedtime, taskdescrips, unitslefts, recordedtym, breaktym;

    private SessionManagement session;
    SharedPreferences.Editor editor;

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
        taskid = getIntent().getStringExtra("taskid");
        taskname = getIntent().getStringExtra("taskname");
        sharedPreferences = getSharedPreferences("tasktimer", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        session = new SessionManagement(getApplicationContext());
        session.checkLogin();
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        tvtask = (TextView) findViewById(R.id.task);

        // name
        String name = user.get(SessionManagement.KEY_USERNAME);

        // email
        String email = user.get(SessionManagement.KEY_PASSWORD);
        //Toast.makeText(getApplicationContext(), "LoggedIN" + name, Toast.LENGTH_SHORT).show();
        units = (RecyclerView) findViewById(R.id.units);
        units2 = (RecyclerView) findViewById(R.id.units2);
        finishtask = (Button) findViewById(R.id.finishtask);
        startedtime = (TextView) findViewById(R.id.startedtime);
        breaktym = (TextView) findViewById(R.id.breaktym);
        recordedtym = (TextView) findViewById(R.id.recordedtym);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        coordinate = (CoordinatorLayout) findViewById(R.id.coordinate);
        myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        list.add("Task  1");  // Add the item in the list
        openDialog = (View) findViewById(R.id.openDialog);
        openDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Intialize  readable sequence of char values
                final CharSequence[] dialogList = list.toArray(new CharSequence[list.size()]);
                final AlertDialog.Builder builderDialog = new AlertDialog.Builder(NoTaskActivity.this);
                builderDialog.setTitle("SELECT TASK");
                int count = dialogList.length;
                boolean[] is_checked = new boolean[count];

                // Creating multiple selection by using setMutliChoiceItem method
                builderDialog.setMultiChoiceItems(dialogList, is_checked,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton, boolean isChecked) {
                            }
                        });

                builderDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                ListView list = ((AlertDialog) dialog).getListView();
                                // make selected item in the comma seprated string

                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < list.getCount(); i++) {
                                    boolean checked = list.isItemChecked(i);

                                    if (checked) {
                                      /*  if (stringBuilder.length() > 0) stringBuilder.append(",");
                                        stringBuilder.append(list.getItemAtPosition(i));*/
                                        coordinate.setVisibility(View.VISIBLE);
                                        taskdescrips = (TextView) findViewById(R.id.taskdescrip2);

                                        makeTextViewResizable(taskdescrips, 3, "View More", true);

                                        openDialog.setVisibility(View.GONE);


                                    }
                                }

                        /*Check string builder is empty or not. If string builder is not empty.
                          It will display on the screen.
                         */
                                /*if (stringBuilder.toString().trim().equals("")) {

                                    ((TextView) findViewById(R.id.text)).setText("SELECT SIMILAR TASK");
                                    stringBuilder.setLength(0);

                                } else {

                                    ((TextView) findViewById(R.id.text)).setText(stringBuilder);
                                }*/
                            }
                        });

                builderDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog alert = builderDialog.create();
                alert.show();

            }
        });
        linqc = (LinearLayout) findViewById(R.id.lineqc);
        taskd = (LinearLayout) findViewById(R.id.taskd);

        unitsdata = (LinearLayout) findViewById(R.id.unitsdata);
        unitsdatas = (LinearLayout) findViewById(R.id.unitsdata2);
        lintask = (LinearLayout) findViewById(R.id.lintask);
        unitsproduced = (EditText) findViewById(R.id.unitsproduced);
        unitsproduced2 = (EditText) findViewById(R.id.unitsproduced2);
        taskdescrip = (TextView) findViewById(R.id.taskdescrip);
        requiredunits = (TextView) findViewById(R.id.requiredunits);
        tvTime = (TextView) findViewById(R.id.tvTime);
        unitsleft = (TextView) findViewById(R.id.unitsleft);
        unitslefts = (TextView) findViewById(R.id.unitsleft2);
        totalunits = (TextView) findViewById(R.id.totalunits);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnComplete = (Button) findViewById(R.id.btnComplete);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        undobtn = (ImageButton) findViewById(R.id.undobtn);
        undobtn2 = (ImageButton) findViewById(R.id.undobtn2);
        videoattach = (ImageButton) findViewById(R.id.action_video);
        attachment = (ImageButton) findViewById(R.id.action_attach);
        nextqcbtn = (LinearLayout) findViewById(R.id.nextqcbtn);
        recyclerView = (RecyclerView) findViewById(R.id.qcrecyler);
        btnReset = (Button) findViewById(R.id.btnReset);

        unitsdata.setVisibility(View.GONE);
openDialog.setVisibility(View.GONE);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unitsdata.setVisibility(View.GONE);

                //Toast.makeText(NoTaskActivity.this, "cannot proceed", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(NoTaskActivity.this).setMessage("Cannot Proceed " + taskname + " is already running").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();



                    }
                }).setCancelable(true).show();
            }
        });
getTaskDetails();
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.two_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }








    @Override
    public void onPause() {
        super.onPause();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
          Intent intent=new Intent(NoTaskActivity.this,UserProfileActivity.class);
            startActivity(intent);
            return true;
        }

        return false;
    }


    private void getTaskDetails() {
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        Call<TaskDetails> responseBodyCall=apiInterface.getTaskDetails(taskid);
        responseBodyCall.enqueue(new Callback<TaskDetails>() {
            @Override
            public void onResponse(Call<TaskDetails> call, Response<TaskDetails> response) {
                tvtask.setText(response.body().getTaskname());


                requiredunits.setText(response.body().getQuantity());
                taskdescrip.setText(response.body().getTaskdescription());
                makeTextViewResizable(taskdescrip, 3, "View More", true);


            }

            @Override
            public void onFailure(Call<TaskDetails> call, Throwable t) {

            }
        });







    }

    @Override
    public void onResume() {
        super.onResume();



    }

    }
