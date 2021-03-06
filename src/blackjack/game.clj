(ns blackjack.game
  (:use blackjack.player blackjack.deck))

(def players (atom [(make-player "Player 1" :human [(draw-card) (draw-card)])
                    (make-player "Player 2" :ai [(draw-card) (draw-card)])]))

(defn- highest-scoring-players [player-list]
  (let [high-score (last (sort (map best-hand-value player-list)))]
    (filter #(= high-score (best-hand-value %1)) player-list)))

(defn- keep-going? [deck-size]
  (let [live-players (filter player-alive? @players)]
    (condp = (count live-players)
      0  (println "It's a draw! Everyone died.")
      1  (println "The winner is:" (:name (first live-players)))
      (if (= deck-size (cards-left))
        (let [winners (highest-scoring-players live-players)]
          (if (> (count winners) 1)
            (println "It's a draw!")
            (println "The winner is:" (:name (first winners)))))
        true))))

(defn- play-round []
  (let [workers (map take-async-turn @players)]
    (apply await workers)
    (reset! players (map deref workers))))

(defn -main []
  (let [initial-deck-size (cards-left)]
    (play-round)
    (if (keep-going? initial-deck-size)
      (recur)
      (shutdown-agents))))
