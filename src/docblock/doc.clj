(ns docblock.doc
  "Documentation"
  (:require
   [clojure.walk :as walk]))

(def ^{:doc "Collect documentation"} *current-doc*)

(defmacro docu
  "Document a block of code"
  [doc-string & body]
  `(do ~@body))

(declare inner-walk outer-walk)

(defn- docu?
  "Tests whether the form is (docu ...)."
  [form]
  (flush)
  (and (seq? form)
       (symbol? (first form))
       (#{'docu `docu} (first form))))

(defn document-block
  "Generate documentation from a form"
  [m doc forms]
  {:docblock doc
   :forms (vec (map #(walk/walk inner-walk outer-walk %) forms))})

(defn- inner-walk [form]
  (cond
   :else (walk/walk inner-walk outer-walk form)))

(defn- outer-walk [form]
  (cond
   (docu? form) (document-block (meta form) (second form) (drop 2 form))
   (symbol? form) form
   (seq? form)  form
   :else form))

(defonce ^{:macro true
           :arglists (:arglists (meta #'clojure.core/defn))}
  core-defn (var-get #'clojure.core/defn))

(defn destruct-defn
  [name fdecl]
  (let [m (if (string? (first fdecl))
            [(first fdecl)]
            [])
        fdecl (if (string? (first fdecl))
                (next fdecl)
                fdecl)
        m (if (map? (first fdecl))
            (conj m (first fdecl))
            m)
        fdecl (if (map? (first fdecl))
                (next fdecl)
                fdecl)
        fdecl (if (vector? (first fdecl))
                (list fdecl)
                fdecl)
        m (if (map? (last fdecl))
            (conj m (last fdecl))
            m)
        fdecl (if (map? (last fdecl))
                (butlast fdecl)
                fdecl)]
    [m fdecl]))

(defn- add-docu
  [[args & body]]
  {:args (vec args)
   :forms (list `quote (vec (map (fn [form]
                                   (walk/walk
                                    inner-walk outer-walk form))
                                 body)))})

(defmacro defn-for-output
  "A version of defn that collects docus"
  [name & fdecl]
  (let [[m fdecl] (destruct-defn name fdecl)]
    `(~'core-defn
      ~(with-meta name {:docblock (vec (map add-docu fdecl))})
      ~@m ~@fdecl)))

(defn document
  "Generate a documentation tree for a namespace"
  [ns]
  (binding [clojure.core/defn #'defn-for-output]
    (require ns :reload)))
