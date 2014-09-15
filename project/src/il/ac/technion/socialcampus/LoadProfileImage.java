package il.ac.technion.socialcampus;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
	ImageView bmImage;
	Bitmap m_iconBitmap;
	
	// Profile pic image size in pixels
	private static final int PROFILE_PIC_SIZE = 400;

	public LoadProfileImage(ImageView bmImage, Bitmap iconBitmap) {
		this.bmImage = bmImage;
		this.m_iconBitmap = iconBitmap;
	}

	protected Bitmap doInBackground(String... urls) {
		String urldisplay = urls[0];
		// by default the profile url gives 50x50 px image only
		// we can replace the value with whatever dimension we want by
		urldisplay = urldisplay.substring(0,
				urldisplay.length() - 2)
				+ PROFILE_PIC_SIZE;
		Bitmap mIcon11 = null;
		try {
			InputStream in = new java.net.URL(urldisplay).openStream();
			mIcon11 = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return mIcon11;
	}

	protected void onPostExecute(Bitmap result) {
		bmImage.setImageBitmap(result);
		m_iconBitmap = result;
	}
}
