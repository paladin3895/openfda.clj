(ns openfda
  (:require [clojure.java.io :as io]
            [clojure.data.json :as json]
            [korma.core :as korma]
            [korma.db :as db]
            [me.raynes.fs :as fs]
            [me.raynes.fs.compression :as compression]
            [clojure.string :as str])
  (:gen-class))

(def db-config
  (db/mysql {:db "openfda"
             :user "homestead"
             :password "secret"
             ;; optional keys
             :host "192.168.10.10"
             :port "3306"
             :delimiters ""}))

(def file "/home/rook/Projects/Code/clojure/data/openfda.adr/8baea810-d888-11e6-9192-010a891943b3")
(defn process [filename]
  (-> filename
      io/input-stream
      java.util.zip.GZIPInputStream.
      slurp
      json/read-str))


(db/defdb pg db-config)

(korma/defentity report)

(defn -main []
  (print (process file))
  (print (korma/select report)))
