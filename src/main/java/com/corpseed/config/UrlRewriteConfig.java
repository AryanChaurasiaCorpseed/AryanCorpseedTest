package com.corpseed.config;

import java.io.InputStream;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.tuckey.web.filters.urlrewrite.Conf;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;
import org.tuckey.web.filters.urlrewrite.UrlRewriter;

@Configuration
public class UrlRewriteConfig extends UrlRewriteFilter {

	private UrlRewriter urlRewriter;

    @Bean
    public FilterRegistrationBean<UrlRewriteConfig> tuckeyRegistrationBean() {
        final FilterRegistrationBean<UrlRewriteConfig> registrationBean = new FilterRegistrationBean<UrlRewriteConfig>();
        registrationBean.setFilter(new UrlRewriteConfig());
        return registrationBean;
    }

    @Override
    public void loadUrlRewriter(FilterConfig filterConfig) throws ServletException {
        try {
            ClassPathResource classPathResource = new ClassPathResource("urlrewrite.xml");
            InputStream inputStream = classPathResource.getInputStream();
            Conf conf1 = new Conf(filterConfig.getServletContext(), inputStream, "urlrewrite.xml", "");
            urlRewriter = new UrlRewriter(conf1);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    public UrlRewriter getUrlRewriter(ServletRequest request, ServletResponse response, FilterChain chain) {
        return urlRewriter;
    }

    @Override
    public void destroyUrlRewriter() {
        if (urlRewriter != null)
            urlRewriter.destroy();
    }
}