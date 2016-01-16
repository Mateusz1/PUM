package com.example.pum2016;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.example.pum2016.asynctasks.DataBaseTask;
import com.example.pum2016.asynctasks.DataBaseTaskBoolean;
import com.example.pum2016.asynctasks.DataBaseTaskNonQuery;
import com.example.pum2016.asynctasks.DataBaseTaskSetParameters;
import com.example.pum2016.klasy.TableOptionsObject;
import com.example.pum2016.queries.QueriesStatic;
import com.example.pum2016.service.ServiceReceiver;

import android.support.v7.app.ActionBarActivity;
import android.telephony.*;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	private boolean ifPass=false;
	private String deviceId;
	private int whichScreen=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		TelephonyManager tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);	
		deviceId = tm.getDeviceId();
		SprawdzHaslo task = new SprawdzHaslo();
		task.execute(new String[]{QueriesStatic.getCheckpasswordquery()+"'"+deviceId+"'","1"});
		
		Button login = (Button) findViewById(R.id.trylogin);
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText pass = (EditText) findViewById(R.id.password);
				TryLogin task = new TryLogin();
				task.execute(new String[]{QueriesStatic.getSelectpassword()+"'"+deviceId+"'",pass.getText().toString()});
			}
		});
	}

	private void scheduleAlarm() {
		Toast.makeText(getApplicationContext(), "Us�uga zosta�a uruchomiona.", Toast.LENGTH_SHORT).show();
		// Construct an intent that will execute the AlarmReceiver
	    Intent intent = new Intent(getApplicationContext(), ServiceReceiver.class);
	    // Create a PendingIntent to be triggered when the alarm goes off
	    final PendingIntent pIntent = PendingIntent.getBroadcast(this, ServiceReceiver.REQUEST_CODE,
	        intent, PendingIntent.FLAG_UPDATE_CURRENT);
	    // Setup periodic alarm every 5 seconds
	    long firstMillis = System.currentTimeMillis(); // alarm is set right away
	    AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
	    // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
	    // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
	    alarm.setInexactRepeating(AlarmManager.RTC, firstMillis, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pIntent);
		
	}
	private void cancelAlarm() {
	    Intent intent = new Intent(getApplicationContext(), ServiceReceiver.class);
	    final PendingIntent pIntent = PendingIntent.getBroadcast(this, ServiceReceiver.REQUEST_CODE,
	       intent, PendingIntent.FLAG_UPDATE_CURRENT);
	    AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
	    alarm.cancel(pIntent);
	}
	@Override
	public void onBackPressed() {
		switch(whichScreen) {
			case 2:
				whichScreen=1;
				setContentView(R.layout.logged_buttons);
				break;
			default:
				break;
			
		}
	}
	
	public void selectFunct(View view){
		switch(view.getId()) {
			case R.id.start:
				scheduleAlarm();
				break;
			case R.id.przerwa:
				break;
			case R.id.zakoncz:
				zakonczTrase();
				break;
			case R.id.zgloszenie:
				goToNextView();
				break;
			default:
				break;
		}
	}
	
	private void zakonczTrase() {
		cancelAlarm();
		SendTextToDatabase task = new SendTextToDatabase();
		String sql = QueriesStatic.getUpdate()+TableOptionsObject.getInstance().getPrefix()
				+QueriesStatic.getUpdate2()+"'"+getData()+"'"+QueriesStatic.getUpdate3()+TableOptionsObject.getInstance().getId_trasy();
		task.execute(new String[]{sql});
		
	}

	public void selectFunctSendView(View view){
		switch(view.getId()) {
			case R.id.wypadek:
				sendInformation("Wypadek.");
				break;
			case R.id.korek:
				sendInformation("Korek.");
				break;
			case R.id.objazd:
				sendInformation("Nieplanowany objazd ze wzgl�du na remont.");
				break;
			case R.id.postoj:
				sendInformation("Post�j spowodowany przepisami np. �wi�to.");
				break;
			case R.id.inne:
				showSendLayout();
				break;
			default:
				break;
		}
	}
	
	private void sendInformation(String wiadomosc) {
		SendTextToDatabase task = new SendTextToDatabase();
		String sql = QueriesStatic.getInsertevent1()+TableOptionsObject.getInstance().getPrefix()
				+QueriesStatic.getInsertevent2()+"("+TableOptionsObject.getInstance().getId_trasy()+",'"+wiadomosc+"','"+getData()+"')";
		task.execute(new String[]{sql});
		
	}

	private void showSendLayout() {
		LayoutInflater inflaterDialog = MainActivity.this.getLayoutInflater();
		final View dialogView = inflaterDialog.inflate(R.layout.send_information, null);		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("Podaj zdarzenie");
		builder.setView(dialogView);
		builder.setPositiveButton(R.string.possitiveSetInfo, null); 
		
		
		final AlertDialog dialog2 = builder.create();
		dialog2.setOnShowListener(new DialogInterface.OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				Button b = dialog2.getButton(AlertDialog.BUTTON_POSITIVE);
				b.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						boolean dismiss=true;
		            	final EditText p1 = (EditText) dialogView.findViewById(R.id.informacja);
						if(dismiss) dialog2.dismiss();
						sendInformation(p1.getText().toString());
					}
				});
			}
		});
		dialog2.show();
	}
	
	private String getData() {

		// Parse the input date
		SimpleDateFormat fmt = new SimpleDateFormat("MM-dd-yyyy HH:mm");
		Date inputDate = new Date();

		// Create the MySQL datetime string
		fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = fmt.format(inputDate);
		return dateString;
	}

	private void goToNextView() {
		whichScreen=2;
		setContentView(R.layout.send_info);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_off) {
			cancelAlarm();
			System.exit(0);
		}
		return super.onOptionsItemSelected(item);
	}
	private class SendTextToDatabase extends DataBaseTaskNonQuery {
		@Override
		protected void onPostExecute(Integer result) {
			switch(result) {
			case 0:
				Toast.makeText(getApplicationContext(), "Nie uda�o si� wys�a� informacji.", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(getApplicationContext(), "Informacja zosta�a wys�ana.", Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(getApplicationContext(), "Nie obs�ugiwany wynik.", Toast.LENGTH_SHORT).show();
				break;				
			}
		}
	}
	
	private class SprawdzHaslo extends DataBaseTask {
		@Override
		protected void onPostExecute(List<String[]> result) {
			for(String[] a : result) {
				if(a[0]==null || a[0].isEmpty()) {
					//logowanie po raz pierwszy
					showSetPasswordWindow();
				}
				else {
					ifPass=true;
				}
			}
		}

		private void showSetPasswordWindow() {
			LayoutInflater inflaterDialog = MainActivity.this.getLayoutInflater();
			final View dialogView = inflaterDialog.inflate(R.layout.set_password, null);		
			
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setTitle("Utw�rz has�o");
			builder.setView(dialogView);
			builder.setPositiveButton(R.string.possitiveSet, null); 
			
			
			final AlertDialog dialog2 = builder.create();
			dialog2.setOnShowListener(new DialogInterface.OnShowListener() {
				
				@Override
				public void onShow(DialogInterface dialog) {
					Button b = dialog2.getButton(AlertDialog.BUTTON_POSITIVE);
			        b.setOnClickListener(new View.OnClickListener() {

			            @Override
			            public void onClick(View view) {
			            	boolean dismiss = false;
			            	final EditText p1 = (EditText) dialogView.findViewById(R.id.newPassword);
							final EditText p2 = (EditText) dialogView.findViewById(R.id.confirmPassword);
								if (p1.getText().toString().equals(p2.getText().toString())) {
									dismiss=true;
									SetPassword task = new SetPassword();
									String query = QueriesStatic.getInsertpassword1()+"'"+p1.getText().toString()+"'"+QueriesStatic.getInsertpassword2()+"'"+deviceId+"'";
									task.execute(new String[]{query});
								}
								else {
									dismiss = false;
									Toast.makeText(getApplicationContext(), "Podane has�a nie pasuj� do siebie", Toast.LENGTH_SHORT).show();
								}
								if(dismiss) dialog2.dismiss();
			            }
			        });
					
				}
			});
			dialog2.show();
		}
	}
	private class SetPassword extends DataBaseTaskNonQuery {
		@Override
		protected void onPostExecute(Integer result) {
			switch(result) {
			case 0:
				Toast.makeText(getApplicationContext(), "Nie uda�o si� ustanowi� has�a", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				ifPass=true;
				Toast.makeText(getApplicationContext(), "Has�o zosta�o ustawione.", Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(getApplicationContext(), "Nie obs�ugiwany wynik.", Toast.LENGTH_SHORT).show();
				break;				
			}
		}
	}
	private class TryLogin extends DataBaseTaskBoolean {
		@Override
		protected void onPostExecute(Boolean result) {
			if(result && ifPass) {
				View view = MainActivity.this.getCurrentFocus();
				if (view != null) {  
				    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				}
				GetTableParameters zadanie = new GetTableParameters();
				String query = QueriesStatic.getAfterlogin1()+"'"+deviceId+"'";
				zadanie.execute(new String[]{query});
			}
			else {
				Toast.makeText(getApplicationContext(), "Logowanie nie powiod�o si�.", Toast.LENGTH_SHORT).show();				
			}
		}
	}

	private class GetTableParameters extends DataBaseTaskSetParameters {
		@Override
		protected void onPostExecute(Boolean result) {
			if(result) {
				MainActivity.this.setContentView(R.layout.logged_buttons);
				whichScreen=1;
				Toast.makeText(getApplicationContext(), "Po��czono z systemem. Mo�na rozpocz�� prac�", Toast.LENGTH_SHORT).show();				
			}
			else {
				Toast.makeText(getApplicationContext(), "Nie uda�o si� po��czy� z systemem. Nast�pi ponowna pr�ba po��czenia.", Toast.LENGTH_SHORT).show();
				GetTableParameters zadanie = new GetTableParameters();
				String query = QueriesStatic.getAfterlogin1()+"'"+deviceId+"'";
				zadanie.execute(new String[]{query});
			}
		}
	}
}
