package proj.kinetics.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import proj.kinetics.Adapters.ProjectsAdapter;
import proj.kinetics.MainActivity;
import proj.kinetics.Model.ProjectItem;
import proj.kinetics.TaskActivity;
import proj.kinetics.R;
import proj.kinetics.Utils.LinearLayoutMangerWithSmoothScroll;
import proj.kinetics.Utils.RecyclerTouchListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTaskFragment extends Fragment {
    private View view;

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
    ProjectItem p1,p2,p3,p4,p5,p6;
    ArrayList<ProjectItem> al=new ArrayList();
    private LinearLayoutManager linearLayout;

    GridLayoutManager gridLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_task, container, false);

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
        projadapter=new ProjectsAdapter(al,getActivity());
        tasklist.setAdapter(projadapter);
        tasklist.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), tasklist, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ProjectItem projectItem=al.get(position);

                Intent intent=new Intent(getActivity(),TaskActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                        }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.three_menu, menu);


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (projadapter.getItemCount()>0) {
            al.clear();

            al.add(p1);
            al.add(p2);
            al.add(p3);
            al.add(p4);
            al.add(p5);
            al.add(p6);
            projadapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Intent intent=new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);


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
