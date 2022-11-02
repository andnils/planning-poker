(ns planning-poker.components.http-kit
  (:require [org.httpkit.server :refer [run-server]]
            [com.stuartsierra.component :as component]
            [planning-poker.routes :as routes]
            [clojure.tools.logging :as log]))



(defrecord HttpKitServer [config db]
  component/Lifecycle

  (start [component]
    (log/debug "Http-kit got config: " config)
    (if (:server component)
      component
      (let [port (get-in component [:config :port])
            options {:port port}
            handler (routes/handler (:db component))
            server  (run-server (fn [req] (handler req)) options)]
        (log/info "Starting http server on port" port)
        (assoc component :server server))))
  
  (stop [component]
    (if-let [^Server server (:server component)]
      (do (server :timeout 100)
          (dissoc component :server))
      component)))


(defn http-server [config]
  (map->HttpKitServer {:config config}))


(comment
  (def a-server (http-server {:port "8097"}))
  a-server
  (component/start a-server)
  
  )
