(set-env!
  :source-paths #{"/src"}
  :dependencies '[[pandeiro/boot-http "0.8.3" :scope "test"]

                  [com.h2database/h2 "1.4.193"]
                  [compojure "1.6.1"]
                  [org.clojure/clojure "1.9.0"]
                  [org.clojure/java.jdbc "0.6.0"]
                  [org.clojure/tools.logging "0.4.1"]
                  [ring/ring-defaults "0.3.2"]

                  [org.clojure/core.async "0.4.474"]])

(require
  '[pandeiro.boot-http :refer [serve]])

(task-options!
  serve {:httpkit true
         :dir     "target"
         :port    8000})

(deftask start []
         (comp
           (serve :handler 'passport.tree.server.core/app :reload true)
           (wait)))
