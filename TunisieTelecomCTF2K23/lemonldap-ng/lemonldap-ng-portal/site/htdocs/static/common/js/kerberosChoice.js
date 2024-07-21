// Generated by CoffeeScript 1.12.8
(function() {
  $(document).ready(function() {
    return $.ajax(portal + '/authkrb', {
      dataType: 'json',
      statusCode: {
        401: function() {
          return $('#lformKerberos').submit();
        }
      },
      success: function(data) {
        if (data.ajax_auth_token) {
          $('#lformKerberos').find('input[name="ajax_auth_token"]').attr("value", data.ajax_auth_token);
        }
        return $('#lformKerberos').submit();
      },
      error: function() {
        return $('#lformKerberos').submit();
      }
    });
  });

}).call(this);
