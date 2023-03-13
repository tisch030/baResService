package companyx.ResourceServer.idp.saml;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

public record SamlServiceProviderInformation(@NonNull String id,
                                             @NonNull String entityId,
                                             @Nullable String idpInitiatedLogoutEndpoint,
                                             @Nullable String privateKey,
                                             @Nullable String x509certificate) {
}
