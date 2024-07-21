#!/usr/bin/perl

# CAS sample client
use strict;
use CGI;
use AuthCAS;

# Configuration
my $host = $ENV{HTTP_HOST};
my ( $domain, $port ) = ( $host =~ /\w+\.([^:]+)(:\d+)?/ );
my $protocol       = ( $ENV{HTTPS} =~ /^on$/i ) ? "https" : "http";
my $portal_url     = "$protocol://auth.$domain$port";
my $cas_url        = "$portal_url/cas";
my $cas            = new AuthCAS( casUrl => $cas_url );
my $cgi            = new CGI;
my $pgtUrl         = $cgi->url() . "%3Fproxy%3D1";
my $pgtFile        = '/tmp/pgt.txt';
my $proxiedService = 'http://webmail';

`touch  $pgtFile`;

# CSS
my $css = <<EOT;
html,body{
  height:100%;
  background:#ddd;
}
#content{
  padding:20px;
}
EOT

# Act as a CAS proxy
$cas->proxyMode( pgtFile => '/tmp/pgt.txt', pgtCallbackUrl => $pgtUrl );

# CAS login URL
my $login_url = $cas->getServerLoginURL( $cgi->url() );

# Start HTTP response
print $cgi->header();

# Proxy URL for TGT validation
if ( $cgi->param('proxy') ) {

    # Store pgtId and pgtIou
    $cas->storePGT( $cgi->param('pgtIou'), $cgi->param('pgtId') );
}

else {

    print "<!DOCTYPE html>\n";
    print
"<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n";
    print "<head>\n";
    print "<title>CAS sample client</title>\n";
    print
"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n";
    print
"<meta http-equiv=\"Content-Script-Type\" content=\"text/javascript\" />\n";
    print "<meta http-equiv=\"cache-control\" content=\"no-cache\" />\n";
    print
"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n";
    print
"<link href=\"$portal_url/static/bwr/bootstrap/dist/css/bootstrap.css\" rel=\"stylesheet\">\n";
    print
"<link href=\"$portal_url/static/bwr/bootstrap/dist/css/bootstrap-theme.css\" rel=\"stylesheet\">\n";
    print "<style>\n";
    print "$css\n";
    print "</style>\n";
    print
"<script type=\"text/javascript\" src=\"$portal_url/static/bwr/jquery/dist/jquery.js\"></script>\n";
    print
"<script type=\"text/javascript\" src=\"$portal_url/static/bwr/jquery-ui/jquery-ui.js\"></script>\n";
    print
"<script src=\"$portal_url/static/bwr/bootstrap/dist/js/bootstrap.js\"></script>\n";
    print "</head>\n";
    print "<body>\n";

    print "<div id=\"content\" class=\"container\">\n";
    print "<div class=\"card border-info\">\n";

    print "<div class=\"card-header text-white bg-info mb-3\">\n";
    print "<h1 class=\"text-center\">CAS sample client</h1>\n";
    print "</div>\n";

    print "<div class=\"card-body\">\n";
    my $ticket = $cgi->param('ticket');

    # First time access
    unless ($ticket) {
        print
"<h3 class=\"text-center\">Login on <a href=\"$cas_url\">$cas_url</a></h3>\n";
        print "<div class=\"text-center\">\n";
        print
"<a href=\"$login_url\" class=\"btn btn-info\" role=\"button\">Simple login</a>\n";
        print
"<a href=\"$login_url&renew=true\" class=\"btn btn-warning\" role=\"button\">Renew login</a>\n";
        print
"<a href=\"$login_url&gateway=true\" class=\"btn btn-success\" role=\"button\">Gateway login</a>\n";
        print "</div>\n";
    }

    # Ticket receveived
    else {

        print "<div class=\"card border-info mb-3\">\n";
        print "<div class=\"card-header text-white bg-info\">\n";
        print "<h2 class=\"card-title text-center\">CAS login result</h2>\n";
        print "</div>\n";
        print "<div class=\"card-body\">\n";
        print $cgi->h4("Service ticket: $ticket");

        # Get user
        my $user = $cas->validateST( $cgi->url(), $ticket );
        if ($user) {
            print $cgi->h4("Authenticated user: $user");
        }
        else {
            print "<div class=\"alert alert-danger\"><strong>Error:</strong> "
              . &AuthCAS::get_errors()
              . "</div>\n";
        }

        # Get proxy granting ticket
        my $pgtId = $cas->{pgtId};
        if ($pgtId) {
            print $cgi->h4("Proxy granting ticket: $pgtId");

            # Try to request proxy ticket
            my $pt = $cas->retrievePT($proxiedService);

            if ($pt) {

                print $cgi->h4("Proxy ticket: $pt");

                # Use proxy ticket
                my ( $puser, @proxies ) =
                  $cas->validatePT( $proxiedService, $pt );

                print $cgi->h4("Proxied user: $puser");
                print $cgi->h4("Proxies used: @proxies");

            }
            else {
                print
                  "<div class=\"alert alert-danger\"><strong>Error:</strong> "
                  . &AuthCAS::get_errors()
                  . "</div>\n";
            }
        }
        else {
            print
"<div class=\"alert alert-danger\"><strong>Error:</strong> Unable to get proxy granting ticket</div>\n";
        }

        print "</div>\n";
        print "</div>\n";

        print "<div class=\"text-center\">\n";
        print "<a class=\"btn btn-info\" role=\"button\" href=\""
          . $cgi->url
          . "\">Home</a>\n";
        print "<a class=\"btn btn-danger\" role=\"button\" href=\""
          . $cas_url
          . "/logout"
          . "\">Logout</a>\n";
        print "<a class=\"btn btn-danger\" role=\"button\" href=\""
          . $cas_url
          . "/logout?service="
          . $cgi->url
          . "\">Logout with redirect</a>\n";
        print "</div>\n";

    }

    print "</div>\n";
    print "</div>\n";
    print "</div>\n";

    print $cgi->end_html();

    # Remove PGT file
    unlink $pgtFile;

}

exit;
