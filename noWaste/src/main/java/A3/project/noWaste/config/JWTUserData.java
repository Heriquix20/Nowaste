package A3.project.noWaste.config;

import lombok.Builder;

@Builder
public record JWTUserData(Integer userId, String email) {
}
