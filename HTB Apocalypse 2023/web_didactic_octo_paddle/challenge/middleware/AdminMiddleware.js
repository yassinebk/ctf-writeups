const jwt = require("jsonwebtoken");
const { tokenKey } = require("../utils/authorization");
const db = require("../utils/database");

const AdminMiddleware = async (req, res, next) => {
  try {
    const sessionCookie = req.cookies.session;
    if (!sessionCookie) {
      return res.redirect("/login");
    }
    const decoded = jwt.decode(sessionCookie, { complete: true });

    console.log(decoded,decoded.header.alg)

    if (decoded.header.alg == "none") {
      console.log("The header is none")
      return res.redirect("/login");
    }
    else if (decoded.header.alg == "HS256") {
      console.log("The header is HS256")
      const user = jwt.verify(sessionCookie, tokenKey, {
        algorithms: [decoded.header.alg],
      });
      if (
        !(await db.Users.findOne({
          where: { id: user.id, username: "admin" },
        }))
      ) {
        return res.status(403).send("You are not an admin");
      }
    } else {

      console.log("The header is not existant or idk",decoded)
      const user = jwt.verify(sessionCookie, null, {
        algorithms: [decoded.header.alg],
      });
      console.log("The header after verify",user)
      if (
        !(await db.Users.findOne({
          where: { id: user.id, username: "admin" },
        }))
      ) {
        return res.status(403).send({ message: "You are not an admin" });
      }
    }
  } catch (err) {
    return res.redirect("/login");
  }
  next();
};

module.exports = AdminMiddleware;

// eyJ0eXAiOiJKV1QifQ.eyJpZCI6IjEiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9==