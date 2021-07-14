package zatribune.spring.pps.data.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zatribune.spring.pps.data.entities.PicCategory;
import zatribune.spring.pps.data.entities.Pic;
import zatribune.spring.pps.data.entities.PicStatus;
import zatribune.spring.pps.data.entities.User;

import java.util.List;

public interface PicRepository extends JpaRepository<Pic,Long> {

    List<Pic>findAllByStatusIn(List<PicStatus>  status);

    @Query(value="update Pic m set m.status='DELETED' where m.id=?1")
    void delete(Long id);

    List<Pic>findAllByUser(User user);

    List<Pic>findAllByCategoryIn(List<PicCategory>categories);
}
