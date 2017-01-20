(ns helpers
  (:require [clojure.java.io :as io]
            [clojure.data.json :as json]
            ; [me.raynes.fs :as fs]
            ; [me.raynes.fs.compression :as compression]
            [clojure.string :as str]
            [clj-uuid :as uuid])
  (:gen-class))

(def path (.getAbsolutePath (java.io.File. ".")))
(def data-dir (str/join "/" [path, "data", "openfda.adr"]))

(defn process-json [filename]
  (-> filename
      io/input-stream
      slurp
      (#(json/read-str % :key-fn keyword))))

(defn process-zip [filename]
  (-> filename
      io/input-stream
      java.util.zip.GZIPInputStream.
      slurp
      (#(json/read-str % :key-fn keyword))))

(defn map-patient [data]
  {:id            (uuid/v1)
   :onsetAge      (:patientonsetage data)
   :onsetAgeUnit  (:patientonsetageunit data)
   :onsetAgeGroup (:patientagegroup data)
   :sex           (:patientsex data)})

(defn map-reaction [data]
  {:id              (uuid/v1)
   :reactionOutcome (:reactionoutcome data)
   :reactionName    (:reactionmeddrapt data)
   :reactionVersion (:reactionmeddraversionpt data)})

(defn map-substances [data]
  (->> (str/split (:activesubstancename data) #"\\|\/")
       (map (fn [substance] {:id        (uuid/v1)
                             :substance substance}))))

(defn map-drug [data]
  (let [openfda           (:openfda data)
        substances        (:activesubstance data)]
    {:id (uuid/v1)
     :indication          (:drugindication data)
     :medicinalProduct    (:medicinalproduct data)
     :administrationRoute (:drugadministrationroute data)
     :characterization    (:drugcharacterization data)
     :dosageForm          (:drugdosageform data)
     :dosageText          (:drugdosagetext data)
     :actionDrug          (:actiondrug data)
     :openfda             (if (map? openfda) (json/write-str (:spl_id openfda)) nil)
     :substances          (map-substances substances)}))

(defn map-report [data]
  (let [patient           (:patient data)
        drugs             (:drug patient)
        reactions         (:reaction patient)]
    {:id                  (uuid/v1)
     :type                (:reporttype data)
     :country             (:occurcountry data)
     :safetyReportId      (:safetyreportid data)
     :safetyReportVersion (:safetyreportversion data)
     :serious             (:serious data)
     :patient             (map-patient patient)
     :drugs               (map #(map-drug %) drugs)
     :reactions           (map #(map-reaction %) reactions)}))
