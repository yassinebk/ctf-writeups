const uuid = require("uuid");

function randomUUID() {
  return uuid.v1({
    node: [0x67, 0x69, 0x6e, 0x6b, 0x6f, 0x69],
    clockseq: 0b10101001100100,
  });
}

console.log(randomUUID());
