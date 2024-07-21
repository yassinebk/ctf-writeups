const puppeteer = require("puppeteer");



const browser_options = {
	headless: true,
	args: [
		'--no-sandbox',
		'--disable-extensions',
		'--disable-background-networking',
		'--disable-default-apps',
		'--disable-gpu',
		'--disable-sync',
		'--disable-translate',
		'--hide-scrollbars',
		'--metrics-recording-only',
		'--mute-audio',
		'--no-first-run',
		'--incognito',
		'--safebrowsing-disable-auto-update',
		'--js-flags=--noexpose_wasm,--jitless'
	],
	ignoreHTTPSErrors: true
};

async function checkout(base_url, admin_password, report_id) {
    const url = `${base_url}/note/${report_id}`;
    const login_url = `${base_url}/login`;
    console.log(`Going to ${url}`);
    const browser = await puppeteer.launch(browser_options);
    const page = await browser.newPage();

    await page.setDefaultNavigationTimeout(60000); 

    await page.goto(login_url);
    await page.type('input[name="username"]', 'admin');
    await page.type('input[name="password"]', admin_password);

    await page.click('button[id="submit"]');
    await page.waitForNavigation();

    await page.setCookie({ "name": "flag", "value": "Trustable{FakeFlag}", "sameSite": "Strict", "httpOnly": false, "secure": false });
    await page.goto(url, { waitUntil: 'networkidle2' }); 
    try {
        await browser.close();
    } catch {
        console.log('failed close!');
        process.exit(1);
    }
}
const base_url = process.argv[2];
const admin_password = process.argv[3]
const report_id = process.argv[4];

checkout(base_url, admin_password, report_id)
