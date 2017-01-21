(ns database
  (:require [korma.core :refer
              [defentity has-one has-many belongs-to prepare transform insert values]]
            [korma.db :refer [defdb mysql]]
            [clojure.data.json :as json]
            [helpers :refer [env get-date-time-string]])
  (:gen-class))

(def config
  (mysql {:db       (get env 'database/DBNAME)
          :user     (get env 'database/USERNAME)
          :password (get env 'database/PASSWORD)
          ;; optional keys
          :host     (get env 'database/HOST)
          :port     (get env 'database/PORT)
          :delimiters ""}))

(defdb instance config)

(defentity reaction)
(defentity patient)
(defentity substance)
(defentity drug
  (has-many substance {:fk :drugId}))
(defentity report
  (prepare (fn [report-data]
    (assoc report-data :updatedAt (get-date-time-string)
                       :createdAt (if-let [created-at (:createdAt report-data)]
                                    created-at (get-date-time-string)))))
  (has-one patient    {:fk :reportId})
  (has-many drug      {:fk :reportId})
  (has-many reaction  {:fk :reportId}))

(defn import-model [data]
  (let [report-data (select-keys data
                      [:id :type :country :safetyReportId :safetyReportVersion :serious])
        patient-data   (:patient data)
        reaction-data  (:reactions data)
        drug-data      (:drugs data)
        substance-data (reduce #(concat %1 (:substances %2)) [] drug-data)]
    (insert report     (values report-data))
    (insert patient    (values patient-data))
    (insert reaction   (values reaction-data))
    (insert drug       (values (map #(dissoc % :substances) drug-data)))
    (insert substance  (values substance-data))))
