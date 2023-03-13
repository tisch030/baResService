package companyx.ResourceServer.controller;

import companyx.ResourceServer.idp.IdentityProviderCreateContainer;
import companyx.ResourceServer.idp.IdentityProviderUpdateContainer;
import companyx.ResourceServer.idp.oidc.OidcIdentityProviderService;
import companyx.ResourceServer.idp.oidc.OidcProvider;
import edu.umd.cs.findbugs.annotations.NonNull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static companyx.ResourceServer.controller.IdentityProviderController.IDENTITY_PROVIDER_ENDPOINT;

/**
 * Controller which provides the necessary endpoints in order to crate, update or query identity providers
 * which use the OIDC standard.
 */
@RestController
@RequiredArgsConstructor
public class OidcIdentityProviderController {

    private static final String IDENTITY_PROVIDER_OIDC_ENDPOINT = IDENTITY_PROVIDER_ENDPOINT + "/oidc";
    private static final String IDENTITY_PROVIDER_OIDC_SPECIFIC_ENDPOINT = IDENTITY_PROVIDER_OIDC_ENDPOINT + "/{identityProviderId}";

    @NonNull
    private final OidcIdentityProviderService oidcIdentityProviderService;

    @NonNull
    @GetMapping(value = IDENTITY_PROVIDER_OIDC_SPECIFIC_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OidcProvider> queryOidcIdentityProvider(@PathVariable @NotBlank final String identityProviderId) {

        final OidcProvider oidcIdentityProvider = oidcIdentityProviderService.getOidcIdentityProvider(identityProviderId)
                .orElse(null);

        if (oidcIdentityProvider == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .body(oidcIdentityProvider);
    }

    @NonNull
    @PostMapping(path = IDENTITY_PROVIDER_OIDC_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createOidcIdentityProvider(@RequestBody @NonNull @Valid final OidcIdentityProviderCreateContainer oidcIdentityProviderCreateContainer) {
        final String identityProviderId = oidcIdentityProviderService.create(oidcIdentityProviderCreateContainer);
        return ResponseEntity.ok()
                .body(identityProviderId);
    }

    @NonNull
    @PostMapping(path = IDENTITY_PROVIDER_OIDC_SPECIFIC_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateOidcIdentityProvider(@PathVariable @NotBlank final String identityProviderId,
                                                           @RequestBody @NonNull @Valid final OidcIdentityProviderUpdateContainer oidcIdentityProviderUpdateContainer) {
        oidcIdentityProviderService.update(identityProviderId, oidcIdentityProviderUpdateContainer);
        return ResponseEntity.ok()
                .build();
    }

    public record OidcIdentityProviderCreateContainer(
            @NotNull IdentityProviderCreateContainer identityProviderCreateContainer,
            @NotBlank String clientId,
            @NotBlank String clientSecret,
            @NotEmpty List<@NotBlank String> scopes,
            @NotNull boolean useDiscovery,
            String issuerUrl,
            String authorizationUrl,
            String jwksUrl,
            String tokenUrl,
            String userInfoEndpoint) {

    }

    public record OidcIdentityProviderUpdateContainer(
            @NotNull IdentityProviderUpdateContainer identityProviderUpdateContainer,
            @NotBlank String clientId,
            @NotBlank String clientSecret,
            @NotEmpty List<@NotBlank String> scopes,
            @NotNull boolean useDiscovery,
            String issuerUrl,
            String authorizationUrl,
            String jwksUrl,
            String tokenUrl,
            String userInfoEndpoint) {
    }

}
