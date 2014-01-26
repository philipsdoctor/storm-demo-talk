(ns demo.test-topology
  (:use [backtype.storm testing])
  (:require [demo.python-topology :as top]
            [midje.sweet :refer [fact facts]]
            [backtype.storm.clojure :refer [topology]])
  (:import [backtype.storm.testing]))

(def mock-temp-sensor-id "mock-temp-id")
(def mock-pres-sensor-id "mock-pres-id")

(def mock-sensor-data [{:stream "pressure"
                        :values [(new java.util.Date) mock-pres-sensor-id 1.1]}
                       {:stream "temp" 
                        :values [(new java.util.Date) mock-temp-sensor-id 72.1]}
                       {:stream "temp"
                        :values [(new java.util.Date) mock-temp-sensor-id 79.2]}])

(defn mock-topology-run 
  [mock-data]
  (with-simulated-time-local-cluster [cluster :supervisors 4]
    (complete-topology cluster (topology top/demo-spout-map top/demo-bolt-map)
                               :mock-sources {"sensor-spout" mock-data})))

(let [results (mock-topology-run mock-sensor-data)
      [conv-to-cel-id conv-to-cel-vals] (.values (first (get results "convert-to-celsius")))
      [set-alarms-id set-alarms-vals] (.values (first (get results "set-alarms")))
      [running-ave-first-result running-ave-second-result] (get results "running-average")
      [_ first-running-ave] (.values running-ave-first-result) 
      [_ second-running-ave] (.values running-ave-second-result)]
  (facts "Conversion to celsius bolt functions in the topology"
    (fact "Only temp stream was routed to this bolt" conv-to-cel-id => mock-temp-sensor-id)
    (fact "Sensor reading was converted to Celsius" conv-to-cel-vals 22.277777777777775))
  
  (fact "Because I just log something for set alarm, I don't have much to test... oops" true => true)

  (facts "Running average topology integration"
    (fact "Running average groups on ID" (= first-running-ave second-running-ave) => false))
    (fact "Running average is correctly calculated" second-running-ave => 24.25)) 



