(ns planning-poker.system
  (:gen-class)
  (:require [com.stuartsierra.component :as component]
            [planning-poker.components.http-kit :refer [http-server]]))


(defn make-system
  "Construct the system.
  Either pull the config from the environment, or supply
  a config."  
  [config]
  (let [{:keys [server]} config]
    (component/system-map
     :http (http-server server))))




(defn -main [& args]
  (let [config {:todo "make a config"}
        system (make-system config)]
    (component/start system)))



