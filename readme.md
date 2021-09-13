Emily Shao <br />
20772922  ye2shao <br />
openjdk version "16.0.1" 2021-04-20 <br />
macOS 11.2.3 (MacBook Pro 2017)

Game Scoring System <br />
- All aliens (regardless of colour or level) are worth 5 points

Alien Movement <br />
- The base speed for the aliens at each level is `level * 0.5`
- After each alien killed, the speed of the remaining enemies is increased by `0.03`

Alien Bullets <br />
- Alien bullets have speed of `4.0` throughout the game
- There can be a maximum of `level * 3` alien bullets on the screen at a time
- Alien firing rate per bullet for level 1 is every 60 seconds; level 2 is every 50 seconds; level 3 is every 40 seconds

Spaceship Movement <br />
- Spaceship speed is `4.0` throughout the game
- Players can use the arrow keys `< and >` OR `A and D` to control spaceship

Spaceship bullets <br />
- Spaceship bullets have speed `5.0` throughout the game
- Spaceship can shoot a maximum of 2 bullets every 60 seconds

Game Sound Effects <br />
- The alien movement sound `invader.wav` is played every 20 seconds
- When an alien is killed, `invaderkilled.wav` is played
- When the spaceship has been destroyed or gameover for some other reason, `shipExplosion.wav` is played
- When spaceship is fired, `shoot.wav` is played

Extras <br />
- Players can press `Q` whenever they'd like to exit the game (including during a game)

Attributes <br />
All Assets - http://www.classicgaming.cc/classics/space-invaders/
