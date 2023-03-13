package companyx.ResourceServer.template.repository;

import companyx.ResourceServer.template.TemplateSettings;
import edu.umd.cs.findbugs.annotations.NonNull;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Repository;

import static eu.companyx.cms.res.dto.tables.TemplateSettings.TEMPLATE_SETTINGS;

/**
 * {@link TemplateSettingsRepository} implementation which uses JOOQ to access the template settings in a database.
 */
@Repository
@ConditionalOnClass(DSLContext.class)
@RequiredArgsConstructor
public class JooqTemplateSettingsRepository implements TemplateSettingsRepository {

    @NonNull
    private final DSLContext dsl;

    @NonNull
    @Override
    public TemplateSettings retrieveSettings() {
        return dsl.select(
                        TEMPLATE_SETTINGS.ID,
                        TEMPLATE_SETTINGS.CONTACT_PERSON_MAIL,
                        TEMPLATE_SETTINGS.CONTACT_PERSON_NAME,
                        TEMPLATE_SETTINGS.UNIVERSITY_NAME,
                        TEMPLATE_SETTINGS.UNIVERSITY_DISPLAY_NAME,
                        TEMPLATE_SETTINGS.UNIVERSITY_URL)
                .from(TEMPLATE_SETTINGS)
                .fetchSingleInto(TemplateSettings.class);
    }
}
