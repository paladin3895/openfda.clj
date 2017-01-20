(ns database
  (:require [korma.core :refer [defentity has-one has-many belongs-to]]
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

(defentity substance)
(defentity drug
  (has-many substance))
(defentity reaction)
(defentity patient)
(defentity report
  (has-one patient)
  (has-many drug)
  (has-many reaction))
