Dodgeball
=========

This is a plugin witch brings back the Old school Dodgeball to minecraft. There are allowed 12 max players on each side on a 100 block arena. A game is started by either a admin Executing /db start [arena name] or if said amount of players in arena do /db vote. once a arena is started there is a 30 second count down waiting period. You start out with 3 hearts representing 3 lives you have. Once that waiting period is over, One random player on your team or enemy team will get 3 snowballs and try to hit the opposing team. When you run out of lives you go into spectating mode about 4 blocks above the arena and and below the arena roof [you have the option of leaving the game by doing /db leave ] When one team loses all their players they will all go into spectating mode and will see in chat what arena won and will see a spectacular array of fireworks, after about 10 seconds they will be kicked back to world spawn [or you set up a Dodgeball spawn [/db setspawn] they will spawn there.]

Features
--------

Create dodgeball arena automatically - select area with worldedit and use one command to create arena

Starts automatically when there are enough players


Selecting team when joining (permission)
Two teams, colors selected when creating arena

Autosave when disabling plugin.

Commands
--------

/db am /db automake - create arena. 

Usage:/db am team1Color:team2Color name floor_block_id wall_block_id_1 wall_id_2 line_id height 

height - wall blocks below the specified height are floor_id_1, other are wall_id_2, 

permission: db.automake


/db start name- start dodgeball (after 30 seconds), permission: db.start

/db stop name- stop dodgeball (after 10 seconds), permission: db.stop

/db join name - join, permission: db.join

/db leave - leave, permission: db.leave

/db help - help, no permission needed

/db kickplayer - kick player from dodgeball, permission: db.kickplayer

/db list page- list minigames, permissions: db.list

/db spawn /db setspawn - set lobby spawn, permission: db.setspawn

/db spectate - join dodgeball as spectator, permission: db.spectate

/db vote - vote on starting current dodgeball before maximum amount of players joins. permission: db.vote
NOTE
====
This plugin is in Beta stages and still could be buggy, please Create a Ticket if you have any bugs or concerns.

Development builds
------------------
Development builds of this project can be acquired at the provided continuous integration server. These builds have not been approved by the BukkitDev staff. Use them at your own risk.

https://buildhive.cloudbees.com/job/Barteks2x/job/Dodgeball/
