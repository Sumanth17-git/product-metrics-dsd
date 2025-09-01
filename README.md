

```yaml
# 1) DogStatsD + metric prefix + tags (your code reads these from env)
export DD_ENV=dev
export DD_SERVICE=product-api
export DD_VERSION=0.0.1
export DD_REGION=ap-south-1
export DD_METRIC_PREFIX=shop
export DD_DOGSTATSD_HOST=127.0.0.1
export DD_DOGSTATSD_PORT=8125

# 2) Optional: APM/profiling settings for the Datadog Java Agent
java -javaagent:/home/ubuntu/dd-java-agent.jar \
  -Xms500m \
  -Xmx1g \
  -Ddd.service=$DD_SERVICE \
  -Ddd.env=$DD_ENV \
  -Ddd.version=$DD_VERSION \
  -Ddd.site=us5.datadoghq.com \
  -Ddd.tags=app-name:product-api,team:devops \
  -Ddd.logs.injection=true \
  -Ddd.trace.sample.rate=1 \
  -Ddd.trace.health.metrics.enabled=true \
  -Ddd.profiling.enabled=true \
  -Ddd.dynamic.instrumentation.enabled=true \
  -Ddd.jmxfetch.enabled=true \
  -XX:FlightRecorderOptions=stackdepth=1024 \
  -jar product-metrics-dsd-0.0.1-SNAPSHOT.jar
```
### Testing
```bash
# counters
curl -X POST "http://localhost:8080/metrics/product/view?productId=sku-123&category=electronics&brand=acme"
curl -X POST "http://localhost:8080/metrics/product/add-to-cart?productId=sku-123&category=electronics&brand=acme"

# distribution (order value + checkout latency)
curl -X POST "http://localhost:8080/metrics/product/purchase?productId=sku-123&amount=1299.0&latencyMs=420&category=electronics&brand=acme"

# gauges
curl -X POST "http://localhost:8080/metrics/product/price?productId=sku-123&price=1299.0&currency=INR"
curl -X POST "http://localhost:8080/metrics/product/inventory?productId=sku-123&level=42&warehouse=blr-01"

# distribution (rating)
curl -X POST "http://localhost:8080/metrics/product/rating?productId=sku-123&stars=4.5"

```
