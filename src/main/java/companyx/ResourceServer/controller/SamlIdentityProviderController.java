package companyx.ResourceServer.controller;

import companyx.ResourceServer.idp.IdentityProviderCreateContainer;
import companyx.ResourceServer.idp.IdentityProviderUpdateContainer;
import companyx.ResourceServer.idp.saml.SamlIdentityProviderService;
import companyx.ResourceServer.idp.saml.SamlProvider;
import edu.umd.cs.findbugs.annotations.NonNull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static companyx.ResourceServer.controller.IdentityProviderController.IDENTITY_PROVIDER_ENDPOINT;

/**
 * Controller which provides the necessary endpoints in order to crate, update or query identity providers
 * which use the SAML standard.
 */
@RestController
@RequiredArgsConstructor
public class SamlIdentityProviderController {

    private static final String IDENTITY_PROVIDER_SAML_ENDPOINT = IDENTITY_PROVIDER_ENDPOINT + "/saml";
    private static final String IDENTITY_PROVIDER_SAML_SPECIFIC_ENDPOINT = IDENTITY_PROVIDER_SAML_ENDPOINT + "/{identityProviderId}";

    @NonNull
    private final SamlIdentityProviderService samlIdentityProviderService;

    @NonNull
    @GetMapping(value = IDENTITY_PROVIDER_SAML_SPECIFIC_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SamlProvider> querySamlIdentityProvider(@PathVariable @NotBlank final String identityProviderId) {

        final SamlProvider samlProvider = samlIdentityProviderService.getSamlIdentityProvider(identityProviderId)
                .orElse(null);

        if (samlProvider == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .body(samlProvider);
    }

    @NonNull
    @PostMapping(path = IDENTITY_PROVIDER_SAML_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createSamlIdentityProvider(@Valid @NonNull @RequestBody final SamlIdentityProviderCreateContainer samlIdentityProviderCreateContainer) {
        final String identityProviderId = samlIdentityProviderService.create(samlIdentityProviderCreateContainer);
        return ResponseEntity.ok()
                .body(identityProviderId);
    }

    @NonNull
    @PostMapping(path = IDENTITY_PROVIDER_SAML_SPECIFIC_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SamlProvider> updateSamlIdentityProvider(@PathVariable @NotBlank final String identityProviderId,
                                                                   @RequestBody @NonNull @Valid final SamlIdentityProviderUpdateContainer samlIdentityProviderUpdateContainer) {
        samlIdentityProviderService.update(identityProviderId, samlIdentityProviderUpdateContainer);
        return ResponseEntity.ok()
                .build();
    }

    public record SamlIdentityProviderCreateContainer(
            @NotNull IdentityProviderCreateContainer identityProviderCreateContainer,
            @NotBlank String issuerUrl,
            String storkQaaLevel) {
    }

    public record SamlIdentityProviderUpdateContainer(
            @NotNull IdentityProviderUpdateContainer identityProviderUpdateContainer,
            @NotBlank String issuerUrl,
            String storkQaaLevel) {
    }
}
