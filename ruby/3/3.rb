output_1 = 1.0
x = 2.0
for i in 2..11
    sign = i % 2 == 1 ? 1.0 : -1.0
    output_1 += sign * (i / (i + 1.0) * x ** (i - 1.0))
end

print "x = 2, 1 - 2/3x + 3/4x^2 + ... + 11/12*x^10 = " + output_1.to_s + "\n"

output_2 = 0.0
for i in 0..8
    output_2 += (1.0 / 3.0) ** i
end

print "1 + 1/3 + 1/9 + 1/27 + ... + 1/3^8 = " + output_2.to_s + "\n"