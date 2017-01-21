(ns openfda
  (:require [clojure.java.io :as io]
            [database :refer [import-model]]
            [helpers :refer [process-zip data-dir map-report]]
            [korma.core :refer [insert]]
            [clojure.string :as str])
  (:gen-class))

(defn import [filename]
  (let [reports (process-zip (str/join "/" [data-dir filename]))]
    (doall (map #(import-model (map-report %)) reports))))

(defn -main [command & args]
  (case command
    "import" (import (first args))
    (print "Nothing to do")))
