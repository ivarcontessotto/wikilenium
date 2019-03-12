# wikilenium
Project for automated tests with selenium.

Consists of the following parts:
- GameClient: A class wrapping the selenium web driver, that can be conveniently set up to play the wiki game.
- GameClientTests: Tests to assert the GameClient does what it is supposed to do.
- ShowcaseTest: Demonstrates automated wiki game tests using the GameClient.

Currently the tests are setup to run with google chrome. To run it you'll need:
- Google Chrome Browser
- Chrome Driver matching your browser version: https://sites.google.com/a/chromium.org/chromedriver/downloads
- The driver needs to be put at 'C:\Program Files (x86)\WebDrivers'. If you chose another location you'll have to adapt the path in the test setup code.

If you want to use another browser you'll have to download the driver for that browser, 
add a dependency to the selenium webdriver for that browser
and pass the web driver to the GameClient.
