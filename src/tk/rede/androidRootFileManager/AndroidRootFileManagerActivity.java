package tk.rede.androidRootFileManager;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class AndroidRootFileManagerActivity extends Activity {
	
	public static String target;
	private Boolean cut;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        launchFileExploration(Environment.getExternalStorageDirectory());
        Button backBtn = (Button)findViewById(R.id.backButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				TextView textViewPath = (TextView)findViewById(R.id.pathTextView);
				String path = textViewPath.getText().toString();
				if(path.contains("/") && path.length()>1){
					if(path.indexOf("/")!=path.lastIndexOf("/")){
						path = path.substring(0,path.lastIndexOf("/"));
					}else{
						path = "/";
					}
				}
				updateBrowser(path);
			}
		});
        Button sdcardBtn = (Button)findViewById(R.id.sDCardButton);
        sdcardBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v){
				String path = Environment.getExternalStorageDirectory().getAbsolutePath();
				updateBrowser(path);
			}
		});
        Button reloadBtn = (Button)findViewById(R.id.reloadButton);
        reloadBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v){
				TextView textViewPath = (TextView)findViewById(R.id.pathTextView);
				updateBrowser(textViewPath.getText().toString());
			}
		});
    }

    private void updateBrowser(String path) {
    	TextView permissionsTextView = (TextView)findViewById(R.id.permissionsTextView);
    	permissionsTextView.setText(AndroidFile.getLocalPermissions());
		TextView textViewPath = (TextView)findViewById(R.id.pathTextView);
		textViewPath.setText(path);
		ListView listView = (ListView)findViewById(R.id.listView1);
		listView.setAdapter(new FileAdapter(AndroidRootFileManagerActivity.this,AndroidFile.getAnAndroidFile(path)));
		listView.setOnItemClickListener(new FileOnItemClickListener(AndroidRootFileManagerActivity.this));
		listView.setOnCreateContextMenuListener(new DisplayMenuOnCreateContextMenuListener());
	}
    

	private void launchFileExploration(File file) {
		if(file.canRead() && file.isDirectory()) {
			ListView listView = (ListView)findViewById(R.id.listView1);
			listView.setAdapter(new FileAdapter(this,AndroidFile.getAnAndroidFile(file.getAbsolutePath())));
			listView.setOnItemClickListener(new FileOnItemClickListener(this));
			TextView textViewPath = (TextView)findViewById(R.id.pathTextView);
			textViewPath.setText(file.getAbsolutePath());
			listView.setOnCreateContextMenuListener(new DisplayMenuOnCreateContextMenuListener());
//			listView.setTag(file.getAbsolutePath());
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
//		View listViewItem = this.findViewById(id);
//		String path = (listViewItem.getTag()).toString();
		String path = DisplayMenuOnCreateContextMenuListener.target;
        if(item.getTitle().equals("Cut")){ //Cut
        	target = path;
        	cut = true;
        }else if(item.getTitle().equals("Copy")){ //Copy
        	target = path;
        	cut = false;
        }else if(item.getTitle().equals("Paste")){ //Paste
        	if(cut!=null){
        		if(cut){
        			AndroidFile.cutAndPasteAPath(target,path);
        		}else{
        			AndroidFile.copyAndPasteAPath(target,path);
        		}
        		target = null;
            	cut = null;
        	}
        }else if(item.getTitle().equals("Rename")){
        	final EditText editText = new EditText(this);
        	editText.setText(path);
        	new AlertDialog.Builder(this)
            .setView(editText)
            .setPositiveButton("Rename", new OnClickListener() {
                public void onClick(DialogInterface di, int arg1) {
                	AndroidFile.renameAPath(DisplayMenuOnCreateContextMenuListener.target,editText.getText().toString());
                	di.dismiss();
                }
            }).setNegativeButton("Cancel", new OnClickListener() { 
            	public void onClick(DialogInterface arg0, int arg1) {}
            }).show();
        }else if(item.getTitle().equals("Delete")){
        	AndroidFile.deleteAPath(path);
        }else if(item.getTitle().equals("Properties")){ //Properties
        	
        }
        updateBrowser(path.substring(0,path.lastIndexOf("/")));
		return true;  
	}  
}