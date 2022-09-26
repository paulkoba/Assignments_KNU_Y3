A = false
B = false
C = false
X = -8.0
Y = -10.0
Z = -12.0
print "1)\n"
print "a) " + (!(A || B) && (A && !B)).to_s + "\n"
print "b) " + ((Z != Y).object_id <= (6 >= Y).object_id && A || B && C && X >= 1.5).to_s + "\n"
print "c) " + ((8 - X * 2 <= Z) && (X * X <= Y * Y) || (Z >= 15)).to_s + "\n"
print "d) " + (X > 0 && Y < 0 || Z >= (X * Y + (-Y / X)) + (-Z)).to_s + "\n"
print "e) " + (!(A || B && !(C || (!A || B)))).to_s + "\n"
print "f) " + (X * X + Y * Y >= 1 && X >= 0 && Y >= 0).to_s + "\n"
print "g) " + ((A && (C && B != B || A) || C) && B).to_s + "\n"

x = -0.5
P = true
print "2) " + (!(Math.exp(2 * x) > 3.1415 / 3) && !P).to_s + "\n"