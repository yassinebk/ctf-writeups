[Official Writeups](https://github.com/DownUnderCTF/Challenges_2022_Public)


# PrivateLogs

- Transaction's Hash that creates the privateLogs
`0xa17166329e74e3d0f18c033c2816c3c3b557fa343efcdcab14ad8d5714bfb028`

- Transaction's Hash that creates the proxy: 
`0xb53cb36864219e2e0ef8a036536f6b8aaf9ee486065124a35d5f2c77fc2a332f`

First arguments passed to the init function through the delegateCall.  
{
  "name": "init",
  "params": [
    {
      "name": "_secretHash",
      "value": "0xfdb5a9c569a0cfccaa86d81444957d0f9bb1e900572b37199d877b7a6529c850",
      "type": "bytes32"
    }
  ]
}

## Chain Kill: 
Find current password -> Overwrite the _admin in the _admin() slot -> create an evil contract that sends the funds to you  -> call a function in that contract

	^ Problem is here
