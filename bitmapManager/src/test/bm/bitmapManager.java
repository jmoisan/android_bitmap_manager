package test.bm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import java.util.HashMap;
import android.os.AsyncTask;
import java.lang.ref.WeakReference;
import android.util.Log;

public class bitmapManager
{
	private HashMap<Integer, imageWrapper> map = new HashMap();
	private int screenWidth, screenHeight;
	private Resources res;

	//constructor
	public bitmapManager(Resources resources, int width, int height) {
		screenWidth = width;
		screenHeight = height;
		res = resources;
	}
	
	private void loadBitmap(int resId, int width, int height, int x, int y, boolean startState) {
		BitmapWorkerTask task = new BitmapWorkerTask(width, height, x, y, startState, map);
		task.execute(resId);
	}

	public void testrun() {
		loadBitmap(R.drawable.gamebk, screenWidth, screenHeight/2, 0, 0, true);
		loadBitmap(R.drawable.gamebk, screenWidth, screenHeight/2, 0, screenHeight/2, true);
	}

	public void draw(Canvas c) {
		if (map != null) {
			for (imageWrapper value : map.values()) {
				if (value.on)
					c.drawBitmap(value.bitmap, null, value.dest, null);
			}
		}
	}


	class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {

		private int width, height, x, y, resId;
		private boolean startState;
		private final WeakReference<HashMap<Integer, imageWrapper> > mapRef;
	
	    public BitmapWorkerTask(int Width, int Height, int X, int Y, boolean ss, HashMap<Integer, imageWrapper> map) {
			startState = ss;
			width = Width;
			height = Height;
			x = X;
			y = Y;
			mapRef = new WeakReference<HashMap<Integer, imageWrapper> >(map);
	    }

	    // Decode image in background.
	    @Override
	    protected Bitmap doInBackground(Integer... params) {
			resId = params[0];
			return BitmapFactory.decodeResource(res, resId, null);
	    }

	    // Once complete, set bitmap in UI thread.
	    @Override
	    protected void onPostExecute(Bitmap outBitmap) {
			Log.d("asdf", "ONPOSTEXE");
			RectF dest = new RectF(x, y, x + width, y + height);
			imageWrapper imwrap = new imageWrapper(outBitmap, startState, dest);
			final HashMap<Integer, imageWrapper> map = mapRef.get();
			map.put(resId, imwrap);
	    }
	}
}
