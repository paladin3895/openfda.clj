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
  {:id            (uuid/to-string (uuid/v1))
   :onsetAge      (:patientonsetage data)
   :onsetAgeUnit  (:patientonsetageunit data)
   :onsetAgeGroup (:patientagegroup data)
   :sex           (:patientsex data)})

(defn map-reaction [data]
  {:id              (uuid/to-string (uuid/v1))
   :reactionOutcome (:reactionoutcome data)
   :reactionName    (:reactionmeddrapt data)
   :reactionVersion (:reactionmeddraversionpt data)})

(defn map-substances [data]
  (let [substance-names (:activesubstancename data)]
    (if (nil? substance-names) []
      (->> (str/split (:activesubstancename data) #"\\|\/")
           (map (fn [substance] {:id        (uuid/to-string (uuid/v1))
                                 :substance substance}))))))

(defn map-drug [data]
  (let [openfda           (:openfda data)
        substances        (:activesubstance data)
        drug-id           (uuid/to-string (uuid/v1))]
    {:id drug-id
     :indication          (:drugindication data)
     :medicinalProduct    (:medicinalproduct data)
     :administrationRoute (:drugadministrationroute data)
     :characterization    (:drugcharacterization data)
     :dosageForm          (:drugdosageform data)
     :dosageText          (:drugdosagetext data)
     :actionDrug          (:actiondrug data)
     :openfda             (if openfda (json/write-str (:spl_id openfda)) nil)
     :substances          (map #(assoc % :drugId drug-id) (map-substances substances))}))

(defn map-report [data]
  (let [patient           (:patient data)
        drugs             (:drug patient)
        reactions         (:reaction patient)
        report-id         (uuid/to-string (uuid/v1))]
    {:id                  report-id
     :type                (:reporttype data)
     :country             (:occurcountry data)
     :safetyReportId      (:safetyreportid data)
     :safetyReportVersion (:safetyreportversion data)
     :serious             (:serious data)
     :patient             (assoc (map-patient patient)   :reportId report-id)
     :drugs               (map #(assoc (map-drug %)     :reportId report-id)  drugs)
     :reactions           (map #(assoc (map-reaction %) :reportId report-id) reactions)}))
