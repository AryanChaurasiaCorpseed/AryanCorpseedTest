package com.corpseed.serviceImpl;

import com.corpseed.entity.productentity.Product;
import com.corpseed.repository.ProductRepo;
import com.corpseed.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepo productRepo;

    public Product findBySlug(String slug) {
        // TODO Auto-generated method stub
        System.out.println(slug);
        return this.productRepo.findBySlug(slug);

    }

    public Product saveProduct(Product product)
    {
        return this.productRepo.save(product);
    }

    public List<Product> getAllProducts() {
        return this.productRepo.findByDeleteStatusOrderByIdDesc(2);
    }

    public Product getProductById(Long id) {

        return this.productRepo.findByidAndDeleteStatus(id, 2);
    }

    public List<Product> getProductByNameAndStatus(String status) {
        return this.productRepo.findDistinctProductNameByDisplayStatusAndDeleteStatus(status, 2);
    }

    public List<String> getServiceNameByServiceId() {
        return this.productRepo.findServiceNamesByServiceId();
    }

    public List<Product> getTopServiceNameByVisitedStatus(int deleteStatus)
    {
        return this.productRepo.findTop15ByDeleteStatusOrderByVisitedDesc(deleteStatus);
    }

    public Product findBySlugAndDeleteStatus(String slug) {
        return this.productRepo.findBySlugAndDeleteStatus(slug,2);
    }

    public List<Product> getAllProductByDeleteStatus(int deleteStatus)
    {
        return this.productRepo.findAllByDeleteStatus(deleteStatus);
    }

    public Product getProductBySlugAndTitle(String slug, String title ,Long id)
    {
        return this.productRepo.findBySlugAndTitle(slug,title,id);
    }


    public List<Product> getProductBySlug(String slugs) {

        return this.productRepo.findAllBySlug(slugs);

    }

    @Override
    public List<Product> findBySlugIn(List<String> slugList) {
        return this.productRepo.findAllBySlugIn(slugList);
    }


}




