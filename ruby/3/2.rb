def f_1(x)
    if -4 < x && x <= 0
        ((x - 2.0).abs / (x * x * Math.cos(x))) ** (1.0 / 7)
    elsif 0 < x && x <= 12
        1.0 / ((Math.tan(x + 1 / Math.exp(x))) / Math.sin(x) ** 2) ** (2.0 / 7)
    else
        1.0 / (1.0 + x / (1.0 + x / (1.0 + x)))
    end
end

def f_2(x)
    case x
    when -4.0..0.0
        ((x - 2.0).abs / (x * x * Math.cos(x))) ** (1.0 / 7)
    when 0.0..12.0
        1.0 / ((Math.tan(x + 1 / Math.exp(x))) / Math.sin(x) ** 2) ** (2.0 / 7)
    else
        1.0 / (1.0 + x / (1.0 + x / (1.0 + x)))
    end
end

print "Please enter the value of x: "
x = gets.chomp.to_f

print "f_1(x) = " + f_1(x).to_s + "\n"
print "f_2(x) = " + f_2(x).to_s + "\n"