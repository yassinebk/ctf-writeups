  require 'base64'
  require 'openssl'
  
  key = 'super secret'
  
  cookie_data = {
    :username => "janitor"
  }
  
  cookie = Base64.strict_encode64(Marshal.dump(cookie_data)).chomp
  digest = OpenSSL::HMAC.hexdigest(OpenSSL::Digest.new('SHA1'), "5df1a4757a2bb716f67a287a082008ed9f3b76ddaab2d101303249f6e254eaff221522e6cbbd96c5d25967b69348fa86e7202b548875322717735b1e76224651", cookie)
  puts("#{cookie}--#{digest}")