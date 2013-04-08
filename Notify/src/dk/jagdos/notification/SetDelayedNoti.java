package dk.jagdos.notification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SetDelayedNoti extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int i =  super.onStartCommand(intent, flags, startId);
		int id = intent.getIntExtra("notiid", NotificationCreator.NEXT);
		String text = intent.getStringExtra("text");
		
		//DEBUG
//		Toast.makeText(getApplicationContext(), "Added "+ text + " (" + id + ")", Toast.LENGTH_SHORT).show();

		NotificationCreator.createNotification(text, id, getApplicationContext());
		
		stopSelf();
		
		return i;
	}
	
}
