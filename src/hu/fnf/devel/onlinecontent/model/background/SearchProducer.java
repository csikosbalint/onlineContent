package hu.fnf.devel.onlinecontent.model.background;

import hu.fnf.devel.onlinecontent.model.Content;

import java.io.File;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class SearchProducer extends WebCrawler {
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
			+ "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	private static final Pattern imgPatterns = Pattern.compile(".*(\\.(bmp|gif|jpe?g|png|tiff?))$");

	private static String[] crawlDomains;
	private static File storageFolder;
	private static java.util.List<Content> results;

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

	public static void configure(String[] domain, String storageFolderName, java.util.List<Content> results) {
		SearchProducer.crawlDomains = domain;
		SearchProducer.results = results;
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
			results.add(content);
		} else {
			if (imgPatterns.matcher(url).matches()) {
				System.out.println("img: " + url);
			}
		}
	}
}
