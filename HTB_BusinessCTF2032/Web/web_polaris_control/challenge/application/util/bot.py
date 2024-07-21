import time
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options

def bot_runner(username, password, browser_ua, identifier):
    chrome_options = Options()

    chrome_options.add_argument("headless")
    chrome_options.add_argument("no-sandbox")
    chrome_options.add_argument("ignore-certificate-errors")
    chrome_options.add_argument("disable-dev-shm-usage")
    chrome_options.add_argument("disable-infobars")
    chrome_options.add_argument("disable-background-networking")
    chrome_options.add_argument("disable-default-apps")
    chrome_options.add_argument("disable-extensions")
    chrome_options.add_argument("disable-gpu")
    chrome_options.add_argument("disable-sync")
    chrome_options.add_argument("disable-translate")
    chrome_options.add_argument("hide-scrollbars")
    chrome_options.add_argument("metrics-recording-only")
    chrome_options.add_argument("no-first-run")
    chrome_options.add_argument("safebrowsing-disable-auto-update")
    chrome_options.add_argument("media-cache-size=1")
    chrome_options.add_argument("disk-cache-size=1")
  
    client = webdriver.Chrome(options=chrome_options)


    client.execute_cdp_cmd("Network.setUserAgentOverride", {"userAgent": browser_ua})
    client.execute_cdp_cmd("Network.setBlockedURLs", {"urls": ["/panel/server"]})
    client.execute_cdp_cmd("Network.enable", {})

    client.get("http://localhost:1337/panel/login")

    time.sleep(3)
    client.find_element(By.ID, "username").send_keys(username)
    client.find_element(By.ID, "password").send_keys(password)
    client.execute_script("document.getElementById('login-btn').click()")
    time.sleep(3)

    client.get(f"http://localhost:1337/panel/implant/{identifier}")
    time.sleep(5)

    client.quit()
