(ns passport.tree.server.state
  (:require [clojure.edn]
            [clojure.java.jdbc :as jdbc]))

(def db-spec {:dbtype "h2" :dbname "./passport-tree"})

(def max-id (atom (or (second (ffirst (jdbc/query db-spec "select max(id) from factory"))) 0)))

(defn get-state []
  (into {}
        (for [{:keys [id name children]} (jdbc/query db-spec "select * from factory")]
          [id {:name name, :children (clojure.edn/read-string children)}])))

(defmulti change (fn [cmd _] cmd))

(defmethod change :delete-factory [_ [id]]
  (jdbc/db-do-prepared db-spec ["delete from factory where id = ?" id])
  [:delete-factory id])

(defmethod change :add-factory [_ _]
  (swap! max-id inc)
  (let [id @max-id
        factory {:id id
                 :name (str "Factory " id)}]
    (jdbc/insert! db-spec :factory factory)
    [:add-factory id factory]))

(defmethod change :gen-children [_ [id {:keys [count range]}]]
  (let [[cmin cmax] range
        children (vec (repeatedly count #(int (+ cmin (* (Math/random) (- cmax cmin))))))]
    (jdbc/update! db-spec :factory {:children (pr-str children)} ["id = ?" id])
    [:gen-children id children]))

(defmethod change :set-factory-name [_ [id s]]
  (jdbc/update! db-spec :factory {:name s} ["id = ?" id])
  [:set-factory-name id s])

(defn init []
  (try
    (jdbc/db-do-commands db-spec
                         (jdbc/create-table-ddl :factory
                                                [[:id "bigint primary key"]
                                                 [:name "varchar"]
                                                 [:children "varchar"]]))
    (catch Exception e)))
