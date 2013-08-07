(ns blackjack.player
  (:use blackjack.deck)
  (:use [clojure.string :only [lower-case]])
  (:use clojure.math.combinatorics))

(defrecord Player [name type status hand])

(defn make-player [name type hand]
  (->Player name type :alive hand))

(defn player-alive? [player]
  (= :alive (:status player)))

(defn hand-values [player]
  (map #(reduce + %1) (apply cartesian-product (map get-card-values (:hand player)))))

(defn best-hand-value [player]
  (apply max (filter #(<= %1 21) (hand-values player))))

(defn- take-another-card? [player]
  (print "Do you want another card? > ")
  (flush)
  (let [input (read-line)]
    (if (= 0 (count input))
      (recur player)
      (= "y" (lower-case (first input))))))

(defn take-player-turn [player]
  (println (:name player) "'s turn.")
  (println "Hand:" (:hand player))
  (if (take-another-card? player)
    (let* [player (assoc player :hand (conj (:hand player) (draw-card)))
           status (if (<= (apply min (hand-values player)) 21) :alive :dead)]
      (println (:name player) "drew this card:" (peek (:hand player)))
      (if (= status :dead)
        (println (:name player) "is totally busted."))
      (assoc player :status status))
    player))
