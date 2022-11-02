(ns planning-poker.routes
  (:require [ataraxy.core :as ataraxy]))




(def routes '{"/foo" [:foo]})

(defn foo [request]
  {:status 200, :headers {}, :body "Foo"})

(defn handler [db]
  (ataraxy/handler
   {:routes  routes
    :handlers {:foo foo}}))

