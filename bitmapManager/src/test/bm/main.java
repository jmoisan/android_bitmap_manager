package test.bm;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Rect;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.graphics.RectF;
import android.content.Context;
import android.content.res.Resources;



public class main extends Activity
{	
	gameView v;
	int screenWidth;
	int screenHeight;
	bitmapManager bm;
	Resources res;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		
		//get screen dimensions
		Display display = getWindowManager().getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();

		//create gameview
		v = new gameView(this);
		setContentView(v);

		res = getResources();
		bm = new bitmapManager(res, screenWidth, screenHeight);
		bm.testrun();
    }

	@Override
	public void onPause() {
		super.onPause();
		v.pause();
	}

	@Override
	public void onResume() {
		super.onResume();
		v.resume();
	}
	

	public class gameView extends SurfaceView implements Runnable {

		Thread t = null;
		SurfaceHolder holder;
		boolean isItOk = false;
		Canvas c, bitmapCanvas;
		Bitmap bufferBitmap;
		RectF fullView;

		public gameView(Context context) {
			super(context);
			holder = getHolder();
			fullView = new RectF(0, 0, screenWidth, screenHeight);
			bufferBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);

			//set canvas to bitmap
			bitmapCanvas = new Canvas(bufferBitmap);
		}

		public void run() {
			while (isItOk) {
				if (!holder.getSurface().isValid())
					continue;

				//draw to bufferBitmap
				bm.draw(bitmapCanvas);

				//draw bufferBitmap to screen canvas
				c = holder.lockCanvas();
				c.drawBitmap(bufferBitmap, null, fullView, null);
				holder.unlockCanvasAndPost(c);
			}
		}

		public void pause() {
			isItOk = false;

			while (true) {
				try {
					t.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			}
			t = null;
		}

		public void resume() {
			isItOk = true;
			t = new Thread(this);
			t.start();
		}
	}
}
