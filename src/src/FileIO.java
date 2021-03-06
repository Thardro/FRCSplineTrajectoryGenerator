package src;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class FileIO {

	PrintStream writer;
	
	boolean fileExists;
	String dir;
	
	public FileIO(String dir) {
		this.dir = dir;
	}

	public void openFile(String filename) {
		String url = dir + filename;
		System.out.println(url);
		File file = new File(url);
		
		try {
			//Checking if file exists and deleting it and recreating it to clear if it does
			fileExists = file.createNewFile();
			if(!fileExists) {
				file.delete();
				file.createNewFile();
			}
			
			writer = new PrintStream(file);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	public void writeToFile(String content) {
		writer.println(content);
		writer.flush();
	}
	
	public void closeFile() {
		writer.close();
	}
	
}
