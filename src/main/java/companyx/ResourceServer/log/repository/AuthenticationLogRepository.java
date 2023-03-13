package companyx.ResourceServer.log.repository;

import companyx.ResourceServer.log.AuthenticationLog;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.List;

/**
 * Base interface for classes/interfaces which implement a repository for {@link AuthenticationLog} information.
 */
public interface AuthenticationLogRepository {

    /**
     * Returns all authentication log entries.
     *
     * @return all authentication log entries.
     */
    @NonNull
    List<AuthenticationLog> retrieveAuthenticationLogEntries();
}
