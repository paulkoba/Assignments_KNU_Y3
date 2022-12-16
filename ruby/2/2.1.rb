Point = Struct.new(:x, :y)

data = [
    (Point.new 1, 0),
    (Point.new 1, 1),
    (Point.new 0, 1),
    (Point.new 0, 0),
]

result = 0

for i in 0...data.length - 1 do
    result += (data[i].x + data[i + 1].x) * (data[i].y - data[i + 1].y)
end

result += (data[data.length - 1].x + data[0].x) * (data[data.length - 1].y - data[0].y)

result = 0.5 * result.abs

puts "The result is " + result.to_s + ".\n"