(ns planning-poker.config
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]))



(defn read-config [config-file]
  (-> config-file
      (io/resource)
      (slurp)
      (edn/read-string)))

(comment
  (edn/read-string (slurp (io/resource "config.edn")))
  )
