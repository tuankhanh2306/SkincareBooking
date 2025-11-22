package edu.uth.skincarebookingsystem.config;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Order(1)
public class CloudflareAllowlistFilter implements Filter {

    @Value("${cf.ip.ranges:}")
    private String cfIpRanges;

    private List<CIDR> cidrs = new ArrayList<>();

    @PostConstruct
    public void init() {
        if (cfIpRanges == null || cfIpRanges.isBlank()) return;

        cidrs = Arrays.stream(cfIpRanges.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(CIDR::new)
                .collect(Collectors.toList());
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI();

        // Allow Render Health Check
        if (path.equals("/actuator/health")) {
            chain.doFilter(req, res);
            return;
        }

        // Dev environment or no CF config → allow all
        if (cidrs.isEmpty()) {
            chain.doFilter(req, res);
            return;
        }

        // Extract real client IP
        String realIp = Optional.ofNullable(request.getHeader("CF-Connecting-IP"))
                .orElse(Optional.ofNullable(request.getHeader("X-Forwarded-For"))
                        .map(x -> x.split(",")[0].trim())
                        .orElse(request.getRemoteAddr()));

        if (isAllowed(realIp)) {
            chain.doFilter(req, res);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Direct access forbidden");
        }
    }

    private boolean isAllowed(String ip) {
        try {
            InetAddress addr = InetAddress.getByName(ip);
            for (CIDR c : cidrs) {
                if (c.contains(addr)) return true;
            }
        } catch (Exception ignored) {}
        return false;
    }

    // CIDR Matching Helper
    static class CIDR {
        private final byte[] addr;
        private final byte[] mask;

        CIDR(String cidr) {
            String[] p = cidr.split("/");
            try {
                InetAddress inet = InetAddress.getByName(p[0]);
                addr = inet.getAddress();
                int prefix = Integer.parseInt(p[1]);

                byte[] maskBytes = new byte[addr.length];
                for (int i = 0; i < addr.length; i++) {
                    int remain = Math.max(0, Math.min(8, prefix - i*8));
                    maskBytes[i] = (byte) (remain == 8 ? 0xFF :
                            remain == 0 ? 0 : ((0xFF << (8 - remain)) & 0xFF));
                }

                mask = maskBytes;
            } catch (Exception e) {
                throw new RuntimeException("Invalid CIDR: " + cidr);
            }
        }

        boolean contains(InetAddress ip) {
            byte[] target = ip.getAddress();
            if (target.length != addr.length) return false;

            for (int i = 0; i < target.length; i++) {
                if ((target[i] & mask[i]) != (addr[i] & mask[i])) return false;
            }
            return true;
        }
    }
}
