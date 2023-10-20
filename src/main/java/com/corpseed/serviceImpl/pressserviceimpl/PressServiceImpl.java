package com.corpseed.serviceImpl.pressserviceimpl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import com.corpseed.entity.pressentity.Press;
import com.corpseed.entity.pressentity.PressCategory;
import com.corpseed.repository.pressrepo.PressRepository;
import com.corpseed.service.pressservice.PressService;
import com.corpseed.serviceImpl.CommonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PressServiceImpl implements PressService {

    @Autowired
    private PressRepository pressRepository;

    @Autowired
    private CommonServices commonService;

    @Value("${azure_path}")
    private String azure_path;

    @Value("${rssFeedNews.path}")
    private String rssFeed;



    @Override
    public List<Press> findAll() {
        // TODO Auto-generated method stub
        return this.pressRepository.findByDeleteStatusOrderByIdDesc(2);
    }

    @Caching(evict = {
            @CacheEvict(value = "latest5PressRelease",allEntries = true)
    })
    @Override
    public void saveAllPress(List<Press> press) {
        // TODO Auto-generated method stub
        this.pressRepository.saveAll(press);
    }

    @Caching(evict = {
            @CacheEvict(value = "latest5PressRelease",allEntries = true)
    })
    @Override
    public Press savePress(@Valid Press press) {
        // TODO Auto-generated method stub
        return this.pressRepository.save(press);
    }

    @Override
    public Press findBySlug(String slug) {
        // TODO Auto-generated method stub
        return this.pressRepository.findBySlug(slug);
    }

    @Override
    public Press findById(long id) {
        // TODO Auto-generated method stub
        return this.pressRepository.findByIdAndDeleteStatus(id,2);
    }

    @Override
    public Press findBySlugAndIdNot(String slug, long id) {
        // TODO Auto-generated method stub
        return this.pressRepository.findBySlugAndIdNot(slug,id);
    }

    @Override
    public Press findByIdAndDeleteStatus(long typeId, int i) {
        // TODO Auto-generated method stub
        return this.pressRepository.findByIdAndDeleteStatus(typeId, i);
    }

    @Caching(evict = {
            @CacheEvict(value = "latest5PressRelease",allEntries = true)
    })
    @Override
    public void deletePress(Press press) {
        // TODO Auto-generated method stub
        this.pressRepository.delete(press);
    }

    @Override
    public Page<Press> findByCategoryAndDisplayStatusAndDeleteStatus(PressCategory category, String i, int j, Pageable pageable)

    {
        // TODO Auto-generated method stub
        return this.pressRepository.findByPressCategoryAndDisplayStatusAndDeleteStatus(category,i,j,pageable);
    }

    @Override
    public Page<Press> findByDisplayStatusAndDeleteStatus(String i, int j, Pageable pageable) {
        // TODO Auto-generated method stub
        return this.pressRepository.findByDisplayStatusAndDeleteStatus(i,j,pageable);
    }

    @Override
    public List<Press> findTop5ByDisplayStatusAndDeleteStatusOrderByVisitedDesc(String ds, int i) {
        // TODO Auto-generated method stub
        return this.pressRepository.findTop5ByDisplayStatusAndDeleteStatusOrderByVisitedDesc(ds,i);
    }

    @Cacheable("latest5PressRelease")
    @Override
    public List<Press> findTop5ByDisplayStatusAndDeleteStatusOrderByIdDesc(String ds, int i) {
        // TODO Auto-generated method stub
        return this.pressRepository.findTop5ByDisplayStatusAndDeleteStatusOrderByIdDesc(ds,i);
    }

    @Override
    public List<Press> findByTitleContainingAndDeleteStatus(String value,int dStatus) {
        // TODO Auto-generated method stub
        return this.pressRepository.findByTitleContainingAndDeleteStatus(value,dStatus);
    }

    @Override
    public void updatePressRssFeed() {
        BufferedWriter writer=null;
        try {
            //for rss feed
            StringBuffer str =new StringBuffer("<rss xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:content=\"http://purl.org/rss/1.0/modules/content/\" xmlns:atom=\"http://www.w3.org/2005/Atom\" xmlns:media=\"http://search.yahoo.com/mrss/\" version=\"2.0\">"
                    + "<channel>\r\n"
                    + "<atom:link href=\"https://www.corpseed.com/press/rssFeed.xml\" rel=\"self\" type=\"application/rss+xml\"/>\r\n"
                    + "<title>Press: Learn New things | Corpseed</title>\r\n"
                    + "<link>https://www.corpseed.com/press</link>\r\n"
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
                    + "<title>Press: Learn New things | Corpseed</title>\r\n"
                    + "<link>https://www.corpseed.com/press</link>\r\n"
                    + "<url>https://www.corpseed.com/assets/img/logo.png</url>\r\n"
                    + "</image>");

            List<Press> press=findAll();
            press.forEach(n->{

                str.append("<item>\r\n"
                        + "<title>\r\n"
                        + "<![CDATA[ "+n.getTitle()+" ]]></title>\r\n"
                        + "<description>\r\n"
                        + "<![CDATA[ "+n.getMetaDescription()+" ]]>\r\n"
                        + "</description>\r\n"
                        + "<link>\r\n"
                        + "<![CDATA[ https://www.corpseed.com/press/"+n.getSlug()+" ]]>\r\n"
                        + "</link>\r\n"
                        + "<guid>\r\n"
                        + "<![CDATA[ https://www.corpseed.com/press/"+n.getSlug()+" ]]>\r\n"
                        + "</guid>\r\n"
                        + "<category>Article</category>\r\n"
                        + "<pubDate>\r\n"
                        + "<![CDATA[ "+this.commonService.getGMTDate(n.getPostDate())+" ]]>\r\n"
                        + "</pubDate>\r\n<dc:creator>\r\n"
                        + "<![CDATA[ "+n.getUser().getFirstName()+" ]]>\r\n"
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
