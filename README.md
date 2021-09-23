# rpgmgr

A reference implementation of Spring 5 WebFlux ReST controllers. Imagine this small microservice fulfills the requirements of users who are "game masters", folks who organize and schedule table top role playing games such as Dungeons&Dragons.

The code includes controllers for three domains: 
 * Players--humans who participate in a game along with the game master.
 * Characters--fictional characters created by Players during the course of a game.
 * Campaigns--games that continue over the course of months.  

The code is packaged by feature/domain and not by layer/responsibility. 
 * There are no dao or controller packages for example. 
 * All code supporting a feature is grouped together. 
 * Exceptions are shared code, which can be placed in their own packages. See the two exception and domain packages, for example.

## Highlights
Reactive programming examples for happy path and exception handling.

Reactive MongoDB repositories for asynchronous database calls. No more blocking threads! 

Custom exception handling classes streamline responses to the client for easy communication of validation errors.

Unit tests execute independently of the Spring application context, significantly reducing build time.

Integration tests use Cucumber BDD framework and load the application context only once, again reducing build time.


