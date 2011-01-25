(ns VS.example
  (use [VS.core])
  (use [VS.turniermodes])
  (use [VS.executors]))

;;;;;;;;;,,, Lets play out a game ;;;;;;;;;;;;;;;;

(defn player [a h name]
  {:attack a :hp h :name name})

(def player1 (player 30 460 "hero"))
(def player2 (player 15 1000 "monster"))
(def player3 (player 1 10 "noob"))

(playitout [player1 player2 player3]
           (fn [p1 p2] (if (> (inc (inc (mod (:hp p2) (:attack p1))))
                             (inc (inc (mod (:hp p1) (:attack p2)))))
                        p2
                        p1))
           allvsall
           parallel)
