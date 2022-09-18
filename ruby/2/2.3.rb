input = "111000100111"
output = 0

for ch in input.chars do
    output *= 2
    output += ch.to_i
end 

puts "Output: " + output.to_s