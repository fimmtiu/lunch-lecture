(ns blackjack.deck
  (:use clojure.math.combinatorics))

;; REPL demo
;; Do (use 'clojure.math.combinatorics) first

(defn- make-deck []
  (let [suits [:spades :hearts :diamonds :clubs], values (range 1 14)]
    (shuffle (cartesian-product suits values))))

(def deck (atom (make-deck)))

(defn draw-card []
  (let [card (peek @deck)]
    (reset! deck (pop @deck))
    card))

(defn cards-left []
  (count @deck))

(defn get-card-values [card]
  (let [val (second card)]
    (cond
     (= val 1)  [1 11]
     (> val 10) [10]
     :else      [val])))
