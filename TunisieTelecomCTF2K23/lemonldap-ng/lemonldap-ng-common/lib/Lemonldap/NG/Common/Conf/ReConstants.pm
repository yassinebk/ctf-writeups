# This file is generated by Lemonldap::NG::Manager::Build. Don't modify it by hand
package Lemonldap::NG::Common::Conf::ReConstants;

use strict;
use Exporter 'import';
use base qw(Exporter);

our $VERSION = '2.0.15';

our %EXPORT_TAGS = ( 'all' => [qw($simpleHashKeys $doubleHashKeys $specialNodeKeys $casAppMetaDataNodeKeys $casSrvMetaDataNodeKeys $oidcOPMetaDataNodeKeys $oidcRPMetaDataNodeKeys $samlIDPMetaDataNodeKeys $samlSPMetaDataNodeKeys $virtualHostKeys $specialNodeHash $authParameters $issuerParameters $samlServiceParameters $oidcServiceParameters $casServiceParameters)] );
our @EXPORT_OK   = ( @{ $EXPORT_TAGS{'all'} } );
our @EXPORT      = ( @{ $EXPORT_TAGS{'all'} } );

our $specialNodeHash = {
    virtualHosts         => [qw(exportedHeaders locationRules post vhostOptions)],
    samlIDPMetaDataNodes => [qw(samlIDPMetaDataXML samlIDPMetaDataExportedAttributes samlIDPMetaDataOptions)],
    samlSPMetaDataNodes  => [qw(samlSPMetaDataXML samlSPMetaDataExportedAttributes samlSPMetaDataOptions samlSPMetaDataMacros)],
    oidcOPMetaDataNodes  => [qw(oidcOPMetaDataJSON oidcOPMetaDataJWKS oidcOPMetaDataOptions oidcOPMetaDataExportedVars)],
    oidcRPMetaDataNodes  => [qw(oidcRPMetaDataOptions oidcRPMetaDataExportedVars oidcRPMetaDataOptionsExtraClaims oidcRPMetaDataMacros oidcRPMetaDataScopeRules)],
    casSrvMetaDataNodes  => [qw(casSrvMetaDataOptions casSrvMetaDataExportedVars)],
    casAppMetaDataNodes  => [qw(casAppMetaDataOptions casAppMetaDataExportedVars casAppMetaDataMacros)],
};

our $doubleHashKeys = 'issuerDBGetParameters';
our $simpleHashKeys = '(?:(?:c(?:a(?:s(?:StorageOption|Attribute)|ptchaOption)|ustom(?:Plugins|Add)Param|heckUserHiddenHeader|ombModule)|r(?:e(?:moteGlobalStorageOption|st2f(?:Verify|Init)Arg|loadUrl)|adiusExportedVar)|l(?:o(?:calSessionStorageOption|goutService)|dapExportedVar|wp(?:Ssl)?Opt)|f(?:indUser(?:Exclud|Search)ingAttribute|acebookExportedVar)|g(?:r(?:antSessionRule|oup)|lobalStorageOption)|n(?:otificationStorageOption|ginxCustomHandler)|p(?:ersistentStorageOption|ortalSkinRule)|(?:(?:d(?:emo|bi)|webID)E|e)xportedVar|macro)s|o(?:idc(?:S(?:ervice(?:DynamicRegistrationEx(?:portedVar|traClaim)s|MetaDataAuthnContext)|torageOptions)|OPMetaDataJ(?:SON|WKS))|penIdExportedVars)|a(?:(?:daptativeAuthenticationLevelR|ut(?:hChoiceMod|oSigninR))ules|pplicationList)|s(?:(?:amlStorageOption|laveExportedVar)s|essionDataToRemember|fExtra)|S(?:MTPTLSOpts|SLVarIf))';
our $specialNodeKeys = '(?:(?:(?:saml(?:ID|S)|oidc[OR])P|cas(?:App|Srv))MetaDataNode|virtualHost)s';
our $casAppMetaDataNodeKeys = 'casAppMetaData(?:Options(?:(?:UserAttribut|Servic|Rul)e|AuthnLevel|Comment)|(?:ExportedVar|Macro)s)';
our $casSrvMetaDataNodeKeys = 'casSrvMetaData(?:Options(?:Re(?:solutionRule|new)|ProxiedServices|DisplayName|SortNumber|Comment|Gateway|Tooltip|Icon|Url)|ExportedVars)';
our $oidcOPMetaDataNodeKeys = 'oidcOPMetaData(?:Options(?:C(?:o(?:nfigurationURI|mment)|lient(?:Secret|ID)|heckJWTSignature)|To(?:kenEndpointAuthMethod|oltip)|S(?:toreIDToken|ortNumber|cope)|(?:ResolutionRul|MaxAg)e|(?:JWKSTimeou|Promp)t|I(?:DTokenMaxAge|con)|U(?:iLocales|seNonce)|Display(?:Name)?|AcrValues)|ExportedVars|J(?:SON|WKS))';
our $oidcRPMetaDataNodeKeys = 'oidcRPMetaData(?:Options(?:A(?:llow(?:(?:ClientCredentials|Password)Grant|Offline)|ccessToken(?:Expiration|SignAlg|Claims|JWT)|uth(?:orizationCodeExpiration|nLevel)|dditionalAudiences)|I(?:DToken(?:ForceClaims|Expiration|SignAlg)|con)|Logout(?:SessionRequired|BypassConfirm|Type|Url)|R(?:e(?:directUris|freshToken|quirePKCE)|ule)|P(?:ostLogoutRedirectUris|ublic)|C(?:lient(?:Secret|ID)|omment)|UserI(?:nfoSignAlg|DAttr)|OfflineSessionExpiration|BypassConsent|DisplayName|ExtraClaims)|(?:ExportedVar|ScopeRule|Macro)s)';
our $samlIDPMetaDataNodeKeys = 'samlIDPMetaData(?:Options(?:S(?:ign(?:S[LS]OMessage|atureMethod)|toreSAMLToken|[LS]OBinding|ortNumber)|C(?:heck(?:S[LS]OMessageSignatur|Audienc|Tim)e|omment)|Re(?:questedAuthnContext|solutionRule|layStateURL)|(?:EncryptionMod|UserAttribut|DisplayNam)e|A(?:daptSessionUtime|llowLoginFromIDP)|Force(?:Authn|UTF8)|I(?:sPassive|con)|NameIDFormat|Tooltip)|ExportedAttributes|XML)';
our $samlSPMetaDataNodeKeys = 'samlSPMetaData(?:Options(?:S(?:ign(?:S[LS]OMessage|atureMethod)|essionNotOnOrAfterTimeout)|N(?:ameID(?:SessionKey|Format)|otOnOrAfterTimeout)|C(?:heckS[LS]OMessageSignature|omment)|En(?:ableIDPInitiatedURL|cryptionMode)|(?:OneTimeUs|Rul)e|AuthnLevel|ForceUTF8)|(?:ExportedAttribute|Macro)s|XML)';
our $virtualHostKeys = '(?:vhost(?:A(?:ccessToTrace|uthnLevel|liases)|(?:Maintenanc|Typ)e|(?:Commen|Por)t|ServiceTokenTTL|DevOpsRulesUrl|Https)|(?:exportedHeader|locationRule)s|post)';

our $authParameters = {
  adParams => [qw(ADPwdMaxAge ADPwdExpireWarning)],
  apacheParams => [qw(apacheAuthnLevel)],
  casParams => [qw(casAuthnLevel)],
  choiceParams => [qw(authChoiceParam authChoiceModules authChoiceAuthBasic authChoiceFindUser)],
  combinationParams => [qw(combination combModules)],
  customParams => [qw(customAuth customUserDB customPassword customRegister customResetCertByMail customAddParams)],
  dbiParams => [qw(dbiAuthnLevel dbiExportedVars dbiAuthChain dbiAuthUser dbiAuthPassword dbiUserChain dbiUserUser dbiUserPassword dbiAuthTable dbiUserTable dbiAuthLoginCol dbiAuthPasswordCol dbiPasswordMailCol userPivot dbiAuthPasswordHash dbiDynamicHashEnabled dbiDynamicHashValidSchemes dbiDynamicHashValidSaltedSchemes dbiDynamicHashNewPasswordScheme)],
  demoParams => [qw(demoExportedVars)],
  facebookParams => [qw(facebookAuthnLevel facebookExportedVars facebookAppId facebookAppSecret facebookUserField)],
  githubParams => [qw(githubAuthnLevel githubClientID githubClientSecret githubUserField githubScope)],
  gpgParams => [qw(gpgAuthnLevel gpgDb)],
  kerberosParams => [qw(krbAuthnLevel krbKeytab krbByJs krbRemoveDomain krbAllowedDomains)],
  ldapParams => [qw(ldapAuthnLevel ldapExportedVars ldapServer ldapPort ldapVerify ldapBase managerDn managerPassword ldapTimeout ldapIOTimeout ldapVersion ldapRaw ldapCAFile ldapCAPath LDAPFilter AuthLDAPFilter mailLDAPFilter ldapSearchDeref ldapGroupBase ldapGroupObjectClass ldapGroupAttributeName ldapGroupAttributeNameUser ldapGroupAttributeNameSearch ldapGroupDecodeSearchedValue ldapGroupRecursive ldapGroupAttributeNameGroup ldapPpolicyControl ldapSetPassword ldapChangePasswordAsUser ldapPwdEnc ldapUsePasswordResetAttribute ldapPasswordResetAttribute ldapPasswordResetAttributeValue ldapAllowResetExpiredPassword ldapGetUserBeforePasswordChange ldapITDS)],
  linkedinParams => [qw(linkedInAuthnLevel linkedInClientID linkedInClientSecret linkedInFields linkedInUserField linkedInScope)],
  nullParams => [qw(nullAuthnLevel)],
  oidcParams => [qw(oidcAuthnLevel oidcRPCallbackGetParam oidcRPStateTimeout)],
  openidParams => [qw(openIdAuthnLevel openIdExportedVars openIdSecret openIdIDPList)],
  pamParams => [qw(pamAuthnLevel pamService)],
  proxyParams => [qw(proxyAuthnLevel proxyUseSoap proxyAuthService proxySessionService proxyAuthServiceChoiceParam proxyAuthServiceChoiceValue proxyCookieName proxyAuthServiceImpersonation)],
  radiusParams => [qw(radiusAuthnLevel radiusSecret radiusServer radiusExportedVars radiusDictionaryFile)],
  remoteParams => [qw(remotePortal remoteCookieName remoteGlobalStorage remoteGlobalStorageOptions)],
  restParams => [qw(restAuthnLevel restAuthUrl restUserDBUrl restPwdConfirmUrl restPwdModifyUrl)],
  slaveParams => [qw(slaveAuthnLevel slaveUserHeader slaveMasterIP slaveHeaderName slaveHeaderContent slaveDisplayLogo slaveExportedVars)],
  sslParams => [qw(SSLAuthnLevel SSLVar SSLIssuerVar SSLVarIf sslByAjax sslHost)],
  twitterParams => [qw(twitterAuthnLevel twitterKey twitterSecret twitterAppName twitterUserField)],
  webidParams => [qw(webIDAuthnLevel webIDExportedVars webIDWhitelist)],
};
our $issuerParameters = {
  issuerDBCAS => [qw(issuerDBCASActivation issuerDBCASPath issuerDBCASRule)],
  issuerDBGet => [qw(issuerDBGetActivation issuerDBGetPath issuerDBGetRule issuerDBGetParameters)],
  issuerDBOpenID => [qw(issuerDBOpenIDActivation issuerDBOpenIDPath issuerDBOpenIDRule openIdIssuerSecret openIdAttr openIdSPList openIdSreg_fullname openIdSreg_nickname openIdSreg_language openIdSreg_postcode openIdSreg_timezone openIdSreg_country openIdSreg_gender openIdSreg_email openIdSreg_dob)],
  issuerDBOpenIDConnect => [qw(issuerDBOpenIDConnectActivation issuerDBOpenIDConnectPath issuerDBOpenIDConnectRule)],
  issuerDBSAML => [qw(issuerDBSAMLActivation issuerDBSAMLPath issuerDBSAMLRule)],
  issuerOptions => [qw(issuersTimeout)],
};
our $samlServiceParameters = [qw(samlEntityID samlServicePrivateKeySig samlServicePrivateKeySigPwd samlServicePublicKeySig samlServicePrivateKeyEnc samlServicePrivateKeyEncPwd samlServicePublicKeyEnc samlServiceUseCertificateInResponse samlServiceSignatureMethod samlNameIDFormatMapEmail samlNameIDFormatMapX509 samlNameIDFormatMapWindows samlNameIDFormatMapKerberos samlAuthnContextMapPassword samlAuthnContextMapPasswordProtectedTransport samlAuthnContextMapKerberos samlAuthnContextMapTLSClient samlOrganizationDisplayName samlOrganizationName samlOrganizationURL samlSPSSODescriptorAuthnRequestsSigned samlSPSSODescriptorWantAssertionsSigned samlSPSSODescriptorSingleLogoutServiceHTTPRedirect samlSPSSODescriptorSingleLogoutServiceHTTPPost samlSPSSODescriptorSingleLogoutServiceSOAP samlSPSSODescriptorAssertionConsumerServiceHTTPArtifact samlSPSSODescriptorAssertionConsumerServiceHTTPPost samlSPSSODescriptorArtifactResolutionServiceArtifact samlIDPSSODescriptorWantAuthnRequestsSigned samlIDPSSODescriptorSingleSignOnServiceHTTPRedirect samlIDPSSODescriptorSingleSignOnServiceHTTPPost samlIDPSSODescriptorSingleSignOnServiceHTTPArtifact samlIDPSSODescriptorSingleLogoutServiceHTTPRedirect samlIDPSSODescriptorSingleLogoutServiceHTTPPost samlIDPSSODescriptorSingleLogoutServiceSOAP samlIDPSSODescriptorArtifactResolutionServiceArtifact samlAttributeAuthorityDescriptorAttributeServiceSOAP samlMetadataForceUTF8 samlRelayStateTimeout samlUseQueryStringSpecific samlOverrideIDPEntityID samlStorage samlStorageOptions samlCommonDomainCookieActivation samlCommonDomainCookieDomain samlCommonDomainCookieReader samlCommonDomainCookieWriter samlDiscoveryProtocolActivation samlDiscoveryProtocolURL samlDiscoveryProtocolPolicy samlDiscoveryProtocolIsPassive)];
our $oidcServiceParameters = [qw(oidcServiceMetaDataIssuer oidcServiceMetaDataAuthorizeURI oidcServiceMetaDataTokenURI oidcServiceMetaDataUserInfoURI oidcServiceMetaDataJWKSURI oidcServiceMetaDataRegistrationURI oidcServiceMetaDataIntrospectionURI oidcServiceMetaDataEndSessionURI oidcServiceMetaDataCheckSessionURI oidcServiceMetaDataFrontChannelURI oidcServiceMetaDataBackChannelURI oidcServiceMetaDataAuthnContext oidcServiceAllowDynamicRegistration oidcServiceDynamicRegistrationExportedVars oidcServiceDynamicRegistrationExtraClaims oidcServicePrivateKeySig oidcServicePublicKeySig oidcServiceKeyIdSig oidcServiceAllowAuthorizationCodeFlow oidcServiceAllowImplicitFlow oidcServiceAllowHybridFlow oidcServiceAllowOnlyDeclaredScopes oidcServiceAuthorizationCodeExpiration oidcServiceIDTokenExpiration oidcServiceAccessTokenExpiration oidcServiceOfflineSessionExpiration oidcStorage oidcStorageOptions)];

1;