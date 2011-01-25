(ns VS.turniermodes
  (use [VS.executors])
  (use [clojure.pprint]))

(defn fmap [f coll]
  (into {} (map (fn [[k v]] [k (f v)]) coll)))

(defn one? [n] (= n 1))

;;;;;;;;;;;;;;;all vs all ;;;;;;;;;;;;;;;;;
(defn allvsall-parties
  "playes all players against each other"
  [coll]
  (loop [coll coll output []]
    (if (nil? coll)
      output
      (recur (next coll)
             (into output
                   (vec (map (fn [_] [(first coll) _]) coll)))))))

(defn sort-by-wincount
  "Returnes the list of all the players"
  [coll]
  (map first (sort-by second (fmap count (group-by identity coll)))))

(defn allvsall
  "Sends the pairs to the executers"
  [executer players vs-fn]
  (let [played-games (executer vs-fn (allvsall-parties players))]
    (sort-by-wincount played-games)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
