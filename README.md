# wikilenium
The wikilenium test-client is an API for the selenium webdriver.
It provides an interface to write automated tests for the wikipedia philosophie game.
It's goal is to make the test setup easy and readable, while also performing a reliable test that follows the rules of the wiki game.

The project consists of the following packages:
- wikilenium.testclient: Contains the test-client and also unit tests to make sure the test-client works as expected.
- wikilenium.example: Contains examples on how to use the test-client for automated testing the wiki game.

All tests are currently setup to run with google chrome browser. To run the tests you'll need:
- Google Chrome Browser
- Chrome Driver matching your browser version: https://sites.google.com/a/chromium.org/chromedriver/downloads
- The driver needs to be put at 'C:\Program Files (x86)\WebDrivers'. If you choose another location you'll have to adapt the path in the test setup code.

If you want to use another browser you'll have to
- download the driver for that browser,
- add a dependency to the selenium webdriver for that browser
- and pass the web driver to the GameClient.
