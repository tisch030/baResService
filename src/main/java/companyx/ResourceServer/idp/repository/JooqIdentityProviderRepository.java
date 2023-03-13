package companyx.ResourceServer.idp.repository;

import companyx.ResourceServer.idp.IdentityProvider;
import companyx.ResourceServer.idp.IdentityProviderType;
import edu.umd.cs.findbugs.annotations.NonNull;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static eu.companyx.cms.res.dto.tables.CredentialIdentityProviderCorrelation.CREDENTIAL_IDENTITY_PROVIDER_CORRELATION;
import static eu.companyx.cms.res.dto.tables.IdentityProvider.IDENTITY_PROVIDER;
import static eu.companyx.cms.res.dto.tables.OpenIdConnectSettings.OPEN_ID_CONNECT_SETTINGS;
import static eu.companyx.cms.res.dto.tables.SamlSettings.SAML_SETTINGS;

/**
 * {@link IdentityProviderRepository} implementation which uses JOOQ to access the identity provider settings in a database.
 */
@Repository
@ConditionalOnClass(DSLContext.class)
@RequiredArgsConstructor
public class JooqIdentityProviderRepository implements IdentityProviderRepository {

    @NonNull
    private final DSLContext dsl;

    @NonNull
    @Override
    public List<IdentityProvider> loadIdentityProviders() {
        return dsl.select(
                        IDENTITY_PROVIDER.ID,
                        IDENTITY_PROVIDER.NAME,
                        IDENTITY_PROVIDER.UNIQUE_IDENTIFIER_ATTRIBUTE_AT_IDP,
                        IDENTITY_PROVIDER.ENABLED,
                        IDENTITY_PROVIDER.BUTTON_LABEL,
                        IDENTITY_PROVIDER.POSITION,
                        IDENTITY_PROVIDER.IDENTITY_PROVIDER_TYPE)
                .from(IDENTITY_PROVIDER)
                .fetch(row -> new IdentityProvider(
                        row.get(IDENTITY_PROVIDER.ID),
                        row.get(IDENTITY_PROVIDER.NAME),
                        row.get(IDENTITY_PROVIDER.UNIQUE_IDENTIFIER_ATTRIBUTE_AT_IDP),
                        row.get(IDENTITY_PROVIDER.ENABLED),
                        IdentityProviderType.valueOf(row.get(IDENTITY_PROVIDER.IDENTITY_PROVIDER_TYPE)),
                        row.get(IDENTITY_PROVIDER.POSITION),
                        row.get(IDENTITY_PROVIDER.BUTTON_LABEL)
                ));
    }

    @Override
    @NonNull
    public Optional<IdentityProvider> findIdentityProviderById(@NonNull final String identityProviderId) {
        return dsl.select(
                        IDENTITY_PROVIDER.ID,
                        IDENTITY_PROVIDER.NAME,
                        IDENTITY_PROVIDER.UNIQUE_IDENTIFIER_ATTRIBUTE_AT_IDP,
                        IDENTITY_PROVIDER.ENABLED,
                        IDENTITY_PROVIDER.IDENTITY_PROVIDER_TYPE,
                        IDENTITY_PROVIDER.POSITION,
                        IDENTITY_PROVIDER.BUTTON_LABEL)
                .from(IDENTITY_PROVIDER)
                .where(IDENTITY_PROVIDER.ID.eq(identityProviderId))
                .fetchOptional(row ->
                        new IdentityProvider(
                                row.get(IDENTITY_PROVIDER.ID),
                                row.get(IDENTITY_PROVIDER.NAME),
                                row.get(IDENTITY_PROVIDER.UNIQUE_IDENTIFIER_ATTRIBUTE_AT_IDP),
                                row.get(IDENTITY_PROVIDER.ENABLED),
                                IdentityProviderType.valueOf(row.get(IDENTITY_PROVIDER.IDENTITY_PROVIDER_TYPE)),
                                row.get(IDENTITY_PROVIDER.POSITION),
                                row.get(IDENTITY_PROVIDER.BUTTON_LABEL)
                        ));
    }

    @Override
    public void deleteIdentityProvider(@NonNull final String identityProviderId) {
        dsl.transaction(transaction -> {

            dsl.delete(SAML_SETTINGS)
                    .where(SAML_SETTINGS.IDENTITY_PROVIDER_ID.eq(identityProviderId))
                    .execute();

            dsl.delete(OPEN_ID_CONNECT_SETTINGS)
                    .where(OPEN_ID_CONNECT_SETTINGS.IDENTITY_PROVIDER_ID.eq(identityProviderId))
                    .execute();

            dsl.delete(CREDENTIAL_IDENTITY_PROVIDER_CORRELATION)
                    .where(CREDENTIAL_IDENTITY_PROVIDER_CORRELATION.IDENTITY_PROVIDER_ID.eq(identityProviderId))
                    .execute();

            dsl.delete(IDENTITY_PROVIDER)
                    .where(IDENTITY_PROVIDER.ID.eq(identityProviderId))
                    .execute();

        });
    }

    @Override
    public void activateIdentityProvider(@NonNull final String identityProviderId) {
        dsl.update(IDENTITY_PROVIDER)
                .set(IDENTITY_PROVIDER.ENABLED, Boolean.TRUE)
                .where(IDENTITY_PROVIDER.ID.eq(identityProviderId))
                .execute();
    }

    @Override
    public void deactivateIdentityProvider(@NonNull final String identityProviderId) {
        dsl.update(IDENTITY_PROVIDER)
                .set(IDENTITY_PROVIDER.ENABLED, Boolean.FALSE)
                .where(IDENTITY_PROVIDER.ID.eq(identityProviderId))
                .execute();
    }

    @Override
    public boolean isNameUnique(@NonNull final String name) {
        final int count = dsl.select()
                .from(IDENTITY_PROVIDER)
                .where(IDENTITY_PROVIDER.NAME.equalIgnoreCase(name))
                .execute();
        return count == 0;
    }

    @Override
    public boolean isPositionUnique(final int position) {
        final int count = dsl.select()
                .from(IDENTITY_PROVIDER)
                .where(IDENTITY_PROVIDER.POSITION.eq(position))
                .execute();
        return count == 0;
    }

    @Override
    public boolean noProviderSettingsExist(@NonNull final String identityProviderId) {
        final int counterOidcSettings = dsl.select()
                .from(OPEN_ID_CONNECT_SETTINGS)
                .where(OPEN_ID_CONNECT_SETTINGS.IDENTITY_PROVIDER_ID.eq(identityProviderId))
                .execute();

        if (counterOidcSettings != 0) {
            return false;
        }

        final int counterSamlSettings = dsl.select()
                .from(SAML_SETTINGS)
                .where(SAML_SETTINGS.IDENTITY_PROVIDER_ID.eq(identityProviderId))
                .execute();

        return counterSamlSettings == 0;
    }
}
