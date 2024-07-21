import fs from 'node:fs/promises';

import Fastify from 'fastify';
import jq from 'node-jq';

const fastify = Fastify({
    logger: true
});

const indexHtml = await fs.readFile('./index.html');
fastify.get('/', async (request, reply) => {
    reply.type('text/html').send(indexHtml);
});

const KEYS = ['name', 'tags', 'author', 'flag'];
fastify.get('/api/search', async (request, reply) => {

    // Getting values for keys
    const keys = 'keys' in request.query ? request.query.keys.toString().split(',') : KEYS;
    // Getting values for conds
    const conds = 'conds' in request.query ? request.query.conds.toString().split(',') : [];

    // Maxiumum we have 10 keys and 10 conds key1,key2,key3,key4,key5,key6,key7,key8,key9,key10 and key1 in value1,key2 in value2,key3 in value3 ( example )
    if (keys.length > 10 || conds.length > 10) {
        return reply.send({ error: 'invalid key or cond' });
    }

    // build query for selecting keys
    for (const key of keys) {
        if (!KEYS.includes(key)) {
            return reply.send({ error: 'invalid key' });
        }
    }
    // Mapping keys to key:.key,key2:.key2
    const keysQuery = keys.map(key => {
        return `${key}:.${key}`
    }).join(',');

    // build query for filtering results
    let condsQuery = '';

    for (const cond of conds) {
        const [str, key] = cond.split(' in ');
        console.log(str,key)
        if (!KEYS.includes(key)) {
            console.log("[+] returning invalid key inside the conds",key)
            return reply.send({ error: 'invalid key' });
        }

        // check if the query is trying to break string literal
        if (str.includes('"') || str.includes('\\(')) {
            return reply.send({ error: 'hacking attempt detected' });
        }
        // For each condition we will have a query like this `| select(.key | contains("str"))`
        condsQuery += `| select(.${key} | contains("${str}"))`;
    }

    // Final query [.challenges []   | select(.key | contains("str")) | select(.key2 | contains("str")) | {key:.key,key2:.key2}]
    let query = `[.challenges[] ${condsQuery} | {${keysQuery}}]`;
    console.log('[+] keys:', keys,keysQuery);
    console.log('[+] conds:', conds,condsQuery);

    let result;
    try {
        // Using jq and returning result 
        //  We need to find a way to read the environment variable from this function
        result = await jq.run(query, './data.json', { output: 'json' });
    } catch(e) {
        console.log(e)
        return reply.send({ error: 'something wrong' });
    }

    if (conds.length > 0) {
        reply.send({ error: 'sorry, you cannot use filters in demo version' });
    } else {
        reply.send(result);
    }
});
  
fastify.listen({ host: '0.0.0.0', port: 3000 }, (err, address) => {
    if (err) {
        fastify.log.error(err);
        process.exit(1);
    }
    console.log(`Server is now listening on ${address}`);
});