package zatribune.spring.pps.domain;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryCardResponse {

    private String name;

    private String description;

    private Integer count;

}
