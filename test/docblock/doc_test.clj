(ns docblock.doc-test
  (:use
   docblock.doc
   clojure.test)
  (:require docblock.test-ns))


(deftest doc-test
  (is (= 3 (docu "simple addition" (+ 1 2))))
  (is (= 7 (docu "second simple addition" (+ 1 2) (+ 3 4)))))

;; (binding [*current-doc* []]
;;   (deftest doc-test
;;     (docu-for-output "simple addition" (+ 1 2))
;;     (is (= ["simple addition" ['(+ 1 2)]]
;;              *current-doc*))
;;     (docu-for-output "second simple addition" (+ 1 2) (+ 3 4))
;;     (is (= ["second simple addition" ['(+ 1 2) '(+ 3 4)]]
;;              *current-doc*))))

(defn-for-output f "s" [] (+ 1 2))
(deftest doc-undocumented-test
  (is (= "s" (:doc (meta f))))
  (is (= [{:args [], :forms ['(+ 1 2)]}]
         (:docblock (meta f))))
  (is (= 3 (f))))

(defn-for-output f1 "s" [] (docu "simple addition" (+ 1 2)))
(deftest doc-documented-test
  (is (= "s" (:doc (meta f1))))
  (is (= [{:args []
           :forms [{:docblock "simple addition"
                    :forms '[(+ 1 2)]}]}]
         (:docblock (meta f1))))
  (is (= 3 (f1))))

(defn-for-output f2 "s" []
  (docu "simple addition"
        (+ 1 (docu "some arg doc" 2))))
(deftest doc-nested-test
  (is (= "s" (:doc (meta f2))))
  (is (= [{:args []
           :forms [{:docblock "simple addition"
                    :forms '[(+ 1 {:docblock "some arg doc"
                                   :forms [2]})]}]}]
         (:docblock (meta f2))))
  (is (= 3 (f2))))

(deftest document-test
  (is (nil? (document 'docblock.test-ns)))
  (is (= [{:args [], :forms '[(+ 1 2)]}]
           (:docblock (meta #'docblock.test-ns/f0))))
  (is (= [{:args []
           :forms '[(do {:docblock "f1" :forms [(+ 1 2)]})]}]
           (:docblock (meta #'docblock.test-ns/f1))))
  (is (= [{:args [], :forms '[{:docblock "f2" :forms [(+ 1 2)]}]}]
           (:docblock (meta #'docblock.test-ns/f2))))
  (is (= [{:args [], :forms '[{:docblock "f3"
                               :forms [(+ 1 {:docblock "an arg"
                                             :forms [2]})]}]}]
           (:docblock (meta #'docblock.test-ns/f3)))))
