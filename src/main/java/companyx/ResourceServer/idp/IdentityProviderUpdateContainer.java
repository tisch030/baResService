package companyx.ResourceServer.idp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record IdentityProviderUpdateContainer(@NotBlank String name,
                                              @NotBlank String uniqueIdentifierAttributeAtIdp,
                                              @NotNull int position,
                                              @NotBlank String buttonLabel) {
}
