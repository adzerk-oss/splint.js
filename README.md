# splint

splint is an assortment of [ClojureScript] functions packaged for
palliative use in large, extant client-side [JavaScript/jQuery
spaghetti] projects.

splint promotes a functional/dataflow style that can integrate
incrementally with existing spaghetti code.  At the very least, using
splint probably won't make your code any worse.

## Build

* Install [Boot]
* Run `boot build`
* Use the resulting `target/splint-X.Y.Z.min.js` in your project

## Thanks

* To [David Nolen] for [mori], on which this project is based.

Copyright (C) 2012-2015 Alan Dipert, David Nolen, and contributors

Distributed under the
[Eclipse Public License](https://raw.github.com/adzerk/splint/master/epl-v10.html),
the same as Clojure.

[ClojureScript]: https://github.com/clojure/clojurescript
[JavaScript/jQuery spaghetti]: http://en.wikipedia.org/wiki/Spaghetti_code
[Boot]: http://boot-clj.com/
[David Nolen]: http://swannodette.github.io/
[mori]: https://github.com/swannodette/mori