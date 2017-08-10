package proj.kinetics;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import proj.kinetics.Adapters.QCAdapter;
import proj.kinetics.Adapters.UnitsAdapter;
import proj.kinetics.TimerWidget.TimeService;
import proj.kinetics.Utils.MultiSelectionSpinner;
import proj.kinetics.Utils.MySpannable;
import proj.kinetics.Utils.RecyclerTouchListener;

public class OneActivity extends AppCompatActivity implements PropertyChangeListener, View.OnClickListener {
    private Button btnStart,btnReset;
    ImageButton  undobtn;
    private Handler h;
    String data;
    ArrayList<String> al=new ArrayList<>();
    ImageButton videoattach,attachment;
    private TextView tvTime,totalunits,requiredunits,startedtime;
    Toolbar toolbar;
LinearLayout unitsdata;
    private Timer t;
    QCAdapter myAdapter;
    public static Button finishtask;
LinearLayout nextqcbtn;
    boolean touch = true;
    MultiSelectionSpinner spinner;
    Button btnSubmit;
    RecyclerView units;
    RecyclerView recyclerView;
    TextView taskdescrip,unitsleft;
    EditText unitsproduced;
    int count=0;
    LinearLayout linqc,lintask;
    ArrayList arrayList=new ArrayList();
    UnitsAdapter unitsAdapter;
    int recent=0;
    List<CharSequence> list = new ArrayList<CharSequence>();
    private final Runnable updateTextRunnable = new Runnable() {
        @Override
        public void run() {
            updateTimeText();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        units= (RecyclerView) findViewById(R.id.units);
        finishtask= (Button) findViewById(R.id.finishtask);
        startedtime= (TextView) findViewById(R.id.startedtime);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        for (int i=0;i<20;i++){
            list.add("Task " + i);  // Add the item in the list
        }View openDialog = (View) findViewById(R.id.openDialog);
        openDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Intialize  readable sequence of char values
                final CharSequence[] dialogList=  list.toArray(new CharSequence[list.size()]);
                final AlertDialog.Builder builderDialog = new AlertDialog.Builder(OneActivity.this);
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
                                        if (stringBuilder.length() > 0) stringBuilder.append(",");
                                        stringBuilder.append(list.getItemAtPosition(i));


                                    }
                                }

                        /*Check string builder is empty or not. If string builder is not empty.
                          It will display on the screen.
                         */
                                if (stringBuilder.toString().trim().equals("")) {

                                    ((TextView) findViewById(R.id.text)).setText("SELECT SIMILAR TASK");
                                    stringBuilder.setLength(0);

                                } else {

                                    ((TextView) findViewById(R.id.text)).setText(stringBuilder);
                                }
                            }
                        });

                builderDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((TextView) findViewById(R.id.text)).setText("SELECT SIMILAR TASK");
                            }
                        });
                AlertDialog alert = builderDialog.create();
                alert.show();

            }
        });
        linqc = (LinearLayout) findViewById(R.id.lineqc);
        unitsdata = (LinearLayout) findViewById(R.id.unitsdata);
        lintask = (LinearLayout) findViewById(R.id.lintask);
        unitsproduced = (EditText) findViewById(R.id.unitsproduced);
        taskdescrip = (TextView) findViewById(R.id.taskdescrip);
        requiredunits = (TextView) findViewById(R.id.requiredunits);
        tvTime = (TextView) findViewById(R.id.tvTime);
        unitsleft = (TextView) findViewById(R.id.unitsleft);
        totalunits = (TextView) findViewById(R.id.totalunits);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        undobtn = (ImageButton) findViewById(R.id.undobtn);
        videoattach = (ImageButton) findViewById(R.id.action_video);
        attachment = (ImageButton) findViewById(R.id.action_attach);
        nextqcbtn = (LinearLayout) findViewById(R.id.nextqcbtn);
        recyclerView= (RecyclerView) findViewById(R.id.qcrecyler);
        btnReset = (Button) findViewById(R.id.btnReset);
        videoattach.setOnClickListener(this);
        attachment.setOnClickListener(this);

if (tvTime.getText().toString().equalsIgnoreCase("00:00:00")){
    unitsdata.setVisibility(View.GONE);
}



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (unitsproduced.getText().toString().equals("0")){

                    Toast.makeText(OneActivity.this, "Cannot Submit No Units Produced", Toast.LENGTH_SHORT).show();
                }

                else {

                    new AlertDialog.Builder(OneActivity.this).setCancelable(false).setMessage("I have produced "+unitsproduced.getText().toString()+" units").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (requiredunits.getText().toString().trim().equalsIgnoreCase(unitsproduced.getText().toString().trim())) {
                                totalunits.setText(unitsproduced.getText().toString());
                            }
                            if (Integer.parseInt(unitsproduced.getText().toString().trim())>=Integer.parseInt(requiredunits.getText().toString().trim())){

                                Toast.makeText(OneActivity.this, "Production Completed", Toast.LENGTH_SHORT).show();
                                totalunits.setText(requiredunits.getText().toString());
                                                            }
                                                            else {



                                Toast.makeText(OneActivity.this, "Please complete the production", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
                }
            }
        });
        al.add("Checklist 1");
        al.add("Checklist 2");
        al.add("Checklist 3");
       if (unitsproduced.getText().toString().equalsIgnoreCase("0")){
            undobtn.setEnabled(false);
           undobtn.setImageDrawable(getResources().getDrawable(R.mipmap.ic_undodisable));

       }

        undobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OneActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL
                ,false);
        recyclerView.setLayoutManager(layoutManager);
        finishtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog openDialog = new Dialog(OneActivity.this);
                openDialog.setContentView(R.layout.customdialog);

                TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
                Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);



                dialogCloseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      Intent intent=new Intent(OneActivity.this,UserProfileActivity.class);
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
        myAdapter=new QCAdapter(al,this);
        recyclerView.setAdapter(myAdapter);
        nextqcbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (totalunits.getText().toString().equalsIgnoreCase("0")){
                    Toast.makeText(OneActivity.this, "Cannot Proceed to QC Units are still Pending", Toast.LENGTH_SHORT).show();                }
                else {

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
        RecyclerView.LayoutManager layoutManager2=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        units.setLayoutManager(layoutManager2);



        unitsAdapter=new UnitsAdapter(arrayList,this);
        units.setAdapter(unitsAdapter);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unitsdata.setVisibility(View.VISIBLE);

                TimeService.TimeContainer tc = TimeService.TimeContainer.getInstance();
//startedtime.setText(""+tc.getStartTime());
                if (tc.getCurrentState() == TimeService.TimeContainer.STATE_RUNNING ) {
                    TimeService.TimeContainer.getInstance().pause();
                    btnStart.setText("CONTINUE");
                    btnReset.setVisibility(View.VISIBLE);
                    showDialog();
                    Log.d("check","start");
                } else {
                    btnReset.setVisibility(View.GONE);

                    TimeService.TimeContainer.getInstance().start();
                    startUpdateTimer();
                    Log.d("check","stop");
                    btnStart.setText("STOP");
                }
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

                if (unitsproduced.getText().toString().equals("0")){

                    undobtn.setImageDrawable(getResources().getDrawable(R.mipmap.ic_undodisable));


                }
                else {
                    int minus=Integer.parseInt((unitsproduced.getText().toString()))-recent;

                    unitsproduced.setText(String.valueOf(minus));
                    undobtn.setEnabled(false);
                    undobtn.setImageDrawable(getResources().getDrawable(R.mipmap.ic_undodisable));
                }
                }
        });



        units.addOnItemTouchListener(new RecyclerTouchListener(this, units, new RecyclerTouchListener.OnItemClickListener() {
    @Override
    public void onItemClick(View view, int position) {

        recent=Integer.parseInt(arrayList.get(position).toString());
        count=(Integer.parseInt(unitsproduced.getText().toString()))+(Integer.parseInt(arrayList.get(position).toString()));
        unitsproduced.setText(String.valueOf(count));
        undobtn.setEnabled(true);


        if (Integer.parseInt(unitsproduced.getText().toString().trim())>=Integer.parseInt(requiredunits.getText().toString().trim())){

            unitsleft.setText("Production Completed");
            Toast.makeText(OneActivity.this, "Production is Completed Please click to Submit", Toast.LENGTH_SHORT).show();

        }else {
            int leftunits=Integer.parseInt(requiredunits.getText().toString().trim())-Integer.parseInt(unitsproduced.getText().toString().trim());
            unitsleft.setText(String.valueOf(leftunits+" "+"left"));
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
        if(t != null) {
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
        getMenuInflater().inflate(R.menu.one_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void checkServiceRunning() {
        if(!TimeService.TimeContainer.getInstance().isServiceRunning.get()) {
            startService(new Intent(getApplicationContext(), TimeService.class));
        }
    }
    private String getTimeString(long ms) {
        if (ms == 0) {
            return "00:00:00";
        } else {
            long millis = (ms % 1000) / 10;
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
            sb.append(':');
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
            }
            return sb.toString();
        }
    }
    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if(propertyChangeEvent.getPropertyName() == TimeService.TimeContainer.STATE_CHANGED) {
            TimeService.TimeContainer t = TimeService.TimeContainer.getInstance();
            if(t.getCurrentState() == TimeService.TimeContainer.STATE_RUNNING) {
                btnStart.setText("STOP");
                startUpdateTimer();
                btnReset.setVisibility(View.GONE);
            } else {
                btnStart.setText("CONTINUE");
                btnReset.setVisibility(View.VISIBLE);
                updateTimeText();
            }
            checkServiceRunning();
        }
    }
    public void showDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        builder.setTitle("Select Reason");
        builder.setCancelable(false);

        //list of items
        String[] items = getResources().getStringArray(R.array.pausereason);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                    }
                });

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
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
                        Log.d("check","stop");
                        btnStart.setText("STOP");
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(t != null) {
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

        checkServiceRunning();
        TimeService.TimeContainer t = TimeService.TimeContainer.getInstance();
        if(t.getCurrentState() == TimeService.TimeContainer.STATE_RUNNING) {
            btnStart.setText("STOP");
            startUpdateTimer();
        } else {
            btnStart.setText("START");
            updateTimeText();
        }
        TimeService.TimeContainer.getInstance().addObserver(this);


    }
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


            ssb.setSpan(new MySpannable(false){
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
    public void onClick(View view) {
        if (view.getId()==R.id.action_attach){
            Toast.makeText(this, "No Attachment", Toast.LENGTH_SHORT).show();

        }
        if (view.getId()==R.id.action_video){
            showVideo();
        }
    }
}