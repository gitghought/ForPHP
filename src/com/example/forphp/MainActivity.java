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

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public void sendPost(String url, String param) throws ClientProtocolException, IOException {
		HttpPost objPost = new HttpPost(url);

		HttpClient client = new DefaultHttpClient();
		HttpResponse objEXE = client.execute(objPost);
		
		long len = objEXE.getEntity().getContentLength();
		
		System.out.println("len = " + len);
		System.out.println("\r\n");

		
		InputStream is = objEXE.getEntity().getContent();
		
		File localFile = new File("/data/data/com.example.forphp/MainActivity-debug.apk");
		boolean ret = localFile.createNewFile();
		if (ret == false) {
			System.out.println("-----------------create new file ----failed ==------");
		}
		FileOutputStream fos = new FileOutputStream(localFile);
		byte[] buf = new byte[1024];
		while (true) {
			int cont = is.read(buf);
			if (cont == -1) {
				break;	
			}
			
			System.out.println("........");
			fos.write(buf, 0, cont);
		}
		
		fos.close();
		is.close();

		return; 
	}


	public void uploadedAPKFile(String url, String path) throws ClientProtocolException, IOException {
		File file = new File(path);
		if (file.exists() == false) {
			System.out.println("failed");
		}
		
		HttpClient client = new DefaultHttpClient();
		HttpPost postOBJ = new HttpPost(url);
		
//		FileBody fBody = new FileBody(file);
		FileBody fBody = new FileBody(file);
		MultipartEntity entity = new MultipartEntity();
		entity.addPart("file", fBody);
		postOBJ.setEntity(entity);
		
		try {
			HttpResponse response = client.execute(postOBJ);
			
			int statusCode = response.getStatusLine().getStatusCode();
			String result = EntityUtils.toString(response.getEntity(), "UTF-8");
			
			System.out.println("statuscode = " + statusCode);
			System.out.println("result = " + result);
			
			 if (statusCode == 201) {
				 System.out.println("successed");
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
					//			MainActivity.this.uploadFile("http://192.168.155.3/upload.php", "/sdcard/CantvFileManager_v149.apk");
//					MainActivity.this.uploadedAPKFile("http://192.168.1.109/upload.php", "/data/local/MainActivity-debug.apk");
					MainActivity.this.sendPost("http://192.168.1.109/down.php", "/data/local/MainActivity-debug.apk");
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
}
