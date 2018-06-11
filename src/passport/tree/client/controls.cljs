(ns passport.tree.client.controls
  (:require [passport.tree.client.state :as state]
            [reagent.core :as r]))

; Editable Number

(defonce interval (atom nil))

(defn edit-control [cb f]
  (let [hold-fn #(let [edit-fn (fn [] (cb f))
                       i (js/setInterval edit-fn 50)]
                   (edit-fn)
                   (reset! interval i))
        cancel-fn #(do
                     (js/clearInterval @interval)
                     (reset! interval nil))]
    [:a.control {:class          (condp = f
                                   inc "plus"
                                   dec "minus")
                 :on-mouse-down  hold-fn
                 :on-mouse-up    cancel-fn
                 :on-mouse-leave cancel-fn}
     [:span.fa {:class (condp = f
                         inc "fa-plus"
                         dec "fa-minus")}]]))

(defn editable-number [n cb]
  [:span.editable-number
   [edit-control cb inc]
   [edit-control cb dec]
   n])


; Factory Toolbar

(defonce child-gen-atom (r/atom {:count 3
                                 :range [1 100]}))

(defn factory-toolbar [id]
  [:div.toolbar
   [:a.button {:on-click #(state/generate-children id @child-gen-atom)}
    [:span.fa.fa-sync]]
   (let [{child-count           :count
          [child-min child-max] :range} @child-gen-atom]
     [:span.hidden
      [:span {:style {:margin-left "0.5em"}} "Generate"]
      [editable-number child-count (fn [f] (swap! child-gen-atom update-in [:count] #(max 1 (min 15 (f %)))))]
      [:span {:style {:margin-right "0.5em"}} "Numbers"]
      "("
      [editable-number child-min (fn [f] (swap! child-gen-atom update-in [:range 0] #(min child-max (f %))))]
      "-"
      [editable-number child-max (fn [f] (swap! child-gen-atom update-in [:range 1] #(max child-min (f %))))]
      ")"])])


; Folder Toggle

(defn folder-toggle [open-atom content]
  (let [open? @open-atom]
    [:a {:on-click #(reset! open-atom (not open?))}
     (if open?
       [:span.folder-toggle.fa.fa-folder-open]
       [:span.folder-toggle.fa.fa-folder])
     content]))
