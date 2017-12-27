# Slack Relay Bot

A Finagle-based Slack bot made for easily pluggable commands

**For an example bot you can quickly build and deploy, check out the [sample project](???)!**

## Why?
Slack bots aren't just yet another form of IRC bot. Slack's API supports a large number of special kinds of interactions, from interactive buttons to special commands.

At the same time:

- Bots have very limited agency when it comes to who they can talk to (for instance, a bot can't join a channel, they must be invited). 
- Deploying a new bot requires a large number of configurations and plumbing
- Let's face it - interacting with a dozen bots and figuring out which ones you actually need is just a lot of unecessary cognitive overhead.


## What?
This project allows you to bootstrap a Slack bot that serves as a [_façade_](https://en.wikipedia.org/wiki/Facade_pattern) to your existing infrastructure. This is specially useful in organizations where there are many internal services with their own API, and you want to surface those for easy operation via Slack. You can, of course, also implement your commands directly on the bot itself.

Finagle' `Service` architecture allows for easily extending services, so you can, for instance, integrate any command processor with your own authentication mechanisms,  

So instead of modifying each service to "support Slack" or deploying a dozen different bots, you build _one bot_, wire it to all your services, and configure it _only once_.


## Example uses

- Poll the internal invoices API every 10 minutes and notify the #finances channel every time there's a new invoice
- Publish monitoring alerts to a channel and offer the option to "snooze" as a big red button
- Ping an internal machine (inside the VPS) with a `/ping` command you define
- Post a message from an existing system into a channel or as a DM with a simple http POST
- Listen to multiple channels for a specific keyword and reploying


## How

This bot framework is built using [Finagle](https://github.com/twitter/finagle) - Twitter's excellent Open Source stack (and used extensively inside Twitter as well).

- Import this library into your bot project

- Make your main class extend `hervalicious.slackrelay.SlackBot`

- Implementing your commands

- Configure your Slack bot

https://api.slack.com/apps?new_app=1

- Slash Commands

- Webhooks

- Interactive Components

