package companyx.ResourceServer.idp.repository;

import companyx.ResourceServer.idp.IdentityProvider;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.List;
import java.util.Optional;

/**
 * Base interface for classes/interfaces which implement a repository for {@link IdentityProvider} information.
 */
public interface IdentityProviderRepository {

    /**
     * Returns a list of all {@link IdentityProvider}s.
     *
     * @return a list of all {@link IdentityProvider}s.
     */
    @NonNull
    List<IdentityProvider> loadIdentityProviders();

    /**
     * Returns the identity provider with the given id or an empty optional, if no provider
     * with the given id could be found.
     *
     * @param identityProviderId the id of the identity provider which should get returned.
     */
    @NonNull
    Optional<IdentityProvider> findIdentityProviderById(@NonNull final String identityProviderId);

    /**
     * Deletes an identity provider with the given id.
     *
     * @param identityProviderId the id of the identity provider which should get deleted.
     */
    void deleteIdentityProvider(@NonNull final String identityProviderId);

    /**
     * Activates an identity provider with the given id.
     *
     * @param identityProviderId the id of the identity provider which should get activated.
     */
    void activateIdentityProvider(@NonNull final String identityProviderId);

    /**
     * Deactivates an identity provider with the given id.
     *
     * @param identityProviderId the id of the identity provider which should get deactivated.
     */
    void deactivateIdentityProvider(@NonNull final String identityProviderId);

    /**
     * Checks if the given identity provider name is unique.
     *
     * @param name the name of the identity provider which should get checked of uniqueness.
     */
    boolean isNameUnique(@NonNull final String name);

    /**
     * Checks if the given identity provider position is unique.
     *
     * @param position the position of the identity provider which should get checked of uniqueness.
     */
    boolean isPositionUnique(final int position);

    /**
     * Checks that for the given identity provider no saml or oidc settings are configured.
     *
     * @param identityProviderId id of the identity provider for the check.
     */
    boolean noProviderSettingsExist(@NonNull final String identityProviderId);
}
