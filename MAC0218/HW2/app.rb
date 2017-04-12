require 'sinatra'

class MyApp < Sinatra::Base
    get '/' do
        "<!DOCTYPE html><html><head><body><h1>HELLO WORLD!</h1></body></head></html>"
    end
end
