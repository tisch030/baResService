package companyx.ResourceServer.user.repository;

import companyx.ResourceServer.user.UserProfile;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Base interface for classes/interfaces which implement a repository for {@link UserProfile} information.
 */
public interface UserInfoRepository {

    /**
     * Returns the user profile of the given person.
     *
     * @param personId the id of the person for which the profile should get returned.
     * @return the user profile of the given person.
     */
    @NonNull
    UserProfile retrieveUserProfileOfPersonById(@NonNull final String personId);
}
