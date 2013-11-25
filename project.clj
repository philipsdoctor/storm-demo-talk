(defproject demo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [incanter "1.5.1"]]
  :profiles {:dev {:dependencies [[storm "0.8.1"]
                                  [org.clojure/clojure "1.5.1"]
                                  ; to placate lighttable
                                  [org.clojure/clojurescript "0.0-2030"]
                                  [antler/commons-io "2.2.0"]
                                  ; idiomatic clj for lighttable
                                  [lein-kibit "0.0.8"]]}})
