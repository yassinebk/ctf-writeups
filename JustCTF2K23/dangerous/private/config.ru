require 'rack/session'
use Rack::Session::Cookie,
  :domain => 'mywebsite.com',
  :path => '/',
  :expire_after => 3600*24,
  :secret => '5df1a4757a2bb716f67a287a082008ed9f3b76ddaab2d101303249f6e254eaff221522e6cbbd96c5d25967b69348fa86e7202b548875322717735b1e76224651'

class MyApp
  def call(env)
    session = env['rack.session']

    # Set some state:
    session[:username] = "admin"

    
  end
end
