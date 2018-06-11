(ns passport.tree.client.websocket
  (:require [cljs.reader]))

(defonce ws-chan (atom nil))

(defn receive-msg!
  [update-fn]
  (fn [msg]
    (update-fn
      (cljs.reader/read-string (.-data msg)))))

(defn send-msg!
  [msg]
  (if @ws-chan
    (.send @ws-chan (pr-str msg))
    (throw (js/Error. "Websocket is not available!"))))

(defn make-websocket! [url receive-handler]
  (println "attempting to connect websocket")
  (if-let [chan (js/WebSocket. url)]
    (do
      (set! (.-onmessage chan) (receive-msg! receive-handler))
      (reset! ws-chan chan)
      (println "Websocket connection established with: " url))
    (throw (js/Error. "Websocket connection failed!"))))
