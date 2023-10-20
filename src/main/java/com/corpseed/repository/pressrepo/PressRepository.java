package com.corpseed.repository.pressrepo;

import com.corpseed.entity.pressentity.Press;
import com.corpseed.entity.pressentity.PressCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PressRepository extends JpaRepository<Press, Long> {


    Press findBySlug(String slug);

    Press findByIdAndDeleteStatus(long id, int i);

    Press findBySlugAndIdNot(String slug, long id);

    Page<Press> findByPressCategoryAndDisplayStatusAndDeleteStatus(PressCategory category, String i, int j, Pageable pageable);

    Page<Press> findByDisplayStatusAndDeleteStatus(String i, int j, Pageable pageable);

    List<Press> findTop5ByDisplayStatusAndDeleteStatusOrderByVisitedDesc(String ds, int i);

    List<Press> findByTitleContainingAndDeleteStatus(String value,int dStatus);

    List<Press> findTop5ByDisplayStatusAndDeleteStatusOrderByIdDesc(String ds, int i);

    List<Press> findByDeleteStatusOrderByIdDesc(int i);


}
