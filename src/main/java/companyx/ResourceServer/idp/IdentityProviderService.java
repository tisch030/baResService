package companyx.ResourceServer.idp;

import companyx.ResourceServer.idp.repository.IdentityProviderRepository;
import companyx.ResourceServer.template.TemplateSettings;
import companyx.ResourceServer.template.TemplateSettingsService;
import edu.umd.cs.findbugs.annotations.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Services that handles base identity provider information.
 */
@Service
@RequiredArgsConstructor
public class IdentityProviderService {

    @NonNull
    private final TemplateSettingsService templateSettingsService;

    @NonNull
    private final IdentityProviderRepository identityProviderRepository;

    /**
     * Returns all existing identity providers.
     *
     * @return all existing identity providers.
     */
    @NonNull
    public List<IdentityProvider> getIdentityProviders() {
        return identityProviderRepository.loadIdentityProviders();
    }

    /**
     * Activates the identity provider with the given id.
     * Will validate that the contact person information are configured before enabling a SAML idp.
     *
     * @param identityProviderId the {@link IdentityProvider} that will be activated.
     * @throws IllegalArgumentException if the validation fails.
     */
    public void activate(@NonNull final String identityProviderId) throws IllegalArgumentException {

        final IdentityProvider identityProvider = identityProviderRepository.findIdentityProviderById(identityProviderId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown identity provider " + identityProviderId));

        // Validate identity provider configuration before activation
        if (identityProvider.identityProviderType() == IdentityProviderType.SAML) {
            checkSamlMetadataContactPersonConfigured();
        }

        if (identityProvider.uniqueIdentifierAttributeAtIdp().isEmpty()) {
            throw new IllegalArgumentException("Before the activation of an identity provider, make sure to configure a unique identifier, in order to" +
                    "be able to identifier revisiting users.");
        }

        // Activate
        identityProviderRepository.activateIdentityProvider(identityProvider.id());
    }

    /**
     * Deactivates the identity provider with the given id.
     *
     * @param identityProviderId the {@link IdentityProvider} that will be deactivated.
     * @throws IllegalArgumentException if the identity provider with the given id could not be found.
     */
    public void deactivate(@NonNull final String identityProviderId) throws IllegalArgumentException {
        final IdentityProvider identityProvider = identityProviderRepository.findIdentityProviderById(identityProviderId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown identity provider " + identityProviderId));

        identityProviderRepository.deactivateIdentityProvider(identityProvider.id());
    }

    /**
     * Deletes the identity provider with the given id.
     *
     * @param identityProviderId the id of the identity provider which should be deleted.
     */
    public void delete(@NonNull final String identityProviderId) {
        identityProviderRepository.deleteIdentityProvider(identityProviderId);
    }

    /**
     * Validates that there is no existing identity provider with the given name.
     *
     * @param name the name of the identity provider for which uniqueness should get validated.
     * @throws IllegalArgumentException if the validation fails.
     */
    public void validateUniqueName(@NonNull final String name) throws IllegalArgumentException {
        if (!identityProviderRepository.isNameUnique(name)) {
            throw new IllegalArgumentException("The name of the identity provider has to be unique.");
        }
    }

    /**
     * Validates that there is no existing identity provider with the given position.
     *
     * @param position the position of the identity provider for which uniqueness should get validated.
     * @throws IllegalArgumentException if the validation fails.
     */
    public void validateUniquePosition(final int position) throws IllegalArgumentException {
        if (!identityProviderRepository.isPositionUnique(position)) {
            throw new IllegalArgumentException("The position of the identity provider has to be unique.");
        }
    }

    /**
     * Checks that a contact person has been configured for a saml identity provider.
     *
     * @throws IllegalArgumentException if the validation fails.
     */
    private void checkSamlMetadataContactPersonConfigured() throws IllegalArgumentException {
        final TemplateSettings templateSettings = templateSettingsService.loadTemplateSettings();
        if ((templateSettings.contactPersonMail() == null || templateSettings.contactPersonMail().isEmpty())
                || (templateSettings.contactPersonName() == null || templateSettings.contactPersonName().isEmpty())) {
            throw new IllegalArgumentException("Contact Person information have to be configured before activating an identity provider.");
        }
    }

    /**
     * Checks that there are no single sign on specific standard settings configured for the given identity provider.
     *
     * @throws IllegalArgumentException if the validation fails.
     */
    public void validateNoSettingsExist(@NonNull final String identityProviderId) throws IllegalArgumentException {
        if (!identityProviderRepository.noProviderSettingsExist(identityProviderId)) {
            throw new IllegalArgumentException("That identity provider is already configured for a single sign on standard.");
        }
    }
}
