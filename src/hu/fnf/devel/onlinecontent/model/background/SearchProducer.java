package hu.fnf.devel.onlinecontent.model.background;

import hu.fnf.devel.onlinecontent.model.Content;

import java.io.File;
import java.io.Serializable;
import java.util.concurrent.BlockingQueue;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class SearchProducer implements Runnable {

	private final BlockingQueue<Content> sharedQueue;

	public SearchProducer(BlockingQueue<Content> sharedQueue) {
		super();
		this.sharedQueue = sharedQueue;
	}

	@Override
	public void run() {
		String crawlStorageFolder = System.getProperty("java.io.tmpdir") + File.separator + "crawl";
		System.out.println("crawlStorageFolder: " + crawlStorageFolder);
		int numberOfCrawlers = 7;

		CrawlConfig config = new CrawlConfig();

		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setIncludeBinaryContentInCrawling(true);
		config.setResumableCrawling(true);
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
		String[] crawlDomains = new String[] { "http://www.onlinegames7.com", "http://www.netesjatekok.hu",
				"http://jatek-online.hu" };

		for (String domain : crawlDomains) {
			controller.addSeed(domain);
		}

		SwfCrawler.configure(crawlDomains, crawlStorageFolder, sharedQueue);

		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.startNonBlocking(SwfCrawler.class, numberOfCrawlers);

		while (!Thread.currentThread().isInterrupted()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				break;
			}
		}
		System.out.println(Thread.currentThread().getName() + ": stopping crawling...");
		controller.shutdown();
		controller.waitUntilFinish();
	}

}
