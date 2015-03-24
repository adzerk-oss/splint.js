#!/usr/bin/env boot

(def +version+ "1.0.0")

(set-env! :dependencies '[[adzerk/boot-cljs "0.0-2814-3"]
                          [tailrecursion/javelin "3.7.2"]
                          [boot/core "2.0.0-rc13" :scope "provided"]]
          :source-paths #{"src/"}
          :splint {:version +version+})

(require '[adzerk.boot-cljs :refer :all])

(deftask build
  []
  (comp (cljs :optimizations :advanced)
        (sift :to-source #{#"^out"})
        (sift :move {#"^main.js" "splint.min.js"})))
