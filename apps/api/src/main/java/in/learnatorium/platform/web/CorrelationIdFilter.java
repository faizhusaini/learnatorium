package in.learnatorium.platform.web;
import jakarta.servlet.*; import jakarta.servlet.http.*; import java.io.IOException; import java.util.UUID; import org.slf4j.MDC; import org.springframework.stereotype.Component; import org.springframework.web.filter.OncePerRequestFilter;
@Component public class CorrelationIdFilter extends OncePerRequestFilter {
 protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException{String id=req.getHeader("X-Correlation-ID");if(id==null||!id.matches("[A-Za-z0-9_-]{8,64}"))id=UUID.randomUUID().toString();req.setAttribute("correlationId",id);res.setHeader("X-Correlation-ID",id);try(MDC.MDCCloseable ignored=MDC.putCloseable("correlationId",id)){chain.doFilter(req,res);}}
}

