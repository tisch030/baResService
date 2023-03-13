package companyx.ResourceServer.user.repository;

import companyx.ResourceServer.user.UserProfile;
import edu.umd.cs.findbugs.annotations.NonNull;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

import static eu.companyx.cms.res.dto.tables.Person.PERSON;

/**
 * {@link UserInfoRepository} implementation which uses JOOQ to access the user profile in a database.
 */
@Repository
@ConditionalOnClass(DSLContext.class)
@RequiredArgsConstructor
public class JooqUserProfileRepository implements UserInfoRepository {

    @NonNull
    private final DSLContext dsl;

    @NonNull
    @Override
    public UserProfile retrieveUserProfileOfPersonById(@NonNull final String personId) {
        return dsl.select(PERSON.NAME,
                        PERSON.ACTIVITIES,
                        PERSON.MODULES)
                .from(PERSON)
                .where(PERSON.ID.eq(personId))
                .fetchOne(row -> new UserProfile(
                        row.get(PERSON.NAME),
                        Arrays.stream(row.get(PERSON.ACTIVITIES).split(","))
                                .map(String::trim)
                                .toList(),
                        Arrays.stream(row.get(PERSON.MODULES).split(","))
                                .map(String::trim)
                                .toList()
                ));
    }
}
