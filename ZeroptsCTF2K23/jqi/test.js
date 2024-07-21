import jq from 'node-jq';

async function test() {
    // const res=await   jq.run('.challenges,$ENV.flag', "./data.json", { output: 'json' })
    const res=await   jq.run('[.challenges[] | select( $ENV.flag| contains("") ) | key:.key] ', "./data.json", { output: 'json' })
    console.log(res);
}

test().catch(e=>console.log(e))