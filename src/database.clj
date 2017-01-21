(ns database
  (:require [korma.core :refer [defentity has-one has-many belongs-to insert]]
            [korma.db :refer [defdb mysql]])
  (:gen-class))

(def config
  (mysql {:db "openfda"
             :user "homestead"
             :password "secret"
             ;; optional keys
             :host "192.168.10.10"
             :port "3306"
             :delimiters ""}))


(defdb instance config)

(defentity reaction)
(defentity patient)
(defentity substance)
(defentity drug
  (has-many substance {:fk :drugId}))
(defentity report
  (has-one patient    {:fk :reportId})
  (has-many drug      {:fk :reportId})
  (has-many reaction  {:fk :reportId}))

(defn import-report [report]
  ())
