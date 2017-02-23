package com.example.forphp.utils.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.forphp.utils.debug.UtilDebug;

public class UtilDownload {
	public static  void sendPost(String url, String fileName) throws ClientProtocolException, IOException {
		HttpPost objPost = new HttpPost(url);

		HttpClient client = new DefaultHttpClient();
		HttpResponse objEXE = client.execute(objPost);
		
		long len = objEXE.getEntity().getContentLength();
		
		InputStream is = objEXE.getEntity().getContent();
		
		File localFile = new File(fileName);

		boolean ret = localFile.createNewFile();
		if (ret == false) {
			UtilDebug.di(
					UtilDebug.TAG_MAINACTIVITY
					, "-----------------create new file ----failed ==------"
					);
		}
		FileOutputStream fos = new FileOutputStream(localFile);
		byte[] buf = new byte[1024];
		while (true) {
			int cont = is.read(buf);
			if (cont == -1) {
				break;	
			}
			
			UtilDebug.di(UtilDebug.TAG_MAINACTIVITY, "........");
			fos.write(buf, 0, cont);
		}
		
		fos.close();
		is.close();

		return; 
	}
}
