(ns splint
  (:refer-clojure :exclude
   [atom count deref distinct empty first second next rest seq conj cons find nth last assoc dissoc
    get-in update-in assoc-in fnil disj pop peek hash get empty? reverse
    take drop take-nth partition partition-all partition-by iterate
    into merge merge-with subvec
    take-while drop-while group-by
    interpose interleave concat flatten
    keys select-keys vals
    prim-seq lazy-seq keep keep-indexed
    map mapcat map-indexed reduce reduce-kv filter remove some every? equiv
    transduce eduction sequence dedupe completing
    range repeat repeatedly sort sort-by
    into-array
    partial comp complement juxt
    identity constantly
    list vector vec array-map hash-map zipmap set sorted-set keyword symbol
    sorted-set-by sorted-map sorted-map-by str
    sum inc dec even? odd? subseq compare
    meta with-meta vary-meta
    apply])
  (:use-macros [splint.macros :only [make-inspectable splint-export splint-version]])
  (:require [clojure.set :as set]
            tailrecursion.javelin))

(splint-export apply cljs.core/apply)
(splint-export count cljs.core/count)
(splint-export distinct cljs.core/distinct)
(splint-export empty cljs.core/empty)
(splint-export first cljs.core/first)
(splint-export second cljs.core/second)
(splint-export next cljs.core/next)
(splint-export rest cljs.core/rest)
(splint-export seq cljs.core/seq)
(splint-export conj cljs.core/conj)
(splint-export cons cljs.core/cons)
(splint-export find cljs.core/find)
(splint-export nth cljs.core/nth)
(splint-export last cljs.core/last)
(splint-export assoc cljs.core/assoc)
(splint-export dissoc cljs.core/dissoc)
(splint-export getIn cljs.core/get-in)
(splint-export updateIn cljs.core/update-in)
(splint-export assocIn cljs.core/assoc-in)
(splint-export fnil cljs.core/fnil)
(splint-export disj cljs.core/disj)
(splint-export pop cljs.core/pop)
(splint-export peek cljs.core/peek)
(splint-export hash cljs.core/hash)
(splint-export get cljs.core/get)
(splint-export hasKey cljs.core/contains?)
(splint-export isEmpty cljs.core/empty?)
(splint-export reverse cljs.core/reverse)
(splint-export take cljs.core/take)
(splint-export drop cljs.core/drop)
(splint-export takeNth cljs.core/take-nth)
(splint-export partition cljs.core/partition)
(splint-export partitionAll cljs.core/partition-all)
(splint-export partitionBy cljs.core/partition-by)
(splint-export iterate cljs.core/iterate)
(splint-export into cljs.core/into)
(splint-export merge cljs.core/merge)
(splint-export mergeWith cljs.core/merge-with)
(splint-export subvec cljs.core/subvec)
(splint-export takeWhile cljs.core/take-while)
(splint-export dropWhile cljs.core/drop-while)
(splint-export groupBy cljs.core/group-by)
(splint-export interpose cljs.core/interpose)
(splint-export interleave cljs.core/interleave)
(splint-export concat cljs.core/concat)

;; Reference types, functions

(splint-export addWatch  cljs.core/add-watch)
(splint-export reset     cljs.core/reset!)
(splint-export atom      cljs.core/atom)
(splint-export swap      cljs.core/swap!)
(splint-export deref     cljs.core/deref)

;; Javelin

(splint-export cell        tailrecursion.javelin/cell)
(splint-export formula     tailrecursion.javelin/formula)
(splint-export destroyCell tailrecursion.javelin/destroy-cell!)
(splint-export isCell      tailrecursion.javelin/cell?)
(splint-export isInput     tailrecursion.javelin/input?)

(def ^:export version (splint-version))

(defn sequential-or-array? [x]
  (or (array? x)
      (sequential? x)))

(defn ^:export flatten [x]
  (cljs.core/filter #(not (sequential-or-array? %))
    (cljs.core/rest (tree-seq sequential-or-array? seq x))))

; The real lazy-seq is a macro, but it just expands its body into a function
(defn ^:export lazySeq [f]
  (new cljs.core/LazySeq nil f nil nil))

(splint-export keys cljs.core/keys)
(splint-export selectKeys cljs.core/select-keys)
(splint-export vals cljs.core/vals)
(splint-export primSeq cljs.core/prim-seq)
(splint-export map cljs.core/map)
(splint-export mapIndexed cljs.core/map-indexed)
(splint-export mapcat cljs.core/mapcat)
(splint-export reduce cljs.core/reduce)
(splint-export reduceKV cljs.core/reduce-kv)
(splint-export keep cljs.core/keep)
(splint-export keepIndexed cljs.core/keep-indexed)
(splint-export filter cljs.core/filter)
(splint-export remove cljs.core/remove)
(splint-export some cljs.core/some)
(splint-export every cljs.core/every?)
(splint-export equals cljs.core/=)
(splint-export range cljs.core/range)
(splint-export repeat cljs.core/repeat)
(splint-export repeatedly cljs.core/repeatedly)
(splint-export sort cljs.core/sort)
(splint-export sortBy cljs.core/sort-by)
(splint-export str cljs.core/str)
(splint-export intoArray cljs.core/into-array)
(splint-export subseq cljs.core/subseq)
(splint-export dedupe cljs.core/dedupe)

;; transducers

(splint-export transduce cljs.core/transduce)
(splint-export eduction cljs.core/eduction)
(splint-export sequence cljs.core/sequence)
(splint-export completing cljs.core/completing)

;; constructors

(splint-export list cljs.core/list)
(splint-export vector cljs.core/vector)
(splint-export vec cljs.core/vec)
(splint-export hashMap cljs.core/array-map)

(splint-export set cljs.core/set)
(splint-export sortedSet cljs.core/sorted-set)
(splint-export sortedSetBy cljs.core/sorted-set-by)
(splint-export sortedMap cljs.core/sorted-map)
(splint-export sortedMapBy cljs.core/sorted-map-by)

(def ^:export queue (fn [& args] (into cljs.core.PersistentQueue.EMPTY args)))

(splint-export keyword cljs.core/keyword)
(splint-export symbol cljs.core/symbol)

(splint-export zipmap cljs.core/zipmap)

;; Predicates
(splint-export isList cljs.core/list?)
(splint-export isSeq cljs.core/seq?)
(splint-export isVector cljs.core/vector?)
(splint-export isMap cljs.core/map?)
(splint-export isSet cljs.core/set?)

(splint-export isKeyword cljs.core/keyword?)
(splint-export isSymbol cljs.core/symbol?)

(splint-export isCollection cljs.core/coll?)
(splint-export isSequential cljs.core/sequential?)
(splint-export isAssociative cljs.core/associative?)
(splint-export isCounted cljs.core/counted?)
(splint-export isIndexed cljs.core/indexed?)
(splint-export isReduceable cljs.core/reduceable?)
(splint-export isSeqable cljs.core/seqable?)
(splint-export isReversible cljs.core/reversible?)

;; Set ops
(splint-export union set/union)
(splint-export intersection set/intersection)
(splint-export difference set/difference)
(splint-export join set/join)
(splint-export index set/index)
(splint-export project set/project)
(splint-export mapInvert set/map-invert)
(splint-export rename set/rename)
(splint-export renameKeys set/rename-keys)
(splint-export isSubset set/subset?)
(splint-export isSuperset set/superset?)

;; Comparisons

(splint-export notEquals cljs.core/not=)
(splint-export gt cljs.core/>)
(splint-export gte cljs.core/>=)
(splint-export lt cljs.core/<)
(splint-export lte cljs.core/<=)
(splint-export compare cljs.core/compare)

(defn ^:export truth [x]
  (if x true false))

(defn ^:export isNull [x]
  (= x nil))

;; HOFs

(splint-export partial cljs.core/partial)
(splint-export comp cljs.core/comp)
(splint-export complement cljs.core/complement)

(defn ^:export pipeline [& args]
  (reduce #(%2 %1) args))

(defn ^:export curry [fun & args]
  (fn [arg]
    (cljs.core/apply fun (cons arg args))))

(defn ^:export juxt [& fns]
  (fn [& args]
    (intoArray (map #(cljs.core/apply % args) fns))))

(defn ^:export knit [& fns]
  (fn [args]
    (intoArray (map #(% %2) fns args))))

;; Useful fns

(splint-export sum cljs.core/+)
(splint-export inc cljs.core/inc)
(splint-export dec cljs.core/dec)
(splint-export isEven cljs.core/even?)
(splint-export isOdd cljs.core/odd?)

(defn ^:export each [xs f]
  (doseq [x xs]
    (f x)))

(splint-export identity cljs.core/identity)
(splint-export constantly cljs.core/constantly)

(splint-export toJs cljs.core/clj->js)
(defn ^:export toClj
  ([x] (cljs.core/js->clj x))
  ([x keywordize-keys] (cljs.core/js->clj x :keywordize-keys keywordize-keys)))

(defn ^:export configure [variable value]
  (case variable
    "print-length" (set! *print-length* value)
    "print-level" (set! *print-level* value)))

(splint-export meta cljs.core/meta)
(splint-export withMeta cljs.core/with-meta)
(splint-export varyMeta cljs.core/vary-meta)
(splint-export alterMeta cljs.core/alter-meta!)
(splint-export resetMeta cljs.core/reset-meta!)

;; =============================================================================
;; Node.js Inspection support

(make-inspectable
  cljs.core.LazySeq
  cljs.core.IndexedSeq
  cljs.core.RSeq
  cljs.core.PersistentTreeMapSeq
  cljs.core.NodeSeq
  cljs.core.ArrayNodeSeq
  cljs.core.List
  cljs.core.Cons
  cljs.core.EmptyList
  cljs.core.PersistentVector
  cljs.core.ChunkedCons
  cljs.core.ChunkedSeq
  cljs.core.Subvec
  cljs.core.BlackNode
  cljs.core.RedNode
  cljs.core.ObjMap
  cljs.core.PersistentArrayMap
  cljs.core.PersistentHashMap
  cljs.core.PersistentTreeMap
  cljs.core.PersistentHashSet
  cljs.core.PersistentTreeSet
  cljs.core.Range
  cljs.core.Keyword
  cljs.core.Symbol
  cljs.core.PersistentQueue
  cljs.core.PersistentQueueSeq
  tailrecursion.javelin.Cell)
