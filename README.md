Autobahn-SW
===============
This repo contains an AutobahnAndroid with SecuredWebSockets implementation.
It allows to perform RPC and Push-Subscribe Wamp requests over a secured connection (wss protocol)

It is based on:

Autobahn Android (0.5.2-snapshot at this time) : 
https://github.com/tavendo/AutobahnAndroid

and SecuredWebSockets:
https://github.com/palmerc/SecureWebSockets

As far as we know, the library works in production for both topologies (RPC and Push-Subscribe).

This repo needs some cleaning, as somes classes are available twice:
ByteBufferInputStream.java
ByteBufferOutputStream.java
Doxygen.java
NoCopyByteArrayOutputStream.java
PrefixMap.java
Utf8Validator.java
Some packages might also need to be renamed.

Please fee free to make any comments that will help improve this implementation.

Download the project
-------

git clone https://github.com/jsebrien/Autobahn-SW.git

