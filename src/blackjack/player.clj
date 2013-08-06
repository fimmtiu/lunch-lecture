(ns blackjack.player
  (:use blackjack.deck)
  (:use [clojure.string :only [lower-case]])
  (:use clojure.math.combinatorics))

(defrecord Player [name type status hand agent])

(defn make-player [name type hand]
  (->Player name type :alive hand (agent nil)))

(defn player-alive? [player]
  (= :alive (:status player)))

;; You can test this on the REPL with:
;;    (def p (->Player "some guy" :hi ['(:spades 1) '(:hearts 12)]))

(defn hand-values [player]
  (map #(reduce + %1) (apply cartesian-product (map get-card-values (:hand player)))))

(defn best-hand-value [player]
  (apply max (filter #(<= %1 21) (hand-values player))))

(defmulti take-another-card? #(:type %1))

(defmethod take-another-card? :human [player]
  (print "Do you want another card? > ")
  (flush)
  (let [input (read-line)]
    (if (= 0 (count input))
      (recur player)
      (= "y" (lower-case (first input))))))

(defmethod take-another-card? :ai [player]
  (if (>= (apply max (hand-values player)) 17)
    (do (println (:name player) "doesn't want another card.")
        false)
    (do (println (:name player) "wants another card.")
        true)))

(defn take-player-turn [_ player]
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

(defn take-async-turn [player]
  (send-off (:agent player) take-player-turn player))
