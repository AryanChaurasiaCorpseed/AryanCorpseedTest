package com.corpseed.serviceImpl.blogserviceimpl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.corpseed.serviceImpl.CommonServices;
import com.corpseed.serviceImpl.SiteMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.corpseed.entity.blogentity.Blogs;
import com.corpseed.entity.Category;
import com.corpseed.entity.SiteMapUrl;
import com.corpseed.repository.blogrepo.BlogRepository;

@Service
public class BlogService {

	@Autowired
	private BlogRepository blogRepository;
	
	@Autowired
	private CommonServices commonService;
	
	@Autowired
	private SiteMapService siteMapService;
	
	@Value("${azure_path}")
	private String azure_path;
	
	@Value("${rssFeed.path}")
	private String rssFeed;
	
	@Value("${sitemap.path}")
	private String sitemap;
	
	@Value("${domain.name}")
	private String domain;
	
	@Caching(evict = {
		    @CacheEvict(value = "allBlogs", allEntries = true),
		    @CacheEvict(value = "blogsByTitle", allEntries = true),
		    @CacheEvict(value = "blogByCategory", allEntries = true),
		    @CacheEvict(value = "top10Category", allEntries = true),
		    @CacheEvict(value = "blogBySlug", allEntries = true),
		    @CacheEvict(value = "top5Blogs", allEntries = true),
			@CacheEvict(value = "allBlogFaq",allEntries = true),
			@CacheEvict(value = "blogTop8ByCategory",allEntries = true),
			@CacheEvict(value = "blogFeedbackByIp",allEntries = true),
			@CacheEvict(value = "blogServiceCardList",allEntries = true),
			@CacheEvict(value = "top5LatestBlogsByStatus", allEntries = true),
			@CacheEvict(value = "top10BlogByStatus",allEntries = true)
		}) 
	public Blogs saveBlogs(Blogs blogs) {
		return this.blogRepository.save(blogs); 
	} 

	@Cacheable("allBlogs")
	public Page<Blogs> getAllBlogsByStatus(Pageable pageable) {
		return this.blogRepository.findByDisplayStatusAndDeleteStatus("1",2,pageable);
	}
	@Caching(evict = {
		    @CacheEvict(value = "allBlogs", allEntries = true),
		    @CacheEvict(value = "blogsByTitle", allEntries = true),
		    @CacheEvict(value = "blogByCategory", allEntries = true),
		    @CacheEvict(value = "top10Category", allEntries = true),
		    @CacheEvict(value = "blogBySlug", allEntries = true),
		    @CacheEvict(value = "top5Blogs", allEntries = true),
			@CacheEvict(value = "allBlogFaq",allEntries = true),
			@CacheEvict(value = "blogTop8ByCategory",allEntries = true),
			@CacheEvict(value = "blogFeedbackByIp",allEntries = true),
			@CacheEvict(value = "blogServiceCardList",allEntries = true),
			@CacheEvict(value = "top5LatestBlogsByStatus", allEntries = true),
			@CacheEvict(value = "top10BlogByStatus",allEntries = true)
		}) 
	public List<Blogs> saveAllBlogs(List<Blogs> blogList) {		
		 return this.blogRepository.saveAll(blogList);
	}

	public Blogs getBlogByUuid(String blogUUID) {
		return this.blogRepository.findByUuidAndDeleteStatus(blogUUID,2);
	}

	@Caching(evict = {
		    @CacheEvict(value = "allBlogs", allEntries = true),
		    @CacheEvict(value = "blogsByTitle", allEntries = true),
		    @CacheEvict(value = "blogByCategory", allEntries = true),
		    @CacheEvict(value = "top10Category", allEntries = true),
		    @CacheEvict(value = "blogBySlug", allEntries = true),
		    @CacheEvict(value = "top5Blogs", allEntries = true),
			@CacheEvict(value = "allBlogFaq",allEntries = true),
			@CacheEvict(value = "blogTop8ByCategory",allEntries = true),
			@CacheEvict(value = "blogFeedbackByIp",allEntries = true),
			@CacheEvict(value = "blogServiceCardList",allEntries = true),
			@CacheEvict(value = "top5LatestBlogsByStatus", allEntries = true),
			@CacheEvict(value = "top10BlogByStatus",allEntries = true)
		})
	public void deleteBlog(Blogs blogs) {
		this.blogRepository.delete(blogs);
	}

	public long countAllBlogs() {
		return this.blogRepository.count();
	}

	@Cacheable(value = "blogBySlug",key = "#slug")
	public Blogs findBySlug(String slug) {
		return this.blogRepository.findBySlugAndDeleteStatus(slug,2);
	}

	public List<Blogs> findByDisplayStatus(String status) {
		return this.blogRepository.findByDisplayStatusAndDeleteStatus(status,2);
	}

	public List<Blogs> findByTitle(String value) {
		return this.blogRepository.findByTitleAndDeleteStatus(value,2);
	}

	@Cacheable("blogsByTitle")
	public List<Blogs> findTop5ByTitleContaining(String value) {
		return this.blogRepository.findTop5ByDisplayStatusAndTitleContainingIgnoreCaseAndDeleteStatus("1",value,2);
	}
	
	public List<Blogs> getRecentFiveBlogs() {
		return this.blogRepository.findTop5ByDeleteStatusOrderByIdDesc(2);
	}

	public List<Blogs> getRecentSixBlogs() {
		return this.blogRepository.findTop6ByDeleteStatusOrderByIdDesc(2);
	}

	public Blogs findBySlugAndUuidNot(String slug, String uuid) {
		return this.blogRepository.findBySlugAndDeleteStatusAndUuidNot(slug,2,uuid);
	}

	public List<Blogs> getAllBlogs() {
		return this.blogRepository.findByDeleteStatusOrderByIdDesc(2);
	}

	@Cacheable(value="blogTop8ByCategory")
	public List<Blogs> findTop8ByCategoryAndDisplayStatusAndUuidNotOrderByIdDesc(Category category, String status,String uuid) {
		return this.blogRepository.findTop8ByCategoryAndDisplayStatusAndDeleteStatusAndUuidNotOrderByIdDesc(category, status,2,uuid);
	}

	@Cacheable("blogByCategory")
	public Page<Blogs> getAllBlogsByCategoryAndStatus(Category category, Pageable pageable) {
		return this.blogRepository.findByCategoryAndDisplayStatusAndDeleteStatus(category,"1",2,pageable);
	}

	public Blogs findBySlugOrTitle(String slug, String title) {
		// TODO Auto-generated method stub
		return this.blogRepository.findBySlugOrTitle(slug, title);
	}

	public Blogs findBySlugOrTitleAndUuidNot(String slug, String title, String uuid) {
		// TODO Auto-generated method stub
		return this.blogRepository.findBySlugOrTitleAndUuidNot(slug, title, uuid);
	}

	public Blogs findByIdAndDeleteStatus(long id, int dStatus) {
		// TODO Auto-generated method stub
		return this.blogRepository.findByIdAndDeleteStatus(id,dStatus);
	}

	@Cacheable(value = "top5Blogs")
	public List<Blogs> findTop5ByDisplayStatusAndDeleteStatusOrderByVisitedDesc(String string, int i) {
		// TODO Auto-generated method stub
		return this.blogRepository.findTop5ByDisplayStatusAndDeleteStatusOrderByVisitedDesc(string, i);
	}

	@Caching(evict = {
			@CacheEvict(value = "top5Blogs", allEntries = true),
			@CacheEvict(value = "top5LatestBlogsByStatus", allEntries = true),
			@CacheEvict(value = "top10BlogByStatus",allEntries = true)
	})
	public void updateBlogsVisitor(Blogs blog) {
		this.blogRepository.save(blog);
	}

	public void updateRssFeed() {
		BufferedWriter writer=null;
		try {	    
	    //for rss feed
		StringBuffer str =new StringBuffer("<rss xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:content=\"http://purl.org/rss/1.0/modules/content/\" xmlns:atom=\"http://www.w3.org/2005/Atom\" xmlns:media=\"http://search.yahoo.com/mrss/\" version=\"2.0\">"
				+ "<channel>\r\n"
				+ "<atom:link href=\""+domain+"/knowledge-centre/rssFeed.xml\" rel=\"self\" type=\"application/rss+xml\"/>\r\n"
				+ "<title>Knowledge Centre: Learn New things | Corpseed</title>\r\n"
				+ "<link>"+domain+"/knowledge-centre</link>\r\n"
				+ "<description>\r\n"
				+ "<![CDATA[ <p>Learn New things.</p> ]]>\r\n"
				+ "</description>\r\n"
				+ "<category>Article</category>\r\n"
				+ "<ttl>60</ttl>\r\n"
				+ "<lastBuildDate>"+new Date()+"</lastBuildDate>\r\n"
				+ "<copyright>Copyright (C) 2021 Corpseed ITES Pvt Ltd. All Rights Reserved.</copyright>\r\n"
				+ "<language>en-US</language>\r\n"
				+ "<docs>"+domain+"</docs>\r\n"
				+ "<image>\r\n"
				+ "<title>Knowledge Cent: Learn New things | Corpseed</title>\r\n"
				+ "<link>"+domain+"/knowledge-centre</link>\r\n"
				+ "<url>"+domain+"/assets/img/logo.png</url>\r\n"
				+ "</image>");
		
		List<Blogs> blogs=getAllBlogs();
		blogs.forEach(b->{			
		
		str.append("<item>\r\n"
				+ "<title>\r\n"
				+ "<![CDATA[ "+b.getTitle()+" ]]></title>\r\n"
				+ "<description>\r\n"
				+ "<![CDATA[ "+b.getMetaDescription()+" ]]>\r\n"
				+ "</description>\r\n"
				+ "<link>\r\n"
				+ "<![CDATA[ "+domain+"/knowledge-centre/"+b.getSlug()+" ]]>\r\n"
				+ "</link>\r\n"
				+ "<guid>\r\n"
				+ "<![CDATA[ "+domain+"/knowledge-centre/"+b.getSlug()+" ]]>\r\n"
				+ "</guid>\r\n"
				+ "<category>Article</category>\r\n"
				+ "<pubDate>\r\n"
				+ "<![CDATA[ "+this.commonService.getGMTDate(b.getPostDate())+" ]]>\r\n"
				+ "</pubDate>\r\n<dc:creator>\r\n"
				+ "<![CDATA[ "+b.getPostedByName()+" ]]>\r\n"
				+ "</dc:creator>"
				+ "<media:content height=\"900\" medium=\"image\" url=\""+azure_path+""+b.getImage()+"\" width=\"1600\"/>\r\n"
				+ "</item>");		
		
		});
		
		str.append("</channel>\r\n"
				+ "</rss>");
	    writer = new BufferedWriter(new FileWriter(rssFeed));
	    writer.write(str.toString());
	    writer.close();   
	    
//	    System.out.println(str1);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(writer!=null)
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	public void updateSiteMap() {
		BufferedWriter writer=null;
		try {
			String today = this.commonService.getToday();
		//for sitemap
	    StringBuffer str =new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
	    		+ "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" >\r\n"
	    		+ "  <url>\r\n"
	    		+ "    <loc>"+domain+"</loc>\r\n"
	    		+ "    <lastmod>"+today+"</lastmod>\r\n"
	    		+ "    <changefreq>daily</changefreq>\r\n"
	    		+ "    <priority>1.0</priority>\r\n"
	    		+ "  </url>");
	    	
	    List<SiteMapUrl> siteMap=this.siteMapService.getByStatus(1);
	    siteMap.forEach(s->{			
				
		str.append("\n<url>\r\n"
				+ "    <loc>"+domain+""+s.getUrl()+"</loc>\r\n"
				+ "    <lastmod>"+today+"</lastmod>\r\n"
				+ "    <changefreq>daily</changefreq>\r\n"
				+ "    <priority>0.9</priority>\r\n"
				+ "  </url>");
		
		});
		   
	    str.append("\n</urlset>");
	    writer = new BufferedWriter(new FileWriter(sitemap));
	    writer.write(str.toString());	    
	    writer.close();
//	    System.out.println(str1);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(writer!=null)
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public List<Blogs> findByPostedByUuidAndDeleteStatus(String uuid, int deleteStatus) {
		return this.blogRepository.findByPostedByUuidAndDeleteStatus(uuid,deleteStatus);
	}

	public List<Blogs> findByTitleContaining(String value) {
		// TODO Auto-generated method stub
		return this.blogRepository.findByDeleteStatusAndDisplayStatusAndTitleIgnoreCaseContaining(2,"1",value);
	}

	@Cacheable("top5LatestBlogsByStatus")
	public List<Blogs> findTop5ByDisplayStatusAndDeleteStatusOrderByIdDesc(String ds, int i) {
		// TODO Auto-generated method stub
		return this.blogRepository.findTop5ByDisplayStatusAndDeleteStatusOrderByIdDesc(ds,i);
	}

	public List<Blogs> findTop5ByDisplayStatusAndDeleteStatusAndPostedByUuidOrderByIdDesc(String ds, int d, String uuid) {
		return this.blogRepository.findTop5ByDisplayStatusAndDeleteStatusAndPostedByUuidOrderByIdDesc(ds,d,uuid);
	}

	public Page<Blogs> findBlogsByStatusAndUserUuid(Pageable pageable, String uuid) {
		return this.blogRepository.findByDisplayStatusAndDeleteStatusAndPostedByUuid("1",2,pageable,uuid);
	}

	public Page<Blogs> findBlogsByCategoryAndStatusAndUserUuid(Category category, Pageable pageable, String uuid) {
		return this.blogRepository.findByCategoryAndDisplayStatusAndDeleteStatusAndPostedByUuid(category,"1",2,pageable,uuid);
	}

	@Cacheable("top10BlogByStatus")
    public List<Blogs> findTop10ByDisplayStatusAndDeleteStatus(String displayStatus, int deleteStatus) {
		return this.blogRepository.findTop10ByDisplayStatusAndDeleteStatusOrderByVisitedDesc(displayStatus,deleteStatus);
    }
}
