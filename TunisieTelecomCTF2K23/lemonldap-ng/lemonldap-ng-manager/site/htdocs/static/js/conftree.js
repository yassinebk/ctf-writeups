function templates(tpl,key) {
    var ind;
    var scalarTemplate = function(r) {
    return {
      "id": tpl+"s/"+(ind++),
      "title": r,
      "get": tpl+"s/"+key+"/"+r
    };
  };
  switch(tpl){
  case 'casAppMetaDataNode':
    return [
   {
      "cnodes" : tpl+"s/"+key+"/"+"casAppMetaDataExportedVars",
      "default" : [
         {
            "data" : "cn",
            "id" : tpl+"s/"+key+"/"+"casAppMetaDataExportedVars/cn",
            "title" : "cn",
            "type" : "keyText"
         },
         {
            "data" : "mail",
            "id" : tpl+"s/"+key+"/"+"casAppMetaDataExportedVars/mail",
            "title" : "mail",
            "type" : "keyText"
         },
         {
            "data" : "uid",
            "id" : tpl+"s/"+key+"/"+"casAppMetaDataExportedVars/uid",
            "title" : "uid",
            "type" : "keyText"
         }
      ],
      "id" : tpl+"s/"+key+"/"+"casAppMetaDataExportedVars",
      "title" : "casAppMetaDataExportedVars",
      "type" : "keyTextContainer"
   },
   {
      "_nodes" : [
         {
            "get" : tpl+"s/"+key+"/"+"casAppMetaDataOptionsService",
            "id" : tpl+"s/"+key+"/"+"casAppMetaDataOptionsService",
            "title" : "casAppMetaDataOptionsService"
         },
         {
            "get" : tpl+"s/"+key+"/"+"casAppMetaDataOptionsUserAttribute",
            "id" : tpl+"s/"+key+"/"+"casAppMetaDataOptionsUserAttribute",
            "title" : "casAppMetaDataOptionsUserAttribute"
         },
         {
            "get" : tpl+"s/"+key+"/"+"casAppMetaDataOptionsAuthnLevel",
            "id" : tpl+"s/"+key+"/"+"casAppMetaDataOptionsAuthnLevel",
            "title" : "casAppMetaDataOptionsAuthnLevel",
            "type" : "int"
         },
         {
            "get" : tpl+"s/"+key+"/"+"casAppMetaDataOptionsRule",
            "id" : tpl+"s/"+key+"/"+"casAppMetaDataOptionsRule",
            "title" : "casAppMetaDataOptionsRule"
         },
         {
            "get" : tpl+"s/"+key+"/"+"casAppMetaDataOptionsComment",
            "id" : tpl+"s/"+key+"/"+"casAppMetaDataOptionsComment",
            "title" : "casAppMetaDataOptionsComment",
            "type" : "longtext"
         }
      ],
      "id" : "casAppMetaDataOptions",
      "title" : "casAppMetaDataOptions",
      "type" : "simpleInputContainer"
   },
   {
      "cnodes" : tpl+"s/"+key+"/"+"casAppMetaDataMacros",
      "default" : [],
      "help" : "exportedvars.html#extend-variables-using-macros-and-groups",
      "id" : tpl+"s/"+key+"/"+"casAppMetaDataMacros",
      "title" : "casAppMetaDataMacros",
      "type" : "keyTextContainer"
   }
]
;
  case 'casSrvMetaDataNode':
    return [
   {
      "cnodes" : tpl+"s/"+key+"/"+"casSrvMetaDataExportedVars",
      "default" : [
         {
            "data" : "cn",
            "id" : tpl+"s/"+key+"/"+"casSrvMetaDataExportedVars/cn",
            "title" : "cn",
            "type" : "keyText"
         },
         {
            "data" : "mail",
            "id" : tpl+"s/"+key+"/"+"casSrvMetaDataExportedVars/mail",
            "title" : "mail",
            "type" : "keyText"
         },
         {
            "data" : "uid",
            "id" : tpl+"s/"+key+"/"+"casSrvMetaDataExportedVars/uid",
            "title" : "uid",
            "type" : "keyText"
         }
      ],
      "id" : tpl+"s/"+key+"/"+"casSrvMetaDataExportedVars",
      "title" : "casSrvMetaDataExportedVars",
      "type" : "keyTextContainer"
   },
   {
      "cnodes" : tpl+"s/"+key+"/"+"casSrvMetaDataOptionsProxiedServices",
      "id" : tpl+"s/"+key+"/"+"casSrvMetaDataOptionsProxiedServices",
      "title" : "casSrvMetaDataOptionsProxiedServices",
      "type" : "keyTextContainer"
   },
   {
      "_nodes" : [
         {
            "get" : tpl+"s/"+key+"/"+"casSrvMetaDataOptionsUrl",
            "id" : tpl+"s/"+key+"/"+"casSrvMetaDataOptionsUrl",
            "title" : "casSrvMetaDataOptionsUrl"
         },
         {
            "default" : 0,
            "get" : tpl+"s/"+key+"/"+"casSrvMetaDataOptionsRenew",
            "id" : tpl+"s/"+key+"/"+"casSrvMetaDataOptionsRenew",
            "title" : "casSrvMetaDataOptionsRenew",
            "type" : "bool"
         },
         {
            "default" : 0,
            "get" : tpl+"s/"+key+"/"+"casSrvMetaDataOptionsGateway",
            "id" : tpl+"s/"+key+"/"+"casSrvMetaDataOptionsGateway",
            "title" : "casSrvMetaDataOptionsGateway",
            "type" : "bool"
         },
         {
            "get" : tpl+"s/"+key+"/"+"casSrvMetaDataOptionsComment",
            "id" : tpl+"s/"+key+"/"+"casSrvMetaDataOptionsComment",
            "title" : "casSrvMetaDataOptionsComment",
            "type" : "longtext"
         }
      ],
      "id" : "casSrvMetaDataOptions",
      "title" : "casSrvMetaDataOptions",
      "type" : "simpleInputContainer"
   },
   {
      "_nodes" : [
         {
            "get" : tpl+"s/"+key+"/"+"casSrvMetaDataOptionsDisplayName",
            "id" : tpl+"s/"+key+"/"+"casSrvMetaDataOptionsDisplayName",
            "title" : "casSrvMetaDataOptionsDisplayName"
         },
         {
            "get" : tpl+"s/"+key+"/"+"casSrvMetaDataOptionsIcon",
            "id" : tpl+"s/"+key+"/"+"casSrvMetaDataOptionsIcon",
            "title" : "casSrvMetaDataOptionsIcon"
         },
         {
            "get" : tpl+"s/"+key+"/"+"casSrvMetaDataOptionsTooltip",
            "id" : tpl+"s/"+key+"/"+"casSrvMetaDataOptionsTooltip",
            "title" : "casSrvMetaDataOptionsTooltip"
         },
         {
            "default" : "",
            "get" : tpl+"s/"+key+"/"+"casSrvMetaDataOptionsResolutionRule",
            "id" : tpl+"s/"+key+"/"+"casSrvMetaDataOptionsResolutionRule",
            "title" : "casSrvMetaDataOptionsResolutionRule",
            "type" : "longtext"
         },
         {
            "get" : tpl+"s/"+key+"/"+"casSrvMetaDataOptionsSortNumber",
            "id" : tpl+"s/"+key+"/"+"casSrvMetaDataOptionsSortNumber",
            "title" : "casSrvMetaDataOptionsSortNumber",
            "type" : "int"
         }
      ],
      "id" : "casSrvMetaDataOptionsDisplay",
      "title" : "casSrvMetaDataOptionsDisplay",
      "type" : "simpleInputContainer"
   }
]
;
  case 'oidcOPMetaDataNode':
    return [
   {
      "get" : tpl+"s/"+key+"/"+"oidcOPMetaDataJSON",
      "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataJSON",
      "title" : "oidcOPMetaDataJSON",
      "type" : "file"
   },
   {
      "get" : tpl+"s/"+key+"/"+"oidcOPMetaDataJWKS",
      "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataJWKS",
      "title" : "oidcOPMetaDataJWKS",
      "type" : "file"
   },
   {
      "cnodes" : tpl+"s/"+key+"/"+"oidcOPMetaDataExportedVars",
      "default" : [
         {
            "data" : "name",
            "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataExportedVars/cn",
            "title" : "cn",
            "type" : "keyText"
         },
         {
            "data" : "email",
            "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataExportedVars/mail",
            "title" : "mail",
            "type" : "keyText"
         },
         {
            "data" : "family_name",
            "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataExportedVars/sn",
            "title" : "sn",
            "type" : "keyText"
         },
         {
            "data" : "sub",
            "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataExportedVars/uid",
            "title" : "uid",
            "type" : "keyText"
         }
      ],
      "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataExportedVars",
      "title" : "oidcOPMetaDataExportedVars",
      "type" : "keyTextContainer"
   },
   {
      "_nodes" : [
         {
            "_nodes" : [
               {
                  "get" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsConfigurationURI",
                  "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsConfigurationURI",
                  "title" : "oidcOPMetaDataOptionsConfigurationURI"
               },
               {
                  "default" : 0,
                  "get" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsJWKSTimeout",
                  "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsJWKSTimeout",
                  "title" : "oidcOPMetaDataOptionsJWKSTimeout",
                  "type" : "int"
               },
               {
                  "get" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsClientID",
                  "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsClientID",
                  "title" : "oidcOPMetaDataOptionsClientID"
               },
               {
                  "get" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsClientSecret",
                  "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsClientSecret",
                  "title" : "oidcOPMetaDataOptionsClientSecret",
                  "type" : "password"
               },
               {
                  "default" : 0,
                  "get" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsStoreIDToken",
                  "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsStoreIDToken",
                  "title" : "oidcOPMetaDataOptionsStoreIDToken",
                  "type" : "bool"
               }
            ],
            "id" : "oidcOPMetaDataOptionsConfiguration",
            "title" : "oidcOPMetaDataOptionsConfiguration",
            "type" : "simpleInputContainer"
         },
         {
            "_nodes" : [
               {
                  "default" : "openid profile",
                  "get" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsScope",
                  "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsScope",
                  "title" : "oidcOPMetaDataOptionsScope"
               },
               {
                  "default" : "",
                  "get" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsDisplay",
                  "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsDisplay",
                  "select" : [
                     {
                        "k" : "",
                        "v" : ""
                     },
                     {
                        "k" : "page",
                        "v" : "page"
                     },
                     {
                        "k" : "popup",
                        "v" : "popup"
                     },
                     {
                        "k" : "touch",
                        "v" : "touch"
                     },
                     {
                        "k" : "wap",
                        "v" : "wap"
                     }
                  ],
                  "title" : "oidcOPMetaDataOptionsDisplay",
                  "type" : "select"
               },
               {
                  "get" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsPrompt",
                  "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsPrompt",
                  "title" : "oidcOPMetaDataOptionsPrompt"
               },
               {
                  "default" : 0,
                  "get" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsMaxAge",
                  "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsMaxAge",
                  "title" : "oidcOPMetaDataOptionsMaxAge",
                  "type" : "int"
               },
               {
                  "get" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsUiLocales",
                  "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsUiLocales",
                  "title" : "oidcOPMetaDataOptionsUiLocales"
               },
               {
                  "get" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsAcrValues",
                  "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsAcrValues",
                  "title" : "oidcOPMetaDataOptionsAcrValues"
               },
               {
                  "default" : "client_secret_post",
                  "get" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsTokenEndpointAuthMethod",
                  "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsTokenEndpointAuthMethod",
                  "select" : [
                     {
                        "k" : "client_secret_post",
                        "v" : "client_secret_post"
                     },
                     {
                        "k" : "client_secret_basic",
                        "v" : "client_secret_basic"
                     }
                  ],
                  "title" : "oidcOPMetaDataOptionsTokenEndpointAuthMethod",
                  "type" : "select"
               },
               {
                  "default" : 1,
                  "get" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsCheckJWTSignature",
                  "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsCheckJWTSignature",
                  "title" : "oidcOPMetaDataOptionsCheckJWTSignature",
                  "type" : "bool"
               },
               {
                  "default" : 30,
                  "get" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsIDTokenMaxAge",
                  "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsIDTokenMaxAge",
                  "title" : "oidcOPMetaDataOptionsIDTokenMaxAge",
                  "type" : "int"
               },
               {
                  "default" : 1,
                  "get" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsUseNonce",
                  "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsUseNonce",
                  "title" : "oidcOPMetaDataOptionsUseNonce",
                  "type" : "bool"
               }
            ],
            "id" : "oidcOPMetaDataOptionsProtocol",
            "title" : "oidcOPMetaDataOptionsProtocol",
            "type" : "simpleInputContainer"
         },
         {
            "get" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsComment",
            "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsComment",
            "title" : "oidcOPMetaDataOptionsComment",
            "type" : "longtext"
         }
      ],
      "id" : "oidcOPMetaDataOptions",
      "title" : "oidcOPMetaDataOptions"
   },
   {
      "_nodes" : [
         {
            "get" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsDisplayName",
            "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsDisplayName",
            "title" : "oidcOPMetaDataOptionsDisplayName"
         },
         {
            "get" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsIcon",
            "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsIcon",
            "title" : "oidcOPMetaDataOptionsIcon"
         },
         {
            "get" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsTooltip",
            "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsTooltip",
            "title" : "oidcOPMetaDataOptionsTooltip"
         },
         {
            "default" : "",
            "get" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsResolutionRule",
            "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsResolutionRule",
            "title" : "oidcOPMetaDataOptionsResolutionRule",
            "type" : "longtext"
         },
         {
            "get" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsSortNumber",
            "id" : tpl+"s/"+key+"/"+"oidcOPMetaDataOptionsSortNumber",
            "title" : "oidcOPMetaDataOptionsSortNumber",
            "type" : "int"
         }
      ],
      "id" : "oidcOPMetaDataOptionsDisplayParams",
      "title" : "oidcOPMetaDataOptionsDisplayParams",
      "type" : "simpleInputContainer"
   }
]
;
  case 'oidcRPMetaDataNode':
    return [
   {
      "cnodes" : tpl+"s/"+key+"/"+"oidcRPMetaDataExportedVars",
      "default" : [
         {
            "data" : [
               "mail",
               "string",
               "auto"
            ],
            "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataExportedVars/email",
            "title" : "email",
            "type" : "oidcAttribute"
         },
         {
            "data" : [
               "sn",
               "string",
               "auto"
            ],
            "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataExportedVars/family_name",
            "title" : "family_name",
            "type" : "oidcAttribute"
         },
         {
            "data" : [
               "cn",
               "string",
               "auto"
            ],
            "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataExportedVars/name",
            "title" : "name",
            "type" : "oidcAttribute"
         }
      ],
      "help" : "idpopenidconnect.html#oidcexportedattr",
      "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataExportedVars",
      "title" : "oidcRPMetaDataExportedVars",
      "type" : "oidcAttributeContainer"
   },
   {
      "cnodes" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsExtraClaims",
      "default" : [],
      "help" : "idpopenidconnect.html#oidcextraclaims",
      "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsExtraClaims",
      "title" : "oidcRPMetaDataOptionsExtraClaims",
      "type" : "keyTextContainer"
   },
   {
      "cnodes" : tpl+"s/"+key+"/"+"oidcRPMetaDataScopeRules",
      "default" : [],
      "help" : "idpopenidconnect.html#scope-rules",
      "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataScopeRules",
      "title" : "oidcRPMetaDataScopeRules",
      "type" : "keyTextContainer"
   },
   {
      "cnodes" : tpl+"s/"+key+"/"+"oidcRPMetaDataMacros",
      "default" : [],
      "help" : "exportedvars.html#extend-variables-using-macros-and-groups",
      "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataMacros",
      "title" : "oidcRPMetaDataMacros",
      "type" : "keyTextContainer"
   },
   {
      "_nodes" : [
         {
            "_nodes" : [
               {
                  "default" : 0,
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsPublic",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsPublic",
                  "title" : "oidcRPMetaDataOptionsPublic",
                  "type" : "bool"
               },
               {
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsClientID",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsClientID",
                  "title" : "oidcRPMetaDataOptionsClientID"
               },
               {
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsClientSecret",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsClientSecret",
                  "title" : "oidcRPMetaDataOptionsClientSecret",
                  "type" : "password"
               },
               {
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsRedirectUris",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsRedirectUris",
                  "title" : "oidcRPMetaDataOptionsRedirectUris"
               }
            ],
            "id" : "oidcRPMetaDataOptionsBasic",
            "title" : "oidcRPMetaDataOptionsBasic",
            "type" : "simpleInputContainer"
         },
         {
            "_nodes" : [
               {
                  "default" : 0,
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsBypassConsent",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsBypassConsent",
                  "title" : "oidcRPMetaDataOptionsBypassConsent",
                  "type" : "bool"
               },
               {
                  "default" : 0,
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsIDTokenForceClaims",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsIDTokenForceClaims",
                  "title" : "oidcRPMetaDataOptionsIDTokenForceClaims",
                  "type" : "bool"
               },
               {
                  "default" : 0,
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsAccessTokenJWT",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsAccessTokenJWT",
                  "title" : "oidcRPMetaDataOptionsAccessTokenJWT",
                  "type" : "bool"
               },
               {
                  "default" : 0,
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsAccessTokenClaims",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsAccessTokenClaims",
                  "title" : "oidcRPMetaDataOptionsAccessTokenClaims",
                  "type" : "bool"
               },
               {
                  "default" : 0,
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsRefreshToken",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsRefreshToken",
                  "title" : "oidcRPMetaDataOptionsRefreshToken",
                  "type" : "bool"
               },
               {
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsUserIDAttr",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsUserIDAttr",
                  "title" : "oidcRPMetaDataOptionsUserIDAttr"
               },
               {
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsAdditionalAudiences",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsAdditionalAudiences",
                  "title" : "oidcRPMetaDataOptionsAdditionalAudiences"
               }
            ],
            "id" : "oidcRPMetaDataOptionsAdvanced",
            "title" : "oidcRPMetaDataOptionsAdvanced",
            "type" : "simpleInputContainer"
         },
         {
            "_nodes" : [
               {
                  "default" : "HS512",
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsIDTokenSignAlg",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsIDTokenSignAlg",
                  "select" : [
                     {
                        "k" : "none",
                        "v" : "None"
                     },
                     {
                        "k" : "HS256",
                        "v" : "HS256"
                     },
                     {
                        "k" : "HS384",
                        "v" : "HS384"
                     },
                     {
                        "k" : "HS512",
                        "v" : "HS512"
                     },
                     {
                        "k" : "RS256",
                        "v" : "RS256"
                     },
                     {
                        "k" : "RS384",
                        "v" : "RS384"
                     },
                     {
                        "k" : "RS512",
                        "v" : "RS512"
                     }
                  ],
                  "title" : "oidcRPMetaDataOptionsIDTokenSignAlg",
                  "type" : "select"
               },
               {
                  "default" : "RS256",
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsAccessTokenSignAlg",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsAccessTokenSignAlg",
                  "select" : [
                     {
                        "k" : "RS256",
                        "v" : "RS256"
                     },
                     {
                        "k" : "RS384",
                        "v" : "RS384"
                     },
                     {
                        "k" : "RS512",
                        "v" : "RS512"
                     }
                  ],
                  "title" : "oidcRPMetaDataOptionsAccessTokenSignAlg",
                  "type" : "select"
               },
               {
                  "default" : "",
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsUserInfoSignAlg",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsUserInfoSignAlg",
                  "select" : [
                     {
                        "k" : "",
                        "v" : "JSON"
                     },
                     {
                        "k" : "none",
                        "v" : "JWT/None"
                     },
                     {
                        "k" : "HS256",
                        "v" : "JWT/HS256"
                     },
                     {
                        "k" : "HS384",
                        "v" : "JWT/HS384"
                     },
                     {
                        "k" : "HS512",
                        "v" : "JWT/HS512"
                     },
                     {
                        "k" : "RS256",
                        "v" : "JWT/RS256"
                     },
                     {
                        "k" : "RS384",
                        "v" : "JWT/RS384"
                     },
                     {
                        "k" : "RS512",
                        "v" : "JWT/RS512"
                     }
                  ],
                  "title" : "oidcRPMetaDataOptionsUserInfoSignAlg",
                  "type" : "select"
               },
               {
                  "default" : 0,
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsRequirePKCE",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsRequirePKCE",
                  "title" : "oidcRPMetaDataOptionsRequirePKCE",
                  "type" : "bool"
               },
               {
                  "default" : 0,
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsAllowOffline",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsAllowOffline",
                  "title" : "oidcRPMetaDataOptionsAllowOffline",
                  "type" : "bool"
               },
               {
                  "default" : 0,
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsAllowPasswordGrant",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsAllowPasswordGrant",
                  "title" : "oidcRPMetaDataOptionsAllowPasswordGrant",
                  "type" : "bool"
               },
               {
                  "default" : 0,
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsAllowClientCredentialsGrant",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsAllowClientCredentialsGrant",
                  "title" : "oidcRPMetaDataOptionsAllowClientCredentialsGrant",
                  "type" : "bool"
               },
               {
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsAuthnLevel",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsAuthnLevel",
                  "title" : "oidcRPMetaDataOptionsAuthnLevel",
                  "type" : "int"
               },
               {
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsRule",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsRule",
                  "title" : "oidcRPMetaDataOptionsRule"
               }
            ],
            "id" : "security",
            "title" : "security",
            "type" : "simpleInputContainer"
         },
         {
            "_nodes" : [
               {
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsAuthorizationCodeExpiration",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsAuthorizationCodeExpiration",
                  "title" : "oidcRPMetaDataOptionsAuthorizationCodeExpiration",
                  "type" : "int"
               },
               {
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsIDTokenExpiration",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsIDTokenExpiration",
                  "title" : "oidcRPMetaDataOptionsIDTokenExpiration",
                  "type" : "int"
               },
               {
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsAccessTokenExpiration",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsAccessTokenExpiration",
                  "title" : "oidcRPMetaDataOptionsAccessTokenExpiration",
                  "type" : "int"
               },
               {
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsOfflineSessionExpiration",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsOfflineSessionExpiration",
                  "title" : "oidcRPMetaDataOptionsOfflineSessionExpiration",
                  "type" : "int"
               }
            ],
            "id" : "oidcRPMetaDataOptionsTimeouts",
            "title" : "oidcRPMetaDataOptionsTimeouts",
            "type" : "simpleInputContainer"
         },
         {
            "_nodes" : [
               {
                  "default" : 0,
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsLogoutBypassConfirm",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsLogoutBypassConfirm",
                  "title" : "oidcRPMetaDataOptionsLogoutBypassConfirm",
                  "type" : "bool"
               },
               {
                  "default" : 0,
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsLogoutSessionRequired",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsLogoutSessionRequired",
                  "title" : "oidcRPMetaDataOptionsLogoutSessionRequired",
                  "type" : "bool"
               },
               {
                  "default" : "front",
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsLogoutType",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsLogoutType",
                  "select" : [
                     {
                        "k" : "front",
                        "v" : "Front Channel"
                     }
                  ],
                  "title" : "oidcRPMetaDataOptionsLogoutType",
                  "type" : "select"
               },
               {
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsLogoutUrl",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsLogoutUrl",
                  "title" : "oidcRPMetaDataOptionsLogoutUrl"
               },
               {
                  "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsPostLogoutRedirectUris",
                  "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsPostLogoutRedirectUris",
                  "title" : "oidcRPMetaDataOptionsPostLogoutRedirectUris"
               }
            ],
            "id" : "logout",
            "title" : "logout",
            "type" : "simpleInputContainer"
         },
         {
            "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsComment",
            "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsComment",
            "title" : "oidcRPMetaDataOptionsComment",
            "type" : "longtext"
         }
      ],
      "help" : "idpopenidconnect.html#options",
      "id" : "oidcRPMetaDataOptions",
      "title" : "oidcRPMetaDataOptions"
   },
   {
      "_nodes" : [
         {
            "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsDisplayName",
            "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsDisplayName",
            "title" : "oidcRPMetaDataOptionsDisplayName"
         },
         {
            "get" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsIcon",
            "id" : tpl+"s/"+key+"/"+"oidcRPMetaDataOptionsIcon",
            "title" : "oidcRPMetaDataOptionsIcon"
         }
      ],
      "id" : "oidcRPMetaDataOptionsDisplay",
      "title" : "oidcRPMetaDataOptionsDisplay",
      "type" : "simpleInputContainer"
   }
]
;
  case 'samlIDPMetaDataNode':
    return [
   {
      "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataXML",
      "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataXML",
      "title" : "samlIDPMetaDataXML",
      "type" : "file"
   },
   {
      "cnodes" : tpl+"s/"+key+"/"+"samlIDPMetaDataExportedAttributes",
      "default" : [],
      "help" : "authsaml.html#exported-attributes",
      "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataExportedAttributes",
      "title" : "samlIDPMetaDataExportedAttributes",
      "type" : "samlAttributeContainer"
   },
   {
      "_nodes" : [
         {
            "default" : 0,
            "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsAdaptSessionUtime",
            "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsAdaptSessionUtime",
            "title" : "samlIDPMetaDataOptionsAdaptSessionUtime",
            "type" : "bool"
         },
         {
            "default" : 0,
            "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsForceUTF8",
            "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsForceUTF8",
            "title" : "samlIDPMetaDataOptionsForceUTF8",
            "type" : "bool"
         },
         {
            "default" : 0,
            "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsStoreSAMLToken",
            "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsStoreSAMLToken",
            "title" : "samlIDPMetaDataOptionsStoreSAMLToken",
            "type" : "bool"
         },
         {
            "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsUserAttribute",
            "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsUserAttribute",
            "title" : "samlIDPMetaDataOptionsUserAttribute"
         }
      ],
      "id" : "samlIDPMetaDataOptionsSession",
      "title" : "samlIDPMetaDataOptionsSession",
      "type" : "simpleInputContainer"
   },
   {
      "_nodes" : [
         {
            "default" : "",
            "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsSignatureMethod",
            "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsSignatureMethod",
            "select" : [
               {
                  "k" : "",
                  "v" : "default"
               },
               {
                  "k" : "RSA_SHA1",
                  "v" : "RSA SHA1"
               },
               {
                  "k" : "RSA_SHA256",
                  "v" : "RSA SHA256"
               },
               {
                  "k" : "RSA_SHA384",
                  "v" : "RSA SHA384"
               },
               {
                  "k" : "RSA_SHA512",
                  "v" : "RSA SHA512"
               }
            ],
            "title" : "samlIDPMetaDataOptionsSignatureMethod",
            "type" : "select"
         },
         {
            "default" : -1,
            "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsSignSSOMessage",
            "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsSignSSOMessage",
            "title" : "samlIDPMetaDataOptionsSignSSOMessage",
            "type" : "trool"
         },
         {
            "default" : 1,
            "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsCheckSSOMessageSignature",
            "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsCheckSSOMessageSignature",
            "title" : "samlIDPMetaDataOptionsCheckSSOMessageSignature",
            "type" : "bool"
         },
         {
            "default" : -1,
            "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsSignSLOMessage",
            "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsSignSLOMessage",
            "title" : "samlIDPMetaDataOptionsSignSLOMessage",
            "type" : "trool"
         },
         {
            "default" : 1,
            "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsCheckSLOMessageSignature",
            "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsCheckSLOMessageSignature",
            "title" : "samlIDPMetaDataOptionsCheckSLOMessageSignature",
            "type" : "bool"
         }
      ],
      "id" : "samlIDPMetaDataOptionsSignature",
      "title" : "samlIDPMetaDataOptionsSignature",
      "type" : "simpleInputContainer"
   },
   {
      "_nodes" : [
         {
            "default" : "",
            "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsSSOBinding",
            "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsSSOBinding",
            "select" : [
               {
                  "k" : "",
                  "v" : ""
               },
               {
                  "k" : "http-post",
                  "v" : "POST"
               },
               {
                  "k" : "http-redirect",
                  "v" : "Redirect"
               },
               {
                  "k" : "artifact-get",
                  "v" : "Artifact GET"
               }
            ],
            "title" : "samlIDPMetaDataOptionsSSOBinding",
            "type" : "select"
         },
         {
            "default" : "",
            "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsSLOBinding",
            "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsSLOBinding",
            "select" : [
               {
                  "k" : "",
                  "v" : ""
               },
               {
                  "k" : "http-post",
                  "v" : "POST"
               },
               {
                  "k" : "http-redirect",
                  "v" : "Redirect"
               },
               {
                  "k" : "http-soap",
                  "v" : "SOAP"
               }
            ],
            "title" : "samlIDPMetaDataOptionsSLOBinding",
            "type" : "select"
         }
      ],
      "id" : "samlIDPMetaDataOptionsBinding",
      "title" : "samlIDPMetaDataOptionsBinding",
      "type" : "simpleInputContainer"
   },
   {
      "_nodes" : [
         {
            "default" : "none",
            "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsEncryptionMode",
            "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsEncryptionMode",
            "select" : [
               {
                  "k" : "none",
                  "v" : "None"
               },
               {
                  "k" : "nameid",
                  "v" : "Name ID"
               },
               {
                  "k" : "assertion",
                  "v" : "Assertion"
               }
            ],
            "title" : "samlIDPMetaDataOptionsEncryptionMode",
            "type" : "select"
         },
         {
            "default" : 1,
            "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsCheckTime",
            "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsCheckTime",
            "title" : "samlIDPMetaDataOptionsCheckTime",
            "type" : "bool"
         },
         {
            "default" : 1,
            "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsCheckAudience",
            "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsCheckAudience",
            "title" : "samlIDPMetaDataOptionsCheckAudience",
            "type" : "bool"
         }
      ],
      "id" : "samlIDPMetaDataOptionsSecurity",
      "title" : "samlIDPMetaDataOptionsSecurity",
      "type" : "simpleInputContainer"
   },
   {
      "_nodes" : [
         {
            "default" : "",
            "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsNameIDFormat",
            "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsNameIDFormat",
            "select" : [
               {
                  "k" : "",
                  "v" : ""
               },
               {
                  "k" : "unspecified",
                  "v" : "Unspecified"
               },
               {
                  "k" : "email",
                  "v" : "Email"
               },
               {
                  "k" : "x509",
                  "v" : "X509 certificate"
               },
               {
                  "k" : "windows",
                  "v" : "Windows"
               },
               {
                  "k" : "kerberos",
                  "v" : "Kerberos"
               },
               {
                  "k" : "entity",
                  "v" : "Entity"
               },
               {
                  "k" : "persistent",
                  "v" : "Persistent"
               },
               {
                  "k" : "transient",
                  "v" : "Transient"
               },
               {
                  "k" : "encrypted",
                  "v" : "Encrypted"
               }
            ],
            "title" : "samlIDPMetaDataOptionsNameIDFormat",
            "type" : "select"
         },
         {
            "default" : 0,
            "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsForceAuthn",
            "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsForceAuthn",
            "title" : "samlIDPMetaDataOptionsForceAuthn",
            "type" : "bool"
         },
         {
            "default" : 0,
            "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsIsPassive",
            "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsIsPassive",
            "title" : "samlIDPMetaDataOptionsIsPassive",
            "type" : "bool"
         },
         {
            "default" : 0,
            "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsAllowLoginFromIDP",
            "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsAllowLoginFromIDP",
            "title" : "samlIDPMetaDataOptionsAllowLoginFromIDP",
            "type" : "bool"
         },
         {
            "default" : "",
            "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsRequestedAuthnContext",
            "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsRequestedAuthnContext",
            "select" : [
               {
                  "k" : "",
                  "v" : ""
               },
               {
                  "k" : "kerberos",
                  "v" : "Kerberos"
               },
               {
                  "k" : "password-protected-transport",
                  "v" : "Password protected transport"
               },
               {
                  "k" : "password",
                  "v" : "Password"
               },
               {
                  "k" : "tls-client",
                  "v" : "TLS client certificate"
               }
            ],
            "title" : "samlIDPMetaDataOptionsRequestedAuthnContext",
            "type" : "select"
         },
         {
            "default" : 0,
            "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsRelayStateURL",
            "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsRelayStateURL",
            "title" : "samlIDPMetaDataOptionsRelayStateURL",
            "type" : "bool"
         },
         {
            "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsComment",
            "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsComment",
            "title" : "samlIDPMetaDataOptionsComment",
            "type" : "longtext"
         }
      ],
      "help" : "authsaml.html#options",
      "id" : "samlIDPMetaDataOptions",
      "title" : "samlIDPMetaDataOptions",
      "type" : "simpleInputContainer"
   },
   {
      "_nodes" : [
         {
            "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsDisplayName",
            "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsDisplayName",
            "title" : "samlIDPMetaDataOptionsDisplayName"
         },
         {
            "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsIcon",
            "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsIcon",
            "title" : "samlIDPMetaDataOptionsIcon"
         },
         {
            "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsTooltip",
            "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsTooltip",
            "title" : "samlIDPMetaDataOptionsTooltip"
         },
         {
            "default" : "",
            "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsResolutionRule",
            "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsResolutionRule",
            "title" : "samlIDPMetaDataOptionsResolutionRule",
            "type" : "longtext"
         },
         {
            "get" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsSortNumber",
            "id" : tpl+"s/"+key+"/"+"samlIDPMetaDataOptionsSortNumber",
            "title" : "samlIDPMetaDataOptionsSortNumber",
            "type" : "int"
         }
      ],
      "id" : "samlIDPMetaDataOptionsDisplay",
      "title" : "samlIDPMetaDataOptionsDisplay",
      "type" : "simpleInputContainer"
   }
]
;
  case 'samlSPMetaDataNode':
    return [
   {
      "get" : tpl+"s/"+key+"/"+"samlSPMetaDataXML",
      "id" : tpl+"s/"+key+"/"+"samlSPMetaDataXML",
      "title" : "samlSPMetaDataXML",
      "type" : "file"
   },
   {
      "cnodes" : tpl+"s/"+key+"/"+"samlSPMetaDataExportedAttributes",
      "default" : [],
      "help" : "idpsaml.html#exported-attributes",
      "id" : tpl+"s/"+key+"/"+"samlSPMetaDataExportedAttributes",
      "title" : "samlSPMetaDataExportedAttributes",
      "type" : "samlAttributeContainer"
   },
   {
      "_nodes" : [
         {
            "_nodes" : [
               {
                  "default" : "",
                  "get" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsNameIDFormat",
                  "id" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsNameIDFormat",
                  "select" : [
                     {
                        "k" : "",
                        "v" : ""
                     },
                     {
                        "k" : "unspecified",
                        "v" : "Unspecified"
                     },
                     {
                        "k" : "email",
                        "v" : "Email"
                     },
                     {
                        "k" : "x509",
                        "v" : "X509 certificate"
                     },
                     {
                        "k" : "windows",
                        "v" : "Windows"
                     },
                     {
                        "k" : "kerberos",
                        "v" : "Kerberos"
                     },
                     {
                        "k" : "entity",
                        "v" : "Entity"
                     },
                     {
                        "k" : "persistent",
                        "v" : "Persistent"
                     },
                     {
                        "k" : "transient",
                        "v" : "Transient"
                     },
                     {
                        "k" : "encrypted",
                        "v" : "Encrypted"
                     }
                  ],
                  "title" : "samlSPMetaDataOptionsNameIDFormat",
                  "type" : "select"
               },
               {
                  "get" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsNameIDSessionKey",
                  "id" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsNameIDSessionKey",
                  "title" : "samlSPMetaDataOptionsNameIDSessionKey"
               },
               {
                  "default" : 0,
                  "get" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsOneTimeUse",
                  "id" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsOneTimeUse",
                  "title" : "samlSPMetaDataOptionsOneTimeUse",
                  "type" : "bool"
               },
               {
                  "default" : 72000,
                  "get" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsSessionNotOnOrAfterTimeout",
                  "id" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsSessionNotOnOrAfterTimeout",
                  "title" : "samlSPMetaDataOptionsSessionNotOnOrAfterTimeout",
                  "type" : "int"
               },
               {
                  "default" : 72000,
                  "get" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsNotOnOrAfterTimeout",
                  "id" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsNotOnOrAfterTimeout",
                  "title" : "samlSPMetaDataOptionsNotOnOrAfterTimeout",
                  "type" : "int"
               },
               {
                  "default" : 1,
                  "get" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsForceUTF8",
                  "id" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsForceUTF8",
                  "title" : "samlSPMetaDataOptionsForceUTF8",
                  "type" : "bool"
               }
            ],
            "id" : "samlSPMetaDataOptionsAuthnResponse",
            "title" : "samlSPMetaDataOptionsAuthnResponse",
            "type" : "simpleInputContainer"
         },
         {
            "_nodes" : [
               {
                  "default" : "",
                  "get" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsSignatureMethod",
                  "id" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsSignatureMethod",
                  "select" : [
                     {
                        "k" : "",
                        "v" : "default"
                     },
                     {
                        "k" : "RSA_SHA1",
                        "v" : "RSA SHA1"
                     },
                     {
                        "k" : "RSA_SHA256",
                        "v" : "RSA SHA256"
                     },
                     {
                        "k" : "RSA_SHA384",
                        "v" : "RSA SHA384"
                     },
                     {
                        "k" : "RSA_SHA512",
                        "v" : "RSA SHA512"
                     }
                  ],
                  "title" : "samlSPMetaDataOptionsSignatureMethod",
                  "type" : "select"
               },
               {
                  "default" : -1,
                  "get" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsSignSSOMessage",
                  "id" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsSignSSOMessage",
                  "title" : "samlSPMetaDataOptionsSignSSOMessage",
                  "type" : "trool"
               },
               {
                  "default" : 1,
                  "get" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsCheckSSOMessageSignature",
                  "id" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsCheckSSOMessageSignature",
                  "title" : "samlSPMetaDataOptionsCheckSSOMessageSignature",
                  "type" : "bool"
               },
               {
                  "default" : -1,
                  "get" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsSignSLOMessage",
                  "id" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsSignSLOMessage",
                  "title" : "samlSPMetaDataOptionsSignSLOMessage",
                  "type" : "trool"
               },
               {
                  "default" : 1,
                  "get" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsCheckSLOMessageSignature",
                  "id" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsCheckSLOMessageSignature",
                  "title" : "samlSPMetaDataOptionsCheckSLOMessageSignature",
                  "type" : "bool"
               }
            ],
            "id" : "samlSPMetaDataOptionsSignature",
            "title" : "samlSPMetaDataOptionsSignature",
            "type" : "simpleInputContainer"
         },
         {
            "_nodes" : [
               {
                  "default" : "none",
                  "get" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsEncryptionMode",
                  "id" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsEncryptionMode",
                  "select" : [
                     {
                        "k" : "none",
                        "v" : "None"
                     },
                     {
                        "k" : "nameid",
                        "v" : "Name ID"
                     },
                     {
                        "k" : "assertion",
                        "v" : "Assertion"
                     }
                  ],
                  "title" : "samlSPMetaDataOptionsEncryptionMode",
                  "type" : "select"
               },
               {
                  "default" : 0,
                  "get" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsEnableIDPInitiatedURL",
                  "id" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsEnableIDPInitiatedURL",
                  "title" : "samlSPMetaDataOptionsEnableIDPInitiatedURL",
                  "type" : "bool"
               },
               {
                  "get" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsAuthnLevel",
                  "id" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsAuthnLevel",
                  "title" : "samlSPMetaDataOptionsAuthnLevel",
                  "type" : "int"
               },
               {
                  "get" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsRule",
                  "id" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsRule",
                  "title" : "samlSPMetaDataOptionsRule"
               }
            ],
            "id" : "samlSPMetaDataOptionsSecurity",
            "title" : "samlSPMetaDataOptionsSecurity",
            "type" : "simpleInputContainer"
         },
         {
            "get" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsComment",
            "id" : tpl+"s/"+key+"/"+"samlSPMetaDataOptionsComment",
            "title" : "samlSPMetaDataOptionsComment",
            "type" : "longtext"
         }
      ],
      "help" : "idpsaml.html#options",
      "id" : "samlSPMetaDataOptions",
      "title" : "samlSPMetaDataOptions"
   },
   {
      "cnodes" : tpl+"s/"+key+"/"+"samlSPMetaDataMacros",
      "default" : [],
      "help" : "exportedvars.html#extend-variables-using-macros-and-groups",
      "id" : tpl+"s/"+key+"/"+"samlSPMetaDataMacros",
      "title" : "samlSPMetaDataMacros",
      "type" : "keyTextContainer"
   }
]
;
  case 'virtualHost':
    return [
   {
      "cnodes" : tpl+"s/"+key+"/"+"locationRules",
      "default" : [
         {
            "data" : "deny",
            "id" : tpl+"s/"+key+"/"+"locationRules/default",
            "re" : "default",
            "title" : "default",
            "type" : "rule"
         }
      ],
      "help" : "writingrulesand_headers.html#rules",
      "id" : tpl+"s/"+key+"/"+"locationRules",
      "title" : "locationRules",
      "type" : "ruleContainer"
   },
   {
      "cnodes" : tpl+"s/"+key+"/"+"exportedHeaders",
      "help" : "writingrulesand_headers.html#headers",
      "id" : tpl+"s/"+key+"/"+"exportedHeaders",
      "title" : "exportedHeaders",
      "type" : "keyTextContainer"
   },
   {
      "cnodes" : tpl+"s/"+key+"/"+"post",
      "help" : "formreplay.html",
      "id" : tpl+"s/"+key+"/"+"post",
      "title" : "post",
      "type" : "postContainer"
   },
   {
      "_nodes" : [
         {
            "default" : -1,
            "get" : tpl+"s/"+key+"/"+"vhostPort",
            "id" : tpl+"s/"+key+"/"+"vhostPort",
            "title" : "vhostPort",
            "type" : "int"
         },
         {
            "default" : -1,
            "get" : tpl+"s/"+key+"/"+"vhostHttps",
            "id" : tpl+"s/"+key+"/"+"vhostHttps",
            "title" : "vhostHttps",
            "type" : "trool"
         },
         {
            "default" : 0,
            "get" : tpl+"s/"+key+"/"+"vhostMaintenance",
            "id" : tpl+"s/"+key+"/"+"vhostMaintenance",
            "title" : "vhostMaintenance",
            "type" : "bool"
         },
         {
            "default" : "",
            "get" : tpl+"s/"+key+"/"+"vhostAliases",
            "id" : tpl+"s/"+key+"/"+"vhostAliases",
            "title" : "vhostAliases"
         },
         {
            "default" : "",
            "get" : tpl+"s/"+key+"/"+"vhostAccessToTrace",
            "id" : tpl+"s/"+key+"/"+"vhostAccessToTrace",
            "title" : "vhostAccessToTrace"
         },
         {
            "get" : tpl+"s/"+key+"/"+"vhostAuthnLevel",
            "id" : tpl+"s/"+key+"/"+"vhostAuthnLevel",
            "title" : "vhostAuthnLevel",
            "type" : "int"
         },
         {
            "default" : "Main",
            "get" : tpl+"s/"+key+"/"+"vhostType",
            "id" : tpl+"s/"+key+"/"+"vhostType",
            "select" : [
               {
                  "k" : "AuthBasic",
                  "v" : "AuthBasic"
               },
               {
                  "k" : "CDA",
                  "v" : "CDA"
               },
               {
                  "k" : "DevOps",
                  "v" : "DevOps"
               },
               {
                  "k" : "DevOpsST",
                  "v" : "DevOpsST"
               },
               {
                  "k" : "Main",
                  "v" : "Main"
               },
               {
                  "k" : "OAuth2",
                  "v" : "OAuth2"
               },
               {
                  "k" : "SecureToken",
                  "v" : "SecureToken"
               },
               {
                  "k" : "ServiceToken",
                  "v" : "ServiceToken"
               },
               {
                  "k" : "ZimbraPreAuth",
                  "v" : "ZimbraPreAuth"
               }
            ],
            "title" : "vhostType",
            "type" : "select"
         },
         {
            "get" : tpl+"s/"+key+"/"+"vhostDevOpsRulesUrl",
            "id" : tpl+"s/"+key+"/"+"vhostDevOpsRulesUrl",
            "title" : "vhostDevOpsRulesUrl"
         },
         {
            "default" : -1,
            "get" : tpl+"s/"+key+"/"+"vhostServiceTokenTTL",
            "id" : tpl+"s/"+key+"/"+"vhostServiceTokenTTL",
            "title" : "vhostServiceTokenTTL",
            "type" : "int"
         },
         {
            "default" : "",
            "get" : tpl+"s/"+key+"/"+"vhostComment",
            "id" : tpl+"s/"+key+"/"+"vhostComment",
            "title" : "vhostComment",
            "type" : "longtext"
         }
      ],
      "help" : "configvhost.html#options",
      "id" : "vhostOptions",
      "title" : "vhostOptions",
      "type" : "simpleInputContainer"
   }
]
;
  default:
    return [];
  }
}

function setScopeVars(scope) {
  scope.portal = scope.data[0]._nodes[0]._nodes[0];
  scope.getKey(scope.portal);
  scope.domain = scope.data[0]._nodes[4]._nodes[1];
  scope.getKey(scope.domain);
}