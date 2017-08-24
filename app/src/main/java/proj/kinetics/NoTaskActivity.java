package proj.kinetics;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import proj.kinetics.Adapters.QCAdapter;
import proj.kinetics.Adapters.UnitsAdapter;
import proj.kinetics.Adapters.UnitsAdapter2;
import proj.kinetics.TimerWidget.TimeService;
import proj.kinetics.Utils.MySpannable;
import proj.kinetics.Utils.RecyclerTouchListener;
import proj.kinetics.Utils.SessionManagement;

public class NoTaskActivity extends AppCompatActivity implements View.OnClickListener {
    public static Button finishtask;
    String taskname;
    ArrayList<String> al = new ArrayList<>();
    ImageButton videoattach, attachment, undobtn, undobtn2;
    Toolbar toolbar;
    String string1 = "", string2 = "";
    Animation myAnim;
    SharedPreferences sharedPreferences;
    SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat("hh:mm:ss aa");

    int counts = 1;
    CoordinatorLayout coordinate;
    LinearLayout unitsdatas, unitsdata, nextqcbtn;
    QCAdapter myAdapter;
    RecyclerView units, units2, recyclerView;
    EditText unitsproduced, unitsproduced2;
    int count = 0;
    LinearLayout linqc, lintask;
    ArrayList arrayList = new ArrayList();
    UnitsAdapter unitsAdapter;
    UnitsAdapter2 unitsAdapter2;
    int recent = 0;
    List<CharSequence> list = new ArrayList<CharSequence>();
    private Button btnStart, btnReset, btnSubmit, btnComplete, btnSubmit2;
    private Handler h;
    private TextView tvTime, totalunits, requiredunits, taskdescrip, unitsleft, startedtime, taskdescrips, unitslefts, recordedtym, breaktym;
    private final Runnable updateTextRunnable = new Runnable() {
        @Override
        public void run() {
            updateTimeText();
        }
    };
    private Timer t;
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
        taskname = getIntent().getStringExtra("taskname");
        sharedPreferences = getSharedPreferences("tasktimer", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        session = new SessionManagement(getApplicationContext());
        session.checkLogin();
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManagement.KEY_USERNAME);

        // email
        String email = user.get(SessionManagement.KEY_PASSWORD);
        Toast.makeText(getApplicationContext(), "LoggedIN" + name, Toast.LENGTH_SHORT).show();
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

        final View openDialog = (View) findViewById(R.id.openDialog);
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
                                        taskdescrips = (TextView) findViewById(R.id.taskdescrips);

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
        unitsdata = (LinearLayout) findViewById(R.id.unitsdata);
        unitsdatas = (LinearLayout) findViewById(R.id.unitsdatas);
        lintask = (LinearLayout) findViewById(R.id.lintask);
        unitsproduced = (EditText) findViewById(R.id.unitsproduced);
        unitsproduced2 = (EditText) findViewById(R.id.unitsproduced2);
        taskdescrip = (TextView) findViewById(R.id.taskdescrip);
        requiredunits = (TextView) findViewById(R.id.requiredunits);
        tvTime = (TextView) findViewById(R.id.tvTime);
        unitsleft = (TextView) findViewById(R.id.unitsleft);
        unitslefts = (TextView) findViewById(R.id.unitslefts);
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
        videoattach.setOnClickListener(this);
        attachment.setOnClickListener(this);

        if (tvTime.getText().toString().equalsIgnoreCase("00:00")) {
            unitsdata.setVisibility(View.GONE);
            unitsdatas.setVisibility(View.GONE);
        }


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (unitsproduced.getText().toString().equals("0")) {

                    Toast.makeText(NoTaskActivity.this, "Cannot Submit No Units Produced", Toast.LENGTH_SHORT).show();
                }


            }
        });
        al.add("Checklist 1");
        al.add("Checklist 2");
        al.add("Checklist 3");
        if (unitsproduced.getText().toString().equalsIgnoreCase("0")) {
            undobtn.setEnabled(false);
            undobtn.setImageDrawable(getResources().getDrawable(R.mipmap.ic_undodisable));

        }
        if (unitsproduced2.getText().toString().equalsIgnoreCase("0")) {
            undobtn2.setEnabled(false);
            undobtn2.setImageDrawable(getResources().getDrawable(R.mipmap.ic_undodisable));

        }

        undobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NoTaskActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        undobtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NoTaskActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL
                , false);
        recyclerView.setLayoutManager(layoutManager);
        finishtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog openDialog = new Dialog(NoTaskActivity.this);
                openDialog.setContentView(R.layout.customdialog);

                TextView dialogTextContent = (TextView) openDialog.findViewById(R.id.dialog_text);
                Button dialogCloseButton = (Button) openDialog.findViewById(R.id.dialog_button);


                dialogCloseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(NoTaskActivity.this, UserProfileActivity.class);
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
        myAdapter = new QCAdapter(al, this);
        recyclerView.setAdapter(myAdapter);
        nextqcbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (totalunits.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(NoTaskActivity.this, "Cannot Proceed to QC Units are still Pending", Toast.LENGTH_SHORT).show();
                } else {

                    lintask.setVisibility(View.GONE);
                    linqc.setVisibility(View.VISIBLE);
                    nextqcbtn.setVisibility(View.GONE);
                }

            }
        });
        h = new Handler();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        makeTextViewResizable(taskdescrip, 3, "View More", true);
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


        unitsAdapter = new UnitsAdapter(arrayList, this);
        unitsAdapter2 = new UnitsAdapter2(arrayList, this);
        units.setAdapter(unitsAdapter);
        units2.setAdapter(unitsAdapter2);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NoTaskActivity.this, "cannot proceed", Toast.LENGTH_SHORT).show();

                if (taskname.equalsIgnoreCase(sharedPreferences.getString("task", ""))) {
                    Toast.makeText(NoTaskActivity.this, "cannot proceed", Toast.LENGTH_SHORT).show();

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
                        "%d days, %d hours, %d minutes, %d seconds%n",
                        elapsedDays,
                        elapsedHours, elapsedMinutes, elapsedSeconds);
                breaktym.setText("" + elapsedDays + "" + elapsedHours + ":" + elapsedMinutes + ":" + elapsedSeconds + "");
            }
        });
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeService.TimeContainer.getInstance().pause();
                btnComplete.setVisibility(View.GONE);
                recordedtym.setText(tvTime.getText().toString());

            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeService.TimeContainer.getInstance().reset();
                updateTimeText2();
                btnStart.setText("START");
            }
        });

        undobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (unitsproduced.getText().toString().equals("0")) {

                    undobtn.setImageDrawable(getResources().getDrawable(R.mipmap.ic_undodisable));


                } else {
                    int minus = Integer.parseInt((unitsproduced.getText().toString())) - recent;

                    unitsproduced.setText(String.valueOf(minus));
                    undobtn.setEnabled(false);
                    undobtn.setImageDrawable(getResources().getDrawable(R.mipmap.ic_undodisable));
                }
            }
        });
        undobtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (unitsproduced2.getText().toString().equals("0")) {

                    undobtn2.setImageDrawable(getResources().getDrawable(R.mipmap.ic_undodisable));


                } else {
                    int minus = Integer.parseInt((unitsproduced2.getText().toString())) - recent;

                    unitsproduced2.setText(String.valueOf(minus));
                    undobtn2.setEnabled(false);
                    undobtn2.setImageDrawable(getResources().getDrawable(R.mipmap.ic_undodisable));
                }
            }
        });

        units2.addOnItemTouchListener(new RecyclerTouchListener(this, units, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                recent = Integer.parseInt(arrayList.get(position).toString());
                count = (Integer.parseInt(unitsproduced2.getText().toString())) + (Integer.parseInt(arrayList.get(position).toString()));
                unitsproduced2.setText(String.valueOf(count));
                undobtn2.setEnabled(true);

                counts++;

                if (Integer.parseInt(unitsproduced2.getText().toString().trim()) >= Integer.parseInt(requiredunits.getText().toString().trim())) {

                    if (counts > 2) {
                        new AlertDialog.Builder(NoTaskActivity.this).setCancelable(false).setMessage("Producton is complete, you can now proceed to QC").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (Integer.parseInt(unitsproduced2.getText().toString().trim()) >= Integer.parseInt(requiredunits.getText().toString().trim())) {

                                    Toast.makeText(NoTaskActivity.this, "Production Completed", Toast.LENGTH_SHORT).show();


                                }
                                dialogInterface.dismiss();
                            }
                        }).show();
                    } else {
                        unitslefts.setText("Production Completed");
                        // Toast.makeText(TaskActivity.this, "Production is Completed Please click to Submit", Toast.LENGTH_SHORT).show();

                        TimeService.TimeContainer.getInstance().pause();
                        btnStart.setText("CONTINUE");
                        btnComplete.setVisibility(View.GONE);
                        btnReset.setVisibility(View.VISIBLE);
                        new AlertDialog.Builder(NoTaskActivity.this).setCancelable(false).setMessage("Producton is complete, you can now proceed to QC").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (Integer.parseInt(unitsproduced2.getText().toString().trim()) >= Integer.parseInt(requiredunits.getText().toString().trim())) {

                                    Toast.makeText(NoTaskActivity.this, "Production Completed", Toast.LENGTH_SHORT).show();

                                    totalunits.setText(requiredunits.getText().toString());
                                }
                                dialogInterface.dismiss();
                            }
                        }).show();
                    }


                } else {
                    int leftunits = Integer.parseInt(requiredunits.getText().toString().trim()) - Integer.parseInt(unitsproduced2.getText().toString().trim());
                    unitslefts.setText(String.valueOf(leftunits + " " + "left"));
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

                recent = Integer.parseInt(arrayList.get(position).toString());
                count = (Integer.parseInt(unitsproduced.getText().toString())) + (Integer.parseInt(arrayList.get(position).toString()));
                unitsproduced.setText(String.valueOf(count));
                undobtn.setEnabled(true);

                counts++;

                if (Integer.parseInt(unitsproduced.getText().toString().trim()) >= Integer.parseInt(requiredunits.getText().toString().trim())) {

                    if (counts > 2) {
                        new AlertDialog.Builder(NoTaskActivity.this).setCancelable(false).setMessage("Producton is complete, you can now proceed to QC").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (Integer.parseInt(unitsproduced.getText().toString().trim()) >= Integer.parseInt(requiredunits.getText().toString().trim())) {

                                    Toast.makeText(NoTaskActivity.this, "Production Completed", Toast.LENGTH_SHORT).show();

                                    btnSubmit.startAnimation(myAnim);

                                   /* totalunits.setText(requiredunits.getText().toString());*/
                                }
                                dialogInterface.dismiss();
                            }
                        }).show();
                    } else {
                        unitsleft.setText("Production Completed");
                        // Toast.makeText(TaskActivity.this, "Production is Completed Please click to Submit", Toast.LENGTH_SHORT).show();

                        TimeService.TimeContainer.getInstance().pause();
                        btnStart.setText("CONTINUE");
                        btnComplete.setVisibility(View.GONE);
                        btnReset.setVisibility(View.VISIBLE);
                        new AlertDialog.Builder(NoTaskActivity.this).setCancelable(false).setMessage("Producton is complete, you can now proceed to QC").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (Integer.parseInt(unitsproduced.getText().toString().trim()) >= Integer.parseInt(requiredunits.getText().toString().trim())) {

                                    Toast.makeText(NoTaskActivity.this, "Production Completed", Toast.LENGTH_SHORT).show();

                                    totalunits.setText(requiredunits.getText().toString());
                                }
                                dialogInterface.dismiss();
                            }
                        }).show();
                    }


                } else {
                    int leftunits = Integer.parseInt(requiredunits.getText().toString().trim()) - Integer.parseInt(unitsproduced.getText().toString().trim());
                    unitsleft.setText(String.valueOf(leftunits + " " + "left"));
                }

                undobtn.setImageDrawable(getResources().getDrawable(R.mipmap.ic_undo));


            }


            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
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
            return "00:00";
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
    public void onPause() {
        super.onPause();
        if (t != null) {
            t.cancel();
            t = null;
        }

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



    private void showVideo() {
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

        Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.video);
        video_player_view.setVideoURI(uri);
        video_player_view.start();
    }

    @Override
    public void onResume() {
        super.onResume();



    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.action_attach) {
            Toast.makeText(this, "No Attachment", Toast.LENGTH_SHORT).show();

        }
        if (view.getId() == R.id.action_video) {
            showVideo();
        }
    }
}