#!/usr/bin/env boot

(def +version+ "1.1.0")

(set-env! :dependencies (conj '[[adzerk/boot-cljs "1.7.170-3"]
                                [hoplon/javelin "3.8.4"]
                                [net.sourceforge.htmlunit/htmlunit "2.15"]]
                              ['boot/core *boot-version* :scope "provided"])
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
       tmp-file
       .toURI
       .toURL))

(ns-unmap *ns* 'test)
(deftask test
  [j jquery-versions VERSION #{str} "Versions of jQuery to test with; default is 1.4.4"]
  (let [jquery-versions (or jquery-versions #{"1.4.4"})
        test-page (.. (io/file (tmp-dir!) "testpage_out.html") toURI toURL)]
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

(defn wrap-export
  [name source]
  (format "(function(definition){if(typeof exports===\"object\"){module.exports=definition();}else if(typeof define===\"function\"&&define.amd){define(definition);}else{%s=definition();}})(function(){return function(){%s;return this.%s;}.call({});});"
          name
          source
          name))

(deftask wrap
  [e export-name NAME str "Name to export script content as."
   b bare-script NAME str "Name of the bare script to wrap."
   w wrapped-script NAME str "Name of the wrapped script to create."]
  (with-pre-wrap fs
    (util/info "Wrapping script in export...\n")
    (let [tmp    (tmp-dir!)
          [js]   (by-name [bare-script] (input-files fs))]
      (spit (io/file tmp wrapped-script)
            (wrap-export export-name (slurp (tmp-file js))))
      (-> fs
          (add-resource tmp)
          (rm [js])
          commit!))))

(deftask build
  []
  (comp (cljs :optimizations :advanced)
        (sift :to-source #{#"^out"})
        (sift :move {#"^main.js" "splint.min.js.bare"})
        (wrap :export-name "splint"
              :bare-script "splint.min.js.bare"
              :wrapped-script "splint.min.js")
        (sift :to-resource #{#"jquery.splint.js"})))

(task-options!
 test {:jquery-versions #{"1.4.4" "1.11.2" "2.1.3"}})
