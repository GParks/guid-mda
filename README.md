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

Currently, no statistical analysis is done.

Not yet generating MD5 hashes of the data.
