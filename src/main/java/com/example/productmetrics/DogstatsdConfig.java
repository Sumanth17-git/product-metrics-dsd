package com.example.productmetrics;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.timgroup.statsd.NonBlockingStatsDClientBuilder;
import com.timgroup.statsd.StatsDClient;

@Configuration
public class DogstatsdConfig {

	private static String env(String k, String def) {
		String v = System.getenv(k);
		return (v == null || v.isBlank()) ? def : v;
	}

	@Bean(destroyMethod = "stop")
	public StatsDClient statsDClient() {
		// Metric prefix; final names will be <prefix>.<metric>
		String prefix = env("DD_METRIC_PREFIX", "shop");

		// UDP host/port to the local Datadog Agent (DogStatsD)
		String host = env("DD_DOGSTATSD_HOST", "127.0.0.1");
		int port = Integer.parseInt(env("DD_DOGSTATSD_PORT", "8125"));

		return new NonBlockingStatsDClientBuilder().prefix(prefix).hostname(host).port(port).enableTelemetry(false) // quieter
																													// logs
				.build();
	}
}
