package se.hig.gastrikeappen.gastrikeappen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button startBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Här kommer en liten kommentar /Mattias

        startBt = (Button) findViewById(R.id.startBt);
        startBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start NewActivity.class
                Intent myIntent = new Intent(MainActivity.this,
                        MapsActivity.class);
                startActivity(myIntent);
            }
        });

    }
}
