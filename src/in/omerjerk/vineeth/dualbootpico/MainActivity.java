package in.omerjerk.vineeth.dualbootpico;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		new fileCheck().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public class fileCheck extends AsyncTask<String, Boolean, Boolean>{
		
		@Override
		protected Boolean doInBackground(String... fuck){
			
			File sdcard = Environment.getExternalStorageDirectory();
			File rootFile = new File(sdcard, ".bootsdcard");
			
			if(rootFile.exists())
				return true;
			else
				return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result){
			TextView bootMessage = (TextView) findViewById(R.id.bootText);
			Button bootButton = (Button) findViewById(R.id.bootButton);
			if(result){
				bootMessage.setText("Primary ROM will be booted!");
				bootButton.setText("Boot primary ROM");
			} else {
				bootMessage.setText("Secondary ROM will be booted!");
				bootButton.setText("Boot secondary ROM");
			}
		}
	}
	
	public void boot(View v) throws IOException, InterruptedException{
		File sdcard = Environment.getExternalStorageDirectory();
		File rootFile = new File(sdcard, ".bootsdcard");
		Process p;
		if(rootFile.exists()){
			rootFile.delete();
		} else {
			p = Runtime.getRuntime().exec("touch /sdcard/.bootsdcard");
			//DataOutputStream os = new DataOutputStream(p.getOutputStream());
			//os.writeBytes("touch /sdcard/.bootsdcard");
			//os.flush();
			p.waitFor();
		}
		
		CheckBox isReboot = (CheckBox) findViewById(R.id.checkReboot);
		if(isReboot.isChecked()){
			new fuck().execute();
			//p.waitFor();
		    
		}
	}
	
	public class fuck extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... v){
			System.out.println("Trying to reboot!");
			Process p;
			try {
				p = Runtime.getRuntime().exec("su");
				
				p.waitFor();
				p = Runtime.getRuntime().exec("reboot");
				p.waitFor();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(MainActivity.this, (CharSequence) e, Toast.LENGTH_SHORT).show();
			}
			
			return null;
		}
	}

}
