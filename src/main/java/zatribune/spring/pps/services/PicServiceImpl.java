package zatribune.spring.pps.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zatribune.spring.pps.data.entities.AppUser;
import zatribune.spring.pps.data.entities.Pic;
import zatribune.spring.pps.data.entities.PicCategory;
import zatribune.spring.pps.data.entities.PicStatus;
import zatribune.spring.pps.data.repositories.PicRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class PicServiceImpl implements PicService {

    private final PicRepository repository;


    @Autowired
    public PicServiceImpl(PicRepository picRepository) {
        this.repository = picRepository;
    }

    @Override
    public List<Pic> getAll() {
        return new ArrayList<>(repository.findAll());
    }

    @Override
    public List<Pic> getAllByStatus(List<PicStatus> status) {
        return new ArrayList<>(repository.findAllByStatusIn(status));
    }

    @Override
    public List<Pic> getAllByCategory(List<PicCategory> categories) {
        return new ArrayList<>(repository.findAllByCategoryIn(categories));
    }

    @Override
    public List<Pic> getAllByUser(AppUser appUser) {
        return new ArrayList<>(repository.findAllByAppUser(appUser));
    }

    @Override
    public Optional<Pic> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Pic save(Pic pic) {
        return repository.save(pic);
    }

    @Override
    public void delete(Pic pic) {
        repository.delete(pic);
    }
}
