package com.kbtg.bootcamp.posttest.security;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class DomainExtractor {

    public String getDomainName(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        return extractDomainName(url);
    }

    private String extractDomainName(String url) {
        // Remove the protocol (http:// or https://) if present
        String domain = url.replaceAll("^(http://|https://)", "");

        // Remove the path and query parameters
        int slashIndex = domain.indexOf('/');
        if (slashIndex != -1) {
            domain = domain.substring(0, slashIndex);
        }

        // Remove port number if present
        int portIndex = domain.indexOf(':');
        if (portIndex != -1) {
            domain = domain.substring(0, portIndex);
        }

        return domain;
    }
}

