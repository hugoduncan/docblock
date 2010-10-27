(ns docblock.test-ns
  "A namespace to use in testing docblock"
  (:use docblock.doc))


(defn f0
  "f0"
  []
  (+ 1 2))

(defn f1
  "f1"
  []
  (do
    (docu
     "f1"
     (+ 1 2))))

(defn f2
  "f2"
  []
  (docu
   "f2"
   (+ 1 2)))

(defn f3
  "f3"
  []
  (docu
   "f3"
   (+ 1 (docu "an arg" 2))))
