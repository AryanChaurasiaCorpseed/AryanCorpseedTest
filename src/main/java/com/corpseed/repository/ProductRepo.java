package com.corpseed.repository;


import com.corpseed.entity.productentity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long>

{
    Product findBySlug(String slug);

    List<Product> findByDeleteStatusOrderByIdDesc(int dStatus);

    Product findByidAndDeleteStatus(Long id,int dStatus);

    List<Product> findDistinctProductNameByDisplayStatusAndDeleteStatus(String status, int dStatus);

    @Query("SELECT DISTINCT s.serviceName , s.slug FROM Services s JOIN Product p ON s.id = p.services.id where s.deleteStatus=2 AND p.deleteStatus=2")
    List<String> findServiceNamesByServiceId();

    List<Product> findTop15ByDeleteStatusOrderByVisitedDesc(int deleteStatus);

    Product findBySlugAndDeleteStatus(String slug, int dStatus );

    List<Product> findAllByDeleteStatus(int dStatus);

    @Query("select p from Product p where (p.slug= :slug or p.title= :title or p.id =:id) ")
    Product findBySlugAndTitle(@Param("slug") String slug, @Param("title") String title ,@Param("id") Long id);

    List<Product> findAllBySlug(String slugs);


    List<Product> findAllBySlugIn(List<String> slugList);
}




