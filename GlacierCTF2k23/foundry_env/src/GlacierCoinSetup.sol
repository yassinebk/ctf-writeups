// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "./GlacierCoin.sol";

contract GlacierCoinSetup {
    GlacierCoin public TARGET; // Contract the player will hack

    function checkSetup() public returns (bool) {
        require(address(this).balance == 100 ether);
        TARGET = new GlacierCoin();

        // Send 10 ether to the victim contract as initial balance
        TARGET.buy{value: 10 ether}();

        return true;
    }

    // Our challenge in the CTF framework will call this function to
    // check whether the player has solved the challenge or not.
    function isSolved() public view returns (bool) {
        return address(TARGET).balance == 0;
    }

    receive() external payable {}
}
