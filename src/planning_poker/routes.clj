(ns planning-poker.routes
  (:require [ataraxy.core :as ataraxy]))


"
 POST /newboard
 -> /board/xyz678
 POST /board/xyz678/newuser?name=nisse
 
"

(def routes '{"/foo" [:foo]})

(defn foo [request]
  {:status 200, :headers {}, :body "Foo"})

(defn handler [db]
  (ataraxy/handler
   {:routes  routes
    :handlers {:foo foo}}))

