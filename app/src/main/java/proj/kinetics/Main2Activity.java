package proj.kinetics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import proj.kinetics.Adapters.UnitsAdapter;

import static proj.kinetics.TaskActivity.makeTextViewResizable;

public class Main2Activity extends AppCompatActivity {
TextView taskdescrip,taskdescrip2;
    ArrayList arrayList = new ArrayList();
    UnitsAdapter unitsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sidetask);

    }
}
