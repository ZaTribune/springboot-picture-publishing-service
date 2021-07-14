package zatribune.spring.pps.services;

import zatribune.spring.pps.data.entities.Pic;
import zatribune.spring.pps.data.entities.PicCategory;
import zatribune.spring.pps.data.entities.PicStatus;
import zatribune.spring.pps.data.entities.User;

import java.util.List;
import java.util.Optional;

public interface PicService {
    List<Pic>getAll();
    List<Pic>getAllByStatus(List<PicStatus> status);
    List<Pic>getAllByCategory(List<PicCategory>categories);
    List<Pic> getAllByUser(User user);
    Optional<Pic>getById(Long id);
    Pic save(Pic pic);
    void delete(Pic pic);
}
