package proj.kinetics.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import proj.kinetics.OneActivity;
import proj.kinetics.R;


/**
 * Created by sai on 19/7/17.
 */

public class QCAdapter extends RecyclerView.Adapter<QCAdapter.MyViewHolder> {
    ArrayList arrayList;
    Context context;
   static int checkAccumulator;

    public QCAdapter(ArrayList arrayList, Context context) {
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
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.textView.setText(""+arrayList.get(position));




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
        ArrayList<String> arrayList=new ArrayList();
        public MyViewHolder(View itemView, final Context context, ArrayList al) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.header_textview);
             checkBox1 = (CheckBox) itemView.findViewById(R.id.checkbox_imageview);
            profile_imageview = (ImageView) itemView.findViewById(R.id.profile_imageview);


            CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    countCheck(isChecked);
                    Log.i("MAIN", checkAccumulator + "");

                    if (checkAccumulator==3){
                    //QCFragment.finishtask.setVisibility(View.VISIBLE);
                        OneActivity.finishtask.setBackgroundColor(context.getResources().getColor(R.color.background));
                        OneActivity.finishtask.setEnabled(true);
                        OneActivity.finishtask.setClickable(true);
                    }
                    else {
                        //QCFragment.finishtask.setVisibility(View.INVISIBLE);
                        OneActivity.finishtask.setBackgroundColor(Color.GRAY);
                        OneActivity.finishtask.setEnabled(false);
                        OneActivity.finishtask.setClickable(false);
                    }
                }
            };
            checkBox1.setOnCheckedChangeListener(checkListener);
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

          //  Toast.makeText(ctx, ""+arrayList.get(position), Toast.LENGTH_SHORT).show();




        } private void countCheck(boolean isChecked) {

            checkAccumulator += isChecked ? 1 : -1 ;
        }
    }
}