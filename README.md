# Barely Functioning Web Sequence Diagrams

## How is sequence diagram generated?

[Heroku Web App (Probably working)](https://barely-diagram.herokuapp.com/)

Diagram is generated purely using Graphics2D library in Java 8. The server takes the request body as input.
Each object in sequence can be imagined as vertex in a directed graph, and each message line as a
weighted edge, where weight being the message string itself. An adjacency list representation will suffice.

As we get the sequence of edges in a kind-of sorted order by user. Each object as detected in a simple traversal
is placed in an array let's say X, which determines their placing in the final diagram.

Now for each vertex(object) in X, it's pixel location (x, y) is adjusted/calculated on the basis of the message symbol's
pixel message/edge string length it can be either inbound or outbound edge, only those edges are considered whose vertices
have an index smaller than current vertex in X.

This is done so to prevent overlap between edges weight, after adjusting and estimating each objects pixel location, then comes the actual drawing part
where each object (upper and lower box) + activation lines are drawn first. Then we take the as it is sequence of messages that we get from user
and start drawing them with a constant offset. One also needs to take of self loop edges too.

## Any findings?

I ended up rendering the image using 2 libraries - Graphics2D, Processing, but Graphics2D for this use case I found to be better suited.
Various kind of message symbols, objects, activation can be inculcated into it. But building option loop symbol or alternative symbol with recursive nature
and adjusting pixels accordingly would be an interesting challenge and that is why these are barely functioning web sequence diagrams :)

## What technology is used?

Frontend - Bootstrap 5, jQuery

Backend - Spring Boot