package companyx.ResourceServer.idp.oidc.repository;

import companyx.ResourceServer.idp.IdentityProvider;
import companyx.ResourceServer.idp.IdentityProviderType;
import companyx.ResourceServer.idp.oidc.OidcProvider;
import companyx.ResourceServer.idp.oidc.OidcSettings;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static eu.companyx.cms.res.dto.tables.IdentityProvider.IDENTITY_PROVIDER;
import static eu.companyx.cms.res.dto.tables.OpenIdConnectSettings.OPEN_ID_CONNECT_SETTINGS;

/**
 * {@link OidcIdentityProviderRepository} implementation which uses JOOQ to access the OIDC settings in a database.
 */
@Repository
@ConditionalOnClass(DSLContext.class)
@RequiredArgsConstructor
public class JooqOidcIdentityProviderRepository implements OidcIdentityProviderRepository {

    @NonNull
    private final DSLContext dsl;

    @NonNull
    @Override
    public Optional<OidcProvider> findOidcIdentityProviderById(@NonNull final String identityProviderId) {
        return dsl.select(
                        IDENTITY_PROVIDER.ID,
                        IDENTITY_PROVIDER.NAME,
                        IDENTITY_PROVIDER.UNIQUE_IDENTIFIER_ATTRIBUTE_AT_IDP,
                        IDENTITY_PROVIDER.ENABLED,
                        IDENTITY_PROVIDER.IDENTITY_PROVIDER_TYPE,
                        IDENTITY_PROVIDER.POSITION,
                        IDENTITY_PROVIDER.BUTTON_LABEL,
                        OPEN_ID_CONNECT_SETTINGS.ID,
                        OPEN_ID_CONNECT_SETTINGS.IDENTITY_PROVIDER_ID,
                        OPEN_ID_CONNECT_SETTINGS.CLIENT_ID,
                        OPEN_ID_CONNECT_SETTINGS.CLIENT_SECRET,
                        OPEN_ID_CONNECT_SETTINGS.SCOPES,
                        OPEN_ID_CONNECT_SETTINGS.USE_DISCOVERY,
                        OPEN_ID_CONNECT_SETTINGS.ISSUER_URL,
                        OPEN_ID_CONNECT_SETTINGS.AUTHORIZATION_URL,
                        OPEN_ID_CONNECT_SETTINGS.JWKS_URL,
                        OPEN_ID_CONNECT_SETTINGS.TOKEN_URL,
                        OPEN_ID_CONNECT_SETTINGS.USER_INFO_URL)
                .from(IDENTITY_PROVIDER)
                .innerJoin(OPEN_ID_CONNECT_SETTINGS).on(OPEN_ID_CONNECT_SETTINGS.IDENTITY_PROVIDER_ID.eq(IDENTITY_PROVIDER.ID))
                .where(IDENTITY_PROVIDER.ID.eq(identityProviderId))
                .fetchOptional(row -> new OidcProvider(
                        new IdentityProvider(
                                row.get(IDENTITY_PROVIDER.ID),
                                row.get(IDENTITY_PROVIDER.NAME),
                                row.get(IDENTITY_PROVIDER.UNIQUE_IDENTIFIER_ATTRIBUTE_AT_IDP),
                                row.get(IDENTITY_PROVIDER.ENABLED),
                                IdentityProviderType.valueOf(row.get(IDENTITY_PROVIDER.IDENTITY_PROVIDER_TYPE)),
                                row.get(IDENTITY_PROVIDER.POSITION),
                                row.get(IDENTITY_PROVIDER.BUTTON_LABEL)
                        ),
                        new OidcSettings(
                                row.get(OPEN_ID_CONNECT_SETTINGS.ID),
                                row.get(OPEN_ID_CONNECT_SETTINGS.IDENTITY_PROVIDER_ID),
                                row.get(OPEN_ID_CONNECT_SETTINGS.CLIENT_ID),
                                row.get(OPEN_ID_CONNECT_SETTINGS.CLIENT_SECRET),
                                Arrays.stream(row.get(OPEN_ID_CONNECT_SETTINGS.SCOPES).split(","))
                                        .map(String::trim)
                                        .toList(),
                                row.get(OPEN_ID_CONNECT_SETTINGS.USE_DISCOVERY),
                                row.get(OPEN_ID_CONNECT_SETTINGS.ISSUER_URL),
                                row.get(OPEN_ID_CONNECT_SETTINGS.AUTHORIZATION_URL),
                                row.get(OPEN_ID_CONNECT_SETTINGS.JWKS_URL),
                                row.get(OPEN_ID_CONNECT_SETTINGS.TOKEN_URL),
                                row.get(OPEN_ID_CONNECT_SETTINGS.USER_INFO_URL)
                        )
                ));
    }

    @Override
    public void updateIdentityProvider(@NonNull final String identityProviderId,
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
                                       @Nullable final String userInfoEndpoint) {

        dsl.transaction(transaction -> {

            dsl.update(IDENTITY_PROVIDER)
                    .set(IDENTITY_PROVIDER.NAME, name)
                    .set(IDENTITY_PROVIDER.UNIQUE_IDENTIFIER_ATTRIBUTE_AT_IDP, uniqueIdentifierAttributeAtIdp)
                    .set(IDENTITY_PROVIDER.POSITION, position)
                    .set(IDENTITY_PROVIDER.BUTTON_LABEL, buttonLabel)
                    .where(IDENTITY_PROVIDER.ID.eq(identityProviderId))
                    .execute();

            dsl.update(OPEN_ID_CONNECT_SETTINGS)
                    .set(OPEN_ID_CONNECT_SETTINGS.CLIENT_ID, clientId)
                    .set(OPEN_ID_CONNECT_SETTINGS.CLIENT_SECRET, clientSecret)
                    .set(OPEN_ID_CONNECT_SETTINGS.SCOPES, String.join(",", scopes))
                    .set(OPEN_ID_CONNECT_SETTINGS.USE_DISCOVERY, useDiscovery)
                    .set(OPEN_ID_CONNECT_SETTINGS.ISSUER_URL, issuerUrl)
                    .set(OPEN_ID_CONNECT_SETTINGS.AUTHORIZATION_URL, authorizationUrl)
                    .set(OPEN_ID_CONNECT_SETTINGS.JWKS_URL, jwksUrl)
                    .set(OPEN_ID_CONNECT_SETTINGS.TOKEN_URL, tokenUrl)
                    .set(OPEN_ID_CONNECT_SETTINGS.USER_INFO_URL, userInfoEndpoint)
                    .where(OPEN_ID_CONNECT_SETTINGS.IDENTITY_PROVIDER_ID.eq(identityProviderId))
                    .execute();

        });

    }

    @Override
    public void createOidcIdentityProviderSettings(@NonNull final String identityProviderId,
                                                   @NonNull final String name,
                                                   @NonNull final String uniqueIdentifierAttributeAtIdp,
                                                   final boolean enabled,
                                                   @NonNull final IdentityProviderType identityProviderType,
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
                                                   @Nullable final String userInfoEndpoint) {

        dsl.transaction(transaction -> {

            dsl.insertInto(IDENTITY_PROVIDER)
                    .set(IDENTITY_PROVIDER.ID, identityProviderId)
                    .set(IDENTITY_PROVIDER.NAME, name)
                    .set(IDENTITY_PROVIDER.UNIQUE_IDENTIFIER_ATTRIBUTE_AT_IDP, uniqueIdentifierAttributeAtIdp)
                    .set(IDENTITY_PROVIDER.ENABLED, enabled)
                    .set(IDENTITY_PROVIDER.IDENTITY_PROVIDER_TYPE, identityProviderType.toString())
                    .set(IDENTITY_PROVIDER.POSITION, position)
                    .set(IDENTITY_PROVIDER.BUTTON_LABEL, buttonLabel)
                    .execute();

            dsl.insertInto(OPEN_ID_CONNECT_SETTINGS)
                    .set(OPEN_ID_CONNECT_SETTINGS.ID, UUID.randomUUID().toString())
                    .set(OPEN_ID_CONNECT_SETTINGS.IDENTITY_PROVIDER_ID, identityProviderId)
                    .set(OPEN_ID_CONNECT_SETTINGS.CLIENT_ID, clientId)
                    .set(OPEN_ID_CONNECT_SETTINGS.CLIENT_SECRET, clientSecret)
                    .set(OPEN_ID_CONNECT_SETTINGS.SCOPES, scopes.toString())
                    .set(OPEN_ID_CONNECT_SETTINGS.USE_DISCOVERY, useDiscovery)
                    .set(OPEN_ID_CONNECT_SETTINGS.ISSUER_URL, issuerUrl)
                    .set(OPEN_ID_CONNECT_SETTINGS.AUTHORIZATION_URL, authorizationUrl)
                    .set(OPEN_ID_CONNECT_SETTINGS.JWKS_URL, jwksUrl)
                    .set(OPEN_ID_CONNECT_SETTINGS.TOKEN_URL, tokenUrl)
                    .set(OPEN_ID_CONNECT_SETTINGS.USER_INFO_URL, userInfoEndpoint)
                    .execute();
        });
    }
}
