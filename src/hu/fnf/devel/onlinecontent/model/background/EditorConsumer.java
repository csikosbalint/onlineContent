package hu.fnf.devel.onlinecontent.model.background;

import hu.fnf.devel.onlinecontent.model.Content;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EditorConsumer {
	final static BlockingQueue<Content> searchQueue = new LinkedBlockingQueue<>();
	final static BlockingQueue<Content> uploadQueue = new LinkedBlockingQueue<>();

	final static Thread search = new Thread(new SearchProducer(searchQueue));
	
	static class ShutdownThread extends Thread {

		public void run() {
			System.out.println("interrupting search...");
			search.interrupt();
			while ( searchQueue.size() != 0 && uploadQueue.size() != 0) {
				try {
					System.out.println("waiting for consumer(s) to empty lists...");
					System.out.println("search: " + searchQueue.size());
					System.out.println("upload: " + uploadQueue.size());
					Thread.sleep(1000);
				} catch (InterruptedException ex) {

				}
			}
			System.out.println("exit");
		}
	}

	public static void main(String args[]) {

		Runtime.getRuntime().addShutdownHook(new ShutdownThread());

		// System.out.println(5/0);
		System.out.println("crawling...");

		Thread upload = null;
	  //String url = "http://localhost:8888/receiver";
		String url = "http://onlinejatek.fnf.hu/receiver";
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
				System.out.println("taken from searchQueue");
			} catch (InterruptedException ex) {
				continue;
			}
			s.setContentCreation(new Date());
			try {
				uploadQueue.put(s);
				System.out.println("put to uploadQueue");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
