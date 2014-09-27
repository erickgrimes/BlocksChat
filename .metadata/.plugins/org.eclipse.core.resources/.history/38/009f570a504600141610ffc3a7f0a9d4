package com.quickblox.sample.chat.ui.activities;

import android.app.Activity;
import com.additional.FileTransferComponent;

	
public class ViewLogsActivity extends Activity{

	FileTransferComponent ftp;
	
	public ViewLogsActivity(FileTransferComponent ftp){
		this.ftp = ftp;
	}
	

	public void printLogs(String mentorName){
		for(String thisString:ftp.pullLogsForMentor(mentorName)){
			//print this string somehow lol
		}
	}

}
