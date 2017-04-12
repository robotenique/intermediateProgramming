require 'sinatra'

class MyApp < Sinatra::Base
    get '/' do
        "<!DOCTYPE html><html><head><body><h1>BEM LOCO EMPOLGANTE!</h1></body></head></html>"
    end
end
