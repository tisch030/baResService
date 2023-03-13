package companyx.ResourceServer.idp.oidc;

import companyx.ResourceServer.idp.IdentityProvider;
import edu.umd.cs.findbugs.annotations.NonNull;

public record OidcProvider(@NonNull IdentityProvider identityProvider,
                           @NonNull OidcSettings oidcSettings) {
}