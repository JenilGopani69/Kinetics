package proj.kinetics.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import proj.kinetics.Model.Task;
import proj.kinetics.R;

/**
 * Created by sai on 2/8/17.
 */

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.MyViewHolder> {
    List<Task> list;
    Context context;


    public ProjectsAdapter(List<Task> list, Context context) {
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
       /* Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);*/

       int pos=position+1;
           holder.priorityid.setText(""+pos+"");
        Log.d("checkempty",""+getSet.getPriority());

       if (getSet.getProjectName()!=null) {

           holder.pname.setText("~" + getSet.getProjectName());
       }
       else {
           holder.pname.setText("");
       }
        holder.estimated_time.setText(getSet.getEstimatedTime().trim());
        if (getSet.getDue_date()!=null) {
            holder.duedate.setText(getSet.getDue_date().trim());
            //holder.nooftask.setText(getSet.getNooftask());
        }
        else {
            holder.duedatename.setText("");
        }


    }

    @Override
    public int getItemCount() {

        //return the size of arraylist
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        proj.kinetics.Customlib.MyTextView name, duedate, pname, priorityid, estimated_time, duedatename;

        Context ctx;
        List<Task> arrayList = new ArrayList();

        public MyViewHolder(View itemView, Context context, List<Task> al) {
            super(itemView);
            itemView.setOnClickListener(this);
            name = (proj.kinetics.Customlib.MyTextView) itemView.findViewById(R.id.name);
            duedate = (proj.kinetics.Customlib.MyTextView) itemView.findViewById(R.id.duedate);
            priorityid = (proj.kinetics.Customlib.MyTextView) itemView.findViewById(R.id.priorityid);
            duedatename = (proj.kinetics.Customlib.MyTextView) itemView.findViewById(R.id.duedatename);

            pname = (proj.kinetics.Customlib.MyTextView) itemView.findViewById(R.id.pname);
            estimated_time = (proj.kinetics.Customlib.MyTextView) itemView.findViewById(R.id.estimated_time);


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