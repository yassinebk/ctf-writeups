# Web Cheatsheet

## Fundamentals

----
[CORS by Jakearchibald](https://jakearchibald.com/2021/cors/)
> Same site and same origin are fundamentally different.

- **A simple request** This doesn't require a preflight.
- **An unusual header** This triggers a preflight, and the server doesn't allow the request.
- **An unusual header** again, but this time the preflight is correctly configured, so the request goes through.
- **A normal Range header** This relates to the spec change I made. When browsers implement the change, this request won't need a preflight. It's currently implemented in Chrome Canary.
- **An unusual method** This highlights the Chrome bug documented above. The request won't go through in Chrome, but it'll work in other browsers.
- **An unusual method** again. This works around the Chrome bug.

----

## WAF Bypass

### SQL Injection

- [Common Bypass Techniques](https://learn.dreamhack.io/309#10)
- [Research Paper](https://www.exploit-db.com/papers/17934)

Also we can thing of using `||` and `boolean` to get the flag.

#### Challenges

- `[wargame] SQL Injection WAF Bypass`
- `[wargame] SQL Injection WAF Bypass Advanced`

## Flask

- Check for Debug mode
  - [Exploiting Werkzeug Debug mode]()

> I am leaving this section for your question that can't be answered till you see the whole picture.

## XS-Search

## `postMessage()`

> The method window.postMessage() is used by the application to allow cross-origin communication between different window objects, Since using postMessage() requires the sender to obtain a reference to the receiver’s window, messages can be sent only between a window and its iframes or pop-ups. That’s because only windows that open each other will have a way to reference each other. For example, a window can use window.open to refer to a new window it opened. Alternatively, it can use window.opener to reference the window that spawned the current window. It can use window.frames to reference embedded iframes, and window.parent to reference the parent window of the current iframe. This method provides a way to circumvent the Same Origin Policy restrictions securely. You can use it to send text-based messages to another window.

```js
//  Sending message
targetWindow.postMessage(message, targetOrigin, [transfer]);

// Handling incoming messages
window.addEventListener("message", function()=> {
  //Whatever you want to do with message data...
}, false/true);

```


## Writeups
- [pyYAML exploiting ](https://hackmd.io/@harrier/uiuctf20) Bypassing blacklist, similar to SSTI in flask. ( SPARKCTF 2k22 )
- [Handlebar layout LFR](https://blog.shoebpatel.com/2021/01/23/The-Secret-Parameter-LFR-and-Potential-RCE-in-NodeJS-Apps/)
- [Handlebar RCE](https://mahmoudsec.blogspot.com/2019/04/handlebars-template-injection-and-rce.html)
