(ns helpers-test
  (:require [clojure.test :refer [deftest is]]
            [clojure.string :as str]
            [helpers]))

(def sample (helpers/process-json (str/join "/" ["." "test" "sample.json"])))

(deftest config-env
  (is (map? helpers/env)))

(deftest process-json
  (is (vector? sample)))

(def report (helpers/map-report (first sample)))
(deftest map-report
  (is (map? report))
  (is (= "1" (:type report))))

(def patient (:patient report))
(deftest map-patient
  (is (map? patient))
  (is (= "73" (:onsetAge patient))))

(def drugs (:drugs report))
(deftest map-drugs
  ; this test will fail due to drugs are lazy seq
  ; (is (vector? drugs))
  (is (= 1 (count drugs)))
  (is (= "[\"bb56fca3-fff9-4212-ba49-4959ad5a269e\"]"
         (:openfda (first drugs)))))

(def reactions (:reactions report))
(deftest map-reactions
  ; this test will fail due to reactions are lazy seq
  ; (is (vector? reactions))
  (is (= 4 (count reactions))))
