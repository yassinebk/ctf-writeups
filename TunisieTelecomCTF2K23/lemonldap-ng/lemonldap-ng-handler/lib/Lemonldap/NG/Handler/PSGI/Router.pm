package Lemonldap::NG::Handler::PSGI::Router;

use strict;
use Mouse;
require Lemonldap::NG::Handler::PSGI::Main;

extends 'Lemonldap::NG::Handler::Lib::PSGI',
  'Lemonldap::NG::Common::PSGI::Router';

our $VERSION = '2.0.11';

sub init {
    $_[0]->api('Lemonldap::NG::Handler::PSGI::Main');
    my $tmp = ( $_[0]->Lemonldap::NG::Common::PSGI::Router::init( $_[1] )
          and $_[0]->Lemonldap::NG::Handler::Lib::PSGI::init( $_[1] ) );
    return $tmp;
}

1;
__END__

=head1 NAME

=encoding utf8

Lemonldap::NG::Handler::PSGI::Router - Base library for protected REST APIs of
Lemonldap::NG.

=head1 SYNOPSIS

  package My::PSGI;
  
  use base Lemonldap::NG::Handler::PSGI::Router;
  
  sub init {
    my ($self,$args) = @_;
    $self->protection('manager');
    # See Lemonldap::NG::Common::PSGI::Router for more
    
    # Declare REST routes (could be HTML templates or methods)
    $self->addRoute ( 'index.html', undef, ['GET'] )
         ->addRoute ( books => { ':book' => 'booksMethod' }, ['GET', 'POST'] );
  
    # Default route (ie: PATH_INFO == '/')
    $self->defaultRoute('index.html');
  
    # Return a boolean. If false, then error message has to be stored in
    # $self->error
    return 1;
  }
  
  sub booksMethod {
    my ( $self, $req, @otherPathInfo ) = @_;

    # Will be called only if authorized
    my $userId = $self->userId;
    my $book = $req->params('book');
    my $method = $req->method;
    ...
    $self->sendJSONresponse(...);
  }

This package could then be called as a CGI, using FastCGI,...

  #!/usr/bin/env perl
  
  use My::PSGI;
  use Plack::Handler::FCGI; # or Plack::Handler::CGI

  Plack::Handler::FCGI->new->run( My::PSGI->run() );

=head1 DESCRIPTION

This package provides base class for Lemonldap::NG protected REST API.

=head1 METHODS

See L<Lemonldap::NG::Common::PSGI> for logging methods, content sending,...

=head2 Accessors

See L<Lemonldap::NG::Common::PSGI::Router> for inherited accessors.

=head3 protection

Level of protection. It can be one of:

=over

=item 'none': no protection

=item 'authenticate': all authenticated users are granted

=item 'manager': access is granted following Lemonldap::NG rules

=back

=head2 Running methods

=head3 user

Returns user session data. If empty (no protection), returns:

  { _whatToTrace => 'anonymous' }

But if page is protected by server (Auth-Basic,...), it will return:

  { _whatToTrace => $REMOTE_USER }

=head3 UserId

Returns user()->{'_whatToTrace'}.

=head3 group

Returns a list of groups to which user belongs.

=head1 SEE ALSO

L<http://lemonldap-ng.org/>, L<Lemonldap::NG::Portal>, L<Lemonldap::NG::Handler>,
L<Plack>, L<PSGI>, L<Lemonldap::NG::Common::PSGI::Router>,
L<Lemonldap::NG::Common::PSGI::Request>, L<HTML::Template>,

=head1 AUTHORS

=over

=item LemonLDAP::NG team L<http://lemonldap-ng.org/team>

=back

=head1 BUG REPORT

Use OW2 system to report bug or ask for features:
L<https://gitlab.ow2.org/lemonldap-ng/lemonldap-ng/issues>

=head1 DOWNLOAD

Lemonldap::NG is available at
L<https://lemonldap-ng.org/download>

=head1 COPYRIGHT AND LICENSE

See COPYING file for details.

This library is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see L<http://www.gnu.org/licenses/>.

=cut
