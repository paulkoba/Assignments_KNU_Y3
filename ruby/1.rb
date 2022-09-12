print "Please enter the value of t: "
t = gets.chomp.to_f

print "Please enter the value of phi: "
phi = gets.chomp.to_f

print "Please enter the value of x: "
x = gets.chomp.to_f

l = Math.tan((Math::E ** t + 3 ** phi) / Math.sqrt((t ** 2 + 2).abs))
r = (Math.cos(phi) ** 3 + 2.8 * 10 ** -3.4 + x) / ((Math.sin(Math::PI - 4) ** 3 + 1.2) ** 0.2)
z = l - r

print "The value of Z is %f.\n" % [z]