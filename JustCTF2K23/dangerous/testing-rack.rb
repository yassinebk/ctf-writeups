require "net/http"
require "cgi"
require 'pp'
require 'base64'
# Remote host
 
# URL = "http://dangerous.web.jctf.pro/"
 
# # Create URL object
# url = URI.parse(URL) 
 
# # Proxy configuration
# PROXY = "127.0.0.1"
# PROXY_PORT = "8080"
 
 
# creds = "test"
 
# # Authentication
# resp = Net::HTTP::Proxy(PROXY, PROXY_PORT).start(url.host, url.port) do |http|
#   http.post(url.request_uri, "login=#{creds}&password=#{creds}")
# end
 
# puts get the cookie
c="eZNPDtLwhrnckwc%2BGUemLfkeZiHfKy3bgbt18YacC4ffM4Kv9WQ%2FdslKGdd6nYnTtcO8HGZUg2ny3BHNjuN%2BAYV2g0ddPjKJ31lZuMAe0X1fUe3jTVYRdpcL4jitv55TMckgnC%2F4CH6GPKjZgyaYMycBN4BPSZTwJWK4pZmhZlW%2BsPU79W3pHZRRDUexYgyY1DtEgPFX6DlnWqO39dTwfGufYSOoIJCMIyPRqC%2Fl3G5qJtloQYcX5bil4u5AatCyGNTHREfjJhpsC8bSs2rngxWyzkY9i8DY%2BeN91VEhvMSvEWYWdrnQMO3%2B2e26ZKNcts4YrQewbRmL87jwzoCKJPpCFg5pTRFti72PPw9i62vCKU1xGtr2fa%2FGHpuUOwjbfdNwkVrHKfQJ--hq1OOLKm4Ia6ytuO--4VhNvm72wFLEX79AlMFQkw%3D%3D"
cookie, signature = c.split("--")
# puts cookie
puts CGI.unescape(cookie)
decoded = Base64.decode64(CGI.unescape(CGI.unescape(cookie)))
puts decoded
begin
  # load the object
  object = Marshal.load(decoded)
  pp object
rescue ArgumentError => e
  puts "ERROR: "+e.to_s
end