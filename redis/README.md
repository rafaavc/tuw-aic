# Redis

## Useful parameters in redis.conf

> All parameters are self-documented in the redis.conf file

- ***loglevel*** - self-explanatory
- ***save*** - how often to save the DB to persistent memory (as of 6 dec it is being saved, but this can be disabled)
- ***dbfilename*** - where the DB is dumped to
- ***port*** - where redis accepts connections (port **6379** by default)
- ***protected-mode*** - disabled it, this is no problem because the redis port isn't exposed
- ***bind*** - commented it so that redis can be accessed through any interface of the container, again no problem because the redis port isn't exposed

