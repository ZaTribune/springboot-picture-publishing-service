package zatribune.spring.pps.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zatribune.spring.pps.data.entities.PicCategory;
import zatribune.spring.pps.data.entities.Pic;
import zatribune.spring.pps.data.entities.PicStatus;
import zatribune.spring.pps.data.entities.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PicServiceImplTest {

    @Mock
    private PicServiceImpl picService;

    List<Pic> pics;

    User u1, u2;
    Pic pic1, pic2, pic3, pic4;//one for each status


    @BeforeEach
    void setup() {

        u1 = new User();
        u1.setId(1L);
        u1.setUsername("one.user@pps.com");

        u2 = new User();
        u1.setId(2L);
        u1.setUsername("two.user@pps.com");


        pic1 = new Pic();
        pic1.setId(1L);
        pic1.setDescription("Hello 1");
        pic1.setStatus(PicStatus.APPROVED);
        pic1.setPath("/pic1.jpg");
        pic1.setCategory(PicCategory.living_thing);
        pic1.setUser(u1);


        pic2 = new Pic();
        pic2.setId(2L);
        pic2.setDescription("Hello 2");
        pic2.setStatus(PicStatus.PENDING);
        pic2.setPath("/pic2.jpg");
        pic2.setCategory(PicCategory.machine);
        pic2.setUser(u1);


        pic3 = new Pic();
        pic3.setId(1L);
        pic3.setDescription("Hello 3");
        pic3.setStatus(PicStatus.DECLINED);
        pic3.setPath("/pic3.jpg");
        pic3.setCategory(PicCategory.nature);
        pic3.setUser(u2);

        pic4 = new Pic();
        pic4.setId(4L);
        pic4.setDescription("Hello 4");
        pic4.setStatus(PicStatus.DELETED);
        pic4.setPath("/pic4.jpg");
        pic4.setCategory(PicCategory.nature);
        pic4.setUser(u2);

        pics = List.of(pic1, pic2, pic3, pic4);

    }

    @Test
    void getAll() {
        when(picService.getAll()).thenReturn(pics);

        List<Pic>result=picService.getAll();

        assertNotEquals(0,result.size());
        assertEquals(4,pics.size());

    }

    @Test
    void getAllByStatus() {

        when(picService.getAllByStatus(List.of(PicStatus.APPROVED,PicStatus.PENDING))).thenReturn(List.of(pic1,pic2));
        List<Pic>result=picService.getAllByStatus(List.of(PicStatus.APPROVED,PicStatus.PENDING));
        assertNotEquals(0,result.size());
        assertEquals(2,result.size());
    }

    @Test
    void getAllByCategory() {

        when(picService.getAllByCategory(List.of(PicCategory.machine,PicCategory.living_thing))).thenReturn(List.of(pic1,pic2));
        List<Pic>result=picService.getAllByCategory(List.of(PicCategory.machine,PicCategory.living_thing));
        assertNotEquals(0,result.size());
        assertEquals(2,result.size());
    }

    @Test
    void getAllByUser() {

        when(picService.getAllByUser(u1)).thenReturn(List.of(pic1,pic2));
        List<Pic>result=picService.getAllByUser(u1);
        assertNotNull(result);
        assertEquals(2,result.size());
    }

    @Test
    void getById() {

        when(picService.getById(1L)).thenReturn(Optional.of(pic1));

        Optional<Pic> result=picService.getById(1L);

        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }

}