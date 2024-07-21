
import threading, hashlib, time, os
from numba import jit, cuda
from tqdm import tqdm
import numpy as np

class BruteForcer:
	def __init__(self):
		self.first_thread_target = "32cae2"
		self.second_thread_target="92e1e8"
		self.found = False
		threads = 1000000
		for x in list(range(threads)):
			threading.Thread(target=self.Bruteforce, args=(threads, x)).start()

	def Hash(self, data):
		return hashlib.sha256((data).encode('utf-8')).hexdigest()

	def Log(self, base, ip, c):
		while True:
			print(f"{base}: {c}: "+'.'.join([str(c) for c in ip])+":>"+self.Hash('.'.join([str(c) for c in ip]))+"\n")

	def Bruteforce(self, threads, base):
		c = 0
		thread = int(base)
		ip = [(base // (256 ** 3))+1, ((base // (256**2)) % 255), ((base // 256) % 255)+1, (base % 255)+1]

        first_hash=self.Hash('.'.join([str(c) for c in ip])+'1')[0:6]
        second_hash=self.Hash('.'.join([str(c) for c in ip])+'2')[0:6]


		while not ((first_hash==self.first_thread_target and second_hash== self.second_thread_target) or self.found):
			c += 1
			ip = [(base // (256 ** 3))+1, ((base // (256**2)) % 255)+1, ((base // 256) % 255)+1, (base % 255)+1]
			base += threads

			if ip[0] >= 256:
				break

			if thread % 100 == 0:
				print(f"Thread {thread}: {c}: "+'.'.join([str(c) for c in ip])+" :>"+self.Hash('.'.join([str(c) for c in ip]))+"\n")

		else:
			if not self.found:
				ip = '.'.join([str(c) for c in ip])
				self.found = True
				time.sleep(2.5)
				os.system("cls")
				print(f"Found Ip address {ip}!!")
				print(self.Hash(ip))
				print(ip)
				os.system("PAUSE")
			


if __name__ == "__main__":
	BruteForcer()
