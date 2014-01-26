(ns demo.python-topology
  (:require [demo.spouts.sensor-spout :refer [sensor-readings]]
            [demo.bolts.db-bolts :refer [store]]
            [demo.bolts.conversion :refer [convert-to-celsius convert-atp-hg]]
            [backtype.storm [clojure :refer [topology spout-spec bolt-spec shell-bolt-spec]] [config :refer :all]]
            [cheshire.core :refer [generate-string]]
            [hiccup.core :refer [html]])
  (:import [backtype.storm LocalCluster])
  (:gen-class))

(def demo-spout-map
  {"sensor-spout" (spout-spec sensor-readings)}) ; {"temp" ["time" "id" "reading"]
                                                 ; "pressure" ["time" "id" "reading"]}

(def demo-bolt-map
  {"convert-to-celsius" (bolt-spec {["sensor-spout" "temp"] :shuffle}
                                   convert-to-celsius :p 2) ; ["id" "celsius"]
   "convert-atp-hg" (bolt-spec {["sensor-spout" "pressure"] :shuffle}
                               convert-atp-hg :p 2) ; ["time" "id" "pressure-hg"]
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

; Make Pictures
(defn topology-to-graph
  "Takes a topology specification and converts it to a graph"
  [topology]
  (let [topology (force topology)
        nodes (concat
               (for [[spout-name _] (.get_spouts topology)] {:name spout-name :group "spouts"})
               (for [[bolt-name _] (.get_bolts topology)] {:name bolt-name :group "bolts"}))
        node-idx (into {} (map-indexed #(vector (:name %2) %1) nodes))
        links (flatten
               (for [[bolt-name bolt] (.get_bolts topology)]
                 (for [input (->> bolt .get_common .get_inputs .keySet)]
                   {:source (node-idx (.get_componentId input)), :target (node-idx bolt-name)})))]
    {:nodes nodes, :links links}))

(defn gen-d3!
  "Generate an HTML5 D3.js graph of the Storm Topology"
  []
  (spit "./doc/graph/index.html"
        (html
         [:html
          [:meta {:charset "utf-8"}]
          [:head [:title "Graph of Storm Topology"]
           [:style (slurp "./doc/graph/style.css")]]
          [:body
           [:script {:src "http://d3js.org/d3.v3.min.js"}]
           [:script (str "var graph = " (-> demo-topology
                                            topology-to-graph
                                            (generate-string {:pretty true})) ";\n"
                         (slurp "./doc/graph/graph.js"))]]])))
