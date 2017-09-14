package proj.kinetics.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import proj.kinetics.R;


/**
 * Created by sai on 19/7/17.
 */

public class UnitsAdapter extends RecyclerView.Adapter<UnitsAdapter.MyViewHolder> {
    ArrayList arrayList;
    Context context;
    int selectedPosition = -1;


    public UnitsAdapter(ArrayList arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //we call inflator over here...
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.unitsitems, parent, false);

        return new MyViewHolder(v, context, arrayList);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.textView.setText("" + arrayList.get(position));
        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#5CAF91"));
            holder.itemView.getBackground().setAlpha(125);

        } else
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                notifyDataSetChanged();
                //Toast.makeText(context, ""+arrayList.get(position), Toast.LENGTH_SHORT).show();


            }
        });


    }

    @Override
    public int getItemCount() {

        //return the size of arraylist
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView, textView2, textView3;
        Button btn;
        Context ctx;
        CardView cdunits;
        ArrayList<String> arrayList = new ArrayList();

        public MyViewHolder(View itemView, Context context, ArrayList al) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.unit);
            cdunits = (CardView) itemView.findViewById(R.id.cdunits);


            arrayList = al;
            ctx = context;
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();


            //   Toast.makeText(ctx, ""+arrayList.get(position), Toast.LENGTH_SHORT).show();


        }
    }
}