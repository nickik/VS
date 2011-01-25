(ns VS.core
  (use [VS.executors])
  (use [VS.turniermodes])
  (use [clojure.pprint]))


(defn playitout [players vs-fn turniertype executer & top]
  (if (seq top)
    (take (first top) (turniertype executer players vs-fn))
    (turniertype executer players vs-fn)))



