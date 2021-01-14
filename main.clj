(ns main
  (:require [hiccup.core :refer [html]]))


(defn get-time-string [timestamp]
  (let [minute (quot timestamp 60)
        seconds (rem timestamp 60)]
    (str minute ":"
         (if (< seconds 10)
           (str "0" seconds)
           seconds))))

(defn get-timestamp-position [min max timestamp]
  (* 100 (/ (- timestamp min)
            (- max min))))

(defn timestamps->time-list [timestamps]
  (let [min* (apply min timestamps)
        max* (apply max timestamps)]
    (map (fn [timestamp]
           {:time (get-time-string timestamp)
            :position (get-timestamp-position min* max* timestamp)})
         timestamps)))

(defn make-text-and-line
  [i {:keys [time position]}]
  (let [last-i? (> i 23)
        ;; so that the lines do not overflow the viewBox
        pre-x (cond (zero? i) (+ position 0.2)
                    last-i? (- position 0.2)
                    :else position)
        x (str (float pre-x) "%")

        ;; so that numbers can be large and fit the grid
        y (if (even? i) 2 4.5)

        ;; translate the time text by the length of the
        ;; time string i.e. 10:00 is length 5, so it
        ;; translates by -2.8
        middle-translate (if (= 4 (count time))
                           -1.7 -2.9)
        last-translate (if (= 4 (count time))
                         -4.6 -5.6)
        ;; generate the translations so that
        ;; the numbers align well
        text-transform
        (cond (zero? i) "translate(-0.3 0)"
              last-i? (format "translate(%s 0)"
                              last-translate)
              :else (format "translate(%s 0)"
                            middle-translate))]
    ;; The html for the line and text
    [:g
     [:text {:x x
             :y y
             :stroke-width 0.01
             :fill "#000"
             :font-size "0.15em"
             :font-family "monospace"
             :transform text-transform}
      time]
     [:line {:x1 x :x2 x
             :y1 (+ y 0.5) :y2 77
             :stroke "#000"             ; grid line color
             :stroke-dasharray "0.5, 0.1" ; the dotted line `length,space`
             :stroke-width 0.1}]]))

(defn as-svg-string [time-list]
  [:svg {:fill "none"
         :viewBox "0 0 110 77" ;; doble carta
         :xmlns "http://www.w3.org/2000/svg"}
   (map-indexed make-text-and-line time-list)])

(defn start-minute->timestamps [minute]
  (let [minute-as-seconds (* minute 60)]
    (range minute-as-seconds
           (+ minute-as-seconds 125)
           5)))

(defn make-svg [start-minute]
  (let [timestamps (start-minute->timestamps start-minute)
        svg (->> timestamps timestamps->time-list as-svg-string html)
        filename (format "output/%s-%s.svg" start-minute (+ 2 start-minute))]
    (spit filename svg)))

(comment
  (make-svg 0)
  (make-svg 2)
  (make-svg 4)
  (make-svg 6)
  (make-svg 8)
  (make-svg 10)
  (make-svg 12)
  (make-svg 14))
