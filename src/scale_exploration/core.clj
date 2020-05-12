(ns scale-exploration.core
  (:require [conexp.base :refer :all]
            [conexp.fca.contexts :refer :all]
            [conexp.io.contexts :refer :all]
            [conexp.fca.smeasure :refer :all]
            [clojure.java.io :as io])
  (:gen-class))

(defn- get-context 
  "I/O to read in a context."
  []
  (let [path (ask "\n Please input the path to your formal context:"
                  #(str (read-line))
                  #(.exists (io/file %))
                  "\n Please enter a valid file path:")
        ctx (try (read-context path)
                 (catch Exception e
                   (println "\n Not a valid conexp-clj format for formal contexts. Some valid formats are Burmeister or CSV:")))]
    (if (context? ctx)
      ctx
      (get-context))))

(defn- dump-context 
  "I/O to dump the explored context."
  [ctx]
  (let [path (ask "\n Please input file name to write your explored scale:"
                   #(str (read-line))
                   (constantly true)
                   "")]
    (try (write-context ctx path)
         (catch Exception e 
           (println (str "Exception:" (.getMessage e)))
           (dump-context ctx)))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (-> (get-context)
      scale-exploration
      scale
      dump-context))
