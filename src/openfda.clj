(ns openfda
  (:require [clojure.java.io :as io]
            [database :refer [report]]
            [helpers :refer [process-zip data-dir]]
            [korma.core :refer [insert]]
            [clojure.string :as str])
  (:gen-class))

(defn import [filename]
  (let [reports (process-zip (str/join "/" [data-dir filename]))]
    ))

(defn -main [command & args]
  (case command
    "import" (print "import")
    "select" (print (select report))
    (print "Nothing to do")))
