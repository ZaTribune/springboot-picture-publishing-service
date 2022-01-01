//package zatribune.spring.pps.data.repositories;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Profile;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.context.ActiveProfiles;
//import zatribune.spring.pps.config.SecurityConfig;
//import zatribune.spring.pps.data.entities.Pic;
//import zatribune.spring.pps.data.repositories.elastic.PicElasticRepository;
//
//@ActiveProfiles(value="integration_test")
//@SpringBootTest
//class PicElasticRepositoryIT {
//
//
//    @Autowired
//    PicElasticRepository picElasticRepository;
//
//    @Test
//    void findByDescription() {
//
//        Page<Pic>page=picElasticRepository.findByDescription("Be honest! it's better than a dog.", Pageable.ofSize(5));
//
//        System.out.println(page.getContent());
//        System.out.println(page.getTotalElements());
//    }
//}