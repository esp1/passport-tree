(ns passport.tree.server.websocket
  (:require [clojure.core.async :as async]
            [clojure.edn]
            [clojure.tools.logging :as log]
            [org.httpkit.server :refer [send! with-channel on-close on-receive]]
            [passport.tree.server.state :as state]))

(defonce cmd-ch (async/chan))
(defonce client-ws-channels (atom #{}))

(defn connect! [channel]
  (log/info "channel open")
  (async/put! cmd-ch [:client-connect channel]))

(defn disconnect! [channel status]
  (log/info "channel closed:" status)
  (swap! client-ws-channels #(remove #{channel} %)))

(defn ws-handler [request]
  (let []
    (async/go
      (while true
        (let [[cmd & info] (async/<! cmd-ch)]
          (if (= cmd :client-connect)
            (let [[channel] info]
              (send! channel (pr-str [:initial-state (state/get-state)]))
              (swap! client-ws-channels conj channel))
            (when-let [result (state/change cmd info)]
              (let [result-str (pr-str result)]
                (doseq [channel @client-ws-channels]
                  (send! channel result-str))))))))

    (with-channel request channel
                  (connect! channel)
                  (on-close channel (partial disconnect! channel))
                  (on-receive channel #(async/put! cmd-ch (clojure.edn/read-string %))))))
