package companyx.ResourceServer.controller;

import companyx.ResourceServer.idp.IdentityProvider;
import companyx.ResourceServer.idp.IdentityProviderService;
import edu.umd.cs.findbugs.annotations.NonNull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller which provides all the necessary endpoints in order to
 * retrieve basic information about identity providers, delete them or update their status.
 */
@RestController
@RequiredArgsConstructor
public class IdentityProviderController {

    public static final String IDENTITY_PROVIDER_ENDPOINT = "/identity-provider";
    private static final String IDENTITY_PROVIDER_SPECIFIC_ENDPOINT = "/identity-provider/{identityProviderId}";

    @NonNull
    private final IdentityProviderService identityProviderService;

    @NonNull
    @GetMapping(value = IDENTITY_PROVIDER_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<IdentityProvider>> queryIdentityProviders() {
        final List<IdentityProvider> identityProviders = identityProviderService.getIdentityProviders();
        return ResponseEntity.ok()
                .body(identityProviders);
    }

    @NonNull
    @DeleteMapping(IDENTITY_PROVIDER_SPECIFIC_ENDPOINT)
    public ResponseEntity<Void> deleteIdentityProvider(@PathVariable @NotBlank final String identityProviderId) {
        identityProviderService.delete(identityProviderId);
        return ResponseEntity.ok().build();
    }

    @NonNull
    @PostMapping(path = IDENTITY_PROVIDER_SPECIFIC_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateIdentityProviderStatus(@PathVariable @NotBlank final String identityProviderId,
                                                             @RequestBody @NonNull @Valid final IdentityProviderUpdateStatusContainer identityProviderUpdateStatusContainer) {
        if (identityProviderUpdateStatusContainer.enable()) {
            identityProviderService.activate(identityProviderId);
        } else {
            identityProviderService.deactivate(identityProviderId);
        }
        return ResponseEntity.ok().build();
    }

    record IdentityProviderUpdateStatusContainer(@NotNull boolean enable) {
    }

}
