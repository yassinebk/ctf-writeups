import hashlib
import itertools
import string

def find_sha1_leading_34():
    chars = string.ascii_lowercase + string.digits  # alphanumeric characters
    count=0
    for length in itertools.count(1):
        for combination in itertools.product(chars, repeat=length):
            # print(''.join(combination))
            potential_string = ''.join(combination)
            result = hashlib.sha1(potential_string.encode()).hexdigest()
            if result.startswith("34"):
                print(potential_string, result)
                count+=1
                if count >10:
                    return

print(find_sha1_leading_34())
