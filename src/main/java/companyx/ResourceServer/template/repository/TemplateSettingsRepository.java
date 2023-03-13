package companyx.ResourceServer.template.repository;

import companyx.ResourceServer.template.TemplateSettings;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Base interface for classes/interfaces which implement a repository for {@link TemplateSettings}.
 */
public interface TemplateSettingsRepository {

    @NonNull
    TemplateSettings retrieveSettings();
}
