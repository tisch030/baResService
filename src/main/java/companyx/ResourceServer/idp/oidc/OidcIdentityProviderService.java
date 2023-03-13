package companyx.ResourceServer.idp.oidc;

import companyx.ResourceServer.controller.OidcIdentityProviderController;
import companyx.ResourceServer.idp.IdentityProviderService;
import companyx.ResourceServer.idp.IdentityProviderType;
import companyx.ResourceServer.idp.oidc.repository.OidcIdentityProviderRepository;
import edu.umd.cs.findbugs.annotations.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Service that handles identity providers that use the OIDC standard.
 */
@Service
@RequiredArgsConstructor
public class OidcIdentityProviderService {

    @NonNull
    private final IdentityProviderService identityProviderService;

    @NonNull
    private final OidcIdentityProviderRepository oidcIdentityProviderRepository;

    /**
     * Returns the oidc identity provider with the given id or an empty optional
     * if no provider with the given id could be found.
     *
     * @param identityProviderId the id of the oidc identity provider.
     * @return the oidc identity provider or an empty optional
     * if no provider with the given id could be found.
     */
    @NonNull
    public Optional<OidcProvider> getOidcIdentityProvider(@NonNull final String identityProviderId) {
        return oidcIdentityProviderRepository.findOidcIdentityProviderById(identityProviderId);
    }

    /**
     * Updates an oidc identity provider with the given id and the given update information.
     * Validates that the to be updated name or position is unique and that an issuer url is
     * configured, if the provider should use the discovery mechanism or that the authorization/token/jwks/userinfo
     * endpoint is configured otherwise.
     *
     * @param identityProviderId the id of the identity provider to update.
     * @param updateContainer    the container containing the information for the update.
     * @throws IllegalArgumentException if the validation does not succeed.
     */
    public void update(@NonNull final String identityProviderId,
                       @NonNull final OidcIdentityProviderController.OidcIdentityProviderUpdateContainer updateContainer) throws IllegalArgumentException {

        // Validate before updating
        final OidcProvider oidcProvider = this.getOidcIdentityProvider(identityProviderId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown identity provider " + identityProviderId));

        if (!oidcProvider.identityProvider().name().equals(updateContainer.identityProviderUpdateContainer().name())) {
            identityProviderService.validateUniqueName(updateContainer.identityProviderUpdateContainer().name());
        }
        if (oidcProvider.identityProvider().position() != updateContainer.identityProviderUpdateContainer().position()) {
            identityProviderService.validateUniquePosition(updateContainer.identityProviderUpdateContainer().position());
        }

        if (updateContainer.useDiscovery() && updateContainer.issuerUrl().isEmpty()) {
            throw new IllegalArgumentException("A OIDC identity provider with enabled discovery mechanism must" +
                    "have a issuer url configured.");
        }

        if (!updateContainer.useDiscovery() && (
                updateContainer.tokenUrl().isEmpty()
                        || updateContainer.authorizationUrl().isEmpty()
                        || updateContainer.jwksUrl().isEmpty()
                        || updateContainer.userInfoEndpoint().isEmpty())) {
            throw new IllegalArgumentException("A OIDC identity provider with disabled discovery mechanism must" +
                    "have a authorization url, token url, jwks url and user info url configured.");
        }

        oidcIdentityProviderRepository.updateIdentityProvider(
                identityProviderId,
                updateContainer.identityProviderUpdateContainer().name(),
                updateContainer.identityProviderUpdateContainer().uniqueIdentifierAttributeAtIdp(),
                updateContainer.identityProviderUpdateContainer().position(),
                updateContainer.identityProviderUpdateContainer().buttonLabel(),
                updateContainer.clientId(),
                updateContainer.clientSecret(),
                updateContainer.scopes(),
                updateContainer.useDiscovery(),
                updateContainer.issuerUrl(),
                updateContainer.authorizationUrl(),
                updateContainer.jwksUrl(),
                updateContainer.tokenUrl(),
                updateContainer.userInfoEndpoint());
    }

    /**
     * Creates an identity provider which uses the OIDC standard.
     * Validated that the new provider uses a unique name and position.
     *
     * @param createContainer the container containing the information for the oidc identity provider.
     * @return the id of the created OIDC provider.
     * @throws IllegalArgumentException if the validation does not succeed.
     */
    public String create(final OidcIdentityProviderController.OidcIdentityProviderCreateContainer createContainer) throws IllegalArgumentException {

        final String identityProviderId = UUID.randomUUID().toString();

        // Validate before creating
        identityProviderService.validateUniqueName(createContainer.identityProviderCreateContainer().name());
        identityProviderService.validateUniquePosition(createContainer.identityProviderCreateContainer().position());

        // Validate that no existing oidc or saml settings exist for that identity provider.
        identityProviderService.validateNoSettingsExist(identityProviderId);

        oidcIdentityProviderRepository.createOidcIdentityProviderSettings(
                identityProviderId,
                createContainer.identityProviderCreateContainer().name(),
                createContainer.identityProviderCreateContainer().uniqueIdentifierAttributeAtIdp(),
                false,
                IdentityProviderType.OPENID_CONNECT,
                createContainer.identityProviderCreateContainer().position(),
                createContainer.identityProviderCreateContainer().buttonLabel(),
                createContainer.clientId(),
                createContainer.clientSecret(),
                createContainer.scopes(),
                createContainer.useDiscovery(),
                createContainer.issuerUrl(),
                createContainer.authorizationUrl(),
                createContainer.jwksUrl(),
                createContainer.tokenUrl(),
                createContainer.userInfoEndpoint());

        return identityProviderId;
    }

}
