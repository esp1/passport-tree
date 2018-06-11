(ns passport.tree.server.core
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :as route]
            [passport.tree.server.websocket :as ws]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.util.response :as response]))

(defroutes app-routes
           (GET "/" [] (-> (response/file-response "index.html" {:root "target"})
                           (response/content-type "text/html")))
           (GET "/ws" request (ws/ws-handler request))
           (route/files "/" {:root "target"})
           (route/not-found "Not Found"))

(def app
  (-> app-routes
      #_(wrap-defaults site-defaults)))
