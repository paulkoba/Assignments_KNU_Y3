def prm(a, b, n, func)
    h = (b - a) / n
    x = a + h / 2
    integral = 0.0
    while x <= b
        integral += method(func).call(x)
        x += h
    end

    integral * h
end

def trp(a, b, n, func)
    h = (b - a) / n
    x = a + h
    integral = 0.0

    while x <= b  - h/2
        integral += method(func).call(x)
        x += h
    end
    
    h * (integral + (method(func).call(a) + method(func).call(b)) / 2)
end

def f1(x)
    Math.asin(Math.sqrt(x)) / Math.sqrt(x * (1 - x))
end

def f2(x)
    Math.tan(x / 2 + Math::PI / 4) ** 3
end

a1 = 0.2
b1 = 0.3
n = 1000

puts("prm(" + a1.to_s + ", " + b1.to_s + ", " + n.to_s + ", f1) = " + prm(a1, b1, n, :f1).to_s)
puts("trp(" + a1.to_s + ", " + b1.to_s + ", " + n.to_s + ", f1) = " + trp(a1, b1, n, :f1).to_s)

a2 = 0.0
b2 = Math::PI / 8

puts("prm(" + a2.to_s + ", " + b2.to_s + ", " + n.to_s + ", f2) = " + prm(a2, b2, n, :f2).to_s)
puts("trp(" + a2.to_s + ", " + b2.to_s + ", " + n.to_s + ", f2) = " + trp(a2, b2, n, :f2).to_s)