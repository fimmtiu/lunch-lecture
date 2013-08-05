(ns blackjack.player
  (:use blackjack.deck)
  (:use [clojure.string :only [lower-case]])
  (:use clojure.math.combinatorics))

(defrecord Player [name type status hand])

(defn make-player [name type hand]
  (->Player name type :alive hand))

(defn player-alive? [player]
  (= :alive (:status player)))

(defn take-another-card? [player]
  (print "Do you want another card? > ")
  (flush)
  (let [input (read-line)]
    (= "y" (lower-case (first input)))))

(defn- get-card-values [card]
  (let [val (second card)]
    (cond
     (= val 1)  [1 11]
     (> val 10) [10]
     :else      [val])))

;; You can test this on the REPL with:
;;    (def p (->Player "some guy" :hi ['(:spades 1) '(:hearts 12)]))

(defn hand-values [player]
  (map #(reduce + %1) (apply cartesian-product (map get-card-values (:hand player)))))

(defn best-hand-value [player]
  (apply max (filter #(<= %1 21) (hand-values player))))

(defn take-player-turn [player]
  (println (:name player) "'s turn.")
  (println "Your hand:" (:hand player))
  (if (take-another-card? player)
    (let* [player (assoc player :hand (conj (:hand player) (draw-card)))
           status (if (<= (apply min (hand-values player)) 21) :alive :dead)]
      (println "You drew this card:" (peek (:hand player)))
      (if (= status :dead)
        (println "You're totally busted."))
      (assoc player :status status))
    player))