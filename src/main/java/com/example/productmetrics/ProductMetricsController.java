package com.example.productmetrics;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/metrics/product")
public class ProductMetricsController {
	private final ProductMetricsService svc;

	public ProductMetricsController(ProductMetricsService svc) {
		this.svc = svc;
	}

	@PostMapping("/view")
	public String view(@RequestParam String productId, @RequestParam(defaultValue = "unknown") String category,
			@RequestParam(defaultValue = "unknown") String brand) {
		svc.productView(productId, category, brand);
		return "ok";
	}

	@PostMapping("/add-to-cart")
	public String addToCart(@RequestParam String productId, @RequestParam(defaultValue = "unknown") String category,
			@RequestParam(defaultValue = "unknown") String brand) {
		svc.addToCart(productId, category, brand);
		return "ok";
	}

	@PostMapping("/purchase")
	public String purchase(@RequestParam String productId, @RequestParam double amount, @RequestParam double latencyMs,
			@RequestParam(defaultValue = "unknown") String category,
			@RequestParam(defaultValue = "unknown") String brand) {
		svc.purchase(productId, amount, latencyMs, category, brand);
		return "ok";
	}

	@PostMapping("/price")
	public String price(@RequestParam String productId, @RequestParam double price,
			@RequestParam(defaultValue = "USD") String currency) {
		svc.price(productId, price, currency);
		return "ok";
	}

	@PostMapping("/inventory")
	public String inventory(@RequestParam String productId, @RequestParam int level,
			@RequestParam(defaultValue = "default") String warehouse) {
		svc.inventory(productId, level, warehouse);
		return "ok";
	}

	@PostMapping("/rating")
	public String rating(@RequestParam String productId, @RequestParam double stars) {
		svc.rating(productId, stars);
		return "ok";
	}

	@GetMapping("/health")
	public String health() {
		return "healthy";
	}
}
