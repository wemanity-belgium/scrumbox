package com.wemanity.scrumbox.android.gui;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.wemanity.scrumbox.android.time.CountDown;
import com.wemanity.scrumbox.android.R;


public class MainActivity extends Activity {

    private Button startButton;
    private TextView timeTextView;
    private CountDownTimer countDownTimer;
    private CountDown countDown = null;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*timeTextView = (TextView) findViewById(R.id.timeTextView);
        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDown == null) {
                    ((Button)v).setText("Stop");
                    final BigDecimal delay = new BigDecimal(1000);
                    countDown = new CountDown(3000, 100) {

                        public void onTick(long millisUntilFinished) {
                            timeTextView.setText("seconds remaining: " + new BigDecimal(millisUntilFinished).divide(delay).setScale(2, RoundingMode.HALF_UP));
                        }

                    }.start();
                } else {
                    countDown.stop();
                    countDown = null;
                    ((Button)v).setText("Start");
                }

            }
        });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
