def compute_infinite(x, eps)
    sum = 0.0
    prev2 = 0.0
    prev = 0.0
    i = 0.0
    loop do
        prev2 = prev
        prev = sum
        
        sum += Math.cos(i * Math::PI / 4) / (2..i).reduce(1,:*)
        i += 1.0

        break if (sum - prev2).abs < eps
    end

    sum
end

def compute_fixed(x, n)
    sum = 0.0
    i = 0.0
    while i <= n
        sum += Math.cos(i * Math::PI / 4) / (2..i).reduce(1,:*)
        i += 1.0
    end

    sum
end

x = 0.8
n = 50
eps = 0.001
puts("Fixed " + n.to_s + " iterations at " + x.to_s + ": " + compute_fixed(x, n).to_s)
puts("Computation with precision set to eps = " + eps.to_s + ": " + compute_infinite(x, eps).to_s)