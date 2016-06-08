package com.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class FTPUploader extends Thread{
	private boolean isActive = true;
	private FTPSettings settings;
	private List<QueMessage> que = new ArrayList<QueMessage>();
	private FTPClient client;

	public FTPUploader(FTPSettings settings){
		this.settings = settings;
		client = new FTPClient();
	}

	public void addToQue(QueMessage message){
		que.add(message);
	}

	public void close(){
		isActive = false;
		try{
			this.join();
		}catch(Exception e){
		}
	}

	public void run(){
		while(isActive || !que.isEmpty()){
			try{
				boolean sleep = false;
				if(que.isEmpty()){
					sleep = true;
				}
				if(sleep){
					Thread.sleep(500);
					continue;
				}
				QueMessage current = que.get(0);
				if(current.getStart()!=null){
					current.getStart().run();
				}
				que.remove(0);
				client.connect(settings.getHost(), settings.getPort());
				client.login(settings.getUser(), settings.getPass());
				client.enterLocalPassiveMode();
				client.setFileType(FTP.BINARY_FILE_TYPE);
				File file = current.getFile();
				if(!file.exists()){
					System.out.println("File "+file.getAbsolutePath()+" doesn't exists, no upload");
				}
				String target = current.getTarget();
				client.makeDirectory(current.getFolder());
				FileInputStream inputStream = new FileInputStream(file);
				OutputStream output = client.storeFileStream(current.getFolder()+"/"+target);
				byte[] bytes = new byte[2048];
				int read = 0;
				while ((read = inputStream.read(bytes)) != -1) {
					output.write(bytes, 0, read);
				}
				output.close();
				inputStream.close();
				client.completePendingCommand();
				if(current.getEnd()!=null){
					current.getEnd().run();
				}
				if(client.isConnected()){
					client.logout();
					client.disconnect();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void addFolderToQue(File file, String string, Runnable start, Runnable end) {
		if(!file.isDirectory()){
			System.out.println("Not a directory");
			return;
		}
		List<QueMessage> queList = new ArrayList<QueMessage>();
		queList.addAll(getMessages(file,string));
		if(!queList.isEmpty()){
			queList.get(0);
			queList.get(queList.size()-1);
		}
		que.addAll(queList);
	}

	private List<QueMessage> getMessages(File file,String target) {
		List<QueMessage> queList = new ArrayList<QueMessage>();
		File[] files = file.listFiles();
		for(File file1:files){
			if(file1.isDirectory()){
				queList.addAll(getMessages(file1,target+"/"+file1.getName()));
				continue;
			}
			QueMessage message = new QueMessage(file1,target,file1.getName(),null,null);
			queList.add(message);
		}
		return queList;
	}
}
