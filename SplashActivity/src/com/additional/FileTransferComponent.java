package com.additional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class FileTransferComponent {
	String server = "ec2-54-196-141-230.compute-1.amazonaws.com";
	int port = 22;
	String user = "ubuntu";
	String pass = "team5";

	
	public static final String serverAddress = "";
	
	public void push(String filename){
	
		try {
		    JSch jsch = new JSch();
		Session session = jsch.getSession(user, server, port);
		session.setPassword(pass);
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();
		Channel channel = session.openChannel("sftp");
		channel.connect();
		ChannelSftp channelSftp = (ChannelSftp) channel;
		File f1 = new File(filename);
		channelSftp.put(new FileInputStream(f1), f1.getName());
		channelSftp.exit();
		session.disconnect();
		} catch (Exception ex) {
		  ex.printStackTrace();
		  }
	}
	
	public List<String> pullLogsForMentor(String mentorName){
		List<String> allLogs = new ArrayList<String>();
		try{
		JSch jsch = new JSch();
        Session session = jsch.getSession(user,server,port);
        session.setPassword(pass);

        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        //logger.info("Connected to " + dto.getHost() + ".");

        Channel channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp channelSftp = (ChannelSftp) channel;
        Vector<ChannelSftp.LsEntry> list = channelSftp.ls(".");
		for(ChannelSftp.LsEntry file: list){
			String filename = file.getFilename();
			String[] subStrings = filename.split("_");
			if(subStrings[0].equals(mentorName)){
				allLogs.addAll(loadFile(filename));
			}	
		}
		}
		catch(Exception e){
			
		}
		
		return allLogs;
	}
	
	private List<String> loadFile(String filename){
		List<String> stringList = new ArrayList<String>();
		BufferedReader reader;
		try{
			JSch jsch = new JSch();
	        Session session = jsch.getSession(user,server,port);
	        session.setPassword(pass);

	        session.setConfig("StrictHostKeyChecking", "no");
	        session.connect();
	        //logger.info("Connected to " + dto.getHost() + ".");

	        Channel channel = session.openChannel("sftp");
	        channel.connect();
	        ChannelSftp channelSftp = (ChannelSftp) channel;
	        InputStream input = channelSftp.get(filename);
			
			reader = new BufferedReader(new InputStreamReader(input));
			String line = null;
				while ((line = reader.readLine()) != null) {
				    stringList.add(line);
				}
		}
		catch(Exception e){
			
		}
		return stringList;
	}
}
