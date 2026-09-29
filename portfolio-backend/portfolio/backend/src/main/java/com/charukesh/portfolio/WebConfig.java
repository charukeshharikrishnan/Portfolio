package com.charukesh.portfolio;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.concurrent.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Value("${app.cors.allowed-origin}") String origin;
  @Value("${app.rate.max-per-minute}") int max;

  @Override public void addCorsMappings(CorsRegistry r){
    r.addMapping("/api/**").allowedOrigins(origin).allowedMethods("GET","POST").allowedHeaders("Content-Type").maxAge(3600);
  }

  /** Security headers + per-IP rate limit on POST /api/contact (in-memory; use a shared store if you run several instances). */
  @Bean Filter securityFilter(){
    ConcurrentHashMap<String,long[]> hits = new ConcurrentHashMap<>();
    return (req,res,chain)->{
      HttpServletRequest rq=(HttpServletRequest)req; HttpServletResponse rs=(HttpServletResponse)res;
      rs.setHeader("X-Content-Type-Options","nosniff"); rs.setHeader("X-Frame-Options","DENY");
      rs.setHeader("Referrer-Policy","no-referrer"); rs.setHeader("Cache-Control","no-store");
      rs.setHeader("Content-Security-Policy","default-src 'none'; frame-ancestors 'none'");
      if("POST".equals(rq.getMethod()) && rq.getRequestURI().startsWith("/api/contact")){
        String ip = rq.getRemoteAddr(); long now=System.currentTimeMillis();
        long[] w = hits.compute(ip,(k,v)->{ if(v==null||now-v[0]>60_000) return new long[]{now,1}; v[1]++; return v; });
        if(w[1]>max){ rs.setStatus(429); rs.setContentType("application/json"); rs.getWriter().write("{\"error\":\"Too many requests. Try again in a minute.\"}"); return; }
      }
      chain.doFilter(req,res);
    };
  }
}
