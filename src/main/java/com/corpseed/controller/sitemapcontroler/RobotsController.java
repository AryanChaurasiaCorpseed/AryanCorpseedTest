package com.corpseed.controller.sitemapcontroler;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RobotsController {

	@Value("${robot}")
    private String robotsTxtLocation;

	 @RequestMapping(value = "/robots.txt", method = RequestMethod.GET)
	 @ResponseBody
	    public void getRobots(HttpServletResponse response) {
		 try {
		        response.getWriter().write("User-agent: *\n\nAllow: /\n"
		        		+ "Disallow: /admin\n"
		        		+ "Disallow: /login\n"
		        		+ "Disallow: /hrm\n"
		        		+ "Disallow: /404\n"
		        		+ "Disallow: /candidate\n"
						+ "Disallow: /chatGPT-User\n"
		        		+ "Disallow: /my-profile\n"
		        		+ "Disallow: /https://corpseed.azurewebsites.net/*\n\n"
		        		+ "Noindex: /https://corpseed.azurewebsites.net/*\n\n"
		        		+ "Sitemap: https://www.corpseed.com/sitemap.xml");
		    } catch (IOException e) {
		       e.printStackTrace();
		    }
	    }
}
