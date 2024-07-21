const puppeteer = require("puppeteer");
const express = require("express");

const app = express();

require("dotenv").config();

URL = process.env.URL;

const browser_options = {
  headless: true,
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
function delay(timeout) {
  return new Promise((resolve) => {
    setTimeout(resolve, timeout);
  });
}

async function visit(URL) {
  const browser = await puppeteer.launch(browser_options);
  const page = await browser.newPage();

  await page.goto("http://web2.africacryptctf.online:80/login", {
    waitUntil: "networkidle2",
    timeout: 50000,
  });

  await page.type("#username", "meowx1");
  await page.type("#password", "meowx");
  await page.click("#submit");

  console.log('Bot waiting for login ...')
  console.log(await page.waitForNavigation({ timeout: 30000 }))

  console.log(await page.cookies());

  await page.bringToFront();
  await page.goto(URL, {
    waitUntil: "networkidle2",
    timeout: 12000,
  })
  await delay(5000);
  await browser.close();
}

app.get("/", (req, res) => {
  const story = req.query.story;
  if (story) {
    return visit(story)
      .then((e) => {
        res.send("Visited successfully!");
      })
      .catch((err) => {
        console.log(err);
        res.send(500);
      });
  }


  return res.send("No story provided");
});
app.listen(5000, () => {
  console.log("app listening");
});
