package com.example.forphp.utils.upload;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.example.forphp.utils.debug.UtilDebug;

public class UtilUpload {
	public static void uploadedAPKFile(String url, String path) throws ClientProtocolException, IOException {
		File file = new File(path);
		if (file.exists() == false) {
			UtilDebug.di(UtilDebug.TAG_MAINACTIVITY, "failed");
		}
		
		HttpClient client = new DefaultHttpClient();
		HttpPost postOBJ = new HttpPost(url);
		
		FileBody fBody = new FileBody(file);
		MultipartEntity entity = new MultipartEntity();
		entity.addPart("file", fBody);
		postOBJ.setEntity(entity);
		
		try {
			HttpResponse response = client.execute(postOBJ);
			
			int statusCode = response.getStatusLine().getStatusCode();
			String result = EntityUtils.toString(response.getEntity(), "UTF-8");
			
			UtilDebug.di(UtilDebug.TAG_MAINACTIVITY, "statuscode = " + statusCode);
			UtilDebug.di(UtilDebug.TAG_MAINACTIVITY, "result = " + result);
			
			 if (statusCode == 201) {
				 UtilDebug.di(UtilDebug.TAG_MAINACTIVITY, "successed");
			 }
			 
			 client.getConnectionManager().shutdown();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ;	
	}
}
