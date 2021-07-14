package zatribune.spring.pps.data.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
@Table
public class Pic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn (name = "user", referencedColumnName = "id")
    private User user;

    private String description;

    @Enumerated(EnumType.STRING)
    private PicCategory category;

    private String path;

    @Enumerated(EnumType.STRING)
    private PicStatus status;

}
