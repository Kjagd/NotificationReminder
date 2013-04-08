package dk.jagdos.notification;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class InFragment extends Fragment implements OnTimeChangedListener {

	private Calendar cal = new GregorianCalendar();
	
	public interface PickCallback { //just chilling here...
		void timePicked(Calendar pickedCal);
	}
	
	public static InFragment newInstance() {
		return new InFragment();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.inlayout, container, false);
		TimePicker picker = (TimePicker) view.findViewById(R.id.timePicker);
		picker.setIs24HourView(true); //oh noes, I have to change this for americans :)
		picker.setOnTimeChangedListener(this);
		picker.setCurrentHour(1);
		picker.setCurrentMinute(0);
		
		return view;
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

		cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
		cal.set(Calendar.MINUTE, minute);
		
		//callback
		((PickCallback) getActivity()).timePicked(cal);
		
	}
}
