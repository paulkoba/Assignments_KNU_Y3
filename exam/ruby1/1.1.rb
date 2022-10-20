def f_internal(a, b, c, x)
    if c < 0 && b != 0
        a * x ** 2 + b ** 2 * x
    elsif c > 0 && b == 0
        (x + a) / (x + c)
    else
        x / c
    end
end

def f(a, b, c, x)
    if (a.to_i & b.to_i) | (a.to_i & c.to_i)
        f_internal(a, b, c, x).to_i
    else
        f_internal(a, b, c, x)
    end
end

puts "Please enter values of a, b, and c:"
a = gets.chomp.to_f
b = gets.chomp.to_f
c = gets.chomp.to_f

puts "Please enter values of x_start, x_end, and dx:"
x_start = gets.chomp.to_f
x_end = gets.chomp.to_f
dx = gets.chomp.to_f

if dx <= 0 || x_start > x_end
    puts "Invalid input provided. Halting..."
    exit
end

(x_start..x_end).step(dx) do |x|
    print("f(", x.round(2), ") = ", f(a, b, c, x), "\n")
end