data = Array.new(24) { rand(-100..100) }

puts("Array contents: ")
puts(data)

even_negatives = 0
odd_positives = 0

data.each do |el|
    if el % 2 == 0 && el < 0
        even_negatives += el
    end
    
    if el % 2 == 1 && el > 0
        odd_positives += el
    end
end

puts("Sum of even negatives: " + even_negatives.to_s)
puts("Sum of odd positives: " + odd_positives.to_s)