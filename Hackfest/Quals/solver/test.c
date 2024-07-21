#include <stdio.h>
#include <stdbool.h>
#include <math.h>
#include <stdlib.h>
#include <time.h>

long long mod_exp(long long base, long long exp, long long mod) {
    long long result = 1;
    base %= mod;
    while (exp > 0) {
        if (exp % 2 == 1) {
            result = (result * base) % mod;
        }
        exp >>= 1;
        base = (base * base) % mod;
    }
    return result;
}

bool is_prime(long long n, bool prev_prime) {
    if (n < 2) return false;
    if (n == 2 || n == 3) return true;
    if (n % 2 == 0) return false;
    if (prev_prime && n == 3) return true;

    long long r = 0, s = n - 1;
    while (s % 2 == 0) {
        r++;
        s /= 2;
    }

    srand(time(NULL));
    for (int i = 0; i < 5; i++) {
        long long a = 2 + rand() % (n - 3);
        long long x = mod_exp(a, s, n);
        if (x == 1 || x == n - 1) continue;
        bool found = false;
        for (int j = 0; j < r - 1; j++) {
            x = mod_exp(x, 2, n);
            if (x == n - 1) {
                found = true;
                break;
            }
        }
        if (!found) return false;
    }
    return true;
}

long long gcd(long long a, long long b) {
    if (a == 0) return b;
    if (b == 0) return a;

    unsigned shift;
    for (shift = 0; ((a | b) & 1) == 0; ++shift) {
        a >>= 1;
        b >>= 1;
    }

    while ((a & 1) == 0) {
        a >>= 1;
    }

    do {
        while ((b & 1) == 0) {
            b >>= 1;
        }

        if (a > b) {
            long long temp = a;
            a = b;
            b = temp;
        }

        b -= a;
    } while (b != 0);

    return a << shift;
}



long long is_carmichael(unsigned long long  n){
    int a,s;
    int factor_found = 0;
    if (n%2 == 0) return 0;
    //else:
    s = sqrt(n);
    a = 2;
    while(a < n){
        if(a > s && !factor_found){
            return 0;
        }
        if(gcd(a,n) > 1){
            factor_found = 1;
        }
        else{
            if(mod_exp(a,n-1,n) != 1){
                return 0;
            }
        }
        a++;
    }
    return 1; //anything that survives to here is a carmichael
}

void find_carmichaels(long long start, long long end, char *result) {
    int count = 0, n = 0, ref = 1333337;
    long long c = 0;


    // if num is charamical then count ++ < - looking for count 1337*charamicals, each one is 1337 one aparat
    // if found we add it to the result string
    for (long long num = start; num <= end; num++) {
        if (num % 1000000==0){
            printf("%lld\n", num);
        }
        if (is_carmichael(num)) {
            c = num;
            count++;
        }

        if (count == ref) {
            sprintf(result + n * 19, "%019lld", c);
            ref += 1337;
            n++;
        }

    }
}

int main() {
    long long start = 2;
    long long end = powl(10, 18);

    printf("Bring your ☕ 🚬 ...\n");

    char carmichaels[190] = {0};
    find_carmichaels(start, end, carmichaels);

    printf("Hackfest{%s}\n", carmichaels);

    return 0;
}
