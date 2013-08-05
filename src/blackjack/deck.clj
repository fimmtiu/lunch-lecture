(ns blackjack.deck
  (:use clojure.math.combinatorics))

;; REPL demo, next 3 expressions
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
