package com.corpseed.service;

import com.corpseed.entity.productentity.Product;

import java.util.List;

public interface ProductService {

    Product findBySlug(String slug);
    Product saveProduct(Product product);
    List<Product> getAllProducts();
    Product getProductById(Long id);
    List<Product> getProductByNameAndStatus(String status);
    List<String> getServiceNameByServiceId();
    List<Product> getTopServiceNameByVisitedStatus(int deleteStatus);
    Product findBySlugAndDeleteStatus(String slug);
    List<Product> getAllProductByDeleteStatus(int deleteStatus);
    Product getProductBySlugAndTitle(String slug, String title ,Long id);
    List<Product> getProductBySlug(String slugs);

    List<Product> findBySlugIn(List<String> slugList);
}
