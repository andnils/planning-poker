(ns build
  (:require [clojure.tools.build.api :as b]))

(def project-name "planning-poker")
(def version (format "1.2.%s" (b/git-count-revs nil)))
(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn"}))
(def uber-file (format "target/%s-%s-standalone.jar" project-name version))

(defn clean [_]
  (b/delete {:path "target"}))

(defn ui [_]
  (println "build ui")
  (b/delete {:path "ui/dist"})
  (b/process {:command-args ["/usr/local/bin/npm" "ci"]
              :dir "ui"})
  (b/process {:command-args ["/usr/local/bin/npm" "run" "prod"]
              :Xdir "/home/andnils/Projects/planning-poker/ui/"
              :dir (str (b/resolve-path "ui"))
              :out :write
              :out-file "out.log"
              :err :write
              :err-file "err.log"})
  )

(defn uber [_]
  (println "build uber")
  (clean nil)
  (b/copy-dir {:src-dirs ["src" "resources"]
               :target-dir class-dir})
  (b/compile-clj {:basis basis
                  :src-dirs ["src"]
                  :class-dir class-dir})
  (b/uber {:class-dir class-dir
           :uber-file uber-file
           :basis basis
           :main 'planning-poker.main}))

(defn all [_]
  (ui nil)
  (uber nil))

(comment
  (uber nil)
  (ui nil)
  b/*project-root*
  )