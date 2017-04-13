    class HangpersonGame

      # add the necessary class methods, attributes, etc. here
      # to make the tests in spec/hangperson_game_spec.rb pass.

      # Get a word from remote "random word" service

      attr_accessor :guesses
      attr_accessor :wrong_guesses
      attr_accessor :word

      def initialize(word)
        @word = word
        @word_guessed = '-'*word.length
        @guesses = ''
        @wrong_guesses = ''
        @n = 0
      end

      def guess(l)
        unless !!(l =~ /^[a-z]+/i)
            raise ArgumentError.new("You must pass a LETTER!")
        end
        if @word.include? l.downcase
            oldG = @guesses.include? l.downcase
            @guesses += l.downcase unless oldG
            unless oldG
                populate_word(l.downcase)
            end
            return !oldG
        end
        oldWG = @wrong_guesses.include? l.downcase
        @wrong_guesses += l.downcase unless oldWG
        unless oldWG
            @n += 1
        end
        return !oldWG
      end

      def populate_word(l)
        @word.split('').each_with_index {|val, ind|
            if val == l
                @word_guessed[ind] = l
            end
        }
      end

      def word_with_guesses
          @word_guessed
      end

      def check_win_or_lose
          if @n >= 7
            return :lose
          end
          if @word_guessed == @word
              return :win
          end
          return :play
      end

      def self.get_random_word
        require 'uri'
        require 'net/http'
        uri = URI('http://watchout4snakes.com/wo4snakes/Random/RandomWord')
        Net::HTTP.post_form(uri ,{}).body
      end

    end
