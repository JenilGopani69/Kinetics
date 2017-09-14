package proj.kinetics.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import proj.kinetics.Model.Qualitycheck;
import proj.kinetics.QCActivity;
import proj.kinetics.R;
import proj.kinetics.VideoActivity;


/**
 * Created by sai on 19/7/17.
 */

public class QCAdapter extends RecyclerView.Adapter<QCAdapter.MyViewHolder> {
    List<Qualitycheck> arrayList;
     static Context context;
    public static Activity parentActivity;

    public  static ArrayList arrayListQc=new ArrayList();
    ArrayList<String> al=new ArrayList<>();
   static int checkAccumulator;

    public QCAdapter(List<Qualitycheck> arrayList, Context context, Activity parentActivity) {
        this.arrayList = arrayList;
        this.context = context;
        this.parentActivity = parentActivity;
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

        final Qualitycheck qualitycheck=arrayList.get(position);
        holder.textView.setText(qualitycheck.getDescripton());

        holder.checkBox1.setChecked(arrayList.get(position).getSelected());
holder.sub_textview.setText(qualitycheck.getDescripton());
        holder.checkBox1.setTag(position);
        holder.profile_imageview.setTag("http://www.tivix.com/uploads/blog_pics/Android-logo.png");
        holder.checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer pos = (Integer) holder.checkBox1.getTag();

                if (arrayList.get(pos).getSelected()) {
                    arrayList.get(pos).setSelected(false);
                 //   Toast.makeText(context, "unselected"+qualitycheck.getId(), Toast.LENGTH_SHORT).show();
                    al.remove(arrayList.get(pos).getDescripton());
                    arrayListQc.remove(qualitycheck.getId());


                } else {
                    al.add(arrayList.get(pos).getDescripton());

//                    Toast.makeText(context, "selected"+qualitycheck.getId(), Toast.LENGTH_SHORT).show();
                    arrayList.get(pos).setSelected(true);
                    arrayListQc.add(qualitycheck.getId());
                    Log.d("myselection","a"+arrayListQc);
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
                  //  Toast.makeText(context, "pending", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
    public static ArrayList<String> getCount(){
        return arrayListQc;
    }

    @Override
    public int getItemCount() {

        //return the size of arraylist
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textView,sub_textview;

        Context ctx;
        CheckBox checkBox1;
        ImageView profile_imageview,video_view;
        List<Qualitycheck> arrayList=new ArrayList();
        public MyViewHolder(View itemView, final Context context, final List<Qualitycheck> al) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.header_textview);
            sub_textview = (TextView) itemView.findViewById(R.id.sub_textview);
             checkBox1 = (CheckBox) itemView.findViewById(R.id.checkbox_imageview);
            profile_imageview = (ImageView) itemView.findViewById(R.id.profile_imageview);
            video_view = (ImageView) itemView.findViewById(R.id.video_view);


            arrayList=al;
            ctx=context;

            profile_imageview.setOnClickListener(this);
            video_view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (view.getId()== R.id.profile_imageview){
                int position=getAdapterPosition();
                Qualitycheck qualitycheck=arrayList.get(position);
                Dialog dialog=new Dialog(parentActivity);


                dialog.setContentView(R.layout.image_layout);
                ImageView image = (ImageView) dialog.findViewById(R.id.previewimg);
                Glide.with(context)
                        //.load("http://www.tivix.com/uploads/blog_pics/Android-logo.png")
                        .load(qualitycheck.getImageLink())
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(image);

                dialog.show();
                Toast.makeText(ctx, "File not Uploaded"+qualitycheck.getImageLink(), Toast.LENGTH_SHORT).show();

            }
            if (view.getId()== R.id.video_view)
            {

                int position=getAdapterPosition();
                Qualitycheck qualitycheck=arrayList.get(position);
                Intent intent=new Intent(parentActivity,VideoActivity.class);
                intent.putExtra("url",qualitycheck.getVideoLink());
                parentActivity.startActivity(intent);
            }








        }

        private void countCheck(boolean isChecked) {

            checkAccumulator += isChecked ? 1 : -1 ;
        }
    }
}