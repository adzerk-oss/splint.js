#!/usr/bin/env boot

(def +version+ "1.0.0")

(set-env! :dependencies '[[adzerk/boot-cljs "0.0-2814-3"]
                          [tailrecursion/javelin "3.7.2"]
                          [boot/core "2.0.0-rc13" :scope "provided"]
                          [net.sourceforge.htmlunit/htmlunit "2.15"]]
          :source-paths #{"src/" "test/"}
          :splint {:version +version+}
          :target-path "release")

(require '[adzerk.boot-cljs :refer :all]
         '[clojure.java.io :as io]
         '[clojure.string :as str]
         '[boot.util :as util])

(import '[com.gargoylesoftware.htmlunit WebClient BrowserVersion]
        '[com.gargoylesoftware.htmlunit.html HtmlPage]
        '[java.util.logging Level Logger])

(.setLevel (Logger/getLogger "com.gargoylesoftware") Level/OFF)

(defn web-client []
  (WebClient. BrowserVersion/CHROME))

(defn fill-template [string replacements]
  (reduce-kv #(.replaceAll %1 (str "\\{" %2 "\\}") (str %3)) string replacements))

(defn ->url [name files]
  (->> (or (seq (by-name [name] files))
           (throw (ex-info "File not found, can't create URL" {:name name})))
       first
       tmpfile
       .toURI
       .toURL))

(ns-unmap *ns* 'test)
(deftask test
  [j jquery-versions VERSION #{str} "Versions of jQuery to test with; default is 1.4.4"]
  (let [jquery-versions (or jquery-versions #{"1.4.4"})
        test-page (.. (io/file (temp-dir!) "testpage_out.html") toURI toURL)]
    (with-pre-wrap fileset
      (doseq [version jquery-versions]
        (util/info "Testing with jQuery version %s\n" version)
        (spit test-page
              (fill-template (slurp (->url "testpage.html" (input-files fileset)))
                             {"JQUERY_URL"        (->url (format "jquery-%s.min.js" version) (input-files fileset))
                              "SPLINT_URL"        (->url "splint.min.js" (output-files fileset))
                              "JQUERY_SPLINT_URL" (->url "jquery.splint.js" (output-files fileset))
                              "TEST_JS_URL"       (->url "test.js" (input-files fileset))}))
        (.getPage (web-client) test-page))
      fileset)))

(deftask build
  []
  (comp (cljs :optimizations :advanced)
        (sift :to-source #{#"^out"})
        (sift :move {#"^main.js" "splint.min.js"})
        (sift :to-resource #{#"jquery.splint.js"})))

(task-options!
 test {:jquery-versions #{"1.4.4" "1.11.2" "2.1.3"}})
