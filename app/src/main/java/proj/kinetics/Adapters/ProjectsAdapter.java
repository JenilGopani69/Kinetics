package proj.kinetics.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import proj.kinetics.Database.DBHelper;
import proj.kinetics.Model.ProjectItem;
import proj.kinetics.Model.Task;
import proj.kinetics.R;

/**
 * Created by sai on 2/8/17.
 */

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.MyViewHolder> {
    List<Task> list;
    Context context;


    public ProjectsAdapter( List<Task> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //we call inflator over here...
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(v, context, list);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Task getSet = (Task) list.get(position);
        holder.name.setText(getSet.getTaskName());
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);
        holder.priorityid.setText(getSet.getPriority());
        holder.pname.setText("~"+getSet.getProjectName());
        holder.estimated_time.setText(getSet.getEstimatedTime());

        holder.duedate.setText(getSet.getRequiredTime());
        //holder.nooftask.setText(getSet.getNooftask());



    }

    @Override
    public int getItemCount() {

        //return the size of arraylist
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, duedate, pname, priorityid,estimated_time;

        Context ctx;
        List<Task> arrayList = new ArrayList();

        public MyViewHolder(View itemView, Context context, List<Task> al) {
            super(itemView);
            itemView.setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.name);
            duedate = (TextView) itemView.findViewById(R.id.duedate);
            priorityid = (TextView) itemView.findViewById(R.id.priorityid);
            pname = (TextView) itemView.findViewById(R.id.pname);
            estimated_time = (TextView) itemView.findViewById(R.id.estimated_time);


            arrayList = al;
            ctx = context;
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Task data = arrayList.get(position);
            //  Toast.makeText(ctx, ""+data.getName(), Toast.LENGTH_SHORT).show();

            // openBottomSheet(view);


        }
    }
}