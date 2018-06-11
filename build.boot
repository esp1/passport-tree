(set-env!
  :source-paths #{"src" "sass"}
  :resource-paths #{"resources"}
  :dependencies '[
                  ; boot
                  [adzerk/boot-cljs "2.1.4" :scope "test"]
                  [adzerk/boot-reload "0.5.2" :scope "test"]
                  [adzerk/boot-cljs-repl "0.3.3" :scope "test"]
                  [deraen/boot-sass "0.3.1" :scope "test"]
                  [onetom/boot-lein-generate "0.1.3" :scope "test"]
                  [pandeiro/boot-http "0.8.3" :scope "test"]

                  ; clj
                  [com.h2database/h2 "1.4.193"]
                  [compojure "1.6.1"]
                  [org.clojure/clojure "1.9.0"]
                  [org.clojure/java.jdbc "0.6.0"]
                  [org.clojure/tools.logging "0.4.1"]
                  [ring/ring-defaults "0.3.2"]

                  ; clj/cljs
                  [org.clojure/core.async "0.4.474"]

                  ; cljs
                  [org.clojure/clojurescript "1.10.238"]
                  [reagent "0.8.1"]])

(require '[boot.lein])
(boot.lein/generate)

(require
  '[adzerk.boot-cljs :refer [cljs]]
  '[adzerk.boot-reload :refer [reload]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
  '[deraen.boot-sass :refer [sass]]
  '[pandeiro.boot-http :refer [serve]])

(task-options!
  serve {:httpkit true
         :dir     "target"
         :port    8000})

(deftask build []
         (comp
           (sass)
           (cljs)
           (target)))

(deftask dev []
         (comp
           (serve :handler 'passport.tree.server.core/app :reload true)
           (watch)
           (reload :on-jsload 'passport.tree.client.core/init!)
           (build)))
