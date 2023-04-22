import jwt from "jsonwebtoken";

// const private_key = `-----BEGIN RSA PRIVATE KEY-----
// MIIEpAIBAAKCAQEApfnLMHfhzKCIjfW9J29qDR2q23HuLODI+tzCtl5sn6TdF4Xi
// c1UsD6ifH10W513/I/9IJvnWvUqQOJdLjwN1pWI4YekBTCY+E5cvCzelGkQqcPZM
// MzzVQubbFv1aqVOgebw8qRYyR9+2pEL/6Ph1IcXgazT/0oOORrtyxFz0f42lhQrB
// FaA/8IFbQ98U4sHPbI35uuCbjNW6ZK0V5WD7AIKdJFxvLPwIL2w7NA0+LplarnfQ
// G5KkZHyYz3ptaL69bPwoVgCB34qkwedbhr+F1YLEv9cTUIjK6bn1bfWruuFLHu3k
// yqZvtzkeX0SjeAmHKJnigTNQpoBrd0L2iqIs6QIDAQABAoIBAQCVUltEesZRg6f3
// F7uai/j8XdGGZkSiaYVHdG9VBTRymC8054XovxrJ56lo3SYq930AlL4EGsfgOwHv
// d+ZJfxJLvUb+h1NcgBdfJqfzTpeiPutz/5JkknlHHEzFrR/wsMTyYLCuasLHuPQn
// agO0RDuVK+IQcXkqmqLY7fF9yidJzNqoiVzs4uBf9HvnUQPKUBal7ObIZiqqh95E
// c74RgJYEIBm+mlat6aqaT5zIJnltdpbFaZKhCikFZ1+ScCWw8+R+x+Co1QZaq7Wz
// Vv7ZbdK+g9EJWVZ43uABuoVXSfZx0QSM+12vuLd1HhJxgeJhVHOc3hEEJUO26FFo
// pxgq5vFRAoGBAPrV0Dnv9HKlSEcSE5wEEqzx77jFCZRJl7B6c2wKNAU8SSXBt03E
// Grvek1po0OQkhQaBFohf+5A5tn6/O9US90Y3HLEH3ALTfjs2gFKiSImMkmMi2NCG
// uMYD4U90I76kbJMb8H+zYLHl4z1DXjd7KHssVzBHTrbyXvJ46na8VhelAoGBAKlk
// rLDLCkVVctopg+eo1SgNyYjcLyVf7lT1SK4BgueeHq+1ihIjZ5Jv1GfUtWQx+yCv
// 7emjR/GjYmvxZHxgoH3CZlOQepBjxn1N2W7dBRouc4UuVcoBeT9rRx9FTCegI3AN
// 3LgzyJvGIrH0NbRdlf5pT9kh8BBB1wsP8gTucZz1AoGAZ5eKKYNaF0b/dKqYgqSD
// +fzpHgvyCtXh2baYmJQWK9tb8kXwqCNHTzN686m6K56RecAySBwrzsKnBe2Ajl0I
// 1mzIYqx2RoyF43p8wKPZBUWS53JqzKP3B4BuutuiL4fEd3TgfDzewFnK4XBDJvKs
// I0KKt5ubfJ7Ya5dYbbUWcSkCgYA2Jj6mDWHtJBGg9+B4r5nkQfQI0gooMnBnrDld
// lpIYJI2+sZO/lJ3JTAJkZrTNoMScgGDXbSDUd9xAkVwD/oePbBN15xunCJ4s9DPr
// oq+83NiE3HkEKFBwG4US0/4yNJjfey7xm2wnj8ELjfuU+s22FEg4Kwcoy0NJAAHn
// 3aln+QKBgQD2JFl718Fh8hloqM4J4Mcj8SaNnH5OYtwX4JTRQzg03QZ5BDWyMxTq
// 5nxxQ+UMXsSPDn6fzhB8h1MsvTr0UANrOXzbXqe1SL2YAIiDwvlvUNKxW+efDXMA
// QbAYUBWPnlm8jNd6NoUyYdgQarJSmE2jwhB7TRqGmT78yZbRd0ckAg==
// -----END RSA PRIVATE KEY-----`;

// const token = jwt.sign({ id: 1 }, private_key, { algorithm: "RS256" });

const verified = jwt.verify("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MiwiaWF0IjoxNjc5ODEzNjExLCJleHAiOjE2Nzk4MTcyMTF9.ObNbS9bTUQFiyDYSFlY5OzXZp_vDCKTw0K746VPvUa4",null as any, {
  algorithms: ["none"],
});

console.log("here 2");
