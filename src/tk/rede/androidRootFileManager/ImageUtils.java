package tk.rede.androidRootFileManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ImageUtils {
	public static ImageView rotateBitmap(Bitmap bitmapOrg,Context context) {
		// createa matrix for the manipulation
        Matrix matrix = new Matrix();
        // rotate the Bitmap
        matrix.postRotate(180);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,bitmapOrg.getWidth(),bitmapOrg.getHeight(),matrix, true);
        // make a Drawable from Bitmap to allow to set the BitMap
        // to the ImageView, ImageButton or what ever
        BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
        ImageView imageView = new ImageView(context);
        // set the Drawable on the ImageView
        imageView.setImageDrawable(bmd);
        // center the Image
        imageView.setScaleType(ScaleType.CENTER);
		return imageView;
	}
	
	public static BitmapDrawable rotateBitmap(Bitmap bitmapOrg,Integer radious) {
		// createa matrix for the manipulation
        Matrix matrix = new Matrix();
        // rotate the Bitmap
        matrix.postRotate(radious);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,bitmapOrg.getWidth(),bitmapOrg.getHeight(),matrix, true);
        // make a Drawable from Bitmap to allow to set the BitMap
        // to the ImageView, ImageButton or what ever
        BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
		return bmd;
	}
	
	public static Bitmap resizeBitmap(Bitmap bitmap,float newWidth,float newHeight){
	    Matrix matrix = new Matrix();
	    int width = bitmap.getWidth();
	    int height = bitmap.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    matrix.postScale(scaleWidth, scaleHeight);
	    return Bitmap.createBitmap(bitmap,0,0,width,height,matrix,false);
	}
}
