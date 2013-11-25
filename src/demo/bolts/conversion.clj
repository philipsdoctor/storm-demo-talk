(ns demo.bolts.conversion
  (:require [backtype.storm.clojure :refer [emit-bolt! defbolt ack! bolt]]
            [clojure.tools.logging :as log])
  (:gen-class))

(defbolt convert-to-celsius ["time" "id" "celsius"] [tuple collector]
  (do

      (let [[timestamp id fahrenheit] tuple
            celsius (* (- fahrenheit 32) (/ 5.0 9.0))]
        (log/debug (str "time: " timestamp " id: " id " fahrenheit: " fahrenheit))

    (ack! collector tuple))))

(defbolt convert-atp-hg ["time" "id" "pressure-hg"] [tuple collector]
  (do
    (let [[timestamp id atm] tuple
          mmhg (* atm 760)]
      (log/debug (str "time: " timestamp " id: " id " mmhg: " mmhg)))
    (ack! collector tuple)))

