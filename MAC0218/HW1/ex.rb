w1 = words.select{ |item| item != nil }
w2 = words.select{ |item| item != nil and item != ""}
w3 = words.select{ |item|  item != nil and item.length == 3}
w4 = words.select{ |item|  item =~ /[aeiou]/i}
w5 = words.select{ |item| item != nil and item != ""}.inject("") {|a, b| a + b}
w6 = words.map {|w| w.chars.uniq if w != nil}.select{ |item| item != nil and item != ""}.inject(){|a, b| a + b}.uniq.sort.inject(){|a,b| a + b}


def fib n
    top = [0,1]
    count = 0
    while count < n
        yield k = top.reduce(:+)
        top = [top[1], k]
        count += 1
    end
end

class Numeric
    def perfect_square?
        f = Math.sqrt(self).floor
        f * f == self
    end
end
