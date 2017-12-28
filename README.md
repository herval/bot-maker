# Slack Relay

A [Finagle](https://github.com/twitter/finagle)-based Slack bot framework for easily pluggable commands

**For an example bot you can quickly build and deploy, check out the [sample project](https://github.com/herval/slack-relay-sample)!**

## Why?
Slack bots aren't just yet another form of IRC bot. Slack's API supports a large number of special kinds of interactions, from interactive buttons to special commands.

At the same time:

- Building on top of Slack's rich interactivity APIs is time consuming (which leads people to simply fall back to old IRC-style bot-in-a-room-parsing-messages when building their bots)
- Bots have very limited agency when it comes to who they can talk to (for instance, a bot can't join a channel, they must be invited). 
- Deploying a new bot requires a large number of configurations and plumbing
- Let's face it - interacting with a dozen bots and figuring out which ones you actually need is just a lot of unecessary cognitive overhead.


## What?
This project allows you to easily bootstrap a Slack bot and implement all sorts of interactive commands with as few configurations as possible.

Finagle' `Service` architecture allows for easily extending services, so you can, for instance, call existing microservices (or public APIs). In fact, the initial driver to develop this was to build a façade to internal company services - a "company concierge", if you will.
So instead of modifying each service to "support Slack" or deploying a dozen different bots, you build _one bot_, wire it to all your services, and configure it _only once_.


## Example uses
- Poll the internal invoices API every 10 minutes and notify the #finances channel every time there's a new invoice
- Publish monitoring alerts to a channel and offer the option to "snooze" as a big red button - then handle the "snooze" click
- Ping an internal machine (inside the VPS) with a `/ping` command you define
- Post a message from an existing system into a channel or as a DM with a simple http POST
- Listen to multiple channels for a specific keyword and replying


## How
- Import this library into your bot project
- Make your main class extend `hervalicious.slackrelay.SlackBot`
- Implementing your commands
- Configure your Slack credentials
- Profit 🚀 

### Slash Commands
TO DO

### Webhooks
TO DO

### Interactive Components
TO DO
