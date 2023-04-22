
# Ruby deserialization

## Link

[ResearchLink](https://www.elttam.com/blog/ruby-deserialization/#content)

Done by *LUKE JAHNKE, NOVEMBER 08, 2018*

## Context

Exploiting unsecure deserialization in ruby's applications.

In the case of Ruby, the terms marshalling and unmarshalling are commonly used.
The Marshal class has the class methods “dump” and “load” which can be used as follows:

```rb
class Person
  attr_accessor :name
end
nil

p = Person.new
#<Person:0x00005584ba9af490>

p.name = "Luke Jahnke"
"Luke Jahnke"

p
#<Person:0x00005584ba9af490 @name="Luke Jahnke">

Marshal.dump(p)
"\x04\bo:\vPerson\x06:\n@nameI\"\x10Luke Jahnke\x06:\x06ET"

Marshal.load("\x04\bo:\vPerson\x06:\n@nameI\"\x10Luke Jahnke\x06:\x06ET")
#<Person:0x00005584ba995dd8 @name="Luke Jahnke">

```

We can manipulate serialized objects to change the value of attributes but how can we leverage such vulnerability to force arbitrary code execution ?

## Gadget Chaining

Exploiting the fact that `everything is an object` in Ruby and that there are methods that are defined and acts on these objects to construct a chain of functions that either:

- Cause arbitrary code execution which is our ultimate goal.
- Pollute the context and includes other modules / functions / files to cause the CE.

Figure-2: An example of a method calling require (lib/rubygems.rb)
```rb
module Gem
...
  def self.deflate(data)
    require 'zlib'
    Zlib::Deflate.deflate data
  end
...
end

```

Figure-6: A gadget allowing partial control of the require argument (ext/digest/lib/digest.rb)

```rb
module Digest
  def self.const_missing(name) # :nodoc:
    case name
    when :SHA256, :SHA384, :SHA512
      lib = 'digest/sha2.so'
    else
      lib = File.join('digest', name.to_s.downcase)
    end

    begin
      require lib
```

> `autoload` `@const_missing` etc can include other modules which benefits us.

The ultimate payload found after the research can be found in the article. Also, by understanding the objective and the general method we can adapt to available contexts and find our chain of gadgets.