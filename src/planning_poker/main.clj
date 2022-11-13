(ns planning-poker.main
  (:gen-class)
  (:require [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [org.httpkit.server :refer [on-close on-receive run-server send! with-channel]]
            [ring.util.response :as resp]))


(def appstate (atom {}))



(defn get-room-state [room]
  (json/write-str
   (sort-by :name
            (for [[k v] (get @appstate room)]
              {:name k :vote (:vote v)}))))




(defn notify-clients [room]
  (doseq [[k v] (get @appstate room)]
    (when-let [ch (:channel v)]
      (send! ch (get-room-state room)))))


(defn connect! [channel room name]
  (log/info "channel open" room)
  (swap! appstate update room assoc-in [name :channel] channel)
  (notify-clients room))

(defn disconnect! [channel room name status]
  (log/info "Player" name "left room" room ": " status)
  (swap! appstate update room dissoc name)
  (notify-clients room))


(defmulti handle-message :type)

(defmethod handle-message "reset" [msg]
  (let [room (:room msg)]
    (swap! appstate
           assoc
           room (apply merge (for [[k v] (get @appstate room)] {k (dissoc v :vote)})))
    (notify-clients room)))



(defmethod handle-message "vote" [msg]
  (let [{:keys [room name vote]} msg]
    (swap! appstate update-in [room name] assoc :vote vote)
    (notify-clients room)))

(defmethod handle-message :default [msg]
  (println (type msg)))

(defn receive-ws-msg [msg]
  (->
    (json/read-str msg :key-fn keyword)
    handle-message))

(defn handler [request]
  (let [room (get-in request [:params :room])
        name (get-in request [:params :name])]
    (with-channel request channel
      (connect! channel room name)
      (on-close channel (partial disconnect! channel room name))
      (on-receive channel receive-ws-msg))))


(defroutes app
  (GET "/api/ok" [] (resp/response "OK"))
  (GET "/api/ws/:room/:name" [] handler)
  (resources "/")
  (GET "*" req (assoc-in (ring.util.response/resource-response "/public/index.html") [:headers "Content-Type"] "text/html")))



(defonce server (atom nil))

(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))

(defn start-server
  ([] (start-server 5000))
  ([port]
   (log/info "starting server on port " port)
   (reset! server (run-server app {:port port}))))

(defn -main [& args]
  (let [port (or (System/getProperty "poker.port") "5000")]
    (start-server (Integer/parseInt port))))



(comment
  (stop-server)
  (start-server)
  
  )



