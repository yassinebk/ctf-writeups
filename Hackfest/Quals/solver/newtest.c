#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <math.h>

#define MAX_PRIMES 1000000

bool is_prime(long long n, long long *primes, int num_primes) {
    for (int i = 0; i < num_primes; i++) {
        if(i%1000000 == 0)
        {printf("%ll\n",i);}

        if (n % primes[i] == 0) {
            return false;
        }
        if (primes[i] * primes[i] > n) {
            return true;
        }
    }
    return true;
}

void sieve(long long n, long long *primes, int *num_primes) {
    bool *is_composite = calloc(n + 1, sizeof(bool));
    for (long long i = 2; i <= n; i++) {
        if (!is_composite[i]) {
            primes[*num_primes] = i;
            (*num_primes)++;
            for (long long j = i * i; j <= n; j += i) {
                is_composite[j] = true;
            }
        }
    }
    free(is_composite);
}

bool is_carmichael(long long n, long long *primes, int num_primes) {
    if (is_prime(n, primes, num_primes)) {
        return false;
    }
    for (int i = 0; i < num_primes; i++) {
        long long p = primes[i];
        if (p * p > n) {
            break ;       }
        if (n % p == 0) {
            long long q = n / p;
            if ((q - 1) % (p - 1) != 0) {
                return false;
            }
        }
    }
    return true;
}

char *find_carmichaels(long long start, long long end) {
    long long *primes= malloc(MAX_PRIMES * sizeof(long long));
    int num_primes = 0;
    sieve(sqrt(end), primes, &num_primes);

    int count = 0;
    int n = 0;
    int ref = 1333337;
    char *nums[10];

    printf("%s\n","here");
    for (long long num = start; num<= end; num++) {
        if(num % 100000000 == 0)
        {
        printf("%ll\n",num);
        }
        if (n == 10) {
            break;
        }
        if (num % 2 == 0 || num % 3 == 0 || num % 5 == 0) {
            continue;
 }
        if (is_carmichael(num, primes, num_primes)) {
            char *c = malloc(30 * sizeof(char));
            sprintf(c, "%lld", num);
            nums[n] = c;
            count++;
        }
        if (count == ref) {
            ref += 1337;
            n++;
        }
    }

    char *result = malloc(200 * sizeof(char));
    sprintf(result, "Hackfest{%s}", nums[0]);
    for (int i = 1; i < n; i++) {
        sprintf(result, "%s%s", result,nums[i]);
    }

    return result;
}

int main() {
    long long start = 561;
    long long end = 1000000000000000000LL;

    printf("Bring your ☕ 🚬 ...\n");

    char *carmichaels = find_carmichaels(start, end);

    printf("%s\n", carmichaels);

    free(carmichaels);

    return 0;
}
