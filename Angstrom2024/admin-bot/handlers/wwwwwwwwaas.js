const puppeteer = require("puppeteer");

const sleep = (s) => new Promise((res) => setTimeout(res, s * 1000));

const browser_options = {
  headless: false,
  args: [
    "--no-sandbox",
    "--disable-background-networking",
    "--disable-default-apps",
    "--disable-extensions",
    "--disable-gpu",
    "--disable-sync",
    "--disable-translate",
    "--hide-scrollbars",
    "--metrics-recording-only",
    "--ignore-certificate-errors",
    "--mute-audio",
    "--no-first-run",
    "--safebrowsing-disable-auto-update",
    "--disable-dev-shm-usage",
  ],
};

module.exports = {
  name: "wwwwwwwwaas",
  timeout: 45000,
  noContext: true,
  async execute(url) {
    const key = "meow"
    const domain = "localhost:5000";
    const browser = await puppeteer.launch(browser_options);
    try {
      let page = await browser.newPage();
      const cookie = {
        domain: domain,
        name: "admin_cookie",
        value: key,
        httpOnly: true,
        secure: true,
        sameSite: "Lax",
      };
      await page.setCookie(cookie);
      await page.goto(url);
      await sleep(30);
    } finally {
      await browser.close();
    }
  },
};
