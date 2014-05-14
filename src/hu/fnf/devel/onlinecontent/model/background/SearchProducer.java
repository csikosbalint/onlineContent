package hu.fnf.devel.onlinecontent.model.background;

import hu.fnf.devel.onlinecontent.model.Content;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;

public class SearchProducer implements Runnable {
    private final BlockingQueue<Content> sharedQueue;
    
	public SearchProducer(BlockingQueue<Content> sharedQueue) {
		super();
		this.sharedQueue = sharedQueue;
	}

	@Override
	public void run() {
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
		String[] crawlDomains = new String[] { "http://www.onlinegames7.com", "http://www.netesjatekok.hu",
				"http://jatek-online.hu" };

		for (String domain : crawlDomains) {
			controller.addSeed(domain);
		}

		Search.configure(crawlDomains, crawlStorageFolder, sharedQueue);

		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.startNonBlocking(Search.class, numberOfCrawlers);
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
	
}
class Search extends WebCrawler {
	public static BlockingQueue<Content> sharedQueue;
	
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
			+ "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	private static final Pattern imgPatterns = Pattern.compile(".*(\\.(bmp|gif|jpe?g|png|tiff?))$");

	private static String[] crawlDomains;
	private static File storageFolder;

	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		if (FILTERS.matcher(href).matches()) {
			return false;
		}

		for (String domain : crawlDomains) {
			if (href.startsWith(domain)) {
				return true;
			}
		}
		return false;
	}

	public static void configure(String[] domain, String storageFolderName, BlockingQueue<Content> results) {
		Search.crawlDomains = domain;
		Search.sharedQueue = results;
		storageFolder = new File(storageFolderName);
		if (!storageFolder.exists()) {
			storageFolder.mkdirs();
		}
	}

	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String html = htmlParseData.getHtml();

			if (!html.contains(".swf")) {
				return;
			}

			Document doc = Jsoup.parseBodyFragment(html);
			System.out.println("Title: " + doc.title().split("-")[0]);
			Content content = new Content(doc.title().split("-")[0].trim());
			content.setDisplayName(doc.title().split("-")[0].trim());

			String[] lines = html.split(System.getProperty("line.separator"));

			for (String line : lines) {
				if (line.contains("swf") && line.contains("http")) {
					String srcSwf = line.substring(line.indexOf("http"), line.indexOf(".swf", line.indexOf("http"))
							+ ".swf".length());
					System.out.println("swf: " + srcSwf);
					content.setContentSourceUrl(srcSwf);
				}
			}
			try {
				sharedQueue.put(content);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			if (imgPatterns.matcher(url).matches()) {
				System.out.println("img: " + url);
			}
		}
	}
}
