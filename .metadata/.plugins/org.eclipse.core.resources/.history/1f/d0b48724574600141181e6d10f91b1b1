package com.additional;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileTransferComponent {
	
	public static final String serverAddress = "";
	
	public void push(String filename){
		//save the file to the server
	}
	
	public List<String> pullLogsForMentor(String mentorName){
		List<String> allLogs = new ArrayList<String>();
		
		//for all files in the directory,
			String fileName = "load the filename";
			String[] subStrings = fileName.split("_");
			if(subStrings[0].equals(mentorName)){
				allLogs.addAll(loadFile(fileName));
			}	
		//close for loop
		
		return allLogs;
	}
	
	private List<String> loadFile(String filename){
		List<String> stringList = new ArrayList<String>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filename));
		
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
				    stringList.add(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return stringList;
	}
}
