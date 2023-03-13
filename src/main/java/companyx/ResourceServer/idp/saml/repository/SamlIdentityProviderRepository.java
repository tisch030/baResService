package companyx.ResourceServer.idp.saml.repository;

import companyx.ResourceServer.idp.IdentityProviderType;
import companyx.ResourceServer.idp.saml.SamlProvider;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

import java.util.Optional;

/**
 * Base interface for classes/interfaces which implement a repository for {@link SamlProvider} information.
 */
public interface SamlIdentityProviderRepository {

    /**
     * Returns the {@link SamlProvider} with the given id or an empty optional if no provider
     * with the given id could be found.
     *
     * @return the {@link SamlProvider} with the given id or an empty optional if no provider
     * with the given id could be found.
     */
    @NonNull
    Optional<SamlProvider> findSamlIdentityProviderById(@NonNull final String identityProviderId);


    /**
     * Updates an existing saml identity provider with the given id and the new information.
     *
     * @param identityProviderId             the id of the identity provider which should be updated.
     * @param name                           the new name.
     * @param uniqueIdentifierAttributeAtIdp the new unique identifier attribute.
     * @param position                       the new position.
     * @param buttonLabel                    the new button text.
     * @param issuerUrl                      the new issuer url.
     * @param storkQaaLevel                  new stork qaa level.
     */
    void updateIdentityProvider(@NonNull final String identityProviderId,
                                @NonNull final String name,
                                @NonNull final String uniqueIdentifierAttributeAtIdp,
                                final int position,
                                @NonNull final String buttonLabel,
                                @NonNull String issuerUrl,
                                @Nullable String storkQaaLevel);

    /**
     * Create an identity provider which uses the SAML standard.
     *
     * @param identityProviderId             the id of the identity provider.
     * @param name                           the name of the identity provider.
     * @param uniqueIdentifierAttributeAtIdp the unique identifier attribute.
     * @param enabled                        the status of the identity provider.
     * @param identityProviderType           the type.
     * @param position                       the position.
     * @param buttonLabel                    the button text.
     * @param issuerUrl                      the issuer url.
     * @param storkQaaLevel                  the stork qaa level.
     */
    void createSamlIdentityProviderSettings(@NonNull String identityProviderId,
                                            @NonNull String name,
                                            @NonNull String uniqueIdentifierAttributeAtIdp,
                                            boolean enabled,
                                            @NonNull IdentityProviderType identityProviderType,
                                            int position,
                                            @NonNull String buttonLabel,
                                            @NonNull String issuerUrl,
                                            @Nullable String storkQaaLevel);
}
