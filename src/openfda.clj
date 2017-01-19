(ns openfda
  (:require [korma.core :as korma]
            [korma.db :as db]
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

(db/defdb pg db-config)

(korma/defentity report)

(defn -main []
  (println "This is the main function")
  (print (korma/select report)))
