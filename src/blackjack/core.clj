(ns blackjack.core
  (:use blackjack.game)
  (:gen-class))

(defn -main [& args]
  (let [game (make-game)]
    (while (not (winner game))
      (play-round game))
    (println "Game over! " (winner game) " wins.")))
