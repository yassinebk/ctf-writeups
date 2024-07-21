use lib 'inc';
use Test::More;
use strict;
use IO::String;
use LWP::UserAgent;
use LWP::Protocol::PSGI;
use MIME::Base64;
use URI::QueryParam;

BEGIN {
    require 't/test-lib.pm';
    require 't/oidc-lib.pm';
}

my $debug = 'error';
my ( $op, $rp, $res );

my $access_token;

LWP::Protocol::PSGI->register(
    sub {
        my $req = Plack::Request->new(@_);
        ok( $req->uri =~ m#http://auth.((?:o|r)p).com(.*)#, ' REST request' );
        my $host = $1;
        my $url  = $2;
        my ( $res, $client );
        count(1);
        if ( $host eq 'op' ) {
            pass("  Request from RP to OP,     endpoint $url");
            $client = $op;
        }
        elsif ( $host eq 'rp' ) {
            pass('  Request from OP to RP');
            $client = $rp;
        }
        else {
            fail('  Aborting REST request (external)');
            return [ 500, [], [] ];
        }
        if ( $req->method =~ /^post$/i ) {
            my $s = $req->content;
            if ( $req->uri eq '/token/oauth2' ) {
                is( $req->param("my_param"),
                    "my value", "oidcGenerateTokenRequest called" );
                count(1);
            }
            ok(
                $res = $client->_post(
                    $url, IO::String->new($s),
                    length => length($s),
                    type   => $req->header('Content-Type'),
                ),
                '  Execute request'
            );
        }
        else {
            ok(
                $res = $client->_get(
                    $url,
                    custom => {
                        HTTP_AUTHORIZATION => $req->header('Authorization'),
                    }
                ),
                '  Execute request'
            );
        }
        ok( $res->[0] == 200, '  Response is 200' );
        ok( getHeader( $res, 'Content-Type' ) =~ m#^application/json#,
            '  Content is JSON' )
          or explain( $res->[1], 'Content-Type => application/json' );
        count(4);
        if ( $res->[2]->[0] =~ /"access_token":"(.*?)"/ ) {
            $access_token = $1;
            pass "Found access_token $access_token";
            count(1);
        }
        return $res;
    }
);

# Initialization
ok( $op = op(), 'OP portal' );

ok( $res = $op->_get('/oauth2/jwks'), 'Get JWKS,     endpoint /oauth2/jwks' );
expectOK($res);
my $jwks = $res->[2]->[0];

ok(
    $res = $op->_get('/.well-known/openid-configuration'),
    'Get metadata, endpoint /.well-known/openid-configuration'
);
expectOK($res);
my $metadata = $res->[2]->[0];
count(3);

switch ('rp');
&Lemonldap::NG::Handler::Main::cfgNum( 0, 0 );
ok( $rp = rp( $jwks, $metadata ), 'RP portal' );
count(1);

# Query RP for auth
ok( $res = $rp->_get( '/', accept => 'text/html' ), 'Unauth SP request' );
count(1);
my ( $url, $query ) =
  expectRedirection( $res, qr#http://auth.op.com(/oauth2/authorize)\?(.*)$# );

# Push request to OP
switch ('op');
ok( $res = $op->_get( $url, query => $query, accept => 'text/html' ),
    "Push request to OP,         endpoint $url" );
count(1);
expectOK($res);

# Try to authenticate to OP with unallowed user
my $failquery = "user=rtyler&password=rtyler&$query";
ok(
    $res = $op->_post(
        $url,
        IO::String->new($failquery),
        accept => 'text/html',
        length => length($failquery),
    ),
    "Post authentication,        endpoint $url"
);
count(1);
my $idpId = expectCookie($res);

# Should be denied by rule
expectPortalError( $res, 84 );

# Try to authenticate to OP with allowed user
$query = "user=french&password=french&$query";
ok(
    $res = $op->_post(
        $url,
        IO::String->new($query),
        accept => 'text/html',
        length => length($query),
    ),
    "Post authentication,        endpoint $url"
);
count(1);
$idpId = expectCookie($res);
my ( $host, $tmp );
( $host, $tmp, $query ) = expectForm( $res, '#', undef, 'confirm' );

ok(
    $res = $op->_post(
        $url,
        IO::String->new($query),
        accept => 'text/html',
        cookie => "lemonldap=$idpId",
        length => length($query),
    ),
    "Post confirmation,          endpoint $url"
);
count(1);

($query) = expectRedirection( $res, qr#^http://auth.rp.com/?\?(.*)$# );

# Push OP response to RP
switch ('rp');

ok( $res = $rp->_get( '/', query => $query, accept => 'text/html' ),
    'Call openidconnectcallback on RP' );
count(1);
my $spId = expectCookie($res);

switch ('op');
ok(
    $res = $op->_get( '/oauth2/checksession.html', accept => 'text.html' ),
    'Check session,      endpoint /oauth2/checksession.html'
);
count(1);
expectOK($res);
ok( getHeader( $res, 'Content-Security-Policy' ) !~ /frame-ancestors/,
    ' Frame can be embedded' )
  or explain( $res->[1],
    'Content-Security-Policy does not contain a frame-ancestors' );
count(1);

# Verify UTF-8
ok(
    $res = $op->_get(
        '/oauth2/userinfo', query => 'access_token=' . $access_token,
    ),
    'Get userinfo'
);
$res = expectJSON($res);
ok( $res->{name} eq 'Frédéric Accents', 'UTF-8 values' )
  or explain( $res, 'name => Frédéric Accents' );
count(2);

ok( $res = $op->_get("/sessions/global/$spId"), 'Get UTF-8' );
$res = expectJSON($res);
ok( $res->{cn} eq 'Frédéric Accents', 'UTF-8 values' )
  or explain( $res, 'cn => Frédéric Accents' );
count(2);

switch ('rp');
ok( $res = $rp->_get("/sessions/global/$spId"), 'Get UTF-8' );
$res = expectJSON($res);
my $access_token_eol = $res->{_oidc_access_token_eol};
my $access_token_old = $res->{_oidc_access_token};
ok( $access_token_eol, 'OIDC EOL time is stored' );
ok( $access_token_old, 'Obtained refresh token' );
is( $res->{cn},   'Frédéric Accents', 'UTF-8 values' );
is( $res->{mail}, 'fa@badwolf.org',   'Correct email' );
count(5);

is( $res->{userinfo_hook}, "op/french", "oidcGotUserInfo called" );
is( $res->{id_token_hook}, "op/french", "oidcGotIDToken called" );
count(2);

my $id_token_decoded = id_token_payload( $res->{_oidc_id_token} );
is( $id_token_decoded->{acr}, 'customacr-1', "Correct custom ACR" );
count(1);

# Update session at OP
$Lemonldap::NG::Portal::UserDB::Demo::demoAccounts{french} = {
    uid  => 'french',
    cn   => 'Frédéric Accents',
    mail => 'fa2@badwolf.org',
    guy  => '',
    type => '',
};
switch ('op');
ok( $op->_get( '/refresh', cookie => "lemonldap=$idpId" ) );
count(1);
switch ('rp');

# Test session refresh (before access token refresh)
ok(
    $res = $rp->_get(
        '/refresh',
        cookie => "lemonldap=$spId",
        accept => 'text/html'
    ),
    'Query RP for refresh'
);
count(1);

ok( $res = $rp->_get("/sessions/global/$spId"), 'Get session after refresh' );
count(1);
$res = expectJSON($res);
my $access_token_new     = $res->{_oidc_access_token};
my $access_token_new_eol = $res->{_oidc_access_token_eol};
is( $access_token_new_eol, $access_token_eol,
    "Access token EOL has not changed" );
is( $access_token_new, $access_token_old, "Access token has not changed" );
is( $res->{mail},      'fa2@badwolf.org', 'Updated RP session' );
count(3);

# Update session at OP
$Lemonldap::NG::Portal::UserDB::Demo::demoAccounts{french} = {
    uid  => 'french',
    cn   => 'Frédéric Accents',
    mail => 'fa3@badwolf.org',
    guy  => '',
    type => '',
};
switch ('op');
ok( $op->_get( '/refresh', cookie => "lemonldap=$idpId" ) );
count(1);
switch ('rp');

# Test session refresh (with access token refresh)
Time::Fake->offset("+2h");
ok(
    $res = $rp->_get(
        '/refresh',
        cookie => "lemonldap=$spId",
        accept => 'text/html'
    ),
    'Query RP for refresh'
);
count(1);

ok( $res = $rp->_get("/sessions/global/$spId"), 'Get session after refresh' );
count(1);
$res                  = expectJSON($res);
$access_token_new     = $res->{_oidc_access_token};
$access_token_new_eol = $res->{_oidc_access_token_eol};
isnt( $access_token_new_eol, $access_token_eol,
    "Access token EOL has changed" );
isnt( $access_token_new, $access_token_old, "Access token has changed" );
is( $res->{mail}, 'fa3@badwolf.org', 'Updated RP session' );
count(3);

# Logout initiated by RP
ok(
    $res = $rp->_get(
        '/',
        query  => 'logout',
        cookie => "lemonldap=$spId",
        accept => 'text/html'
    ),
    'Query RP for logout'
);
count(1);
( $url, $query ) = expectRedirection( $res,
    qr#http://auth.op.com(/oauth2/logout)\?.*(post_logout_redirect_uri=.+)$# );

# Push logout to OP
switch ('op');

ok(
    $res = $op->_get(
        $url,
        query  => $query,
        cookie => "lemonldap=$idpId",
        accept => 'text/html'
    ),
    "Push logout request to OP,     endpoint $url"
);
count(1);

( $host, $tmp, $query ) = expectForm( $res, '#', undef, 'confirm' );

ok(
    $res = $op->_post(
        $url, IO::String->new($query),
        length => length($query),
        cookie => "lemonldap=$idpId",
        accept => 'text/html',
    ),
    "Confirm logout,                endpoint $url"
);
count(1);

( $url, $query ) = expectRedirection( $res, qr#.# );

my $removedCookie = expectCookie($res);
is( $removedCookie, 0, "SSO cookie removed" );
count(1);

# Test logout endpoint without session
ok(
    $res = $op->_get(
        '/oauth2/logout',
        accept => 'text/html',
        query  => 'post_logout_redirect_uri=http://auth.rp.com/?logout=1'
    ),
    'logout endpoint with redirect, endpoint /oauth2/logout'
);
count(1);
expectRedirection( $res, 'http://auth.rp.com/?logout=1' );

ok( $res = $op->_get('/oauth2/logout'),
    'logout endpoint,               endpoint /oauth2/logout' );
count(1);
expectReject($res);

# Test if logout is done
ok(
    $res = $op->_get(
        '/', cookie => "lemonldap=$idpId",
    ),
    'Test if user is reject on IdP'
);
count(1);
expectReject($res);

switch ('rp');
ok(
    $res = $rp->_get(
        '/',
        accept => 'text/html',
        cookie => "lemonldap=$spId"
    ),
    'Test if user is reject on SP'
);
count(1);
( $url, $query ) =
  expectRedirection( $res, qr#^http://auth.op.com(/oauth2/authorize)\?(.*)$# );

my $u = URI->new;
$u->query($query);
is( $u->query_param('my_param'),
    "my value", "oidcGenerateAuthenticationRequest called" );
count(1);

# Test if consent was saved
# -------------------------

# Push request to OP
switch ('op');
ok( $res = $op->_get( $url, query => $query, accept => 'text/html' ),
    "Push request to OP,         endpoint $url" );
count(1);
expectOK($res);

# Try to authenticate to OP
$query = "user=french&password=french&$query";
ok(
    $res = $op->_post(
        $url,
        IO::String->new($query),
        accept => 'text/html',
        length => length($query),
    ),
    "Post authentication,        endpoint $url"
);
count(1);
$idpId = expectCookie($res);

#expectRedirection( $res, qr#^http://auth.rp.com/# );

#print STDERR Dumper($res);

clean_sessions();
done_testing( count() );

sub op {
    return LLNG::Manager::Test->new( {
            ini => {
                logLevel                        => $debug,
                domain                          => 'idp.com',
                portal                          => 'http://auth.op.com/',
                authentication                  => 'Demo',
                userDB                          => 'Same',
                issuerDBOpenIDConnectActivation => "1",
                restSessionServer               => 1,
                restExportSecretKeys            => 1,
                oidcRPMetaDataExportedVars      => {
                    rp => {
                        email       => "mail",
                        family_name => "cn",
                        name        => "cn"
                    }
                },
                oidcServiceAllowHybridFlow            => 1,
                oidcServiceAllowImplicitFlow          => 1,
                oidcServiceAllowAuthorizationCodeFlow => 1,
                oidcRPMetaDataOptions                 => {
                    rp => {
                        oidcRPMetaDataOptionsDisplayName       => "RP",
                        oidcRPMetaDataOptionsIDTokenExpiration => 3600,
                        oidcRPMetaDataOptionsClientID          => "rpid",
                        oidcRPMetaDataOptionsIDTokenSignAlg    => "HS512",
                        oidcRPMetaDataOptionsBypassConsent     => 0,
                        oidcRPMetaDataOptionsClientSecret      => "rpsecret",
                        oidcRPMetaDataOptionsRefreshToken      => 1,
                        oidcRPMetaDataOptionsUserIDAttr        => "",
                        oidcRPMetaDataOptionsAccessTokenExpiration  => 3600,
                        oidcRPMetaDataOptionsPostLogoutRedirectUris =>
                          "http://auth.rp.com/?logout=1",
                        oidcRPMetaDataOptionsRule => '$uid eq "french"',
                    }
                },
                oidcOPMetaDataOptions           => {},
                oidcOPMetaDataJSON              => {},
                oidcOPMetaDataJWKS              => {},
                oidcServiceMetaDataAuthnContext => {
                    'loa-4'       => 4,
                    'customacr-1' => 1,
                    'loa-5'       => 5,
                    'loa-2'       => 2,
                    'loa-3'       => 3
                },
                oidcServicePrivateKeySig => oidc_key_op_private_sig,
                oidcServicePublicKeySig  => oidc_cert_op_public_sig,
            }
        }
    );
}

sub rp {
    my ( $jwks, $metadata ) = @_;
    return LLNG::Manager::Test->new( {
            ini => {
                logLevel                   => $debug,
                domain                     => 'rp.com',
                portal                     => 'http://auth.rp.com/',
                authentication             => 'OpenIDConnect',
                userDB                     => 'Same',
                restSessionServer          => 1,
                restExportSecretKeys       => 1,
                oidcOPMetaDataExportedVars => {
                    op => {
                        cn   => "name",
                        uid  => "sub",
                        sn   => "family_name",
                        mail => "email"
                    }
                },
                oidcOPMetaDataOptions => {
                    op => {
                        oidcOPMetaDataOptionsCheckJWTSignature => 1,
                        oidcOPMetaDataOptionsJWKSTimeout       => 0,
                        oidcOPMetaDataOptionsClientSecret      => "rpsecret",
                        oidcOPMetaDataOptionsScope => "openid profile email",
                        oidcOPMetaDataOptionsStoreIDToken     => 0,
                        oidcOPMetaDataOptionsMaxAge           => 30,
                        oidcOPMetaDataOptionsDisplay          => "",
                        oidcOPMetaDataOptionsClientID         => "rpid",
                        oidcOPMetaDataOptionsStoreIDToken     => 1,
                        oidcOPMetaDataOptionsConfigurationURI =>
                          "https://auth.op.com/.well-known/openid-configuration"
                    }
                },
                oidcOPMetaDataJWKS => {
                    op => $jwks,
                },
                oidcOPMetaDataJSON => {
                    op => $metadata,
                },
                customPlugins => 't::OidcHookPlugin',
            }
        }
    );
}
