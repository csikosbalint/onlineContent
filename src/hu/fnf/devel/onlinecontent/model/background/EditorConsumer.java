package hu.fnf.devel.onlinecontent.model.background;

import hu.fnf.devel.onlinecontent.model.Content;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.CopyOnWriteArrayList;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class EditorConsumer {

	private static void startCrawling(java.util.List<Content> results) throws InstantiationException {
		String crawlStorageFolder = "/tmp/crawl";
		int numberOfCrawlers = 7;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);

		/*
		 * Since images are binary content, we need to set this parameter to
		 * true to make sure they are included in the crawl.
		 */
		config.setIncludeBinaryContentInCrawling(true);

		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

		CrawlController controller = null;
		try {
			controller = new CrawlController(config, pageFetcher, robotstxtServer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */
		String[] crawlDomains = new String[] { "http://www.onlinegames7.com", "http://www.netesjatekok.hu" };

		for (String domain : crawlDomains) {
			controller.addSeed(domain);
		}

		SearchProducer.configure(crawlDomains, crawlStorageFolder, results);

		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.startNonBlocking(SearchProducer.class, numberOfCrawlers);
		// controller.start(ContentCrawler.class, numberOfCrawlers);
		try {
			Thread.sleep(30000);
			controller.shutdown();
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		System.out.println("crawling...");
		java.util.List<Content> results = new CopyOnWriteArrayList<>();// Uploader.search();
		try {
			startCrawling(results);
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		}

		System.out.println("uploading..." + results.size());
		for (Content content : results) {
			URL url = null;
			HttpURLConnection connection = null;
			try {
				// url = new URL("http://xahcieyei4.appspot.com/receiver");
				url = new URL("http://localhost:8888/receiver");
				System.out.println("done1");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
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
			} catch (IOException e1) {
				e1.printStackTrace();
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
				e.printStackTrace();
			}
			connection.disconnect();
		}
	}
}
