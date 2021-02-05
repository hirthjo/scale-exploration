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
    (try (write-context :burmeister ctx path)
         (catch Exception e 
           (println (str "Exception:" (.getMessage e)))
           (dump-context ctx)))))

(defn- post-processing
  "Determine optional normalforms for your explored scale."
  [sm]
  (let [canonical? (= "yes" (ask "\n Do you want to convert your scale into canonical representation? (yes or no):"
                                         #(str (read-line))
                                         #(or (= "no" %) (= "yes" %))
                                         "\n Enter yes or no:"))
        conjunctive? (= "yes" (ask "\n Do you want to convert your scale into conjunctive normalform? (yes or no):"
                                           #(str (read-line))
                                           #(or (= "no" %) (= "yes" %))
                                           "\n Enter yes or no:"))
        reduce?  (= "yes" (ask "\n Do you want to reduce the set of attributes to meet-irreducibles only? (yes or no):"
                                      #(str (read-line))
                                      #(or (= "no" %) (= "yes" %))
                                      "\n Enter yes or no:"))
        make-representation (if conjunctive?
                              conjunctive-normalform-smeasure-representation
                              (if canonical?
                                canonical-smeasure-representation 
                                identity))
        reduce (if reduce? 
                 meet-irreducibles-only-smeasure 
                 identity)] 
    (-> sm make-representation reduce)))

(defn -main
  "This is a wrapper project for the exploration of scales with I/O."
  [& args]
  (-> (get-context)
      exploration-of-scales
      post-processing
      scale
      dump-context))
