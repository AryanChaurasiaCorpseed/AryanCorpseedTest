package com.corpseed.serviceImpl.newsserviceimpl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import com.corpseed.serviceImpl.CommonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.corpseed.entity.newsentity.News;
import com.corpseed.entity.newsentity.NewsCategory;
import com.corpseed.repository.newsrepo.NewsRepository;
import com.corpseed.service.newsservice.NewsService;

@Service
public class NewsServiceImpl implements NewsService {

	@Autowired
	private NewsRepository newsRepository;
	
	@Autowired
	private CommonServices commonService;
	
	@Value("${azure_path}")
	private String azure_path;
	
	@Value("${rssFeedNews.path}")
	private String rssFeed;
	
	@Override
	public List<News> findAll() {
		// TODO Auto-generated method stub
		return this.newsRepository.findByDeleteStatusOrderByIdDesc(2);
	}

	@Caching(evict = {
			@CacheEvict(value = "top5NewsByStatus",allEntries = true),
			@CacheEvict(value = "top5LatestNewsByStatus",allEntries = true)
	})
	@Override
	public void saveAllNews(List<News> news) {
		// TODO Auto-generated method stub
		this.newsRepository.saveAll(news);
	}

	@Caching(evict = {
			@CacheEvict(value = "top5NewsByStatus",allEntries = true),
			@CacheEvict(value = "top5LatestNewsByStatus",allEntries = true)
	})
	@Override
	public News saveNews(@Valid News news) {
		// TODO Auto-generated method stub
		return this.newsRepository.save(news);
	}

	@Override
	public News findBySlug(String slug) {
		// TODO Auto-generated method stub
		return this.newsRepository.findBySlug(slug);
	}

	@Override
	public News findById(long id) {
		// TODO Auto-generated method stub
		return this.newsRepository.findByIdAndDeleteStatus(id,2);
	}

	@Override
	public News findBySlugAndIdNot(String slug, long id) {
		// TODO Auto-generated method stub
		return this.newsRepository.findBySlugAndIdNot(slug,id);
	}

	@Override
	public News findByIdAndDeleteStatus(long typeId, int i) {
		// TODO Auto-generated method stub
		return this.newsRepository.findByIdAndDeleteStatus(typeId, i);
	}

	@Caching(evict = {
			@CacheEvict(value = "top5NewsByStatus",allEntries = true),
			@CacheEvict(value = "top5LatestNewsByStatus",allEntries = true)
	})
	@Override
	public void deleteNews(News news) {
		// TODO Auto-generated method stub
		this.newsRepository.delete(news);
	}

	@Override
	public Page<News> findByCategoryAndDisplayStatusAndDeleteStatus(NewsCategory category, String i, int j,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return this.newsRepository.findByNewsCategoryAndDisplayStatusAndDeleteStatus(category,i,j,pageable);
	}

	@Override
	public Page<News> findByDisplayStatusAndDeleteStatus(String i, int j, Pageable pageable) {
		// TODO Auto-generated method stub
		return this.newsRepository.findByDisplayStatusAndDeleteStatus(i,j,pageable);
	}

	@Cacheable("top5NewsByStatus")
	@Override
	public List<News> findTop5ByDisplayStatusAndDeleteStatusOrderByVisitedDesc(String ds, int i) {
		// TODO Auto-generated method stub
		return this.newsRepository.findTop5ByDisplayStatusAndDeleteStatusOrderByVisitedDesc(ds,i);
	}

	@Cacheable("top5LatestNewsByStatus")
	@Override
	public List<News> findTop5ByDisplayStatusAndDeleteStatusOrderByIdDesc(String ds, int i) {
		// TODO Auto-generated method stub
		return this.newsRepository.findTop5ByDisplayStatusAndDeleteStatusOrderByIdDesc(ds,i);
	}

	@Override
	public List<News> findByTitleContaining(String value) {
		// TODO Auto-generated method stub
		return this.newsRepository.findByTitleContaining(value);
	}

	@Override
	public void updateNewsRssFeed() {
			BufferedWriter writer=null;
			try {			
		    //for rss feed
			StringBuffer str =new StringBuffer("<rss xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:content=\"http://purl.org/rss/1.0/modules/content/\" xmlns:atom=\"http://www.w3.org/2005/Atom\" xmlns:media=\"http://search.yahoo.com/mrss/\" version=\"2.0\">"
					+ "<channel>\r\n"
					+ "<atom:link href=\"https://www.corpseed.com/news/rssFeed.xml\" rel=\"self\" type=\"application/rss+xml\"/>\r\n"
					+ "<title>News: Learn New things | Corpseed</title>\r\n"
					+ "<link>https://www.corpseed.com/news</link>\r\n"
					+ "<description>\r\n"
					+ "<![CDATA[ <p>Learn New things.</p> ]]>\r\n"
					+ "</description>\r\n"
					+ "<category>Article</category>\r\n"
					+ "<ttl>60</ttl>\r\n"
					+ "<lastBuildDate>"+new Date()+"</lastBuildDate>\r\n"
					+ "<copyright>Copyright (C) 2021 Corpseed ITES Pvt Ltd. All Rights Reserved.</copyright>\r\n"
					+ "<language>en-US</language>\r\n"
					+ "<docs>https://www.corpseed.com</docs>\r\n"
					+ "<image>\r\n"
					+ "<title>News: Learn New things | Corpseed</title>\r\n"
					+ "<link>https://www.corpseed.com/news</link>\r\n"
					+ "<url>https://www.corpseed.com/assets/img/logo.png</url>\r\n"
					+ "</image>");
			
			List<News> news=findAll();
			news.forEach(n->{			
			
			str.append("<item>\r\n"
					+ "<title>\r\n"
					+ "<![CDATA[ "+n.getTitle()+" ]]></title>\r\n"
					+ "<description>\r\n"
					+ "<![CDATA[ "+n.getMetaDescription()+" ]]>\r\n"
					+ "</description>\r\n"
					+ "<link>\r\n"
					+ "<![CDATA[ https://www.corpseed.com/news/"+n.getSlug()+" ]]>\r\n"
					+ "</link>\r\n"
					+ "<guid>\r\n"
					+ "<![CDATA[ https://www.corpseed.com/news/"+n.getSlug()+" ]]>\r\n"
					+ "</guid>\r\n"
					+ "<category>Article</category>\r\n"
					+ "<pubDate>\r\n"
					+ "<![CDATA[ "+this.commonService.getGMTDate(n.getPostDate())+" ]]>\r\n"
					+ "</pubDate>\r\n<dc:creator>\r\n"
					+ "<![CDATA[ "+n.getAuthorName()+" ]]>\r\n"
					+ "</dc:creator>"
					+ "<media:content height=\"900\" medium=\"image\" url=\""+azure_path+""+n.getImage()+"\" width=\"1600\"/>\r\n"
					+ "</item>");
			
			});
			
			str.append("</channel>\r\n"
					+ "</rss>");
		    writer = new BufferedWriter(new FileWriter(rssFeed));
		    writer.write(str.toString());
		    writer.close();	    
		    //rssFeed writer closed	   
		   
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

}
