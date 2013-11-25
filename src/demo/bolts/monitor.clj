(ns demo.bolts.monitor
  (:require [backtype.storm.clojure :refer [emit-bolt! defbolt ack! bolt]]
            [clojure.tools.logging :as log])
  (:gen-class))

(defbolt set-alarms ["time" "id" "celsius"] [tuple collector]
  (do
    (let [[timestamp id celsius] tuple]
      (ack! collector tuple))))

(defbolt running-average ["time" "id" "celsius"] [tuple collector]
  (do
    (let [[timestamp id celsius] tuple]
      (ack! collector tuple))))
