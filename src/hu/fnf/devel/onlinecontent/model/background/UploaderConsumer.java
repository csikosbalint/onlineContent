package hu.fnf.devel.onlinecontent.model.background;

import hu.fnf.devel.onlinecontent.model.Content;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;

public class UploaderConsumer implements Runnable {
	private final BlockingQueue<Content> sharedQueue;
	private final URL url;

	public UploaderConsumer(BlockingQueue<Content> sharedQueue, URL url) {
		super();
		this.sharedQueue = sharedQueue;
		this.url = url;
	}

	@Override
	public void run() {
		while (true) {

			Content content = null;
			try {
				content = sharedQueue.take();
			} catch (InterruptedException ex) {
				continue;
			}
			HttpURLConnection connection = null;
			try {
				connection = (HttpURLConnection) url.openConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			connection.setDoOutput(true);
			try {
				connection.setRequestMethod("POST");
			} catch (ProtocolException e) {
				e.printStackTrace();
			}
			try {
				connection.connect();
			} catch (IOException e) {
				System.out.println("Unable to upload: " + e.getMessage());
				// TODO: handle unsuccessful upload and store data as to-upload
				continue;
			}
			ObjectOutputStream oos = null;
			try {
				oos = new ObjectOutputStream(connection.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				System.out.println();
				oos.writeObject(content);
				oos.flush();
				oos.close();
				System.out.print("upload: " + content.getNameKey());
				System.out.println(" res: " + connection.getResponseCode());
			} catch (IOException e) {
				System.out.println("Unable to upload: " + e.getMessage());
			}
			connection.disconnect();
		}
	}
}
