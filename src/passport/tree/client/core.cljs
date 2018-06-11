(ns passport.tree.client.core
  (:require [goog.dom]
            [passport.tree.client.state :as state]
            [passport.tree.client.controls :refer [editable-number factory-toolbar folder-toggle]]
            [reagent.core :as r]))

(def factory-edit-atom (r/atom nil))

(defn factory [id factory-info]
  (let [open-atom (r/atom false)]
    (fn [id {:keys [name children]}]
      [:div.factory
       [:div.item
        (if (= id @factory-edit-atom)
          [:span
           [folder-toggle open-atom nil]
           [:input {:type      "text"
                    :value     name
                    :autoFocus true
                    :on-change #(state/set-factory-name id (.-target.value %))
                    :on-key-up #(when (#{"Enter" "Escape"} (.-key %)) (reset! factory-edit-atom nil))}]
           [:a.button {:on-click #(reset! factory-edit-atom nil)} [:span.fa.fa-check]]]
          [:span
           [folder-toggle open-atom name]
           [:a.button.hidden {:on-click #(reset! factory-edit-atom id)} [:span.fa.fa-edit]]
           [:a.button.hidden {:on-click #(state/delete-factory id)} [:span.fa.fa-times]]])]

       (when @open-atom
         [:div.node
          [factory-toolbar id]

          [:div.children
           (for [n (range (count children))]
             [:div.child {:key (str id "-" n)}
              (get children n)])]])])))

(defn root []
  (let [open-atom (r/atom false)]
    (fn []
      [:div.root.node
       [:div.item
        [folder-toggle open-atom "root"]]

       (when @open-atom
         [:div.node
          (for [[id v] @state/factories]
            ^{:key (str "factory-" id)} [factory id v])

          [:div.toolbar
           [:a.button {:on-click state/add-factory} [:span.fa.fa-plus] " New Factory"]]])])))

(defn ^:export init! []
  (state/init!)
  (r/render [root] (goog.dom/getElement "root")))

(init!)
