(ns demo.bolts.monitor
  (:require [backtype.storm.clojure :refer [emit-bolt! defbolt ack! bolt]]
            [clojure.tools.logging :as log])
  (:gen-class))

(defbolt set-alarms ["time" "id" "celsius"] [tuple collector]
  (do
    (let [[timestamp id celsius] tuple]
      (when (> celsius 22)
        ; one would do something like set a flag in a DB or call an alert service/etc
        (log/error (str "ALARM STATE!!! time: " timestamp " celsius: " celsius)))
      (ack! collector tuple))))

(defbolt running-average ["time" "id" "celsius"] [tuple collector]
  (do
    (let [[timestamp id celsius] tuple]
      (ack! collector tuple))))
