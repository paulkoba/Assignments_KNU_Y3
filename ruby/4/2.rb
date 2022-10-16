require 'matrix'

x = Vector[Array.new(8) { rand(10) }]
y = Vector[Array.new(8) { rand(10) }]

puts("X: " + x.to_s)
puts("Y: " + y.to_s)

result = 0

for i in 0..7
    result += x[0][i] * y[0][i]
end

puts("Result is: " + result.to_s)