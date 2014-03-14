package tk.rede.androidRootFileManager;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AndroidFile{
	
	private String name;
	private String size;
	private String parentPath;
	private String permisions;
	private String propietary;
	private String group;
	private String date;
	private Boolean directory;
	private String path;
	
	private static List<AndroidFile> listAndroidFiles = null; //Needs to be static, it's used in more than one of all functionalities.
	private static Runtime runtime;
	private static Process process;
	
	public boolean isFile(){
		return !isDirectory();
	}
	public boolean isDirectory() {
		return (directory==null)?directory=permisions.startsWith("d"):directory;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getPropietary() {
		return propietary;
	}
	public void setPropietary(String propietary) {
		this.propietary = propietary;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getParentPath() {
		return parentPath;
	}
	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}
	public String getPermisions() {
		return (permisions==null||permisions.length()>0)?permisions.substring(1):permisions;
	}
	public void setPermisions(String permisions) {
		this.permisions = permisions;
	}

	public AndroidFile() {
		this.date = this.group = this.name = this.parentPath = this.permisions = this.propietary = this.size = "";
	}
	
	public AndroidFile(File file){
		this.date = "";
		this.group = "";
		this.name = file.getName();
		String path = file.getAbsolutePath();
		path = path.substring(0,path.length()-1);
		this.setPath(path.substring(0, path.lastIndexOf(File.separatorChar)));
		this.parentPath = path.substring(0, path.lastIndexOf(File.separatorChar));
		this.permisions = (file.canRead()?"r":"-")+(file.canWrite()?"r":"-")+"???????";
		this.permisions = "";
		this.size = new Long(file.length()).toString();
	}
	
	public AndroidFile(AndroidFile androidFile){
		this.date = androidFile.getDate();
		this.group = androidFile.getGroup();
		this.name = androidFile.getName();
		this.parentPath = androidFile.getParentPath();
		this.permisions = androidFile.getPermisions();
		this.propietary = androidFile.getPropietary();
		this.size = androidFile.getSize();
	}
	
	public AndroidFile(String date,String group,String name, String parentPath, String permisions,String propietary,String size){
		this.date = date;
		this.group = group;
		this.name = name;
		this.parentPath = parentPath;
		this.permisions = permisions;
		this.propietary = propietary;
		this.size = size;
	}
	
	public static File getAFile(String path){
		return new File(path);
	}

	public static void remount(String path, String permissions){
		File file = null;
		if(path!=null && path.length()>0 && (file = new File(path))!=null && !file.canRead() && permissions.length()==2){
			runtime = Runtime.getRuntime();
			try {
				Process process = runtime.exec(new String[]{"su","-c","busybox mount -o "+permissions+",remount "+path});
				try {
					process.waitFor();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
//				DataOutputStream os = new DataOutputStream(process.getOutputStream());
//				os.writeBytes("busybox mount -o "+permissions+",remount "+path+"\n");
//				os.writeBytes("exit\n");
//				os.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void cutAndPasteAPath(String target,String path){
		if(path.endsWith("..")){
			path = path.substring(0,path.lastIndexOf(File.separatorChar));
		}
		if(runtime==null){
			runtime = Runtime.getRuntime();
		}
		if(process==null){
			try {
				process = runtime.exec(new String[]{"su","-c","busybox mv "+target+" "+path});
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else{
			try {
				process = runtime.exec("mv "+target+" "+path);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void deleteAPath(String path){
		if(path.endsWith("..")){
			path = path.substring(0,path.lastIndexOf(File.separatorChar));
		}
		if(runtime==null){
			runtime = Runtime.getRuntime();
		}
		if(process==null){
			try {
				process = runtime.exec(new String[]{"su","-c","busybox rm -R "+path});
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else{
			try {
				process = runtime.exec("busybox rm -R "+path);
				process.waitFor();
			}catch (IOException e) {
				e.printStackTrace();
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void copyAndPasteAPath(String target,String path){
		if(path.endsWith("..")){
			path = path.substring(0,path.lastIndexOf(File.separatorChar));
		}
		if(runtime==null){
			runtime = Runtime.getRuntime();
		}
		if(process==null){
			try {
				process = runtime.exec(new String[]{"su","-c","busybox cp "+target+" "+path});
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else{
			try {
				process = runtime.exec("busybox cp "+target+" "+path);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static List<AndroidFile> getAnAndroidFile(String path){
		listAndroidFiles = new ArrayList<AndroidFile>();
		try {
			if(path.endsWith("..")){
				path = path.substring(0,path.lastIndexOf(File.separatorChar));
			}
			if(runtime==null){
				runtime = Runtime.getRuntime();
			}
			if(process==null){
				process = runtime.exec(new String[]{"su","-c","ls -la "+path});
//				DataOutputStream os = new DataOutputStream(process.getOutputStream());
//				os.writeBytes("ls -la "+path+"\n");
//				os.writeBytes("exit\n");
//				os.flush();
				try {
					process.waitFor();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
//				os.close();
			}else{
				process = runtime.exec("ls -la "+path);
				try {
					process.waitFor();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			String result = "";
		    DataInputStream input = new DataInputStream(process.getInputStream());
		    if(input.available()>0){
		    	StringBuffer buffer = new StringBuffer(input.readLine());
		    	while(input.available()>0){
		    		buffer.append("\n"+input.readLine());
		    	}
		    	result = buffer.toString();
		    	input.close();
		    }
		    if(result!=null && result.contains("\n")){
		    	String[] results = result.split("\n");
		    	for(int i=0;i<results.length;i++){
		    		parseAnFileResult(results[i],path);
		    	}
		    }else if(result.length()>1 && (result.startsWith("-") || result.startsWith("d"))){
		    	parseAnFileResult(result,path);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listAndroidFiles;
	}

	private static void parseAnFileResult(String result,String path) {
//		if(!result.endsWith(" ..") && !result.endsWith(" .")){
			AndroidFile androidFile = new AndroidFile();
			Integer lastIndex = result.indexOf(" ");
	    	String permisions = result.substring(0,lastIndex);
	    	result = result.substring(lastIndex+1);
	    	lastIndex = result.indexOf(" ");
	    	int i=0;
	    	for(;result.substring(lastIndex+i,lastIndex+i+1).equals(" ") || Character.isDigit(result.charAt(lastIndex+i));i++){
	    		result = result.substring(1);
	    	}
	    	lastIndex+=i-1;
	    	result = result.substring(lastIndex);
	    	String propietary = result.substring(0,result.indexOf(" "));
	    	result = result.substring(result.indexOf(" "));
	    	while(result.startsWith(" ")){
	    		result = result.substring(1);
	    	}
	    	String group = result.substring(0,result.indexOf(" "));
	    	String size = "";
	    	for(i=1;size.equals("") || Character.isDigit(result.charAt(lastIndex+i));i++){
	    		if(Character.isDigit(result.charAt(lastIndex+i))){
	    			size+=result.charAt(lastIndex+i);
	    		}
	    	}
	    	result = result.substring(result.indexOf(size)+size.length()+1);
	    	String date = "";
	    	if(!size.equals("")){
	    		String name = "";
	    		if(result.contains(":")){
	    			date=result.substring(0,result.indexOf(":")+3);
	    			name = result.substring(result.indexOf(":")+4);
	    		}else{
	    			i=0;
	    			while(!Character.isDigit(result.charAt(i))){
	    				date+=result.charAt(i++);
	    			}
	    			while(Character.isDigit(result.charAt(i))){
	    				date+=result.charAt(i++);
	    			}
	    			while(!Character.isDigit(result.charAt(i))){
	    				date+=result.charAt(i++);
	    			}
	    			while(Character.isDigit(result.charAt(i))){
	    				date+=result.charAt(i++);
	    			}
	    			while(result.substring(i,i+1).equals(" ")) {i++;}
	    			name = result.substring(i);
	    		}
	    		if(!name.equals(".") && !name.equals("..")){
	        		androidFile.setName(name);
	            	androidFile.setGroup(group);
	            	androidFile.setPropietary(propietary);
	            	if(!permisions.substring(0,1).equals("d")){
	            		androidFile.setSize(size);
	            	}
	            	androidFile.setPermisions(permisions);
	            	androidFile.setDate(date);
	            	androidFile.setPath(path);
	            	listAndroidFiles.add(androidFile);
	        	}else if(listAndroidFiles.size()==0 && path.length()>1){
	    			androidFile.setName("..");
	    			androidFile.setPath(path.substring(0,path.lastIndexOf(File.separatorChar)));
	    			listAndroidFiles.add(androidFile);
	    		}
	    	}
//		}
	}
	public static void renameAPath(String target, String path) {
		if(path.endsWith("..")){
			path = path.substring(0,path.lastIndexOf(File.separatorChar));
		}
		if(runtime==null){
			runtime = Runtime.getRuntime();
		}
		if(process==null){
			try {
				process = runtime.exec(new String[]{"su","-c","mv "+target+" "+path});
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else{
			try {
				process = runtime.exec("mv "+target+" "+path);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				process.waitFor();
				String result = "";
			    DataInputStream input = new DataInputStream(process.getInputStream());
			    if(input.available()>0){
			    	StringBuffer buffer = new StringBuffer(input.readLine());
			    	while(input.available()>0){
			    		buffer.append("\n"+input.readLine());
			    	}
			    	result = buffer.toString();
			    	input.close();
			    }
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String getLocalPermissions(){
		String result = "permissions";
		if(runtime==null){
			runtime = Runtime.getRuntime();
		}
		try {
			if (process == null) {
				process = runtime.exec(new String[] { "su", "-c", "busybox ls -ld" });
			} else {
				process = runtime.exec("busybox ls -ld");
			}
			process.waitFor();
			DataInputStream input = new DataInputStream(process.getInputStream());
		    if(input.available()>0){
		    	StringBuffer buffer = new StringBuffer(input.readLine());
		    	while(input.available()>0){
		    		buffer.append("\n"+input.readLine());
		    	}
		    	result = buffer.toString();
		    	input.close();
		    }
		    result = result.substring(0,result.indexOf(" "));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}