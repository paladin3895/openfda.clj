(defproject openfda "1.0-SNAPSHOT"
  :description "Openfda project."
  :dependencies [
    [org.clojure/clojure "1.8.0"]
    [org.clojure/data.json "0.2.6"]
    [org.clojure/tools.cli "0.3.5"]
    ;; First you'll need to add Korma as a dependency in your lein/cake project:
    [korma "0.4.3"]
    ;; You'll also need the JDBC driver for your database. These are easy to find if
    ;; you search for "my-db jdbc driver maven".
    [org.clojure/java.jdbc "0.6.1"]
    [mysql/mysql-connector-java "5.1.35"]
    ;; Example for postgres:
    [org.postgresql/postgresql "9.2-1002-jdbc4"]
    [me.raynes/fs "1.4.6"]
    [danlentz/clj-uuid "0.1.6"]]
  :main openfda
  :source-path "src")
