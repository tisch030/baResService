package companyx.ResourceServer.idp.saml.repository;

import companyx.ResourceServer.idp.IdentityProvider;
import companyx.ResourceServer.idp.IdentityProviderType;
import companyx.ResourceServer.idp.saml.SamlProvider;
import companyx.ResourceServer.idp.saml.SamlServiceProviderInformationUi;
import companyx.ResourceServer.idp.saml.SamlSettings;
import companyx.ResourceServer.idp.saml.StorkQaaLevel;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static eu.companyx.cms.res.dto.tables.IdentityProvider.IDENTITY_PROVIDER;
import static eu.companyx.cms.res.dto.tables.SamlServiceProviderInformation.SAML_SERVICE_PROVIDER_INFORMATION;
import static eu.companyx.cms.res.dto.tables.SamlSettings.SAML_SETTINGS;

/**
 * {@link SamlIdentityProviderRepository} implementation which uses JOOQ to access the SAML settings in a database.
 */
@Repository
@ConditionalOnClass(DSLContext.class)
@RequiredArgsConstructor
public class JooqSamlIdentityProviderRepository implements SamlIdentityProviderRepository {

    @NonNull
    private final DSLContext dsl;

    @NonNull
    @Override
    public Optional<SamlProvider> findSamlIdentityProviderById(@NonNull final String identityProviderId) {

        final SamlServiceProviderInformationUi samlServiceProviderInformationUi = dsl.select(
                        SAML_SERVICE_PROVIDER_INFORMATION.ENTITY_ID,
                        SAML_SERVICE_PROVIDER_INFORMATION.IDP_INITIATED_LOGOUT_ENDPOINT,
                        SAML_SERVICE_PROVIDER_INFORMATION.X509CERTIFICATE)
                .from(SAML_SERVICE_PROVIDER_INFORMATION)
                .fetchOne(row -> new SamlServiceProviderInformationUi(
                        row.get(SAML_SERVICE_PROVIDER_INFORMATION.ENTITY_ID),
                        row.get(SAML_SERVICE_PROVIDER_INFORMATION.IDP_INITIATED_LOGOUT_ENDPOINT),
                        row.get(SAML_SERVICE_PROVIDER_INFORMATION.X509CERTIFICATE)
                ));

        return dsl.select(
                        IDENTITY_PROVIDER.ID,
                        IDENTITY_PROVIDER.NAME,
                        IDENTITY_PROVIDER.UNIQUE_IDENTIFIER_ATTRIBUTE_AT_IDP,
                        IDENTITY_PROVIDER.ENABLED,
                        IDENTITY_PROVIDER.IDENTITY_PROVIDER_TYPE,
                        IDENTITY_PROVIDER.POSITION,
                        IDENTITY_PROVIDER.BUTTON_LABEL,
                        SAML_SETTINGS.ID,
                        SAML_SETTINGS.IDENTITY_PROVIDER_ID,
                        SAML_SETTINGS.ISSUER_URL,
                        SAML_SETTINGS.STORK_QAA_LEVEL)
                .from(IDENTITY_PROVIDER)
                .innerJoin(SAML_SETTINGS).on(SAML_SETTINGS.IDENTITY_PROVIDER_ID.eq(IDENTITY_PROVIDER.ID))
                .where(IDENTITY_PROVIDER.ID.eq(identityProviderId))
                .fetchOptional(row -> new SamlProvider(
                        new IdentityProvider(
                                row.get(IDENTITY_PROVIDER.ID),
                                row.get(IDENTITY_PROVIDER.NAME),
                                row.get(IDENTITY_PROVIDER.UNIQUE_IDENTIFIER_ATTRIBUTE_AT_IDP),
                                row.get(IDENTITY_PROVIDER.ENABLED),
                                IdentityProviderType.valueOf(row.get(IDENTITY_PROVIDER.IDENTITY_PROVIDER_TYPE)),
                                row.get(IDENTITY_PROVIDER.POSITION),
                                row.get(IDENTITY_PROVIDER.BUTTON_LABEL)
                        ),
                        new SamlSettings(
                                row.get(SAML_SETTINGS.ID),
                                row.get(SAML_SETTINGS.IDENTITY_PROVIDER_ID),
                                row.get(SAML_SETTINGS.ISSUER_URL),
                                row.get(SAML_SETTINGS.STORK_QAA_LEVEL) != null ?
                                        StorkQaaLevel.valueOf(row.get(SAML_SETTINGS.STORK_QAA_LEVEL))
                                        : null
                        ),
                        samlServiceProviderInformationUi
                ));
    }

    @Override
    public void updateIdentityProvider(@NonNull final String identityProviderId,
                                       @NonNull final String name,
                                       @NonNull final String uniqueIdentifierAttributeAtIdp,
                                       final int position,
                                       @NonNull final String buttonLabel,
                                       @NonNull final String issuerUrl,
                                       @Nullable final String storkQaaLevel) {

        dsl.transaction(transaction -> {

            dsl.update(IDENTITY_PROVIDER)
                    .set(IDENTITY_PROVIDER.NAME, name)
                    .set(IDENTITY_PROVIDER.UNIQUE_IDENTIFIER_ATTRIBUTE_AT_IDP, uniqueIdentifierAttributeAtIdp)
                    .set(IDENTITY_PROVIDER.POSITION, position)
                    .set(IDENTITY_PROVIDER.BUTTON_LABEL, buttonLabel)
                    .where(IDENTITY_PROVIDER.ID.eq(identityProviderId))
                    .execute();

            dsl.update(SAML_SETTINGS)
                    .set(SAML_SETTINGS.ISSUER_URL, issuerUrl)
                    .set(SAML_SETTINGS.STORK_QAA_LEVEL, storkQaaLevel != null && storkQaaLevel.isEmpty() ? null : storkQaaLevel)
                    .where(SAML_SETTINGS.IDENTITY_PROVIDER_ID.eq(identityProviderId))
                    .execute();

        });
    }


    @Override
    public void createSamlIdentityProviderSettings(@NonNull final String identityProviderId,
                                                   @NonNull final String name,
                                                   @NonNull final String uniqueIdentifierAttributeAtIdp,
                                                   final boolean enabled,
                                                   @NonNull final IdentityProviderType identityProviderType,
                                                   final int position,
                                                   @NonNull final String buttonLabel,
                                                   @NonNull final String issuerUrl,
                                                   @Nullable final String storkQaaLevel) {

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

            dsl.insertInto(SAML_SETTINGS)
                    .set(SAML_SETTINGS.ID, UUID.randomUUID().toString())
                    .set(SAML_SETTINGS.IDENTITY_PROVIDER_ID, identityProviderId)
                    .set(SAML_SETTINGS.ISSUER_URL, issuerUrl)
                    .set(SAML_SETTINGS.STORK_QAA_LEVEL, storkQaaLevel)
                    .execute();
        });

    }
}
