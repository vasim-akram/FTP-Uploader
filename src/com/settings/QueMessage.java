package com.settings;

import java.io.File;

public class QueMessage {
	private File file;
	private String folder;
	private String target;
	private Runnable start;
	private Runnable end;
	
	public QueMessage(File file, String folder, String target, Runnable start, Runnable end){
		this.file = file;
		this.folder = folder;
		this.target = target;
		this.start = start;
		this.end = end;
	}
	
	public File getFile() {
		return file;
	}
	public Runnable getStart() {
		return start;
	}
	public Runnable getEnd() {
		return end;
	}
	public String getTarget(){
		return target;
	}
	public String getFolder(){
		return folder;
	}
}
