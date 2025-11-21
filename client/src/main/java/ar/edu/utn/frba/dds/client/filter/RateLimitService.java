package ar.edu.utn.frba.dds.client.filter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RateLimitService {
    // Mapa simple: IP -> contador de requests
    private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();

    // Mapa para resetear contadores cada minuto
    private final Map<String, Long> lastResetTime = new ConcurrentHashMap<>();

    private final int MAX_REQUESTS = 1000;  // requests por minuto
    private final long WINDOW_MS = 60_000; // 1 minuto

    public boolean isAllowed(String clientIP) {
      long now = System.currentTimeMillis();
      // Resetear contador si pasó el tiempo
      Long lastReset = lastResetTime.get(clientIP);
      if (lastReset == null || (now - lastReset) > WINDOW_MS) {
        requestCounts.put(clientIP, new AtomicInteger(0));
        lastResetTime.put(clientIP, now);
      }

      // Incrementar y verificar
      AtomicInteger counter = requestCounts.get(clientIP);
      int currentCount = counter.incrementAndGet();
      log.info("Checking for requests for clientIP: {}, counts: {}", clientIP, currentCount);


      if (currentCount > MAX_REQUESTS) {
        log.warn("Rate limit exceeded for IP: {} ({} requests)", clientIP, currentCount);
        return false;
      }

      return true;
  }
}
