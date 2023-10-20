package com.corpseed.repository.servicerepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.corpseed.entity.Category;
import com.corpseed.entity.serviceentity.Services;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<Services, Long> {

	List<Services> findByCategoryAndDeleteStatus(Category category,int dStatus);

	Services findByUuidAndDeleteStatus(String serviceUUID,int dStatus);

//	Services findByServiceNameAndCategoryAndUuidNot(String serviceName, Category category, String uuid);

	Services findBySlugAndDeleteStatus(String slug,int dStatus);

	List<Services> findByCategoryAndDisplayStatusAndDeleteStatus(Category category, String status,int dStatus);

	List<Services> findByServiceNameIgnoreCaseContainingAndDeleteStatus(String value,int dStatus);

	List<Services> findTop6ByDeleteStatusOrderByIdDesc(int dStatus);

//	Services findBySlugAndCategory(String slug, Category category);

	Services findByIdAndCategoryAndDeleteStatus(long serviceId, Category category,int dStatus);

	List<Services> findByDeleteStatusOrderByIdDesc(int dStatus);

	List<Services> findByUuidAndDisplayStatusAndDeleteStatus(String uuid, String status,int dStatus);

	Services findByIdAndDisplayStatus(long id, String status);

	List<Services> findByDisplayStatusAndDeleteStatus(String status,int dStatus);

	@Query("select s from Services s where s.displayStatus=?1 and s.deleteStatus=2 and s.servicePackage IS NOT EMPTY")
	List<Services> findByDisplayStatusAndPackageNotEmpty(@Param("status") String status);

	Services findByProductNoAndUuidNot(String productNo,String uuid);

	Services findByProductNo(String productNo);

	Services findBySlugOrServiceName(String slug, String serviceName);

	@Query("select s from Services s where (s.slug= :slug or s.serviceName= :serviceName) and s.uuid!= :uuid")
	Services findBySlugOrServiceNameAndUuidNotIn(@Param("slug") String slug,@Param("serviceName") String serviceName,@Param("uuid") String uuid);

	Services findByIdAndDeleteStatus(long serviceId,int dStatus);

	@Query("select s from Services s, Category c WHERE s.category=c and s.deleteStatus=2 and c.categoryName= :category")
	List<Services> findByCategoryName(@Param("category") String category);

	@Query(nativeQuery = true, value="SELECT * FROM Services where " +
			"display_status= ?1 and delete_status= ?2 order by visited desc limit 4")
	List<Services> findTop4ByDisplayStatusAndDeleteStatusOrderByVisitedDesc(String string, int i);

	@Query(nativeQuery = true, value="SELECT * FROM Services where " +
			"display_status= ?1 and delete_status= ?2 and " +
			"(service_name like %?3% or meta_keyword like %?3% or meta_description like %?3% or " +
			"meta_title like %?3% or slug like %?3% or summary like %?3%) order by visited desc limit 4")
	List<Services> findTop4ByDisplayStatusAndDeleteStatusAndServiceNameIgnoreCaseContaining(String displayStatus,int deleteStatus,String searchKey);

	@Query("SELECT distinct s from Services s,Product p where p.services=s and s.deleteStatus=?1 and s.displayStatus=?2")
	List<Services> findAllProductServicesByDeleteStatus(int dStatus,String displayStatus);

	@Query("SELECT s FROM Services s WHERE NOT EXISTS (SELECT 1 FROM ServiceCity c WHERE c.services = s.serviceCity)")
    List<Services> findAllServicesNotInCity(int deletStatus);
}
