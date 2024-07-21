const express = require('express');
const http=require("http")
const path= require("path")
const session = require('express-session');

const app = express();

// Configure session middleware
app.use(session({
  secret: 'your-secret-key',
  resave: false,
  saveUninitialized: true
}));


app.get("/", (req, res) => {
		if (req.socket.remoteAddress === "127.0.0.1")
			res.send("Hackfest{base_fake_flag} ");
		if (req.session.username === "SLIMEN")
			res.redirect("/elbirou");
		res.sendFile(path.join(__dirname, "pages/index.html"));
	});

	app.get("/sidikhouya", (req, res) => {
		let username = req.query.username;
		if (username && (typeof username === "string")) {
			if (!/slimen/i.test(username)) {
				req.session.username = username.toUpperCase();
			}
		}
		res.redirect("/elbirou");
		res.end();
	});

	app.get("/elbirou", (req, res) => {
		if (req.session.username === "SLIMEN") {
			res.sendFile(path.join(__dirname, "/pages/elbirou.html"))
		} else {
			res.redirect("/");
		}
	});

	app.get("/elficha", (req, res) => {
		// if (req.session.username === "SLIMEN") {
			let patient = JSON.parse(req.query.patient);
		if (Object.keys(patient).indexOf("fb_uri") === -1) {
			patient = Object.assign({}, patient);
			let fb_uri = patient.fb_uri;
			console.log("after assignment", fb_uri)
			if (!fb_uri || (typeof fb_uri !== "string"))
	{			console.log(fb_uri)
			res.send("No fb_uri")
			return
		}
				fb_uri = fb_uri.replace(/[\[a-zA-Z0-9:\]]/gm, "");
				console.log(fb_uri)
				try {
					let req = http.get("http://" + fb_uri, resp => {
						resp.on("data", data => res.send(data));
						
						return
					});
					req.on("error", error => {
						res.send("Error streaming data")
						return
					});
				} catch (error) {
					console.log(error);
					res.send(error)
					return
				}
			} else {
				res.send("fb_uri exists");
				return
			}
		// } else {
		// 	res.redirect("/");
		// }
	});

// Start the server
app.listen(3000, () => {
  console.log('Server is running on port 3000');
});