<p align="center">
  <a href="https://wodo.io/" target="blank"><img src="https://github.com/wodo-platform/wodo-branding/blob/main/src/img/branding/logo_purple.png" width="320" alt="Wodo Platform" /></a>
</p>

<div align="center">
<h2> Wodo Platform Blockchain BSC Engine </h2>
</div>

<div align="center">
  <h4>
    <a href="https://wodo.io/">
      Website
    </a>
    <span> | </span>
    <a href="#">
      Product Docs
    </a>
    <span> | </span>
    <a href="#">
      Architecture Docs
    </a>
    <span> | </span>
    <!-- <a href="#"> -->
    <!--   CLI -->
    <!-- </a> -->
    <!-- <span> | </span> -->
    <a href="#/CONTRIBUTING.md">
      Contributing
    </a>
    <span> | </span>
    <a href="https://twitter.com/wodoio">
      Twitter
    </a>
    <span> | </span>
    <a href="https://t.me/wodoio">
      Telegram
    </a>
    <span> | </span>
    <a href="https://discord.gg/fbyns8Egpb">
      Discourd
    </a>
    <span> | </span>
    <a href="https://wodoio.medium.com/">
      Medium
    </a>
    <span> | </span>
    <a href="https://www.reddit.com/r/wodoio">
      Reddit
    </a>
  </h4>
</div>


<h3> Table of Contents </h3> 

- [About](#about)
- [Installation](#installation)
- [Create a Local PubSub+ Software Message Broker](#create-a-local-pubsub-software-message-broker)
- [Running the app](#running-the-app)
- [Building docker image](#building-docker-image)
- [CI and Github Workflows](#ci-and-github-workflows)
- [Next Steps](#next-steps)

----

## About

This is the service implementing platform's blockchain API for Binance Smart Chain network. It offers wide arrange or functionalities to manage blockain transactions on Nano network. The implementation adhere's to fundamental aspects of building a service - scalability, security and simplicity. 

## Installation

All dependency management and project configurations are done within Maven.

## Create a Local PubSub+ Software Message Broker

Run the following command to create a PubSub+ software message broker using the docker compose template:

```bash
docker-compose -f docker-compose-solace-single-node.yml up -d
```
The Compose template runs a message broker named `pubSubStandardSingleNode`, using the `latest` PubSub+ Standard image pulled from Docker Hub, creates an `admin` user with global access permissions, and publishes the following message broker container ports to the same ports on the host:

* port 8008 -- Web transport
* port 1883 -- MQTT Default VPN
* port 5672 -- AMQP Default VPN
* port 8000 -- MQTT Default VPN over WebSockets
* port 8080 -- SEMP / PubSub+ Manager
* port 9000 -- REST Default VPN
* port 55555 -- SMF
* port 2222 -- SSH connection to the Solace CLI

For more information about the default ports used for each service, refer to [Software Message Broker Configuration Defaults](https://docs.solace.com/Configuring-and-Managing/SW-Broker-Specific-Config/SW-Broker-Configuration-Defaults.htm).
Once the container is created, it will take about 60 seconds for the message broker to finish activating.

You can access the Solace management tool, [PubSub+ Manager](https://docs.solace.com/Solace-PubSub-Manager/PubSub-Manager-Overview.htm), or the [Solace CLI](https://docs.solace.com/Solace-CLI/Using-Solace-CLI.htm) to start issuing configuration or monitoring commands on the message broker.

Solace PubSub+ Manager management access:
1. Open a browser and enter this url: _http://localhost:8080_.
2. Log in as user `admin` with default password `admin`.

Solace CLI management access:
1. Enter the following `docker exec` command:
```
docker exec -it pubSubStandardSingleNode /usr/sw/loads/currentload/bin/cli -A
```
2. Enter the following commands to enter configuration mode:
```
solace> enable
solace# config
solace(configure)#
```
3. Issue configuration or monitoring commands. For a list of commands currently supported on the message broker, refer to [Software Message Broker CLI Commands](https://docs.solace.com/Solace-CLI/CLI-Reference/VMR_CLI_Commands.html).

You now have a message broker Docker container with a basic configuration that is ready for messaging tasks. When you are feeling comfortable with your message broker, you can test messaging using the Solace SDKPerf application. You can download SDKPerf from the dev.solace.com Downloads page.

## Running the app

```bash
# development
$ mvnw spring-boot:run -Dspring.profiles.active=dev

# production mode
$ mvnw spring-boot:run -Dspring.profiles.active=prod
```

## Building docker image

Along with build and run functionality on your command line, we need to build docker images as well. It means we need to build your project from scratch while preparing docker images.

In your repo root folder, run the following command with your own git token. It will build docker image and add it to your configured docker registery on your laptop

```bash
$ docker build -t wp-blockchain-bsc-engine --build-arg NPM_TOKEN=your_token . 
```

To run the nodejs app on your local laptop, you can run the wollowinf command

```bash
$ docker run -dp 8080:3000 wp-blockchain-bsc-engine
```

Open the url "http://localhost:8080/api/demos" and "http://localhost:8080/docs" in your browser to see API and swagger doc.


## CI and Github Workflows

In order to build and package your repo through CI/CD, please have a a look at the file .github/workflows/pipeline.yml under the root project folder. It is preconfigured githubflow. Whenever you push a change onto the main branch, it is triggered. It will be improved to be able to package and release artifacts based on a release process later.

## Next Steps

Once you compose your new repo, you can create helm charts in wodo-helm-charts repo then conitinue with local deployment and official CI/CD gitops deployment. Please refer to Wodo Platform Local Dev Environment guide. 
