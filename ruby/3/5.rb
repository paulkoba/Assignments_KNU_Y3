def y(n, x, c)
    ((n ** 0.25 + x ** 0.25) ** 2 + (x ** (2.0 / n) + n ** (2.0 / n)) + 1) / (2.0 * (x - n)) + (n * x - x ** c) / (x ** 2 + x + 1)
end

def z(n, x, c)
    Math.tan(3.0 * x) / (Math.tan(3.0 * x) ** 2 - 1) * (1 - 1.0 / Math.tan(3.0 * x) ** 2) / (1.0 / Math.tan(2.0 * x) - Math.tan(2.0 * Math::PI / 5.0 * x) ** (1.0 / n)) + Math.acos(2.0 * x)
end

n = 42
x = 1
c = 10

for i in 0..(n + c)
    step = (n - 1.0) / (n + c)
    puts "Value of y(" + x.to_s + ") = " + y(n, x, c).to_s
    x += step
end

n = 42
x = Math::PI / n
c = 10

for i in 0..(n + c)
    step = (Math::PI - x) / ((3.0 / 2) * n + c)
    puts "Value of z(" + x.to_s + ") = " + z(n, x, c).to_s
    x += step
end
