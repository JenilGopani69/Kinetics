package proj.kinetics;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import proj.kinetics.Fragments.CompletedTask;
import proj.kinetics.Fragments.MyTaskFragment;
import proj.kinetics.Fragments.Reports;
import proj.kinetics.Utils.NonSwipeableViewPager;

public class UserProfileActivity extends AppCompatActivity {


    static boolean mIsLocked = false;
    ViewPagerAdapter adapter;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private NonSwipeableViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        TextView tv = new TextView(this);
        tv.setTextColor((Color.WHITE));
        tv.setTextSize(20);
        tv.setTypeface(Typeface.SANS_SERIF);
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm aaa");
        String formattedDate = df.format(c.getTime());
        tv.setText(formattedDate);
        toolbar.addView(tv);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager = (NonSwipeableViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOffscreenPageLimit(0);
tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        if (tab.getPosition()==1){
            Toast.makeText(UserProfileActivity.this, tab.getText(), Toast.LENGTH_SHORT).show();


        }
        if (tab.getPosition()==2){
            Toast.makeText(UserProfileActivity.this, tab.getText(), Toast.LENGTH_SHORT).show();
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


    private void setupViewPager(NonSwipeableViewPager viewPager) {
        adapter.addFragment(new MyTaskFragment(), "My Task");
        adapter.addFragment(new CompletedTask(), "Completed Task");
        adapter.addFragment(new Reports(), "Reports");
        viewPager.setAdapter(adapter);

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
