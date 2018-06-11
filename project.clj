(defproject
  boot-project
  "0.0.0-SNAPSHOT"
  :repositories
  [["clojars" {:url "https://repo.clojars.org/"}]
   ["maven-central" {:url "https://repo1.maven.org/maven2"}]]
  :dependencies
  [[adzerk/boot-cljs "2.1.4" :scope "test"]
   [adzerk/boot-reload "0.5.2" :scope "test"]
   [adzerk/boot-cljs-repl "0.3.3" :scope "test"]
   [deraen/boot-sass "0.3.1" :scope "test"]
   [onetom/boot-lein-generate "0.1.3" :scope "test"]
   [pandeiro/boot-http "0.8.3" :scope "test"]
   [com.h2database/h2 "1.4.193"]
   [compojure "1.6.1"]
   [org.clojure/clojure "1.9.0"]
   [org.clojure/java.jdbc "0.6.0"]
   [org.clojure/tools.logging "0.4.1"]
   [ring/ring-defaults "0.3.2"]
   [org.clojure/core.async "0.4.474"]
   [org.clojure/clojurescript "1.10.238"]
   [reagent "0.8.1"]]
  :source-paths
  ["src" "sass"]
  :resource-paths
  ["resources"])