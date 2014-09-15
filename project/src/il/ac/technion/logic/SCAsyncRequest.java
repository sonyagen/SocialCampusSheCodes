package il.ac.technion.logic;
import java.io.IOException;
import java.net.ConnectException;

import android.os.AsyncTask;


/*
 * Use this class to call server requests in a asynchronous manner
 * Supply CooliePriority in order to define the priority of your request
 * You should override actionOnServer , onResult and onProgress 
 * actionOnServer sends a requests to the server in a asynchronous manner
 * onResult gets the result back after the server has processed your request
 * onProgress handles the progressbar
 * 
 *  I made an example for usage.. one should create CoolieAsyncRequest, override the functions,
 *  and call the run() function
 *  actionOnServer calls the getUgCourse from the server
 *  onReslut gets the list of courses that was assigned by the server
 *  
 *   
 * 
		CoolieAsyncRequest c = new CoolieAsyncRequest(CooliePriority.IMMEDIATELY) {
		      
		    List<ServerCourse> list;

			@Override
			public Void actionOnServer(Void... params) {
				 SemesterSeason ss = SemesterSeason.WINTER;
				 Semester semester = new Semester(2011,ss);
				 list = UgFactory.getUgCourse().getAllCourses(semester);
				return null;
			}

			@Override
			public Void onResult(CoolieStatus status) {
				if (status==CoolieStatus.INTERNET_CONNECTION_FAILED)
				{
					Log.v("result", "no connection");
				}
				Log.v("result", list.toString());
				return null;
			}

		
		};
		
		c.run();
 * 
 * 
 */

public abstract class SCAsyncRequest
{

	private final class LEAsyncWorker extends AsyncTask<Void, Void, SCConnectionStatus> {
		@Override
		final protected SCConnectionStatus doInBackground(Void... params) {
			
			try{
				actionOnServer(params);
			}
			
			catch(ConnectException tx){
				
				return SCConnectionStatus.INTERNET_CONNECTION_FAILED;
			} 
			
			catch(IOException t){
				
				return SCConnectionStatus.SERVER_IS_UNREACHABLE;
			}
			catch(Throwable th){
				
				return SCConnectionStatus.RESULT_FAIL;
			} 
		
			
		
			return SCConnectionStatus.RESULT_OK;
		}

		@Override
		protected void onPostExecute(SCConnectionStatus status) {
			onResult(status);
			super.onPostExecute(status);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
			onProgress(values);
			
		}

		public void publish()
		{
			publishProgress();
		}
	}

	SCPriority mPriority;
	private  LEAsyncWorker mAsyncTask;

	public SCAsyncRequest(SCPriority priority)
	{
		
		mPriority = priority;
		mPriority = SCPriority.IMMEDIATELY;
		mAsyncTask = new LEAsyncWorker();
	}
	

	
	abstract public Void actionOnServer(Void... params) throws IOException,ConnectException;
	abstract public Void onResult(SCConnectionStatus status);
	public Void onProgress(Void... values){
		return null;
	}
	
	public void run()
	{
		mAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	public final void publishProgress()
	{
		mAsyncTask.publish();
	}
}
