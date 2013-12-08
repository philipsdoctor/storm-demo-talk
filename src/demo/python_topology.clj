(ns demo.python-topology
  (:require [demo.spouts.sensor-spout :refer [sensor-readings]]
            [demo.bolts.db-bolts :refer [store]]
            [demo.bolts.conversion :refer [convert-to-celsius convert-atp-hg]]
            [backtype.storm [clojure :refer [topology spout-spec bolt-spec shell-bolt-spec]] [config :refer :all]])
  (:import [backtype.storm LocalCluster])
  (:gen-class))

(def demo-spout-map
  {"sensor-spout" (spout-spec sensor-readings)})

(def demo-bolt-map
  {"convert-to-celsius" (bolt-spec {["sensor-spout" "temp"] :shuffle} convert-to-celsius :p 2)
   "convert-atp-hg" (bolt-spec {["sensor-spout" "pressure"] :shuffle} convert-atp-hg :p 2)
   "set-alarms" (shell-bolt-spec
                 {"convert-to-celsius" :shuffle}
                 "python"
                 "set_alarms.py"
                 ["id" "celsius"]
                 :p 5)
   "running-average" (shell-bolt-spec
                      {"convert-to-celsius" ["id"]}
                      "python"
                      "running_average.py"
                      ["id" "average"]
                      :p 5)
   "store" (bolt-spec {"convert-to-celsius" :shuffle} store :p 2)
   })

(def demo-topology
  (topology
   demo-spout-map
   demo-bolt-map))

; debugging code

(defn run! [& {debug "debug" workers "workers" :or {debug "true" workers "2"}}]
  (doto (LocalCluster.)
    (.submitTopology "demo topology"
                     {TOPOLOGY-DEBUG (Boolean/parseBoolean debug)
                      TOPOLOGY-WORKERS (Integer/parseInt workers)
                      TOPOLOGY-MAX-SPOUT-PENDING 200}
                      demo-topology)))
