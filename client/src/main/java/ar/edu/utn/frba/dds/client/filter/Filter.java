package ar.edu.utn.frba.dds.client.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Filter implements jakarta.servlet.Filter {
  @Autowired
  private RateLimitService rateLimitService;

  @Autowired
  private IPControlService ipControlService;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;

    String clientIP = getClientIP(req);

    // 1. Check IP permitida
    if (!ipControlService.isAllowed(clientIP)) {
      res.setStatus(403);
      res.getWriter().write("{\"error\":\"IP blocked\"}");
      return;
    }

    // 2. Check rate limit
    if (!rateLimitService.isAllowed(clientIP)) {
      ipControlService.blockTemporarily(clientIP); // Bloquear si abusa
      res.setStatus(429);
      res.getWriter().write("{\"error\":\"Too many requests\"}");
      return;
    }

    // 3. Continuar
    chain.doFilter(request, response);
  }

  private String getClientIP(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (ip != null && !ip.isEmpty()) {
      return ip.split(",")[0].trim();
    }
    return request.getRemoteAddr();
  }
}
