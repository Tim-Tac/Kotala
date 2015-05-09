package be.timtac.ephec.kotala;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import be.timtac.ephec.R;

public class Start extends Activity {
	private static final long DELAY = 1000;
    private boolean scheduled = false;
    private Timer splashTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		final ImageView kota = (ImageView)findViewById(R.id.kota);
		kota.setScaleType(ScaleType.FIT_XY);
		
		
		splashTimer = new Timer();
        splashTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                Start.this.finish();
                startActivity(new Intent(Start.this, MainActivity.class));
            }
         }, DELAY);
       scheduled = true;
		
		
	}
	
	
	 @Override
	    protected void onDestroy()
	    {
	        super.onDestroy();
	        if (scheduled)
	            splashTimer.cancel();
	        splashTimer.purge();
	    }
	
	
}
