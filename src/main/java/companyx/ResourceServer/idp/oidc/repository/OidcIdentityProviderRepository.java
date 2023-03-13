package companyx.ResourceServer.idp.oidc.repository;

import companyx.ResourceServer.idp.IdentityProviderType;
import companyx.ResourceServer.idp.oidc.OidcProvider;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * Base interface for classes/interfaces which implement a repository for {@link OidcProvider} information.
 */
public interface OidcIdentityProviderRepository {

    /**
     * Returns the {@link OidcProvider} with the given id or an empty optional if no provider
     * with the given id could be found.
     *
     * @param identityProviderId the id of the identity provider.
     * @return the {@link OidcProvider} with the given id or an empty optional if no provider
     * with the given id could be found.
     */
    @NonNull
    Optional<OidcProvider> findOidcIdentityProviderById(@NonNull final String identityProviderId);


    /**
     * Updates an existing oidc identity provider with the given id and the new information.
     *
     * @param identityProviderId             id of the to be updated identity provider.
     * @param name                           the new name.
     * @param uniqueIdentifierAttributeAtIdp the new unique identifier attribute.
     * @param position                       the new position.
     * @param buttonLabel                    the new button label.
     * @param clientId                       the new client id.
     * @param clientSecret                   the new client secret.
     * @param scopes                         the new scopes.
     * @param useDiscovery                   the new use discovery flag.
     * @param issuerUrl                      the new issuer url.
     * @param authorizationUrl               the new authorization url.
     * @param jwksUrl                        the new jwks url.
     * @param tokenUrl                       the new token url.
     * @param userInfoEndpoint               the new user info url.
     */
    void updateIdentityProvider(@NonNull final String identityProviderId,
                                @NonNull final String name,
                                @NonNull final String uniqueIdentifierAttributeAtIdp,
                                final int position,
                                @NonNull final String buttonLabel,
                                @NonNull final String clientId,
                                @NonNull final String clientSecret,
                                @NonNull final List<String> scopes,
                                final boolean useDiscovery,
                                @Nullable final String issuerUrl,
                                @Nullable final String authorizationUrl,
                                @Nullable final String jwksUrl,
                                @Nullable final String tokenUrl,
                                @Nullable final String userInfoEndpoint);

    /**
     * Create an identity provider which uses the OIDC standard.
     *
     * @param identityProviderId             the id of the identity provider.
     * @param name                           the name of the identity provider.
     * @param uniqueIdentifierAttributeAtIdp the unique identifier attribute.
     * @param enabled                        the status of the identity provider.
     * @param identityProviderType           the type.
     * @param position                       the position.
     * @param buttonLabel                    the button text.
     * @param clientId                       the client id.
     * @param clientSecret                   the client secret.
     * @param scopes                         the scopes.
     * @param useDiscovery                   the use discovery flag.
     * @param issuerUrl                      the issuer url.
     * @param authorizationUrl               the authorization url.
     * @param jwksUrl                        the jwks url.
     * @param tokenUrl                       the token url.
     * @param userInfoEndpoint               the user info url.
     */
    void createOidcIdentityProviderSettings(@NonNull String identityProviderId,
                                            @NonNull String name,
                                            @NonNull String uniqueIdentifierAttributeAtIdp,
                                            boolean enabled,
                                            @NonNull IdentityProviderType identityProviderType,
                                            int position,
                                            @NonNull String buttonLabel,
                                            @NonNull final String clientId,
                                            @NonNull final String clientSecret,
                                            @NonNull final List<String> scopes,
                                            final boolean useDiscovery,
                                            @Nullable final String issuerUrl,
                                            @Nullable final String authorizationUrl,
                                            @Nullable final String jwksUrl,
                                            @Nullable final String tokenUrl,
                                            @Nullable final String userInfoEndpoint);
}
