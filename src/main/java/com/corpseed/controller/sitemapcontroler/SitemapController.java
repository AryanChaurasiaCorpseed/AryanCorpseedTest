package com.corpseed.controller.sitemapcontroler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SitemapController {
	
	@Value("${sitemap.path}")
	private String sitemap;
	
	Logger log=LoggerFactory.getLogger(SitemapController.class);
	
	@GetMapping("/sitemap.xml")
    public void sitemap(HttpServletResponse response)throws IOException {
		
		response.setContentType("application/xml");
		try (BufferedReader bufferedReader = 
                new BufferedReader(new FileReader(new File(sitemap)))) {
			String line;
            StringBuilder siteMapBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
            	siteMapBuilder.append(line);
            }
            response.getWriter().write(siteMapBuilder.toString());
        }catch (Exception e) {
        	log.error(e.getMessage());
			e.printStackTrace();
		}
		
	}
}
