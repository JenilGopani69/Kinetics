package proj.kinetics;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import proj.kinetics.BroadcastReceivers.ConnectivityReceiver;
import proj.kinetics.Database.DBHelper;
import proj.kinetics.Fragments.CompletedTask;
import proj.kinetics.Fragments.MyTaskFragment;
import proj.kinetics.Fragments.Reports;
import proj.kinetics.Utils.ApiClient;
import proj.kinetics.Utils.ApiInterface;
import proj.kinetics.Utils.NonSwipeableViewPager;
import proj.kinetics.Utils.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {


    static boolean mIsLocked = false;
    private static boolean RUN_ONCE = true;
    SessionManagement session;
    ViewPagerAdapter adapter;
    TextView textcount;
    DBHelper dbHelper;
    int count1 = 0, count2 = 0;
    int count = 0;
    CoordinatorLayout containermain;

    Button btn;
    String userid;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private NonSwipeableViewPager viewPager;
TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new DBHelper(UserProfileActivity.this);
        getSupportActionBar().setTitle("");

        containermain= (CoordinatorLayout) findViewById(R.id.containermain);
         tv = toolbar.findViewById(R.id.timing);
        textcount = toolbar.findViewById(R.id.textcount);
        btn = toolbar.findViewById(R.id.button);
        if (RUN_ONCE == true) {
            RUN_ONCE = false;
            textcount.setText("0");


        } else {
            textcount.setText(String.valueOf(count));

        }
        boolean isconnect = ConnectivityReceiver.isConnected();

        if (isconnect) {
            btn.setEnabled(true);
        } else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            btn.setEnabled(false);

        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserProfileActivity.this, "Data Successfully Synced", Toast.LENGTH_SHORT).show();
                textcount.setText("0");

                Cursor cursor = dbHelper.getAllDataToSync();
                if (cursor.getCount() > 0)

                {

                    if (cursor.moveToFirst()) {
                        do {
                            String task_id, duration, amount, status;
                            task_id = cursor.getString(cursor.getColumnIndex("task_id"));
                            amount = cursor.getString(cursor.getColumnIndex("done_qty"));
                            duration = cursor.getString(cursor.getColumnIndex("recordedtime"));
                            status = cursor.getString(cursor.getColumnIndex("status"));


                            updateTaskDetails(userid, task_id, duration, amount);
                            //  Toast.makeText(UserProfileActivity.this, ""+task_id+""+amount+" "+duration+""+status, Toast.LENGTH_SHORT).show();


                        }
                        while (cursor.moveToNext());
                    }
                }
                //update data from local db to server
            }
        });
        ImageView iv = toolbar.findViewById(R.id.action_logout);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
                builder.setTitle("");

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
        });
        tv.setTextColor((Color.WHITE));
        tv.setTextSize(20);
        tv.setTypeface(Typeface.SANS_SERIF);
        Calendar c = Calendar.getInstance();
        session = new SessionManagement(getApplicationContext());
        session.checkLogin();
        checkConnection(containermain);
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // username
        String username = user.get(SessionManagement.KEY_USERNAME);


        // password
        String email = user.get(SessionManagement.KEY_PASSWORD);
        userid = user.get(SessionManagement.KEY_USERID);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm aaa");
        String formattedDate = df.format(c.getTime());
        tv.setText(formattedDate);


        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager = (NonSwipeableViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOffscreenPageLimit(0);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 1) {
                    //Toast.makeText(UserProfileActivity.this, tab.getText(), Toast.LENGTH_SHORT).show();


                }
                if (tab.getPosition() == 2) {
                    //Toast.makeText(UserProfileActivity.this, tab.getText(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void updateTaskDetails(String userId, String taskid, String duration, String amt) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        //   Toast.makeText(this, "user-"+userId+"taskid- "+taskid+" "+duration+" "+amt+" "+id+" "+pausetime, Toast.LENGTH_SHORT).show();
        Call<ResponseBody> responseBody = apiInterface.updateTask(userId, (taskid), duration, amt);

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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        boolean isconnect = ConnectivityReceiver.isConnected();

        if (hasFocus) {

            if (isconnect) {

                btn.setEnabled(true);

            } else {

                btn.setEnabled(false);
               count1 = Integer.parseInt(dbHelper.getSyncCountTask());
                count2 = Integer.parseInt(dbHelper.getSyncCountPause());
                count = count1 + count2;
                textcount.setText(""+count);
                Log.d("nnnnnnnnnnn",""+dbHelper.getSyncCountTask());
                Log.d("nnnnnnnnnnn",""+dbHelper.getSyncCountPause());
                Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            }

        } /*else {
            if (isconnect) {
                btn.setEnabled(true);
            } else {
                btn.setEnabled(false);
                Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            }*/

    }
    @Override
    protected void onResume() {
        super.onResume();

        final Handler someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv.setText(new SimpleDateFormat("hh:mm:ss dd/MM/yyyy", Locale.US).format(new Date()));
                someHandler.postDelayed(this, 1000);
            }
        }, 10);

       boolean isconnect = ConnectivityReceiver.isConnected();

        if (isconnect) {
            btn.setEnabled(true);
        } else {
            btn.setEnabled(false);
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
        checkConnection(containermain);



    }

    private void setupViewPager(NonSwipeableViewPager viewPager) {
        adapter.addFragment(new MyTaskFragment(), "Task (s)");
        adapter.addFragment(new CompletedTask(), "Completed Task (s)");
        adapter.addFragment(new Reports(), "Reports");
        viewPager.setAdapter(adapter);

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected, containermain);

    }
    private void checkConnection(ViewGroup container) {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected, container);
    }
    private void showSnack(boolean isConnected, ViewGroup container) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
          btn.setEnabled(true);



        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;

            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            btn.setEnabled(false);



        }

        Snackbar snackbar = Snackbar
                .make(container, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }
    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public void setLocked(boolean isLocked) {
            mIsLocked = isLocked;
            notifyDataSetChanged();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
