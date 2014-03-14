package tk.rede.androidRootFileManager;

import java.io.File;

import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;

public class DisplayMenuOnCreateContextMenuListener implements OnCreateContextMenuListener{

	public static String target;

	public void onCreateContextMenu(ContextMenu menu,View v,ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		AndroidFile file = FileAdapter.getFile(new Long(info.id).intValue());
		target = file.getPath()+File.separatorChar+file.getName();
		menu.add(0,v.getId(),0,"Cut");
		menu.add(0,v.getId(),1,"Copy");
		if(AndroidRootFileManagerActivity.target!=null){
			menu.add(0,v.getId(),2,"Paste");
		}
	    menu.add(1,v.getId(),3,"Rename");
	    menu.add(1,v.getId(),4,"Delete");
	    menu.add(2,v.getId(),5,"Properties");
	}
}
