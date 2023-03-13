package companyx.ResourceServer.controller;

import companyx.ResourceServer.log.AuthenticationLog;
import companyx.ResourceServer.log.AuthenticationLogService;
import edu.umd.cs.findbugs.annotations.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller which provides the endpoint to request the authentication log information.
 */
@RestController
@RequiredArgsConstructor
public class AuthenticationLogController {

    private static final String AUTHENTICATION_LOG_ENDPOINT = "/authentication-log";

    @NonNull
    private final AuthenticationLogService authenticationLogService;

    @NonNull
    @GetMapping(value = AUTHENTICATION_LOG_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AuthenticationLog>> queryIdentityProviders() {
        final List<AuthenticationLog> authenticationLogs = authenticationLogService.loadAuthenticationLogEntries();
        return ResponseEntity.ok()
                .body(authenticationLogs);
    }

}
