# splint <a href="https://travis-ci.org/adzerk-oss/splint"><img src="https://travis-ci.org/adzerk-oss/splint.svg?branch=master" alt="build status"></img></a>

splint is a set of [ClojureScript] functions assembled for use in
unstructured "[spaghetti code]" JavaScript applications.  These kinds
of applications will eventually need to either be discarded or
rewritten.  For when you can't yet do either, there's splint.

In addition to ClojureScript functions for working with immutable
collections, splint includes [Javelin] for spreadsheet-like reactive
programming.  Javelin can be used to incrementally consolidate and
subsume callbacks and global variables.

At the very least, using splint probably won't make what you have any
worse.

## Use

* Grab [release/splint.min.js](https://raw.githubusercontent.com/adzerk-oss/splint/master/release/splint.min.js) and copy it to your project.
* See [splint.cljs](src/splint.cljs) for the list of available functions.
* The optional [release/jquery.splint.js](https://raw.githubusercontent.com/adzerk-oss/splint/master/release/jquery.splint.js) provides some convenient jQuery extensions.

## Build

* Install [Boot]
* Run `boot build test`

## Thanks

* To [David Nolen] for [mori], on which this project is based.

Copyright (C) 2012-2015 Alan Dipert, David Nolen, and contributors

Distributed under the
[Eclipse Public License](https://raw.github.com/adzerk/splint/master/epl-v10.html),
the same as Clojure.

[ClojureScript]: https://github.com/clojure/clojurescript
[spaghetti code]: http://en.wikipedia.org/wiki/Spaghetti_code
[Javelin]: https://github.com/tailrecursion/javelin
[Boot]: http://boot-clj.com/
[David Nolen]: http://swannodette.github.io/
[mori]: https://github.com/swannodette/mori
