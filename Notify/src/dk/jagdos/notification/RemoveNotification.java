package dk.jagdos.notification;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class RemoveNotification extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int i =  super.onStartCommand(intent, flags, startId);
		int id = intent.getIntExtra("notiid", -1);
		//DEBUG
//		Toast.makeText(getApplicationContext(), "remove id: "+ (id), Toast.LENGTH_SHORT).show();

		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(id);

		stopSelf();
		
		return i;
	}
	
}
