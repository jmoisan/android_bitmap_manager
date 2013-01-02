package test.bm;

import android.graphics.Bitmap;
import android.graphics.RectF;

public class imageWrapper
{
	public Bitmap bitmap;
	public boolean on;
	public RectF dest;

	public imageWrapper(Bitmap x, boolean y, RectF z) {
		bitmap = x;
		on = y;
		dest = z;
	}
}
