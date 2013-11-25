(ns demo.bolts.db-bolts
  (:require [backtype.storm.clojure :refer [emit-bolt! defbolt ack! bolt]]
            [clojure.tools.logging :as log])
  (:gen-class))


(defbolt store ["request"] [tuple collector]
  (do
    (log/debug "Storing data")
    (emit-bolt! collector tuple :anchor tuple)
    (ack! collector tuple)))
