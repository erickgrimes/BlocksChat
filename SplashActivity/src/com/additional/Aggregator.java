package com.additional;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Aggregator {
	
	private final static String bannedWordFileName = "badwords.txt";
	private List<String> bannedWords;
	private FileTransferComponent ftp;
	
	public Aggregator(FileTransferComponent ftp){
		this.ftp = ftp;
		bannedWords = loadFile(bannedWordFileName);
	}
	
	public int getNumberOfBannedWordsForMentor(String mentorName){
		List<String> inputFile = ftp.pullLogsForMentor(mentorName);
		return getNumberOfBannedWords(inputFile);
	}
	public Map<Tag,Integer> getFrequencyOfTagsForMentor(String mentorName){
		List<String> inputFile = ftp.pullLogsForMentor(mentorName);
		return generateTagMapFromFile(inputFile);
	}
	
	private Map<Tag,Integer> updateTagMapWithString(String inputString, Map<Tag,Integer> tagMap){
		String[] subStrings = inputString.split(" ");
		for(String subString:subStrings){
			try{
				Tag thisTag = Tag.valueOf(subString);
				int count = 0;
				if(tagMap.containsKey(thisTag)){
					count+= tagMap.get(thisTag);
				}
				tagMap.put(thisTag, count);
			}catch(IllegalArgumentException e){
				System.out.println("Not a tag");
			}
		}
		return tagMap;
	}
	
	private Map<Tag,Integer> generateTagMapFromFile(List<String> inputFile){
		Map<Tag,Integer> tagMap = new HashMap<Tag,Integer>();
		for(String thisString:inputFile){
			tagMap = updateTagMapWithString(thisString, tagMap);
		}
		return tagMap;
	}
	
	private int getNumberOfBannedWordsInLine(String line){
		String[] subStrings = line.split(" ");
		int count = 0;
		for(String thisString:subStrings){
			if(bannedWords.contains(thisString)){
				count++;
			}
		}
		return count;
	}
	
	private int getNumberOfBannedWords(List<String> inputFile){
		int count = 0;
		for(String thisLine:inputFile){
			count+=getNumberOfBannedWordsInLine(thisLine);
		}
		return count;
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
