package companyx.ResourceServer.log.repository;

import companyx.ResourceServer.log.AuthenticationLog;
import companyx.ResourceServer.log.AuthenticationOperation;
import edu.umd.cs.findbugs.annotations.NonNull;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Repository;

import java.util.List;

import static eu.companyx.cms.res.dto.tables.AuthenticationLog.AUTHENTICATION_LOG;

/**
 * {@link AuthenticationLogRepository} implementation which uses JOOQ to access the authentication logs in a database.
 */
@Repository
@ConditionalOnClass(DSLContext.class)
@RequiredArgsConstructor
public class JooqAuthenticationLogRepository implements AuthenticationLogRepository {

    @NonNull
    private final DSLContext dsl;

    @Override
    @NonNull
    public List<AuthenticationLog> retrieveAuthenticationLogEntries() {

        return dsl.select(
                        AUTHENTICATION_LOG.ID,
                        AUTHENTICATION_LOG.AUTHENTICATION_OPERATION,
                        AUTHENTICATION_LOG.IP_ADDRESS,
                        AUTHENTICATION_LOG.IP_ADDRESS_ANONYMIZED,
                        AUTHENTICATION_LOG.USERNAME,
                        AUTHENTICATION_LOG.PERSON_ID,
                        AUTHENTICATION_LOG.CREATED_AT)
                .from(AUTHENTICATION_LOG)
                .orderBy(AUTHENTICATION_LOG.CREATED_AT.desc())
                .fetch(row -> new AuthenticationLog(
                        row.get(AUTHENTICATION_LOG.ID),
                        AuthenticationOperation.valueOf(row.get(AUTHENTICATION_LOG.AUTHENTICATION_OPERATION)),
                        row.get(AUTHENTICATION_LOG.IP_ADDRESS),
                        row.get(AUTHENTICATION_LOG.IP_ADDRESS_ANONYMIZED),
                        row.get(AUTHENTICATION_LOG.USERNAME),
                        row.get(AUTHENTICATION_LOG.PERSON_ID),
                        row.get(AUTHENTICATION_LOG.CREATED_AT)
                ));
    }
}