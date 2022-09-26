def factorial n
    n > 1 ? n * factorial(n - 1) : 1.0
end

eps = 0.001

# First series

result_1 = 0.0
n = 2

begin
    diff = (factorial(n - 1) / factorial(n + 1)) ** (n * (n + 1.0))
    result_1 += diff
    ++n
end while diff > eps

puts result_1.to_s

result_2 = 0.0
n = 1.0
x = 1.0
sign = 1.0
begin
    diff = x ** n / factorial(n)
    result_2 += diff * sign
    n += 2
    sign *= -1
end while diff.abs > eps

puts result_2.to_s

result_3 = 0.0
n = 1.0

begin
    diff = n * factorial(2.0 * n - 1.0) / ((factorial(3 * n) * 4 ** (2.0 * n) * factorial(2 * n + 1)))
    result_3 += diff * sign
    n += 1
end while diff.abs > eps

puts result_3.to_s
