import express from "express";
import cookieParser from "cookie-parser";
import rateLimit from "express-rate-limit";
import { randomUUID } from "crypto";
import { xss } from "express-xss-sanitizer";
import { report } from "./bot.js";

// Rate limit for report endpoint - 1 request per minute
const limiter = rateLimit({
	windowMs: 60 * 1000,
	max: 1,
	standardHeaders: true,
	legacyHeaders: false,
});

const app = express();
export const adminCookie = randomUUID();

app.set("view engine", "ejs");
app.set("views", "templates/");
app.use(express.static("public"));

// Safety layer 1
// "middleware which sanitizes user input data (in req.body, req.query, req.headers and req.params) to prevent Cross Site Scripting (XSS) attack."
// = XSS impossible ;)
app.use(xss());

const css = "sha256-vLdrwYlWaDndNN9sQ9ZZrxSq93n8/wam7RRrUaZPZuE=";
const commonJs = "sha256-hPqYpiz7JNIo+Pdm+2iyVcEpBmkLbYzZp4wT0VtRo/o=";
const defaultJs = "sha256-PxCHadKfAzMTySbSjFxfuhIk02Azy/H24W0/Yx2wL/8=";
const adminJs = "sha256-5TQWiNNpvAcBZlNow32O2rAcetDLEqM7rl+uvpcnTb8=";

const defaultCSP = `default-src 'none'; img-src 'self'; style-src '${css}'; script-src '${commonJs}' '${defaultJs}'; connect-src 'self';`;

console.log(defaultCSP)

const blacklist = [
	"fetch",
	"eval",
	"alert",
	"prompt",
	"confirm",
	"XMLHttpRequest",
	"request",
	"WebSocket",
	"EventSource",
];

app.use(cookieParser());
app.use(express.json());

app.use((req, res, next) => {
	if (req.query) {
		console.log('whole req.query',req.query)
		console.log('whole req.query.text',req.query.text)
		// Saferty layer 2
		const s = JSON.stringify(req.query).toLowerCase();
		console.log(s)
		console.log(s.a)
		for (const b of blacklist) {
			if (s.includes(b.toLowerCase())) {
				return res.status(403).send("You are not allowed to do that.");
			}
		}

		// Safety layer 3
		for (const c of s) {
			if (c.charCodeAt(0) > 127 || c.charCodeAt(0) < 32) {
				return res.status(403).send("You are not allowed to do that.");
			}
		}
	}

	if (req.cookies?.admin === adminCookie) {
		res.user = {
			isAdmin: true,
			text: "Welcome back :)",
			unmodifiable: {
				background: "admin_background.png",
				CSP: `default-src 'self'; img-src 'self'; style-src '${css}'; script-src '${adminJs}' '${commonJs}';`,
			},
		};
	} else {
		// Safety layer 4
		res.user = {
			text: "Hi! You can modify this text by visiting `?text=Hi`. But I must warn you... you can't have html tags in your text.",
			unmodifiable: {
				background: "background.png",
			},
		};
	}

	if (req.query.text) {
		console.log('query text',req.query.text)
		res.user = { ...res.user, ...req.query };
		console.log(req.query,res.user)
	}

	// Safety layer 5
	console.log('setting csp',res.user.unmodifiable.CSP ?? defaultCSP)
	res.set("Content-Security-Policy", res.user.unmodifiable.CSP ?? defaultCSP);
	next();
});

app.get("/", (req, res) => {
	res.render("index", { ...res.user });
});

app.post("/report", async (req, res) => {
	try {
		if (!req.body.text) {
			throw new Error("Invalid input.");
		}
		if (typeof req.body.text !== "string") {
			throw new Error("Invalid input.");
		}
		await report(req.body.text);

		return res.json({ success: true });
	} catch (err) {
		console.log(err)
		return res.status(400).json({ success: false });
	}
});

app.listen(3000, () => {
	console.log("Server running on port 3000");
});
