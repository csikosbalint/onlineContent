package hu.fnf.devel.onlinecontent.model.background;

import hu.fnf.devel.onlinecontent.model.Content;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class EditorConsumer {

	public static void main(String args[]) {
		System.out.println("crawling...");
		BlockingQueue<Content> searchQueue = new LinkedBlockingQueue<>();
		BlockingQueue<Content> uploadQueue = new LinkedBlockingQueue<>();
		
		Thread search = new Thread(new SearchProducer(searchQueue));
		Thread upload = null;
		String url = "http://localhost:8888/receiver";
		//String url = "http://localhost:8888/receiver";
		try {
			upload = new Thread( new UploaderConsumer(uploadQueue, new URL(url)));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		search.start();
		upload.start();
		
		while(true){
			Content s = null;
            try {
            	s = searchQueue.take();
            } catch ( InterruptedException ex ) {
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
