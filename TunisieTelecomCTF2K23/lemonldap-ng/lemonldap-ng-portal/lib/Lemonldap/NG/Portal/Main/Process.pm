package Lemonldap::NG::Portal::Main::Process;

our $VERSION = '2.0.15';

package Lemonldap::NG::Portal::Main;

use strict;
use MIME::Base64;
use POSIX qw(strftime);
use Lemonldap::NG::Portal::Main::Constants qw(portalConsts);

# Main method
# -----------
# Launch all methods declared in request "steps" array. Methods can be
# declared by their name (in Lemonldap::NG::Portal::Main namespace) or point
# to a subroutine (see Lemonldap::NG::Portal::Main::Run.pm)

sub process {
    my ( $self, $req, %args ) = @_;

    # Store ipAddr in env
    $req->env->{ipAddr} = $req->address;
    my $err = PE_OK;
    while ( my $sub = shift @{ $req->steps } ) {
        if ( ref $sub ) {
            $self->logger->debug("Processing code ref");
            last if ( $err = $sub->( $req, %args ) );
        }
        else {
            $self->logger->debug("Processing $sub");
            if ( my $as = $self->aroundSub->{$sub} ) {
                last if ( $err = $as->( $req, %args ) );
            }
            else {
                last if ( $err = $self->$sub( $req, %args ) );
            }
            if ( $self->afterSub->{$sub} ) {
                unshift @{ $req->steps }, @{ $self->afterSub->{$sub} };
            }
        }
    }
    $self->logger->debug( "Returned " . $self->_formatProcessResult($err) )
      if $err;
    return $err;
}

sub processHook {
    my ( $self, $req, $hookName, @args ) = @_;

    $self->logger->debug("Calling hook $hookName");
    my $err = PE_OK;
    for my $sub ( @{ $self->hook->{$hookName} } ) {
        if ( ref $sub eq 'CODE' ) {

            # If return code is not PE_OK (0), stop processing
            last if ( $err = $sub->( $req, @args ) );
        }
        else {
            $self->logger->debug("Not a code ref: $sub");
        }
    }
    if ( $err != PE_OK ) {
        my $msg = "Hook $hookName returned " . portalConsts->{$err};
        if ( $err > 0 ) {
            $self->logger->warn($msg);
        }
        else {
            # <0 error codes are normal in some use cases
            $self->logger->debug($msg);
        }
    }
    return $err;
}

sub _formatProcessResult {
    my ( $self, $err ) = @_;
    return ( ( $err > 0 ? "error" : "status" )
        . ": $err ("
          . portalConsts->{$err}
          . ")" );
}

# First process block: check args
# -------------------------------

# For post requests, parse data
sub restoreArgs {
    my ( $self, $req ) = @_;
    $req->mustRedirect(1);
    return PE_OK;
}

sub importHandlerData {
    my ( $self, $req ) = @_;
    $req->{sessionInfo} = $req->userData;
    $req->id( $req->sessionInfo->{_session_id} );
    $req->user( $req->sessionInfo->{ $self->conf->{whatToTrace} } );
    return PE_OK;
}

# Verify url and confirm parameter
sub controlUrl {
    my ( $self, $req ) = @_;
    if ( my $c = $req->param('confirm') ) {

        # Replace confirm stamp by 1 or -1
        $c =~ s/^(-?)(.*)$/${1}1/;

        # Decrypt confirm stamp if cipher available
        # and confirm not already decrypted
        if ( $self->conf->{cipher} and $2 ne "1" ) {
            my $time = time() - $self->conf->{cipher}->decrypt($2);
            if ( $time < 600 ) {
                $self->logger->debug("Confirm parameter accepted $c");
                $req->set_param( 'confirm', $c );
            }
            else {
                $self->logger->notice('Confirmation too old, refused');
                $req->set_param( 'confirm', 0 );
            }
        }
    }
    $req->{data}->{_url} ||= '';
    my $url = $req->param('url') || $req->pdata->{_url};
    if ($url) {

        # REJECT NON BASE64 URL
        if ( $req->urlNotBase64 ) {
            $req->{urldc} = $url;
        }
        else {
            if ( $url =~ m#[^A-Za-z0-9\+/=]# ) {
                unless ( $req->maybeNotBase64 ) {
                    $self->userLogger->error(
"Value must be BASE64 encoded (param: url | value: $url)"
                    );
                    return PE_BADURL;
                }
                $req->{urldc} = $url;
            }
            else {
                $req->{urldc} = decode_base64($url);
                $req->{urldc} =~ s/[\r\n]//sg;
            }
        }

        # For logout request, test if Referer comes from an authorized site
        my $tmp = (
              $req->param('logout')
            ? $req->referer
            : $req->{urldc}
        );

        # XSS attack
        if (
            $self->checkXSSAttack(
                $req->param('logout') ? 'HTTP Referer' : 'urldc',
                $req->{urldc}
            )
          )
        {
            delete $req->{urldc};
            return PE_BADURL;
        }

        # Unprotected hosts
        if ( $tmp and ( $tmp !~ URIRE ) ) {
            $self->userLogger->error("Bad URL $tmp");
            delete $req->{urldc};
            return PE_BADURL;
        }
        my ( $proto, $vhost, $appuri ) = ( $2, $3, $5 );

        # Try to resolve alias
        my $originalVhost = $self->HANDLER->resolveAlias($vhost);
        $vhost = "$proto://$originalVhost";
        $self->logger->debug( 'Required URL (param: '
              . ( $req->param('logout') ? 'HTTP Referer'   : 'urldc' )
              . ( $tmp ? " | value: $tmp | alias: $vhost)" : ')' ) );

        # If the target URL has an authLevel set in config, remember it.
        my $level = $self->HANDLER->getLevel( $req, $appuri, $originalVhost );
        $req->pdata->{targetAuthnLevel} = $level if $level;

        if (    $tmp
            and !$self->isTrustedUrl($tmp)
            and !$self->isTrustedUrl($vhost) )
        {
            $self->userLogger->error(
                    "URL contains an unprotected host (param: "
                  . ( $req->param('logout') ? 'HTTP Referer' : 'urldc' )
                  . " | value: $tmp | alias: $vhost)" );
            delete $req->{urldc};
            return PE_UNPROTECTEDURL;
        }

        $req->env->{urldc} = $req->{urldc};
        $req->env->{_url}  = $req->{_url};
        $req->data->{_url} = $req->pdata->{_url} =
          encode_base64( $req->{urldc}, '' );    # Avoid \n or \r
    }
    return PE_OK;
}

sub checkLogout {
    my ( $self, $req ) = @_;
    if ( defined $req->param('logout') ) {
        $req->steps(
            [ @{ $self->beforeLogout }, 'authLogout', 'deleteSession' ] );
    }
    return PE_OK;
}

sub checkUnauthLogout {
    my ( $self, $req ) = @_;
    if ( defined $req->param('logout') ) {
        $self->userLogger->info('Unauthenticated logout request');
        $self->logger->debug('Cleaning pdata');
        $self->logger->debug("Removing $self->{conf}->{cookieName} cookie");
        $req->pdata( {} );
        $req->addCookie(
            $self->cookie(
                name    => $self->conf->{cookieName},
                domain  => $self->conf->{domain},
                secure  => $self->conf->{securedCookie},
                expires => 'Wed, 21 Oct 2015 00:00:00 GMT',
                value   => 0
            )
        );
        $req->steps( [ sub { PE_LOGOUT_OK } ] );
    }
    return PE_OK;
}

sub authLogout {
    my ( $self, $req ) = @_;
    my $res = $self->_authentication->authLogout($req);
    $self->logger->debug('Cleaning pdata');
    my $tmp = $req->pdata->{keepPdata} //= [];
    foreach my $k ( keys %{ $req->pdata } ) {
        delete $req->pdata->{$k} unless ( grep { $_ eq $k } @$tmp );
    }
    $req->pdata->{keepPdata} = $tmp if @$tmp;
    return $res;
}

sub deleteSession {
    my ( $self, $req ) = @_;
    if ( my $id = $req->id || $req->userData->{_session_id} ) {
        my $apacheSession = $self->getApacheSession($id);
        unless ($apacheSession) {
            $self->logger->debug("Session $id already deleted");
            return PE_OK;
        }
        unless ( $self->_deleteSession( $req, $apacheSession ) ) {
            $self->logger->error("Unable to delete session $id");
            $self->logger->error( $apacheSession->error );
            return PE_ERROR;
        }
        else {
            $self->logger->debug("Session $id deleted from global storage");
        }
    }

    # Merge logoutServices from user context (for example CAS logoutServices
    # url) and from global configuration
    if ( $self->conf->{logoutServices}
        and %{ $self->conf->{logoutServices} } )
    {

        # Initialize logoutServices (if not already done)
        $req->data->{logoutServices} ||= {};
        $req->data->{logoutServices} = {
            %{ $req->data->{logoutServices} },
            %{ $self->conf->{logoutServices} }
        };
    }

    # Collect logout services and build hidden iFrames
    if ( $req->data->{logoutServices} and %{ $req->data->{logoutServices} } ) {

        $self->logger->debug("Create iFrames to forward logout to services");

        $req->info(
            $self->loadTemplate(
                $req, 'simpleInfo',
                params => { trspan => 'logoutFromOtherApp' }
            )
        );

        foreach ( keys %{ $req->data->{logoutServices} } ) {
            my $logoutServiceUrl = $req->data->{logoutServices}->{$_};

            $self->logger->debug("Find logout service $_ ($logoutServiceUrl)");

            my $iframe =
                qq'<iframe src="$logoutServiceUrl" alt="$_"'
              . ' marginwidth="0" marginheight="0" scrolling="no"'
              . ' class="hiddenFrame" width="0" height="0"'
              . ' frameborder="0"></iframe>';

            $req->info($iframe);
        }

        # Redirect on logout page if no other target defined
        if ( !$req->urldc and !$req->postUrl ) {
            $self->logger->debug('No other target defined, redirect on logout');
            $req->urldc( $self->buildUrl( { logout => 1 } ) );
        }
    }
    $req->userData( {} );

    # Redirect or Post if asked by authLogout
    if ( $req->postUrl ) {
        $req->steps( ['autoPost'] );
        return PE_OK;
    }

    if ( $req->urldc and $req->urldc ne $self->conf->{portal} ) {
        $req->steps( [] );
        return PE_REDIRECT;
    }

    # If logout redirects to another URL, just remove next steps for the
    # request so autoRedirect will be called
    if ( $req->{urldc} and $req->{urldc} ne $self->conf->{portal} ) {
        $req->steps( [] );
        return PE_OK;
    }

    # Else display "error"
    return PE_LOGOUT_OK;
}

# Check value to detect XSS attack
# @param name Parameter name
# @param value Parameter value
# @return 1 if attack detected, 0 else
sub checkXSSAttack {
    my ( $self, $name, $value ) = @_;

    # Empty values are not bad
    return 0 unless $value;

    # Test value
    $value =~ s/\%25/\%/g;
    if ( $value =~ m/(?:\0|<|'|"|`|\%(?:00|3C|22|27))/ ) {
        $self->userLogger->error(
            "XSS attack detected (param: $name | value: $value)");
        return $self->conf->{checkXSS};
    }
    return 0;
}

# Second block: auth process (call auth or userDB object)
# -------------------------------------------------------

sub extractFormInfo {
    my ( $self, $req ) = @_;
    return PE_ERROR unless ( $self->_authentication );
    my $ret = $self->_authentication->extractFormInfo($req);
    if ( $ret == PE_OK and not( $req->user or $req->continue ) ) {
        $self->logger->error(
            'Authentication module succeed but has not set $req->user');
        return PE_ERROR;
    }
    elsif ( $ret == PE_FIRSTACCESS
        and $req->cookies->{ $self->conf->{cookieName} } )
    {
        $req->addCookie(
            $self->cookie(
                name    => $self->conf->{cookieName},
                value   => 0,
                domain  => $self->conf->{domain},
                secure  => $self->conf->{securedCookie},
                expires => 'Wed, 21 Oct 2015 00:00:00 GMT'
            )
        );
        if ( $self->conf->{portalErrorOnExpiredSession} ) {
            $ret = PE_SESSIONEXPIRED;
        }
    }
    return $ret;
}

sub getUser {
    my ( $self, $req, %args ) = @_;
    return PE_ERROR unless ( $self->_userDB );
    return $self->_userDB->getUser( $req, %args );
}

sub findUser {
    my ( $self, $req, %args ) = @_;
    return PE_ERROR unless ( $self->_userDB );
    return $self->_userDB->findUser( $req, %args );
}

sub authenticate {
    my ( $self, $req ) = @_;
    my $ret = $req->authResult( $self->_authentication->authenticate($req) );
    $self->logger->debug( " -> authResult = " . $req->authResult );
    if ( $ret == PE_OK ) {
        $req->{sessionInfo}->{_lastAuthnUTime} = time();
        return $ret;
    }

    # Store failed login into history
    $req->steps( [
            'setSessionInfo',           'setMacros',
            'setPersistentSessionInfo', 'storeHistory',
            @{ $self->afterData },      sub { PE_BADCREDENTIALS }
        ]
    );

    # Ignore result, process will end at least with PE_BADCREDENTIALS
    my $tmp = $self->process($req);
    $ret = $tmp if ( $tmp == PE_WAIT );
    return $ret;
}

# Third block: Session data providing
# -----------------------------------

sub setAuthSessionInfo {
    my ( $self, $req ) = @_;
    my $ret = $self->_authentication->setAuthSessionInfo($req);
    if ( $ret == PE_OK
        and not( defined $req->sessionInfo->{authenticationLevel} ) )
    {
        $self->logger->error('Authentication level is not set by auth module');
    }
    return $ret;
}

sub setSessionInfo {
    my ( $self, $req ) = @_;

    # Set _user
    $req->{sessionInfo}->{_user} //= $req->user;

    # Get the current user module
    $req->{sessionInfo}->{_auth}   = $self->getModule( $req, "auth" );
    $req->{sessionInfo}->{_userDB} = $self->getModule( $req, "user" );

    # Store IP address from remote address or X-FORWARDED-FOR header
    $req->{sessionInfo}->{ipAddr} = $req->address;

    # Date and time
    if ( $self->conf->{updateSession} ) {
        $req->{sessionInfo}->{_updateTime} =
          strftime( "%Y%m%d%H%M%S", localtime() );
    }
    else {
        $req->{sessionInfo}->{_utime} ||= time();
        $req->{sessionInfo}->{_startTime} =
          strftime( "%Y%m%d%H%M%S", localtime() );
        $req->{sessionInfo}->{_lastSeen} = time()
          if $self->conf->{timeoutActivity};
    }

    # Currently selected language
    $req->{sessionInfo}->{_language} = $req->cookies->{llnglanguage} || 'en';

    # Store URL origin in session
    $req->{sessionInfo}->{_url} = $req->{urldc};

    # Share sessionInfo with underlying handler (needed for safe jail)
    $req->userData( $req->sessionInfo );

    # Call UserDB setSessionInfo
    return $self->_userDB->setSessionInfo($req);
}

sub setMacros {
    my ( $self, $req ) = @_;
    foreach ( sort keys %{ $self->_macros } ) {
        $req->{sessionInfo}->{$_} =
          $self->_macros->{$_}->( $req, $req->sessionInfo );
    }
    return PE_OK;
}

sub setGroups {
    my ( $self, $req ) = @_;
    return $self->_userDB->setGroups($req);
}

sub setPersistentSessionInfo {
    my ( $self, $req ) = @_;

    # Do not restore infos if session already opened
    unless ( $req->id ) {
        my $key = $req->{sessionInfo}->{ $self->conf->{whatToTrace} };

        return PE_OK unless ( $key and length($key) );

        my $persistentSession = $self->getPersistentSession($key);
        if ($persistentSession) {
            $self->logger->debug("Persistent session found for $key");
            foreach my $k ( keys %{ $persistentSession->data } ) {

                # Do not restore some parameters
                next if $k =~ /^_(?:utime|session_(?:u?id|kind))$/;
                $self->logger->debug("Restore persistent parameter $k");
                $req->{sessionInfo}->{$k} = $persistentSession->data->{$k};
            }
        }
    }
    return PE_OK;
}

sub setLocalGroups {
    my ( $self, $req ) = @_;
    $req->{sessionInfo}->{groups}  //= '';
    $req->{sessionInfo}->{hGroups} //= {};
    foreach ( sort keys %{ $self->_groups } ) {
        if ( $self->_groups->{$_}->( $req, $req->sessionInfo ) ) {
            $req->{sessionInfo}->{groups} .=
              $self->conf->{multiValuesSeparator} . $_;
            $req->{sessionInfo}->{hGroups}->{$_}->{name} = $_;
        }
    }

    # Clear values separator at the beginning
    if ( $req->{sessionInfo}->{groups} ) {
        $req->{sessionInfo}->{groups} =~
          s/^$self->{conf}->{multiValuesSeparator}//o;
    }
    return PE_OK;
}

sub store {
    my ( $self, $req ) = @_;

    # Now, user is authenticated => inform handler
    $req->userData( $req->sessionInfo );

    # Create second session for unsecure cookie
    if ( $self->conf->{securedCookie} == 2 and !$req->refresh ) {
        my %infos = %{ $req->{sessionInfo} };
        $infos{_updateTime} = strftime( "%Y%m%d%H%M%S", localtime() );
        $self->logger->debug("Set _updateTime with $infos{_updateTime}");
        $infos{_httpSessionType} = 1;

        my $session2 = $self->getApacheSession( undef, info => \%infos );
        $self->logger->debug("Create second session for unsecured cookie...");
        $req->{sessionInfo}->{_httpSession} = $session2->id;
        $self->logger->debug( " -> Cookie value : " . $session2->id );
    }

    # Fill session
    my $infos = {};
    foreach my $k ( keys %{ $req->{sessionInfo} } ) {
        next unless defined $req->{sessionInfo}->{$k};
        my $displayValue = $req->{sessionInfo}->{$k};
        $displayValue = '****'
          if (  $self->conf->{hiddenAttributes}
            and $self->conf->{hiddenAttributes} =~ /\b$k\b/ );

        $self->logger->debug("Store $displayValue in session key $k");
        $self->_dump($displayValue) if ref($displayValue);
        $infos->{$k} = $req->{sessionInfo}->{$k};
    }

    # Main session
    my $session = $self->getApacheSession(
        $req->id,
        force => $req->{force},
        info  => $infos
    );
    return PE_APACHESESSIONERROR unless $session;

    # Update current request
    $req->id( $session->id );
    unless ( $self->_sfEngine->searchForAuthorized2Fmodules($req) ) {
        $self->logger->debug(
            "No 2F module authorized -> Update current request");
        $req->{sessionInfo}->{_session_id}   = $session->{id};
        $req->{sessionInfo}->{_session_kind} = $session->{kind};
    }

    # Compute unsecured cookie value if needed
    if ( $self->conf->{securedCookie} == 3 and !$req->refresh ) {
        $req->{sessionInfo}->{_httpSession} =
          $self->conf->{cipher}->encryptHex( $req->id, "http" );
        $self->logger->debug( " -> Compute unsecured cookie value : "
              . $req->{sessionInfo}->{_httpSession} );
    }
    $req->refresh(0);
    return PE_OK;
}

sub buildCookie {
    my ( $self, $req ) = @_;
    if ( $req->id ) {
        $req->addCookie(
            $self->cookie(
                name   => $self->conf->{cookieName},
                value  => $req->id,
                domain => $self->conf->{domain},
                secure => $self->conf->{securedCookie},
            )
        );
        if ( $self->conf->{securedCookie} >= 2 ) {
            $req->addCookie(
                $self->cookie(
                    name   => $self->conf->{cookieName} . "http",
                    value  => $req->{sessionInfo}->{_httpSession},
                    domain => $self->conf->{domain},
                    secure => 0,
                )
            );
        }
    }
    my $ref = (
        %{ $req->{sessionInfo} }
        ? $req->{sessionInfo}
        : $req->{userData}
    );
    $self->userLogger->notice( 'User '
          . $ref->{ $self->conf->{whatToTrace} }
          . " successfully authenticated at level $ref->{authenticationLevel}"
    );
    return PE_OK;
}

sub secondFactor {
    my ( $self, $req ) = @_;
    return $self->_sfEngine->run($req);
}

sub storeHistory {
    my ( $self, $req ) = @_;
    if ( $self->conf->{loginHistoryEnabled} ) {
        $self->registerLogin($req);
    }
    return PE_OK;
}

1;
