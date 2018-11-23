package se.hig.gastrikeappen.gastrikeappen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Här kommer en liten kommentar /Mattias

        final Button mapButton = findViewById(R.id.map_button);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //kod för att öppna kartan i en ny view.
            }
        });

        final Button scoreButton = findViewById(R.id.score_button);
        scoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //kod för att visa score osv...
            }
        });
    }


}
