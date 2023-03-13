package companyx.ResourceServer.template;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

public record TemplateSettings(@NonNull String id,
                               @Nullable String contactPersonMail,
                               @Nullable String contactPersonName,
                               @Nullable String universityName,
                               @Nullable String universityDisplayName,
                               @Nullable String universityUrl) {
}
