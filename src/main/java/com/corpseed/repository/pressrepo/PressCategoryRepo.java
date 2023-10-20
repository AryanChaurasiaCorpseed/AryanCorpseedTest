package com.corpseed.repository.pressrepo;

import com.corpseed.entity.pressentity.PressCategory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

    @Repository
    public interface PressCategoryRepo extends JpaRepository<PressCategory, Long> {

        List<PressCategory> findByDeleteStatus(int i);

        PressCategory findBySlugOrTitle(String slug,String title);

        PressCategory findByIdAndDeleteStatus(long id, int i);

        PressCategory findBySlugOrTitleAndIdNot(String slug,String title, long id);

        List<PressCategory> findByDisplayStatusAndDeleteStatus(String i, int j);

        PressCategory findBySlug(String slug);

        @Query("select DISTINCT c from Press b, PressCategory c WHERE b.pressCategory=c and c.deleteStatus=2 order by b.visited desc")
        List<PressCategory> findTop10ByVisited(PageRequest pageRequest);
}
