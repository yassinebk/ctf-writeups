// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.13;

import {Test, console2} from "forge-std/Test.sol";
import "forge-std/console.sol";
import {GlacierCoinExploit} from "../src/GlacierCoinExploit.sol";
import {GlacierCoinSetup} from "../src/GlacierCoinSetup.sol";

contract CounterTest is Test {
    GlacierCoinSetup public taskSetup;
    GlacierCoinExploit public exploitContract;

    function setUp() public {
        taskSetup = new GlacierCoinSetup();
        console.log(address(taskSetup).balance);
        payable(address(taskSetup)).transfer(100 ether);
        console.log(address(taskSetup).balance);
        taskSetup.checkSetup();
        exploitContract = new GlacierCoinExploit(address(taskSetup.TARGET()));
    }

    function test_TestExploit() public {
        exploitContract.attack{value: 10 ether}();
        exploitContract.exploit_again();
        console.log("Heere", address(taskSetup.TARGET()).balance);
        assert(taskSetup.isSolved());
    }
}
