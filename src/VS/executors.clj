(ns VS.executors
  (use [VS.net_eval])
  (use [clojure.pprint]))

(def nodes (cycle [["127.0.0.1" 9999] 
                   ["192.168.1.34" 9999]]))

(defn combine-task
  "Makes one task out of the parms"
  [node vs-fn pair]
  [(first node) (second node) vs-fn pair])

(defn get-task-vector
  "Returnes all the task (put the retern into net-eval)"
  [vs-fn player-pairs]
  (vec (map combine-task nodes (repeat vs-fn) player-pairs)))

(defn seriel [vs-fn players]
  (map #(apply vs-fn %) players))

(defn parallel [vs-fn players]
  (pmap #(apply vs-fn %) players))

(defn distributed [vs-fn player-pairs]
  (let [response (net-eval (get-task-vector vs-fn player-pairs))]
    (map deref response)))






