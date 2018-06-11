(ns passport.tree.client.state
  (:require [passport.tree.client.websocket :as ws]
            [reagent.core :as r]))

(def factories (r/atom {}))

(defn delete-factory [id]
  (ws/send-msg! [:delete-factory id]))

(defn add-factory []
  (ws/send-msg! [:add-factory]))

(defn generate-children
  [id child-gen-config]
  (ws/send-msg! [:gen-children id child-gen-config]))

(defn set-factory-name [id s]
  (ws/send-msg! [:set-factory-name id s]))

(defn update! [[action & info]]
  (js/console.log (pr-str action info))
  (condp = action
    :initial-state (let [[initial-state] info]
                     (reset! factories initial-state))
    :add-factory (let [[id factory] info]
                   (swap! factories assoc id factory))
    :delete-factory (let [[id] info]
                      (swap! factories dissoc id))
    :gen-children (let [[id children] info]
                    (swap! factories assoc-in [id :children] children))
    :set-factory-name (let [[id name] info]
                        (swap! factories assoc-in [id :name] name))))

(defn init! []
  (ws/make-websocket! (str "ws://" (.-host js/location) "/ws") update!))
