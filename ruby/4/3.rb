require 'matrix'

print("Please enter N: ")
n = gets.chomp.to_i

if n >= 10 || n <= 2
    puts("N out of range")
    exit
end

lhs = Matrix.build(n, n) do |i, j|
    if i == j
        2.0
    else
        8.0
    end
end

rhs = Vector[Array.new(n) { |i| i + 1.0 }]

lhs = lhs.hstack(Matrix.column_vector(rhs[0].to_a))

puts("Lhs: " + lhs.to_s)
puts("Rhs: " + rhs.to_s)
puts

for i in 0..n - 1
    acc = lhs[i, i]
    for j in 0..n
        lhs[i, j] /= acc
    end

    for k in 0..n - 1
        if k == i
            next
        end

        temp = lhs[k, i]

        for g in 0..n
            lhs[k, g] -= lhs[i, g] * temp
        end
    end
end

puts("Lhs: " + lhs.to_s)