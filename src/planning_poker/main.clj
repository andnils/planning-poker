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
      ;; TODO: behvöver hantera olika typer av msg: vote och reset...räcker det?
      (on-receive channel receive-ws-msg))))


(defroutes app
  (GET "/api/ok" [] (resp/response "OK"))
  (GET "/api/ws/:room/:name" [] handler)
  (resources "/")
  (GET "*" req (assoc-in (ring.util.response/resource-response "/public/index.html") [:headers "Content-Type"] "text/html"))
  ;(not-found "<p>Page not found.</p>")
  )



(defonce server (atom nil))

(defn stop-server []
  (when-not (nil? @server)
    ;; graceful shutdown: wait 100ms for existing requests to be finished
    ;; :timeout is optional, when no timeout, stop immediately
    (@server :timeout 100)
    (reset! server nil)))

(defn start-server
  ([] (start-server 8080))
  ([port]
   (log/info "starting server on port " 8080)
   (reset! server (run-server app {:port port}))))

(defn -main [& args]
  (start-server))




(comment
  (stop-server)
  (start-server)



  (handle-message {:type "vote" :name "s" :vote 1 :room "abc123"})
  (handle-message {:type "reset" :room "abc123"})

  (keys(get  @appstate "abc123"))

  
  )



