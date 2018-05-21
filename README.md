ETA:
The Spring Boot project skeleton was ready before the test.
Design - 2h.
Implementation - 4h.
Testing  - 8h.

## Working assumption:
Given:
1) big quantity of documents up to 1 mb in size
2) relatively low quantity of tokens for search

### The application was tested on OS Ubuntu 16###

FAQ

# How to build?
mvn clean install

# How to deploy?
1. Build the project
2. Copy /search-engine/src/main/staging to some place on your machine
3. Copy /search-engine/target/search-engine-1.0.0.jar to <your path>/staging/search-engine
4. Make the shell script executable: chmod +x <your path>/staging/search-engine/start.sh

# Configuration
1. To configure logging find and edit <your path>/staging/search-engine/config/logback-spring.xml
2. To setup a number of concurrently running working threads - open <your path>/staging/search-engine/config/application.properties,
   set 'search.engine.concurrency.level' to an appropriate number ('3' by-default)
3. To setup a token separator - open open <your path>/staging/search-engine/config/application.properties,
   set 'search.engine.token.separator' to an appropriate separator (',' by-default)


# Start the application
go to <your path>/staging/search-engine
run ./start.sh
the application will be run on a port 8080

## Manual
# Open the home page
http://localhost:8080

# Upload a new document
Paste content of a new document in the top text area and hit 'Upload document'
As a response you will get the assigned document ID that can be found in 'Document ID' text field.

# Load a document by the ID
Enter the document ID in 'Document ID' text field and click 'Load document'.

# Search document IDs containing all the tokens
Enter tokens in the bottom text area (e.g. token1,token2,token3) separated by the specified separator without spaces in
between. Hit the button 'Find documents'

# To see the currently applied configuration, check: http://localhost:8080/configuration

