(ns openfda
  (:require [clojure.java.io :as io]
            [database :refer [report]]
            [helpers :refer [process]]
            [korma.core :refer [select]])
  (:gen-class))

(defn -main [command & args]
  (case command
    "import" (print "import")
    "select" (print (select report))
    (print "Nothing to do")))
