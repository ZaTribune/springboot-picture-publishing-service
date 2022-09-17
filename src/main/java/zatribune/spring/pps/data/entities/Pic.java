package zatribune.spring.pps.data.entities;

import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Pic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn (name = "app_user", referencedColumnName = "id")
    private AppUser appUser;

    private String description;

    @Enumerated(EnumType.STRING)
    private PicCategory category;

    private String path;

    @Enumerated(EnumType.STRING)
    private PicStatus status;

}
