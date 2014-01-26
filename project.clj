(defproject demo "0.1.0-SNAPSHOT"
  :description "A simple storm demo for a short presentation."
  :url "http://slid.es/philipdoctor/storm-with-python-and-a-side-of-clojure"
  :resource-paths ["multilang"]
  :main demo.TopologySubmitter
  :aot [demo.TopologySubmitter]
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[incanter "1.5.1"]]
  :profiles {:dev {:plugins [[lein-midje "3.0.0"]]
                   :dependencies [[storm "0.8.1"]
                                  [org.clojure/clojure "1.4.0"]
                                  ; to placate lighttable
                                  [org.clojure/clojurescript "0.0-2030"]
                                  [antler/commons-io "2.2.0"]
                                  [cheshire "5.2.0"]
                                  ; idiomatic clj for lighttable
                                  [lein-kibit "0.0.8"]
                                  [midje "1.6.0"]]}})
