input = 161
output = ""

while input != 0 do
    output += input % 2 == 1 ? "1" : "0"
    input /= 2
end

puts "Output: " + output.reverse