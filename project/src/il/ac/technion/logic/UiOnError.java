package il.ac.technion.logic;


import il.ac.technion.socialcampus.R;
import android.content.Context;
import android.widget.Toast;

public class UiOnError{
	Context c;
	String msg; 
	int resid = 0;
	
	public UiOnError(Context c){
		this.c = c;
		msg = c.getResources().getString(R.string.app_connection_erro_msg);
	}
	
	public UiOnError(Context c, String msg){
		this.c = c;
		this.msg = msg;
	}
	
	public UiOnError(Context c, int resid){
		this.c = c;
		this.resid = resid;
	}
	public void execute(){
		if(resid==0)
			Toast.makeText(c, msg , Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(c, resid , Toast.LENGTH_SHORT).show();
	}
}