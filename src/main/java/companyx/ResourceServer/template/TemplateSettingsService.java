package companyx.ResourceServer.template;

import companyx.ResourceServer.template.repository.TemplateSettingsRepository;
import edu.umd.cs.findbugs.annotations.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Services that manages template settings.
 */
@Service
@RequiredArgsConstructor
public class TemplateSettingsService {

    @NonNull
    private final TemplateSettingsRepository templateSettingsRepository;

    /**
     * Returns the template settings.
     *
     * @return the template settings.
     */
    @NonNull
    public TemplateSettings loadTemplateSettings() {
        return templateSettingsRepository.retrieveSettings();
    }
}
