package com.xm.cryptoservice.ratelimiting;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RateLimitInterceptor.class);

    @Value("${rate.limit}")
    private int RATE_LIMIT;
    @Value("${time.duration.in.minutes}")
    private long TIME_DURATION;
    
  

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private Bucket newBucket(String apiKey) {
    	
    	System.out.println("RATE_LIMIT is "+RATE_LIMIT);
    	System.out.println("TIME_DURATION is "+TIME_DURATION);
        Refill refill = Refill.intervally(RATE_LIMIT, Duration.ofMinutes(TIME_DURATION));
        Bandwidth limit = Bandwidth.classic(RATE_LIMIT, refill);
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    public Bucket resolveBucket(String apiKey) {
        return cache.computeIfAbsent(apiKey, this::newBucket);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        logger.info("Inside RateLimitInterceptor preHandle method ");
        String remoteIpAddress = request.getHeader("X-Forwarded-For");
      
        if (remoteIpAddress == null || remoteIpAddress.isEmpty()) {
            remoteIpAddress = request.getRemoteAddr();
            logger.info("remoteIpAddress with getRemoteAddr is "+remoteIpAddress);
        }
        if (remoteIpAddress == null || remoteIpAddress.isEmpty()) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Missing Header: remoteIpAddress");
            return false;
        }
        Bucket tokenBucket = resolveBucket(remoteIpAddress);
        ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            return true;
        } else {
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "You have exhausted your API Request Quota");
            return false;
        }
    }

}
