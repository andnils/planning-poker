# Build uberjar:

    clojure -T:build all

# Run the jar

    java -Dpoker.port=6598 -jar target/planning-poker-standalone.jar
