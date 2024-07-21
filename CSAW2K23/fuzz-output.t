        ___
       __H__
 ___ ___[(]_____ ___ ___  {1.7.6#pip}
|_ -| . [(]     | .'| . |
|___|_  [.]_|_|_|__,|  _|
      |_|V...       |_|   https://sqlmap.org

[!] legal disclaimer: Usage of sqlmap for attacking targets without prior mutual consent is illegal. It is the end user's responsibility to obey all applicable local, state and federal laws. Developers assume no liability and are not responsible for any misuse or damage caused by this program

[*] starting @ 17:44:01 /2023-09-16/

[17:44:01] [DEBUG] cleaning up configuration parameters
[17:44:01] [DEBUG] setting the HTTP timeout
[17:44:01] [DEBUG] setting the HTTP User-Agent header
[17:44:01] [DEBUG] creating HTTP requests opener object
[17:44:01] [DEBUG] forcing back-end DBMS to user defined value
[17:44:01] [DEBUG] setting the HTTP Referer header to the target URL
[17:44:01] [DEBUG] setting the HTTP Host header to the target URL
[17:44:01] [DEBUG] resolving hostname 'web.csaw.io'
[17:44:01] [INFO] testing connection to the target URL
[17:44:01] [TRAFFIC OUT] HTTP request [#1]:
POST /submit HTTP/1.1
Cache-Control: no-cache
User-Agent: sqlmap/1.7.6#pip (https://sqlmap.org)
Referer: http://web.csaw.io:5800/submit
Host: web.csaw.io:5800
Accept: */*
Accept-Encoding: gzip,deflate
Content-Type: application/x-www-form-urlencoded; charset=utf-8
Content-length: 33
Connection: close

email=admin@csaw.io&password=meow

[17:44:01] [DEBUG] declared web page charset 'utf-8'
[17:44:01] [TRAFFIC IN] HTTP response [#1] (200 OK):
Server: gunicorn
Date: Sat, 16 Sep 2023 16:44:01 GMT
Connection: close
Content-Type: text/html; charset=utf-8
Content-Length: 1978
URI: http://web.csaw.io:5800/submit


<!DOCTYPE html>
<html lang="en">

  <style>

    .error {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      width: 100%;
      height: 100px;
      background-size: contain;
      background-position: center;
      background-repeat: no-repeat;
    }
  </style>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../../../favicon.ico">

    <title>Login</title>

    <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.0.0-beta/css/bootstrap.min.css" rel="stylesheet">

  </head>

  <body style="padding-top: 5rem">
    <nav class="navbar navbar-expand-md navbar-dark bg-dark fixed-top">
      <a class="navbar-brand" href="#">NYC Subway Tickets</a>
    </nav>

    <div class="container">
        <form style="margin: 0 auto; width: 300px; text-align: center" action = "/submit" method="POST">
            <h2 class="form-signin-heading">Login</h2>
            <label for="email" class="sr-only">Email</label>
            <input type="email" id="email" name="email" class="form-control" placeholder="Email" required>
            <label for="inputPassword" class="sr-only">Password</label>
            <input type="password" id="inputPassword" name="password" class="form-control" placeholder="Password" required>
            <br/>
            <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
        </form>
    </div><!-- /.container -->
    
            <p class="error"><strong>Error:</strong> Sorry, this user is doesn't exist</p>
    

    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.0.0-beta/js/bootstrap.min.js"></script>
  </body>
</html>

[17:44:01] [INFO] testing if the target URL content is stable
