(ns blackjack.game
  (:use clojure.math.combinatorics))

(defrecord Game [deck players])

;; REPL demo, next 3 expressions
;; Do (use 'clojure.math.combinatorics) first

(def card-suits [:spades :hearts :diamonds :clubs])
(def card-values (range 1 14))

(defn- make-deck []
  (shuffle (cartesian-product card-suits card-values)))

(defn make-game []
  (->Game (make-deck) []))   ;; we'll add players shortly

(defn play-round [game]
  )

;; Change this to "Balls" or something to make sure it works.
(defn winner [game]
  nil)

