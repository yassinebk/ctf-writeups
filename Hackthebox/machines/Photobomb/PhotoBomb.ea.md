# Photobomb

## Basic Enumeration

![Rustscan result](assets/rustscanres.png)

So as we can see we have:
`port 80` open which points to a webpage.

`port 22` which is ssh available.

> If it didn't work for you make sure to add the following lines to `/etc/hosts`:
```bash
MACHINE_IP_ADDRESS www.photobomb.htb
MACHINE-IP_ADDRESS photobomb.htb
```

So I tried to see if there is a web server hosted on `port 80` but it gave me the following
![picture 1](./assets/bf95ebc71a078602eed7c800e7704fa61abf8e7aa525b61b05918f55870534a4.png)  

Okay let's do what we are good at, some basic Web enumerations:

- [x]  Nothing in HTML
- [x]  Something interesting in `photobomb.js`

![picture 2](./assets/f3738af6dfb674a003389d03e0bae56da8f6ce28333437efe396439f476e6c22.png)  

Okay so now we can visit as logged in user.
![picture 3](./assets/e13ad5c4a92971f3fee3637d060904b77ffebd6f6ba0ff8bc3750d3e36a2314a.png)  

As we can see there is the following functionality available:

1. Pick a picture.
   - No LFI found. Knowing the pictures paths gave me no insight other than the folder name `ui_images`
2. Choose the type in which we want the picture in.
   - Some image conversion is happening somewhere. I don't think there is a tool that integrates with the API. **There might be some command call here !**
3. Pick the size of the final picture.
   - The picture is clearly getting resized. I thought here about imagemagick as it's popular tool for image manipulation.
4. Download it.
   - The generated picture is dropped somewhere and we get it back. Can we control where is it dropped to do something malicious ? I will return to it later. Let me analyze the server response first.

![picture 4](./assets/a1a702b213818397df17185a6983053e3bd7662d8bdb850c26a1105ce5144490.png)  

Honestly at this point I wrote the following lines in .txt file and called `dict.txt`. Run my intruder to iterate through the 3 paramaters

```txt
; whoami 
 ; id ;#
&& whoami
; echo "Hi there" ;#"
##
```

![Iterating through `photo`](./assets/e76cccf5f69ae7e0a5eae385721e2d9ea430faccd052d8ed82d00457c8d4e6bc.png)  
![Iterating through `filetype`](./assets/c8e285c81e60ce2df37afa4bae2623dcaf6b3c8b1439a1eb8755b0f21d7ac144.png)
> I felt lucky here, As we can the see my input was reflected in the attachement and we got an error that means the execution of the command had failed but is it really executing the `whoami`.I needed to confirm that.

I tried to ping my machine and felt like a boss seeing this:
![Ping reflected](./assets/29536593dbfe8e6999415c34be32562f8a41c6d1011093aa9810b10456288e4e.png)  

So we can craft a reverse shell:

- I tried the bash one and it didn't work then I kept trying till I decided to go for a python shell. Honestly, I feel I am missing something. There should be a hint somewhere that python is installed.

![Basic Enumeration](./assets/c4fc159fef4fbc5d0fb92a6e44291ae9c63407df095afa65889de9557e3cbe0b.png)  

- The server was `ruby` and here is the vulnerable code.
![Vulnerable code](./assets/86fe5ac9fd6bf710dd7dca9deaff2dc57f8a0b937052a98fc3466e5f045a1f13.png)  

`sudo -l`
![sudo -l result](./assets/e86c08f4b44166c80c1793f5e3fb5ba77020f280108e2c079b4425309337655f.png)  

![Find vulnerable](./assets/279280234033acf68eb242ba81f2a4be5cf6a18dc7924f2c7136f37d5453fb15.png)  
Seeing the `cleanup.sh` script we see that it's vulnerable to $PATH manipulation. We can create a malicious find and we add it's path to the $PATH env variable.

user.txt a60e74e325521fde7c17ba47d76637cd
root.txt b716765843847441a03600dd05303753

===
~ Finished 10/14/22
