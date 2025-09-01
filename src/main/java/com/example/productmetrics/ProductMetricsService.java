package com.example.productmetrics;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.timgroup.statsd.StatsDClient;

@Service
public class ProductMetricsService {
	private final StatsDClient statsd;
	private final String[] baseTags;

	public ProductMetricsService(StatsDClient statsd) {
		this.statsd = statsd;
		String env = env("DD_ENV", "dev");
		String service = env("DD_SERVICE", "product-api");
		String region = env("DD_REGION", "ap-south-1");
		String version = env("DD_VERSION", "0.0.1");
		this.baseTags = new String[] { "env:" + env, "service:" + service, "region:" + region, "version:" + version };
	}

	private static String env(String k, String def) {
		String v = System.getenv(k);
		return (v == null || v.isBlank()) ? def : v;
	}

	private String[] withTags(String... kvPairs) {
		List<String> t = new ArrayList<>();
		for (String s : baseTags)
			t.add(s);
		for (String s : kvPairs)
			t.add(s);
		return t.toArray(String[]::new);
	}

	/** counter: product views */
	public void productView(String productId, String category, String brand) {
		statsd.incrementCounter("product.view",
				withTags("product_id:" + productId, "category:" + category, "brand:" + brand));
	}

	/** counter: add-to-cart */
	public void addToCart(String productId, String category, String brand) {
		statsd.incrementCounter("product.add_to_cart",
				withTags("product_id:" + productId, "category:" + category, "brand:" + brand));
	}

	/** counter + distributions: purchase (value + latency) */
	public void purchase(String productId, double amount, double latencyMs, String category, String brand) {
		String[] tags = withTags("product_id:" + productId, "category:" + category, "brand:" + brand);
		statsd.incrementCounter("product.purchase", tags);
		statsd.recordDistributionValue("product.order_value", amount, tags);
		statsd.recordDistributionValue("product.checkout_latency_ms", latencyMs, tags);
	}

	/** gauge: current price */
	public void price(String productId, double price, String currency) {
		statsd.gauge("product.price", price, withTags("product_id:" + productId, "currency:" + currency));
	}

	/** gauge: inventory level */
	public void inventory(String productId, int level, String warehouse) {
		statsd.gauge("product.inventory", level, withTags("product_id:" + productId, "warehouse:" + warehouse));
	}

	/** distribution: product rating 1–5 */
	public void rating(String productId, double stars) {
		statsd.recordDistributionValue("product.rating", stars, withTags("product_id:" + productId));
	}
}
