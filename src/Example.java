import java.io.File;

import com.settings.FTPSettings;
import com.settings.FTPUploader;
import com.settings.QueMessage;

public class Example {
	public static void main(String[] args) {
		//Set the settings file, server, port, username, password
		FTPSettings set = new FTPSettings("10.20.104.41", 21, "test", "test123");
		//Create a new uploader and start it. Uploader is a Thread that runs async
		FTPUploader uploader = new FTPUploader(set);
		uploader.start();
		//Create and add a new task, file can use File.seperator instead, first string is the target folder on the FTP server, will be created if not existing
		//Second string is the filename, can be anything
		//First runnable runs when the file gets uploaded
		//Second runnable runs when the uploading is done
		//Runnables can be empty, Runnables are running async! Think about this
		uploader.addToQue(new QueMessage(new File("C:\\Users\\11011715\\test.mv"),"C:\\Users\\FISERVER\\KCCGCC","test.mv",
				new Runnable(){
			@Override
			public void run() {
				System.out.println("Starting upload");
			}},
			new Runnable(){
				@Override
				public void run() {
					System.out.println("Done upload");
				}}));
		uploader.addToQue(new QueMessage(new File("C:\\Users\\11011715\\test.mv"),"C:\\Users\\FISERVER\\KCCGCC","test.mv",null,null));
		//Will add all files in the folder to the que, start runnable will be put only on the first file, end runnable on the last file
//		uploader.addFolderToQue(new File("C:\\Users\\Tim\\Testfolder"),"Test folder plugins",null,null);
		//Stops the uploader. This means that a variable will change and that the uploader will finish its current que. With loads of file this could take a while.
		uploader.close();
		System.out.println("Done with uploading");
	}
}
