package dk.jagdos.notification;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.viewpagerindicator.TitlePageIndicator;

import dk.jagdos.notification.InFragment.PickCallback;

public class Main extends FragmentActivity implements OnClickListener,
		PickCallback {

	private Button cancel;
	private Button remind;
	private EditText remindText;
	private Calendar notyCal;
	private ViewPager pager;
	private TitlePageIndicator titlePager;
	boolean pickDateTime = false;
	private	PendingIntent pendingDelay;
	
	private Calendar time = Calendar.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.dialog);
		//Dialog fix
		getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);

		//pager and titlepager for the fragment
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(new ClockAdapter(getSupportFragmentManager(), this));

		titlePager = (TitlePageIndicator) findViewById(R.id.indicator);
		titlePager.setViewPager(pager);

		titlePager.setTextColor(0x8833b5e5);
		titlePager.setSelectedColor(Color.parseColor("#33b5e5"));

		cancel = (Button) findViewById(R.id.cancel);
		remind = (Button) findViewById(R.id.remind);

		remindText = (EditText) findViewById(R.id.remindtext);
		//Show keyboard 
		remind.requestFocus();
		getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);

		//Incoming edit intent
		String text = getIntent().getStringExtra("text");
		if (text != null) {
			remindText.setText(text);
		}

		//set listeners
		cancel.setOnClickListener(this);
		remind.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) { //not really used actually
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onClick(View v) { //Button clicks

		int page = pager.getCurrentItem();

		switch (v.getId()) {
		case R.id.remind:

			//Recieve the notification id, if we are editing a old notification, or set it to NEXT (meaning new notificaiton)
			int notifyID = getIntent().getIntExtra("notiid", NotificationCreator.NEXT);

			//Alarmmanager for delayed intents
			AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

			//The delayed intent contains the message and the id it should use/replace
			Intent delayIntent = new Intent(this, SetDelayedNoti.class);
			delayIntent.putExtra("text", remindText.getText().toString());
			delayIntent.putExtra("notiid", NotificationCreator.NEXT);
			//Super ugly hack for creating unique notifications
			delayIntent.setData((Uri.parse("foobar://"
					+ SystemClock.elapsedRealtime())));

			pendingDelay = PendingIntent.getService(this, 0,
					delayIntent, 0);
			
			//Switch on fragment pages
			switch (page) {
			case 0: //Now tab
				
				NotificationCreator.createNotification(remindText.getText()
						.toString(), notifyID, this);

				finish();
				break;

			case 1: //IN tab
				time.setTimeInMillis(System.currentTimeMillis());
				time.add(Calendar.HOUR_OF_DAY,
						notyCal.get(Calendar.HOUR_OF_DAY));
				time.add(Calendar.MINUTE, notyCal.get(Calendar.MINUTE));

				am.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(),
						pendingDelay);

				//If we are updating, then cancel the notification and requeue it
				((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(notifyID);

				//leading zeros
				String remindHour = String.format("%02d", notyCal.get(Calendar.HOUR_OF_DAY));
				String remindMin = String.format("%02d", notyCal.get(Calendar.MINUTE));

				Toast.makeText(this, "To be reminded in " +remindHour+":"+remindMin, Toast.LENGTH_LONG).show();
				
				finish();
				
				break;
			case 2: //AT tab
				time.setTime(notyCal.getTime());
				
				//Show popup for selecting specific time, which handles the rest
				TimePickerFragment timePickFrag = new TimePickerFragment();
//				timePickFrag.setCallback(this);
				timePickFrag.show(getSupportFragmentManager(), "timepicker");
				pickDateTime = true;
				
				((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(notifyID);

				break;

			default:
				break;
			}

			break;

		case R.id.cancel:
			break;

		default:
			break;
		}
		
	}

	@Override
	public void timePicked(Calendar pickedCal) {
		notyCal = pickedCal;
		
		if (pickDateTime) { //Callback initiated when setting time in AT tab
			time.set(Calendar.HOUR_OF_DAY,
					notyCal.get(Calendar.HOUR_OF_DAY));
			time.set(Calendar.MINUTE, notyCal.get(Calendar.MINUTE));

			AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			am.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(),
					pendingDelay);
			
			//Format based on users prefs
			String dateString = DateFormat.getLongDateFormat(this).format(time.getTime());
			String timeString = DateFormat.getTimeFormat(this).format(time.getTime());

			Toast.makeText(this, "To be reminded at " +timeString + ", " + dateString, Toast.LENGTH_LONG).show();
			
			finish();
		}
	}

}
