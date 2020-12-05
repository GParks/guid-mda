# Background
What is the relationship between the (random-ness of the) distribution of UUIDs,
and the MD5 hash of the same [values]?

# Current Implementation

* **`gradle run`** <br>with no arguments, default to 5 (new) UUIDs, create and save [default file `uuids.json`]

    * `-c <count>`
        create _`<count>`_ UUID values
    * `-o <output filename>`

    * `-f <input filename>`
        read UUIDs from file rather than creating new

To specify command line args thru gradle, include **`--args="..."`**;
(E.g. `gradle run --args="-c 10"` to pass a count of 10 to the prog.)

Currently, no statistical analysis is done.


### "Analysis"

There is much to learn about statistical analysis, but I'm going to start small:
if I divide the results into quartets and quartiles,
(first, divide the results into four equal-sized portions; then divide it into
  four equally spaced data segments, based on the range -- in which case
  there may be zero to many* data points [up to n-1 data, if the data is very, very aberrantly distributed])
Then, for now, all I will do is "compare" the relative sizes and ranges of those ...

Of course, I haven't done "any" of this, yet.


## Refs

Links I found "useful"

[java.math.BigInteger class docs](https://docs.oracle.com/javase/7/docs/api/java/math/BigInteger.html)

[java.security.MessageDigest class docs](https://docs.oracle.com/javase/8/docs/api/java/security/MessageDigest.html)

[Markdown Syntax](https://daringfireball.net/projects/markdown/syntax)

[GSON User Guide](https://github.com/google/gson/blob/master/UserGuide.md#gson-user-guide)

[GSON Streaming](https://sites.google.com/site/gson/streaming)

[UUID class](https://docs.oracle.com/javase/7/docs/api/java/util/UUID.html)

[Java Code Example for com.google.gson.JsonElement#getAsJsonObject](https://www.programcreek.com/java-api-examples/?class=com.google.gson.JsonElement&method=getAsJsonObject)

[Javadocs for com.google.gson.JsonElement](https://javadoc.io/static/com.google.code.gson/gson/2.8.5/index.html?com/google/gson/JsonElement.html)

[Gson - Quick Guide](https://howtodoinjava.com/gson/google-gson-tutorial/)

[java.util.Collections reference](https://docs.oracle.com/javase/7/docs/api/java/util/Collections.html)

[java.util.Arrays ref](https://docs.oracle.com/javase/7/docs/api/java/util/Arrays.html)
