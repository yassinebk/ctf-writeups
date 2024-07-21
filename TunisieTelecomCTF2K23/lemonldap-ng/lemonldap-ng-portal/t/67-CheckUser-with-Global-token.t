use Test::More;
use strict;
use IO::String;

require 't/test-lib.pm';

my $res;

my $client = LLNG::Manager::Test->new( {
        ini => {
            logLevel                        => 'error',
            authentication                  => 'Demo',
            userDB                          => 'Same',
            loginHistoryEnabled             => 0,
            brutForceProtection             => 0,
            checkUser                       => 1,
            requireToken                    => 1,
            tokenUseGlobalStorage           => 1,
            formTimeout                     => 120,
            checkUserDisplayEmptyValues     => 1,
            checkUserDisplayPersistentInfo  => 1,
            checkUserDisplayComputedSession => 1,
            macros                          => {
                _whatToTrace =>
                  '$_auth eq "SAML" ? "$_user\@$_idpConfKey" : "$_user"',
                mail => 'uc $mail',
            }
        }
    }
);

## Try to authenticate
ok( $res = $client->_get( '/', accept => 'text/html' ), 'Get Menu', );
count(1);
my ( $host, $url, $query ) =
  expectForm( $res, '#', undef, 'user', 'password', 'token' );

$query =~ s/user=/user=dwho/;
$query =~ s/password=/password=dwho/;
ok(
    $res = $client->_post(
        '/',
        IO::String->new($query),
        length => length($query),
        accept => 'text/html',
    ),
    'Auth query'
);
count(1);

my $id = expectCookie($res);
expectRedirection( $res, 'http://auth.example.com/' );

# CheckUser form
# ------------------------
ok(
    $res = $client->_get(
        '/checkuser',
        cookie => "lemonldap=$id",
        accept => 'text/html'
    ),
    'CheckUser form',
);
count(1);
( $host, $url, $query ) =
  expectForm( $res, undef, '/checkuser', 'user', 'url', 'token' );
ok( $res->[2]->[0] =~ m%<span trspan="checkUser">%, 'Found trspan="checkUser"' )
  or explain( $res->[2]->[0], 'trspan="checkUser"' );
count(1);

# Wildcarded VHost
$query =~ s/url=/url=http%3A%2F%2Fappli.example.llng/;

ok(
    $res = $client->_post(
        '/checkuser',
        IO::String->new($query),
        cookie => "lemonldap=$id",
        length => length($query),
        accept => 'text/html',
    ),
    'POST checkuser'
);
ok( $res->[2]->[0] =~ m%<span trspan="allowed">%, 'Found allowed' )
  or explain( $res->[2]->[0], 'trspan="allowed"' );
count(2);
( $host, $url, $query ) =
  expectForm( $res, undef, '/checkuser', 'user', 'url', 'token' );

# Bad VHost (checkXSS)
$query =~
  s/url=http%3A%2F%2Fappli.example.llng/url=http%3A%2F%2Fappli'.example.llng/;

ok(
    $res = $client->_post(
        '/checkuser',
        IO::String->new($query),
        cookie => "lemonldap=$id",
        length => length($query),
        accept => 'text/html',
    ),
    'POST checkuser'
);
ok( $res->[2]->[0] =~ m%<span trspan="VHnotFound">%, 'Found VHnotFound' )
  or explain( $res->[2]->[0], 'trspan="VHnotFound"' );
count(2);
( $host, $url, $query ) =
  expectForm( $res, undef, '/checkuser', 'user', 'url', 'token' );

# Skipping time until the form token has expired
Time::Fake->offset("+5m");

ok(
    $res = $client->_post(
        '/checkuser',
        IO::String->new($query),
        cookie => "lemonldap=$id",
        length => length($query),
        accept => 'text/html',
    ),
    'POST checkuser'
);
ok( $res->[2]->[0] =~ m%<span trspan="PE82"></span>%, 'Found PE_TOKENEXPIRED' )
  or explain( $res->[2]->[0], 'trspan="PE82"' );
count(2);
( $host, $url, $query ) =
  expectForm( $res, undef, '/checkuser', 'user', 'url', 'token' );

# Valid token
$query =~ s/user=/user=rtyler/;
$query =~ s/url=/url=http%3A%2F%2Ftest1.example.com/;

ok(
    $res = $client->_post(
        '/checkuser',
        IO::String->new($query),
        cookie => "lemonldap=$id",
        length => length($query),
        accept => 'text/html',
    ),
    'POST checkuser'
);
count(1);

( $host, $url, $query ) =
  expectForm( $res, undef, '/checkuser', 'user', 'url', 'token' );
ok( $res->[2]->[0] =~ m%<span trspan="checkUserComputedSession">%,
    'Found trspan="checkUserComputeSession"' )
  or explain( $res->[2]->[0], 'trspan="checkUserComputedSession"' );
ok(
    $res->[2]->[0] =~
m%<div class="alert alert-success"><div class="text-center"><b><span trspan="allowed"></span></b></div></div>%,
    'Found trspan="allowed"'
) or explain( $res->[2]->[0], 'trspan="allowed"' );
ok( $res->[2]->[0] =~ m%<span trspan="headers">%, 'Found trspan="headers"' )
  or explain( $res->[2]->[0], 'trspan="headers"' );
ok( $res->[2]->[0] =~ m%<span trspan="groups_sso">%,
    'Found trspan="groups_sso"' )
  or explain( $res->[2]->[0], 'trspan="groups_sso"' );
ok( $res->[2]->[0] =~ m%<span trspan="attributes">%,
    'Found trspan="attributes"' )
  or explain( $res->[2]->[0], 'trspan="attributes"' );
ok( $res->[2]->[0] =~ m%<span trspan="macros">%, 'Found trspan="macros"' )
  or explain( $res->[2]->[0], 'trspan="macros"' );
ok( $res->[2]->[0] =~ m%Auth-User: %, 'Found Auth-User' )
  or explain( $res->[2]->[0], 'Header Key: Auth-User' );
ok( $res->[2]->[0] =~ m%: rtyler<br/>%, 'Found rtyler' )
  or explain( $res->[2]->[0], 'Header Value: rtyler' );
ok( $res->[2]->[0] =~ m%<div class="card-text text-left ml-2">su</div>%,
    'Found su' )
  or explain( $res->[2]->[0], 'SSO Groups: su' );
ok( $res->[2]->[0] =~ m%<td scope="row">uid</td>%, 'Found uid' )
  or explain( $res->[2]->[0], 'Attribute Value uid' );
ok( $res->[2]->[0] =~ m%<td scope="row">RTYLER\@BADWOLF\.ORG</td>%,
    'Found uc mail' )
  or explain( $res->[2]->[0], 'Macro Key uc mail' );
ok( $res->[2]->[0] =~ m%<td scope="row">uid</td>%, 'Found uid' )
  or explain( $res->[2]->[0], 'Attribute Value uid' );
count(12);

$query =~
s/url=http%3A%2F%2Ftest1.example.com/url=http%3A%2F%2Fmanager.example.com%2Fmanager.html/;

ok(
    $res = $client->_post(
        '/checkuser',
        IO::String->new($query),
        cookie => "lemonldap=$id",
        length => length($query),
        accept => 'text/html',
    ),
    'POST checkuser'
);
ok(
    $res->[2]->[0] =~
m%<div class="alert alert-danger"><div class="text-center"><b><span trspan="forbidden"></span></b></div></div>%,
    'Found trspan="forbidden"'
) or explain( $res->[2]->[0], 'trspan="forbidden"' );
count(2);

$client->logout($id);
clean_sessions();

done_testing( count() );
