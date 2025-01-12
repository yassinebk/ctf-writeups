// Generated by CoffeeScript 1.12.8

/*
LemonLDAP::NG U2F registration script
 */

(function() {
  var displayError, register, setMsg, verify;

  setMsg = function(msg, level) {
    $('#msg').attr('trspan', msg);
    $('#msg').html(window.translate(msg));
    $('#color').removeClass('message-positive message-warning message-danger alert-success alert-warning alert-danger');
    $('#color').addClass("message-" + level);
    if (level === 'positive') {
      level = 'success';
    }
    $('#color').addClass("alert-" + level);
    return $('#msg').attr('role', (level === 'danger' ? 'alert' : 'status'));
  };

  displayError = function(j, status, err) {
    var res;
    console.log('Error', err);
    res = JSON.parse(j.responseText);
    if (res && res.error) {
      res = res.error.replace(/.* /, '');
      console.log('Returned error', res);
      return setMsg(res, 'warning');
    }
  };

  register = function() {
    return $.ajax({
      type: "POST",
      url: portal + "2fregisters/u/register",
      data: {},
      dataType: 'json',
      error: displayError,
      success: function(ch) {
        var request;
        request = [
          {
            challenge: ch.challenge,
            version: ch.version
          }
        ];
        setMsg('touchU2fDevice', 'positive');
        $('#u2fPermission').show();
        return u2f.register(ch.appId, request, [], function(data) {
          $('#u2fPermission').hide();
          if (data.errorCode) {
            return setMsg(data.error, 'warning');
          } else {
            return $.ajax({
              type: "POST",
              url: portal + "2fregisters/u/registration",
              data: {
                registration: JSON.stringify(data),
                challenge: JSON.stringify(ch),
                keyName: $('#keyName').val()
              },
              dataType: 'json',
              success: function(resp) {
                if (resp.error) {
                  if (resp.error.match(/badName/)) {
                    return setMsg(resp.error, 'warning');
                  } else {
                    return setMsg('u2fFailed', 'danger');
                  }
                } else if (resp.result) {
                  $(document).trigger("mfaAdded", [
                    {
                      "type": "u"
                    }
                  ]);
                  return setMsg('yourKeyIsRegistered', 'positive');
                }
              },
              error: displayError
            });
          }
        });
      }
    });
  };

  verify = function() {
    return $.ajax({
      type: "POST",
      url: portal + "2fregisters/u/verify",
      data: {},
      dataType: 'json',
      error: displayError,
      success: function(ch) {
        setMsg('touchU2fDevice', 'positive');
        return u2f.sign(ch.appId, ch.challenge, ch.registeredKeys, function(data) {
          if (data.errorCode) {
            return setMsg('unableToGetKey', 'warning');
          } else {
            return $.ajax({
              type: "POST",
              url: portal + "2fregisters/u/signature",
              data: {
                signature: JSON.stringify(data),
                challenge: ch.challenge
              },
              dataType: 'json',
              success: function(resp) {
                if (resp.error) {
                  return setMsg('u2fFailed', 'danger');
                } else if (resp.result) {
                  return setMsg('yourKeyIsVerified', 'positive');
                }
              },
              error: function(j, status, err) {
                return console.log('error', err);
              }
            });
          }
        });
      }
    });
  };

  $(document).ready(function() {
    $('#u2fPermission').hide();
    $('#register').on('click', register);
    $('#verify').on('click', verify);
    return $('#goback').attr('href', portal);
  });

}).call(this);
