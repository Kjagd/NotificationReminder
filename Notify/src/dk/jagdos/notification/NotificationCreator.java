package dk.jagdos.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class NotificationCreator {

	public static int NEXT = -1; //get a new notification ID
	
	public static void createNotification(String text, int setID, Context context) {
		
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		
		int id;
		if(setID == NEXT) { //Increment id 
			id = prefs.getInt("notiidCount", 0) + 1;
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt("notiidCount", id);
			editor.commit();
		} else {
			id = setID; //Updating
		}

		Intent clearIntent = new Intent(context, RemoveNotification.class); //Intent for when user clears
		clearIntent.putExtra("notiid", id);
		//hacky hacky
		clearIntent.setData((Uri.parse("foobar://"
				+ SystemClock.elapsedRealtime())));

		Intent dataIntent = new Intent(context, Main.class);
		dataIntent.putExtra("text", text);
		dataIntent.putExtra("notiid", id);
		//yum yum
		dataIntent.setData((Uri.parse("foobar://" + SystemClock.elapsedRealtime())));

		//the notification juice
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context)
				.setSmallIcon(R.drawable.updatewhite)
				.setContentTitle(text)
				.setContentText("Reminder")
				.addAction(
						R.drawable.edit,
						"Edit",
						PendingIntent.getActivity(context,
								0, dataIntent, 0))
				.addAction(
						R.drawable.accept,
						"Clear",
						PendingIntent.getService(context,
								0, clearIntent, 0));

		mBuilder.setContentIntent(PendingIntent.getActivity(
				context, 0, dataIntent,
				PendingIntent.FLAG_UPDATE_CURRENT));
		Notification noti = mBuilder.build();
		noti.flags = Notification.FLAG_ONGOING_EVENT; //Don't go away with normal clears
		
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//Debug
//		Toast.makeText(context, "create id: " + id, 
//				Toast.LENGTH_SHORT).show();
		
		mNotificationManager.notify(id, noti);
	}
}
