package main

import (
	"fmt"
	"math"
	"sync"
)

var primes []bool

func sieve(num int64) {

	// do not use
	// prime := [num+1]bool
	// will cause : non-constant array bound num + 1 error

	// an array of boolean - the idiomatic way
	primes := make([]bool, num+1)

	// initialize everything with false first(not crossed)
	for i := int64(0); i < num+1; i++ {
		primes[i] = false
	}

	for i := int64(2); i*i <= num; i++ {
		if primes[i] == false {
			for j := i * 2; j <= num; j += i {
				primes[j] = true // cross
			}
		}

	}
}

func modExp(base, exp, mod int64) int64 {
	result := int64(1)
	base %= mod
	for exp > 0 {
		if exp%2 == 1 {
			result = (result * base) % mod
		}
		exp >>= 1
		base = (base * base) % mod
	}
	return result
}

func isPrime(n int64) bool {
	if n < 2 {
		return false
	}
	return primes[n]
}

func gcd(a, b int64) int64 {
	if a == 0 {
		return b
	}
	if b == 0 {
		return a
	}

	shift := uint(0)
	for (a|b)&1 == 0 {
		a >>= 1
		b >>= 1
		shift++
	}

	for a&1 == 0 {
		a >>= 1
	}

	for b != 0 {
		for b&1 == 0 {
			b >>= 1
		}

		if a > b {
			a, b = b, a
		}

		b -= a
	}

	return a << shift
}

func isCarmichael(n int64) bool {
	if isPrime(n) {
		return false
	}

	for a := int64(2); a < n; a++ {
		if gcd(a, n) == 1 && modExp(a, n-1, n) != 1 {
			return false
		}
	}
	return true
}

func findCarmichaels(start, end int64, wg *sync.WaitGroup, results chan<- string) {

	defer wg.Done()

	count := 0
	n := 0
	ref := 1333337
	var c int64
	result := ""

	for num := start; num <= end; num++ {
		if n == 10 {
			break
		}

		if isCarmichael(num) {
			c = num
			count++
		}

		if count == ref {
			result += fmt.Sprintf("%019d", c)
			ref += 1337
			n++
		}
	}

	results <- result
}

func main() {
	start := int64(2)
	end := int64(math.Pow10(18))

	fmt.Println("Bring your ☕ 🚬 ...")

	divide := int64(1000000)
	endValue := int64((end - start) / divide)

	var wg sync.WaitGroup
	results := make(chan string, endValue)
	sieve(end)

	for i := int64(0); i < endValue; i++ {
		wg.Add(1)
		go findCarmichaels(i*int64(divide), (i+1)*int64(divide), &wg, results)
	}

	go func() {
		wg.Wait()
		close(results)
	}()

	for res := range results {
		fmt.Println(res)
	}
}
