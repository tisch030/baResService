package companyx.ResourceServer.idp.saml;

import companyx.ResourceServer.controller.SamlIdentityProviderController;
import companyx.ResourceServer.idp.IdentityProviderService;
import companyx.ResourceServer.idp.IdentityProviderType;
import companyx.ResourceServer.idp.saml.repository.SamlIdentityProviderRepository;
import edu.umd.cs.findbugs.annotations.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Service that handles identity providers that use the saml standard.
 */
@Service
@RequiredArgsConstructor
public class SamlIdentityProviderService {

    @NonNull
    private final IdentityProviderService identityProviderService;

    @NonNull
    private final SamlIdentityProviderRepository samlIdentityProviderRepository;

    /**
     * Returns the saml identity provider with the given id or an empty optional
     * if no provider with the given id could be found.
     *
     * @param identityProviderId the id of the saml identity provider.
     * @return the saml identity provider or an empty optional
     * if no provider with the given id could be found.
     */
    public Optional<SamlProvider> getSamlIdentityProvider(@NonNull final String identityProviderId) {
        return samlIdentityProviderRepository.findSamlIdentityProviderById(identityProviderId);
    }

    /**
     * Updates a saml identity provider with the given id and the given update information.
     * Validates that the new name and position is unique, if they get updated.
     *
     * @param identityProviderId the id of the identity provider to update.
     * @param updateContainer    the container containing the information for the update.
     * @throws IllegalArgumentException if the validation does not succeed.
     */
    public void update(@NonNull final String identityProviderId,
                       @NonNull final SamlIdentityProviderController.SamlIdentityProviderUpdateContainer updateContainer) throws IllegalArgumentException {

        // Validate before updating
        final SamlProvider samlProvider = this.getSamlIdentityProvider(identityProviderId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown identity provider " + identityProviderId));

        if (!samlProvider.identityProvider().name().equals(updateContainer.identityProviderUpdateContainer().name())) {
            identityProviderService.validateUniqueName(updateContainer.identityProviderUpdateContainer().name());
        }
        if (samlProvider.identityProvider().position() != updateContainer.identityProviderUpdateContainer().position()) {
            identityProviderService.validateUniquePosition(updateContainer.identityProviderUpdateContainer().position());
        }

        samlIdentityProviderRepository.updateIdentityProvider(
                identityProviderId,
                updateContainer.identityProviderUpdateContainer().name(),
                updateContainer.identityProviderUpdateContainer().uniqueIdentifierAttributeAtIdp(),
                updateContainer.identityProviderUpdateContainer().position(),
                updateContainer.identityProviderUpdateContainer().buttonLabel(),
                updateContainer.issuerUrl(),
                updateContainer.storkQaaLevel());
    }

    /**
     * Creates a saml identity provider.
     * Validates that the given name and position is unique.
     *
     * @param createContainer the container containing the information for the saml settings.
     * @return the id of the created identity provider.
     * @throws IllegalArgumentException if the validation does not succeed.
     */
    public String create(@NonNull final SamlIdentityProviderController.SamlIdentityProviderCreateContainer createContainer) throws IllegalArgumentException {

        final String identityProviderId = UUID.randomUUID().toString();

        // Validate before creating
        identityProviderService.validateUniqueName(createContainer.identityProviderCreateContainer().name());
        identityProviderService.validateUniquePosition(createContainer.identityProviderCreateContainer().position());

        // Validate that no existing oidc or saml settings exist for that identity provider.
        identityProviderService.validateNoSettingsExist(identityProviderId);

        samlIdentityProviderRepository.createSamlIdentityProviderSettings(
                identityProviderId,
                createContainer.identityProviderCreateContainer().name(),
                createContainer.identityProviderCreateContainer().uniqueIdentifierAttributeAtIdp(),
                false,
                IdentityProviderType.SAML,
                createContainer.identityProviderCreateContainer().position(),
                createContainer.identityProviderCreateContainer().buttonLabel(),
                createContainer.issuerUrl(),
                createContainer.storkQaaLevel());

        return identityProviderId;
    }

}
