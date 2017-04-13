# When done, submit this entire file to the autograder.

# Part 1

def sum arr
    sum = 0
  for i in arr do
      sum += i
  end
  return sum
end

# Auxiliar function
def get_max arr
    max = arr[0]
    for i in arr do
        max = i > max ? i : max
    end
    return max
end

def max_2_sum arr
    if arr.length == 0 then
        return 0
    end
    if arr.length == 1 then
        return arr[0]
    end
    arrC = arr.dup    
    m1 = get_max arrC
    arrC.slice!(arrC.index(m1))
    m2 = get_max arrC
    return m1 + m2
end


def sum_to_n? arr, n
    comb = arr.permutation(2)
    for i, j in comb do
        if(i + j == n)
            return true
        end
    end
    return false
end

# Part 2

def hello(name)
    return "Hello, " + name
end

def starts_with_consonant? s
    return !!(s =~ /^[^aeiou0-9\d:\u00C0-\u00FF\u0020-\u0040].*/i)
end

def binary_multiple_of_4? s
  if !!!( s =~ /^[01]+$/) then
      return false;
  end
  return s.to_i(base=2) % 4 == 0
end

# Part 3

class BookInStock
    # constructor
    def initialize(isbn, price)
        if isbn.empty? or price <= 0 then
            raise ArgumentError, "Invalid argument values!"
        end
        @isbn, @price = isbn, price
    end

    def isbn
        @isbn
    end

    def price
        @price
    end

    def isbn=(new_isbn)
        @isbn = new_isbn
    end

    def price=(new_price)
        @price = new_price
    end

    def price_as_string()
        return "$#{format("%.2f", @price)}"
    end

end
