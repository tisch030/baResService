package companyx.ResourceServer.idp;

import edu.umd.cs.findbugs.annotations.NonNull;

public record IdentityProvider(@NonNull String id,
                               @NonNull String name,
                               @NonNull String uniqueIdentifierAttributeAtIdp,
                               boolean enabled,
                               @NonNull IdentityProviderType identityProviderType,
                               int position,
                               @NonNull String buttonLabel) {
}
