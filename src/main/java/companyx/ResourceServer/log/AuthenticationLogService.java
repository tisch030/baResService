package companyx.ResourceServer.log;

import companyx.ResourceServer.log.repository.AuthenticationLogRepository;
import edu.umd.cs.findbugs.annotations.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service that manages the authentication log.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationLogService {

    @NonNull
    private final AuthenticationLogRepository authenticationLogRepository;

    /**
     * Returns all authentication log entries.
     *
     * @return all authentication log entries.
     */
    @NonNull
    public List<AuthenticationLog> loadAuthenticationLogEntries() {
        return authenticationLogRepository.retrieveAuthenticationLogEntries();
    }
}
