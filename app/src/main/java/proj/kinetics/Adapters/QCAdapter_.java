package proj.kinetics.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import proj.kinetics.Model.Qualitycheck_;
import proj.kinetics.QCActivity;
import proj.kinetics.R;


/**
 * Created by sai on 19/7/17.
 */

public class QCAdapter_ extends RecyclerView.Adapter<QCAdapter_.MyViewHolder> {
    List<Qualitycheck_> arrayList;
    Context context;
    ArrayList<String> al=new ArrayList<>();
   static int checkAccumulator;

    public QCAdapter_(List<Qualitycheck_> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        checkAccumulator = 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //we call inflator over here...
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.qcitem,parent,false);

        return new MyViewHolder(v,context,arrayList);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Qualitycheck_ qualitycheck=arrayList.get(position);
        holder.textView.setText(qualitycheck.getDescripton());
        Toast.makeText(context, ""+qualitycheck.getDescripton(), Toast.LENGTH_SHORT).show();
        holder.checkBox1.setChecked(arrayList.get(position).getSelected());

        holder.checkBox1.setTag(position);
        holder.checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer pos = (Integer) holder.checkBox1.getTag();

                if (arrayList.get(pos).getSelected()) {
                    arrayList.get(pos).setSelected(false);
                    Toast.makeText(context, "unselected", Toast.LENGTH_SHORT).show();
                    al.remove(arrayList.get(pos).getDescripton());

                } else {
                    al.add(arrayList.get(pos).getDescripton());

                    Toast.makeText(context, "selected", Toast.LENGTH_SHORT).show();
                    arrayList.get(pos).setSelected(true);
                }

                if (arrayList.size()==al.size()){
                    QCActivity.finishtask.setBackgroundColor(context.getResources().getColor(R.color.background));
                    QCActivity.finishtask.setEnabled(true);
                    QCActivity.finishtask.setClickable(true);
                    Toast.makeText(context, "done", Toast.LENGTH_SHORT).show();
                }
                else {
                    QCActivity.finishtask.setBackgroundColor(Color.GRAY);
                    QCActivity.finishtask.setEnabled(false);
                    QCActivity.finishtask.setClickable(false);
    Toast.makeText(context, "pending", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    @Override
    public int getItemCount() {

        //return the size of arraylist
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textView;

        Context ctx;
        CheckBox checkBox1;
        ImageView profile_imageview;
        List<Qualitycheck_> arrayList=new ArrayList();
        public MyViewHolder(View itemView, final Context context, final List<Qualitycheck_> al) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.header_textview);
             checkBox1 = (CheckBox) itemView.findViewById(R.id.checkbox_imageview);
            profile_imageview = (ImageView) itemView.findViewById(R.id.profile_imageview);


            arrayList=al;
            ctx=context;

            profile_imageview.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId()== R.id.profile_imageview){
                Toast.makeText(ctx, "File not Uploaded", Toast.LENGTH_SHORT).show();
            }
            int position=getAdapterPosition();






        } private void countCheck(boolean isChecked) {

            checkAccumulator += isChecked ? 1 : -1 ;
        }
    }
}