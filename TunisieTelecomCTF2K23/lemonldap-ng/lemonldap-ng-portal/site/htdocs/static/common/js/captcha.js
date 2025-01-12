// Generated by CoffeeScript 1.12.8
(function() {
  var renewCaptcha;

  renewCaptcha = function() {
    console.log('Call URL -> ', portal + "renewcaptcha");
    return $.ajax({
      type: "GET",
      url: portal + "renewcaptcha",
      dataType: 'json',
      error: function(j, status, err) {
        var res;
        if (err) {
          console.log('Error', err);
        }
        if (j) {
          res = JSON.parse(j.responseText);
        }
        if (res && res.error) {
          return console.log('Returned error', res);
        }
      },
      success: function(data) {
        var newimage, newtoken;
        newtoken = data.newtoken;
        console.log('GET new token -> ', newtoken);
        newimage = data.newimage;
        console.log('GET new image -> ', newimage);
        $('#token').attr('value', newtoken);
        return $('#captcha').attr('src', newimage);
      }
    });
  };

  $(document).ready(function() {
    $('#logout').attr('href', portal);
    return $('.renewcaptchaclick').on('click', renewCaptcha);
  });

}).call(this);
