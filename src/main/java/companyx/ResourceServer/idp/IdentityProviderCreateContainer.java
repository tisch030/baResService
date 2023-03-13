package companyx.ResourceServer.idp;

import jakarta.validation.constraints.NotBlank;

public record IdentityProviderCreateContainer(@NotBlank String name,
                                              @NotBlank String uniqueIdentifierAttributeAtIdp,
                                              @NotBlank int position,
                                              @NotBlank String buttonLabel) {
}
