(ns dom2edn.core)

(def attributes (js/Function.
                 "element"
                 "  out = []
                 for (var i = 0; i < element.attributes.length; i++) {
                 var x = element.attributes[i]
                 out.push([x.nodeName, x.nodeValue])
                 }
                 return out"))

(defn dom2edn [element]
  (if (.-tagName element)
    (let [
          a (-> element .-tagName .toLowerCase keyword)
          b (into {} (map (fn [[a b]] [(keyword a) b]) (js->clj (attributes element))))
          children (filter identity (map dom2edn (array-seq (.-childNodes element))))
          ]
      (if (not-empty children)
        (vec (list* a b children))
        [a b]))
    (if (.-textContent element)
      (let [
            trimmed (-> element .-textContent .trim)
            ]
        (if (not-empty trimmed) trimmed)))))
