package companyx.ResourceServer.idp.saml;

import companyx.ResourceServer.idp.IdentityProvider;
import edu.umd.cs.findbugs.annotations.NonNull;

public record SamlProvider(@NonNull IdentityProvider identityProvider,
                           @NonNull SamlSettings samlSettings,
                           @NonNull SamlServiceProviderInformationUi samlServiceProviderInformationUi) {
}
