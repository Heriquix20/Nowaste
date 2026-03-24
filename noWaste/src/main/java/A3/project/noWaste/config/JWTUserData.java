package A3.project.noWaste.config;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JWTUserData {

    private Integer userId;

    private String email;

}
