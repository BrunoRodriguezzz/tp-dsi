package ar.edu.utn.frba.dds.client.filter;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IPControlService {
    private final Set<String> blacklist = Set.of(
        "192.168.1.100",
        "10.0.0.50"
    );

    private final Set<String> whitelist = Set.of(
        "127.0.0.1",
        "localhost"
    );

    private final Map<String, Long> tempBlocked = new ConcurrentHashMap<>();
    private final long BLOCK_DURATION_MS = 3600_000; // 1 hora

    public boolean isAllowed(String clientIP) {
      if (whitelist.contains(clientIP)) {
        return true;
      }

      if (blacklist.contains(clientIP)) {
        log.warn("Blocked blacklisted IP: {}", clientIP);
        return false;
      }

      // Check bloqueo temporal
      Long blockedUntil = tempBlocked.get(clientIP);
      if (blockedUntil != null) {
        if (System.currentTimeMillis() < blockedUntil) {
          log.warn("Blocked temporarily blocked IP: {}", clientIP);
          return false;
        } else {
          // Expiró el bloqueo
          tempBlocked.remove(clientIP);
        }
      }

      return true;
    }

    public void blockTemporarily(String clientIP) {
      long blockUntil = System.currentTimeMillis() + BLOCK_DURATION_MS;
      tempBlocked.put(clientIP, blockUntil);
      log.error("IP {} blocked for 1 hour due to suspicious activity", clientIP);
    }
}
