package il.ac.technion.socialcampus;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class CreateNewHotSpotActivity extends FragmentActivity {

	static String HotSpotId = "hotspotid";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_hot_spot);
		
		if(savedInstanceState==null){
			Long id = (Long) getIntent().getLongExtra(HotSpotId,CreateHotSpotFragment.defHotSpotID);
			CreateHotSpotFragment f = (CreateHotSpotFragment) 
					CreateHotSpotFragment.newInstance(id);
			
			getSupportFragmentManager().beginTransaction().add(R.id.frame, f).commit();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_new_hot_spot, menu);
		return true;
	}
}
