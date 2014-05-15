package hu.fnf.devel.onlinecontent.model.background;

import hu.fnf.devel.onlinecontent.model.Content;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EditorConsumer {
	static class Message extends Thread {

		public void run() {
			System.out.println("Bye.");
		}
	}

	public static void main(String args[]) {

		Runtime.getRuntime().addShutdownHook(new Message());

		System.out.println("crawling...");
		BlockingQueue<Content> searchQueue = new LinkedBlockingQueue<>();
		BlockingQueue<Content> uploadQueue = new LinkedBlockingQueue<>();

		Thread search = new Thread(new SearchProducer(searchQueue));
		Thread upload = null;
		String url = "http://localhost:8888/receiver";
		// String url = "http://localhost:8888/receiver";
		try {
			upload = new Thread(new UploaderConsumer(uploadQueue, new URL(url)));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.exit(1);
		}

		search.start();
		upload.start();

		while (true) {
			Content s = null;
			try {
				s = searchQueue.take();
			} catch (InterruptedException ex) {
				continue;
			}

			try {
				uploadQueue.put(s);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
