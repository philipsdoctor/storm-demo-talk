(ns demo.spouts.sensor-spout
  (:import [java.util.Date])
  (:require [incanter.distributions :refer [normal-distribution draw]]
            [backtype.storm.clojure :refer [defspout spout emit-spout!]])
  (:gen-class))

(defspout sensor-readings {"temp" ["time" "id" "reading"]
                           "pressure" ["time" "id" "reading"]}
  [conf context collector]
  (let [temp-ids [["temp-1" (normal-distribution 70 1)]
                  ["temp-2" (normal-distribution 69.8 1.1)]
                  ["temp-3" (normal-distribution 70 9)]
                  ["temp-4" (normal-distribution 72.1 0.221)]]
        pressure-ids [["pressure-1" (normal-distribution 0.98 0.001)]
                      ["pressure-2" (normal-distribution 1.0 0.05)]]]
    (spout
     (nextTuple []
                (Thread/sleep 500)
                (if (> (rand-int 2) 0)
                  (let [[next-id next-dist] (rand-nth temp-ids)]
                    (emit-spout! collector [(new java.util.Date) next-id (draw next-dist)] :stream "temp"))
                  (let [[next-id next-dist] (rand-nth pressure-ids)]
                    (emit-spout! collector [(new java.util.Date) next-id (draw next-dist)] :stream "pressure")
                    ))))))
