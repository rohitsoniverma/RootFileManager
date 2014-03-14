package tk.rede.androidRootFileManager;

import java.io.File;
import java.net.URI;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FileOnItemClickListener implements OnItemClickListener{

	private Context context;

	public FileOnItemClickListener(Context context) {
		this.context = context;
	}

	public void onItemClick(AdapterView<?> adapter, View v, int pos, long id) {
		Object object = v.getTag();
		if(object instanceof String){
			String path = (String)object;
			if(path.contains(".")&&(path.length()-path.lastIndexOf(".")==4)){
				Intent intent = new Intent();
				intent.setAction(android.content.Intent.ACTION_VIEW);
				if(path.endsWith("mp3")){
					intent.setDataAndType(Uri.parse("file://"+path), "audio/mp3");
				}else if(path.endsWith("mp4")){
					intent.setDataAndType(Uri.parse("file://"+path),"video/mp4");
				}else if(path.endsWith("apk")){
					intent.setDataAndType(Uri.parse("file://"+path),"application/vnd.android.package-archive");
				}
				context.startActivity(intent); 
			}else{
				if(path.endsWith("..")){
					path = path.replace("..","");
					if(path.endsWith(File.separator) && path.length()>1){
						path = path.substring(0,path.lastIndexOf(File.separator));
					}
				}
				if(path.contains(File.separator+File.separator)){
					path = path.replace(File.separator+File.separator,File.separator);
				}
				View rootView = v.getRootView();
				TextView pathTextView = (TextView)rootView.findViewById(R.id.pathTextView);
				pathTextView.setText(path);
				ListView listView = (ListView)rootView.findViewById(R.id.listView1);
				listView.setAdapter(new FileAdapter(context,AndroidFile.getAnAndroidFile(path)));
				listView.setOnItemClickListener(new FileOnItemClickListener(context));
			}
		}
	}
	
}