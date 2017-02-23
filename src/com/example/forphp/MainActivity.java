package com.example.forphp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import com.example.forphp.utils.UtilDebug;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public void sendPost(String url, String fileName) throws ClientProtocolException, IOException {
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

	public void uploadedAPKFile(String url, String path) throws ClientProtocolException, IOException {
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					MainActivity.this.sendPost(
							"http://192.168.1.109/down.php", 
							"/data/data/com.example.forphp/MainActivity-debug.apk"
							);

				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
