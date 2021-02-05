(defproject scale-exploration "1.0-SNAPSHOT"
  :description "This is a wrapper project for the exploration of scales algorithm."
  :url "https://github.com/hirthjo/scale-exploration"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [conexp-clj "2.2.1-smeasure"]]
  :main ^:skip-aot scale-exploration.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
