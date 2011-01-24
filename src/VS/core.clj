(ns VS.core
  (use [VS.executors])
  (use [VS.turniermodes])
  (use [clojure.pprint]))


(defn playitout [players vs-fn turniertype executer & top]
  (if (seq top)
    (take (first top) (turniertype executer players vs-fn))
    (turniertype executer players vs-fn)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Example usecase

(defmulti  point-system (fn [x y] [x y]))
(defmethod point-system [:cooperate :cooperate] [x y] 3)
(defmethod point-system [:defact :defact]       [x y] 1)
(defmethod point-system [:defact :cooperate]    [x y] 5)
(defmethod point-system [:cooperate :defact]    [x y] 0)

(defn points
  "Calculates and ads the point to the object"
  [player opponent]
  (update-in player [:score] +
             (point-system (first (:decisions player))
                           (first (:decisions opponent)))))

(declare tft-move ftft-move decison-cons)

(defn randomnum
  "returnes random 0-100"[n]
  (< (rand-int 100) n))

(defprotocol Player
  (choose  [player opponent]))

(defrecord nice [score decisions name]
  Player
  (choose [this opp] (decison-cons this :cooperate)))

(defrecord asshole [score decisions name]
  Player
  (choose [this opp] (decison-cons this :defact)))

(defrecord randomplayer [score decisions name]
  Player
  (choose [this opp] (decison-cons this
                                   (rand-nth [:defact :cooperate]))))

(defrecord titfortat [score decisions name]
  Player
  (choose [this opp]
          (decison-cons this
                        (tft-move decisions (:decisions opp)))))

(defrecord forgiving-titfortat [score decisions name]
  Player
  (choose [this opp]
          (decison-cons this
                        (ftft-move decisions (:decisions opp)))))

(defn ftft-move
  "calculates the forgiving players move"
  [my-dec opp-dec]
  (let [dec  (tft-move my-dec opp-dec)]
    (if (= dec :defact)
      (if (randomnum 5) :cooperate :defact)
      :cooperate)))

(defn tft-move
  "calculate a titfortats players move"
  [my-dec opp-dec]
  (if (empty? opp-dec)
    :cooperate
    (if (= :cooperate (first opp-dec))
      :cooperate
      :defact)))

(defn decison-cons
"Adds a decision to a player"
  [player decision]
  (assoc player :decisions
         (vec (cons decision (:decisions player)))))

(defn game
"Takes 2 players and how many roundes the play"
[rounds p1 p2]
  (if (pos? rounds)
    (let [p1-c (choose p1 p2) p2-c (choose p2 p1)]
      (recur
       (dec rounds)
       (points p1-c p2-c)
       (points p2-c p1-c)))
    (if (> (:score p1) (:score p2))
      p1
      p2)))

(def game-with-10-rounds (partial game 10))

;;;;;;;;;,,, Lets play out a game ;;;;;;;;;;;;;;;;
;;;;; Plyers
(def players [(nice. 0 [] "nice")
              (asshole. 0 [] "asshole")
              (titfortat. 0 [] "titfortat")
              (randomplayer. 0 [] "randomplayer")])

(def seriel-winner   (playitout players game-with-10-rounds allvsall serial 1))
(def parallel-winner (playitout players game-with-10-rounds allvsall parallel 1))
#_(def parallel-winner (playitout players game-with-10-rounds allvsall distributed 1))
