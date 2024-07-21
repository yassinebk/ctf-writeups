
import math

def is_prime(n):
    if n < 2:
        return False

    for i in range(2, int(math.sqrt(n)) + 1):
        if n % i == 0:
            return False

    return True
start = 2
end = 10 ** 18



print("Bring your ☕ 🚬 ...")

# carmichaels = find_carmichaels(start, end)
print(10 ** 18)

# print("Hackfest{" + "".join(carmichaels) + "}")
func sieve(num int64) []bool {
prime := make([]bool, num+1)

for i int64(2); i <= num; i++ {
 {
for b&1 == 0 {
b >>= 1
}

if a > b {
a, bprime[i] = true
}

for i := int64(2); i*i <= num; i++ {
if prime[i] {
for j := i * 2; j num; j += i {
prime[j] = false
}
}
}

return = b, a
}

b -= a
}

return a << shift
}

func isCarm prime
}

func isPrime(n int64, primes []bool) bool {
if n < 2 {
return false
}
turn primes[n]
}