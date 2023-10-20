package com.corpseed.controller.feedController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeedController {
	
	@Value("${rssFeed.path}")
	private String rssFeed;
	
	@Value("${rssFeedNews.path}")
	private String rssFeedNews;
	
	
	Logger log=LoggerFactory.getLogger(FeedController.class);
	
	@GetMapping("/knowledge-centre/rssFeed.xml")
    public void blogRss(HttpServletResponse response) {
		
		response.setContentType("application/xml");
		try (BufferedReader bufferedReader = 
                new BufferedReader(new FileReader(new File(rssFeed)))) {
            String line;
            StringBuilder rsFeedBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
            	rsFeedBuilder.append(line);
            }
            response.getWriter().write(rsFeedBuilder.toString());
        }catch (Exception e) {
        	log.error(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	@GetMapping("/news/rssFeed.xml")
    public void newsRss(HttpServletResponse response) {
		
		response.setContentType("application/xml");
		try (BufferedReader bufferedReader = 
                new BufferedReader(new FileReader(new File(rssFeedNews)))) {
            String line;
            StringBuilder rsFeedBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
            	rsFeedBuilder.append(line);
            }
            response.getWriter().write(rsFeedBuilder.toString());
        }catch (Exception e) {
        	log.error(e.getMessage());
			e.printStackTrace();
		}
	}
}