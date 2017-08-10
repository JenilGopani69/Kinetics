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

import proj.kinetics.Model.ProjectItem;
import proj.kinetics.R;

/**
 * Created by sai on 2/8/17.
 */

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.MyViewHolder> {
    ArrayList arrayList;
    Context context;


    public ProjectsAdapter(ArrayList arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //we call inflator over here...
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(v, context, arrayList);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        ProjectItem getSet = (ProjectItem) arrayList.get(position);
        holder.name.setText(getSet.getName());
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);
        holder.priorityid.setText(getSet.getPriority());
        //holder.descript.setText(getSet.getDescription());
        //holder.nooftask.setText(getSet.getNooftask());


    }

    @Override
    public int getItemCount() {

        //return the size of arraylist
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, descript, nooftask, priorityid;

        Context ctx;
        ArrayList<ProjectItem> arrayList = new ArrayList();

        public MyViewHolder(View itemView, Context context, ArrayList al) {
            super(itemView);
            itemView.setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.name);
            descript = (TextView) itemView.findViewById(R.id.description);
            priorityid = (TextView) itemView.findViewById(R.id.priorityid);


            arrayList = al;
            ctx = context;
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ProjectItem data = arrayList.get(position);
            //  Toast.makeText(ctx, ""+data.getName(), Toast.LENGTH_SHORT).show();

            // openBottomSheet(view);


        }
    }
}