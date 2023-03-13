package companyx.ResourceServer.idp.saml;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

public record SamlServiceProviderInformationUi(@NonNull String entityId,
                                               @Nullable String idpInitiatedLogoutEndpoint,
                                               @Nullable String x509certificate) {
}
