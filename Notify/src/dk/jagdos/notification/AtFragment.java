package dk.jagdos.notification;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import dk.jagdos.notification.InFragment.PickCallback;

public class AtFragment extends Fragment implements OnDateChangedListener {

	private Calendar cal = new GregorianCalendar();
	
	public static AtFragment newInstance() {
		return new AtFragment();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.atlayout, container, false);
		DatePicker picker = (DatePicker) view.findViewById(R.id.datepick);
		
		Calendar now = new GregorianCalendar();
		//Set to current date
		picker.init(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), this);
		
		//manual call..
		onDateChanged(picker, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
		
		return view;
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		
		cal.set(year, monthOfYear, dayOfMonth, 12, 0);
		//Callback to main activity (updating the calendar)
		((PickCallback) getActivity()).timePicked(cal);
		
	}

}
