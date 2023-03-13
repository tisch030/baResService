package companyx.ResourceServer.controller;

import companyx.ResourceServer.user.UserProfile;
import companyx.ResourceServer.user.UserProfileService;
import edu.umd.cs.findbugs.annotations.NonNull;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller which provides the necessary endpoints in order to retrieve the user profile information.
 */
@RestController
@RequiredArgsConstructor
public class UserProfileController {

    private static final String USER_PROFILE_ENDPOINT = "/user/{personId}";

    @NonNull
    private final UserProfileService userProfileService;

    @NonNull
    @GetMapping(value = USER_PROFILE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserProfile> queryUser(@PathVariable @NotBlank final String personId) {
        final UserProfile userProfile = userProfileService.loadUserProfile(personId);
        return ResponseEntity.ok()
                .body(userProfile);
    }
}
