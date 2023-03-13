package companyx.ResourceServer.user;

import companyx.ResourceServer.user.repository.UserInfoRepository;
import edu.umd.cs.findbugs.annotations.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service that manages all user profile related information.
 */
@Service
@RequiredArgsConstructor
public class UserProfileService {

    @NonNull
    private final UserInfoRepository userInfoRepository;

    /**
     * Returns the user profile information of the given person.
     *
     * @param personId the id of the person for which the profile should get returned.
     * @return the user profile information of the given person.
     */
    @NonNull
    public UserProfile loadUserProfile(@NonNull final String personId) {
        return userInfoRepository.retrieveUserProfileOfPersonById(personId);
    }

}
