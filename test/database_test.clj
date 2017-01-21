(ns database-test
  (:require [clojure.test :refer [deftest is]]
            [clojure.string :as str]
            [database]
            [helpers]))

(def sample (helpers/process-json (str/join "/" ["." "test" "sample.json"])))
(def report (helpers/map-report (first sample)))

(deftest import-sample
  (is (not (false? (database/import-model report)))))
