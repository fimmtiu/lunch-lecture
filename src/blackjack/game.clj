(ns blackjack.game
  (:use blackjack.deck)
  (:gen-class))

(defn- play-round []
  nil)

(defn- keep-going? []
  nil)

(defn -main [& args]
  (while (keep-going?)
    (play-round)))
