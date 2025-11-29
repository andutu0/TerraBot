# TerraBot

## ## Project Overview

This project simulates a small ecosystem where a robot moves around a grid and interacts with air, soil, plants, water, and animals. Everything is driven by JSON inputs pre-parsed by the assignment's authors.

The main concept is: the robot moves, scans things, learns facts, improves the environment, while all the other entities interact with eachother and change environmental attributes.

## Project Structure

\- `src/main/java/fileio` \- Jackson parsing for simulation inputs.  
\- `src/main/java/main` \- entry point that dispatches all simulations sequentially.  
\- `src/main/java/terrabot` \- entities (robots, animals, plants, water, cells), simulation engine, command handlers, and utilities.  

## How the Project Works
### 1. Loading the Simulation

The entry point is in Main.java.
It loads all simulations and processes commands one by one.
If a simulation isn’t started yet, commands give a specific error message.

### 2. Building the Map

The map is created in MapInit.build().

It reads the territory size, then places: air, soil, water, plants and animals.

Each entity is placed in the correct cell using the coordinates from the input.

### 3. Map Structure

The world is stored in a SimMap object .
It’s basically a 2D array of Cell objects, and each cell can contain air, soil, water, a plant, and an animal at the same time. (only one of each)

### 4. Simulation Logic

- everything happens in Simulation.java.

#### Time Handling:

- Every processed command increases the internal time.

- Between timestamps, missing ticks are simulated automatically.

#### Robot Actions:

The robot can move, scan objects, learn facts, print the knowledge base, get it's energy status, recharge, improve the environment and print the map or the environment conditions.

All commands return JSON objects exactly like the checker expects.

#### Weather:

Weather effects apply temporarily and revert after 2 timestamps.

#### Interactions:

The whole ecosystem updates once per timestamp in Interactions.interact(), when all the interactions specified in the assignment are processed, including plant growth, animal movement and feeding, change in soil quality, etc.

## What I Learned

- how to manage the simulation state and keep it stable across many steps and timestamp jumps.

- how to work with many subclasses that interact with each other.

- how to structure a project with many components while keeping it maintainable.