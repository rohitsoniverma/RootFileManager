package tk.rede.androidRootFileManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FileAdapter extends BaseAdapter {
	
	private static List<AndroidFile> files;
	private Context context;
	
	public FileAdapter(Context context) {
		this.context = context;
		this.files = new ArrayList<AndroidFile>();
	}
	
	public static List<AndroidFile> getFiles() {
		return files;
	}
	
	public static AndroidFile getFile(int position){
		return files.get(position);
	}

	public FileAdapter(Context context,File[] files,File parent){
		this.context = context;
		this.files = new ArrayList<AndroidFile>();
		if(parent!=null && parent.isDirectory()){
			this.files.add(new AndroidFile(parent));
		}
		for(File file: files){
			this.files.add(new AndroidFile(file));
		}
	}
	
	public FileAdapter(Context context,List<AndroidFile> files){
		this.context = context;
		this.files = new ArrayList<AndroidFile>();
		Iterator<AndroidFile> itFiles = files.iterator();
		while(itFiles.hasNext()){
			this.files.add(itFiles.next());
		}
	}

	public int getCount() {
		return files.size(); //To put return to parent
	}

	public Object getItem(int position) {
		return files.get(position%getCount());
	}

	public long getItemId(int position) {
		return position%(getCount());
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout relativeLayout = (RelativeLayout)((LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.file, null, false);
		AndroidFile file = files.get(position);
		TextView fileName = (TextView)relativeLayout.findViewById(R.id.nameFileTextView);
		String additional = "";
		if(file.getName().length()>0){
			additional = File.separatorChar+file.getName();
		}
		if(!file.getName().equals("..")){
			fileName.setText(file.getName());
		}else{
			fileName.setText("");
		}
		relativeLayout.setTag(file.getPath()+additional);
		TextView permisions = (TextView)relativeLayout.findViewById(R.id.permissionsFileTextView);
		permisions.setText(file.getPermisions());
		TextView sizeFile = (TextView)relativeLayout.findViewById(R.id.sizeFileTextView);
		String stringSize = file.getSize().trim();
		if(stringSize.length()>0){
			Float size = new Float(stringSize);
			String unit = "bytes";
			if(size/1024>1){
				size = size/1024;
				unit = "KB";
				if(size/1024>1){
					size = size/1;
					unit = "MB";
					if(size/1024>1){
						size = size/1024;
						unit = "GB";
					}
				}
			}
			stringSize = size.toString()+" "+unit;
		}
		sizeFile.setText(stringSize);
		ImageView imageFile = (ImageView)relativeLayout.findViewById(R.id.imageFileView);
		if(file.isFile()){
			relativeLayout.setBackgroundColor(Color.LTGRAY);
			if(file.getName().equals("..")){ //Back button
				BitmapDrawable bitmap = ImageUtils.rotateBitmap(ImageUtils.resizeBitmap(BitmapFactory.decodeResource(relativeLayout.getResources(), R.drawable.next),35,35),270);
				imageFile.setImageBitmap(bitmap.getBitmap());
				RelativeLayout rela2 = (RelativeLayout)relativeLayout.findViewById(R.id.relativeLayout2);
				rela2.setVisibility(View.GONE);
			}else if(file.getName().contains(".") && !file.getName().endsWith(".") && (file.getName().length()-file.getName().lastIndexOf(".")==4)){
				String extension = file.getName().substring(file.getName().lastIndexOf(".")+1).toLowerCase();
				if(extension.equals("mp3")){
					imageFile.setImageBitmap(ImageUtils.resizeBitmap(BitmapFactory.decodeResource(relativeLayout.getResources(), R.drawable.mp3),48,48));
				}else if(extension.equals("png") || extension.equals("jpg")){
					try{
						if(new File(relativeLayout.getTag().toString()).canRead()){
							imageFile.setImageBitmap(ImageUtils.resizeBitmap(BitmapFactory.decodeFile(relativeLayout.getTag().toString()),48,48));
						}
					}catch(Exception e){ //
						e.printStackTrace();
					}
				}else if(extension.equals("mp4")){
					imageFile.setImageBitmap(ImageUtils.resizeBitmap(BitmapFactory.decodeResource(relativeLayout.getResources(), R.drawable.mp4),48,48));
				}else if(extension.equals("pdf")){
					imageFile.setImageBitmap(ImageUtils.resizeBitmap(BitmapFactory.decodeResource(relativeLayout.getResources(), R.drawable.pdf),48,48));
				}
			}
		}else{
//			imageFile.setImageResource(R.drawable.icon_folder);
			Bitmap bitmap = BitmapFactory.decodeResource(relativeLayout.getResources(), R.drawable.icon_folder);
			bitmap = ImageUtils.resizeBitmap(bitmap,48,48);
			imageFile.setImageBitmap(bitmap);
			relativeLayout.setBackgroundColor(Color.GRAY);
		}
		return relativeLayout;
	}

}
