(ns demo.TopologySubmitter
  (:require [demo.python-topology :refer [demo-topology]]
            [backtype.storm [config :refer :all]])
  (:import [backtype.storm StormSubmitter])
  (:gen-class))

(defn -main [& {debug "debug" workers "workers" :or {debug "false" workers "4"}}]
  (StormSubmitter/submitTopology
   "Demo Topology"
   {TOPOLOGY-DEBUG (Boolean/parseBoolean debug)
    TOPOLOGY-WORKERS (Integer/parseInt workers)
    TOPOLOGY-MAX-SPOUT-PENDING 200}
   demo-topology))
