# Yoga App !

Yoga is a booking platform for Yoga sessions.

This is the Yoga app front-end.

## Prerequisites

Angular 14.2.0

Node 16.20.2

Npm 8.19.4

## Installation

First install a node version manager like [nvm](https://github.com/nvm-sh/nvm#installing-and-updating).

Execute `nvm install 16.20.2` to install Node and npm.

Execute `npm install -g @angular/14.2.0` to install Angular.

Run `npm install` to install all the dependencies.

## Tests

### Unitary and Integration Tests

Execute `npm run test` to launch unit and integration tests.

Tests report with code coverage is available at `coverage/jest/lcov-report/index.html`.

### E2E Tests

Execute `npm run e2e` to launch the e2e tests.

Select a browser and run ONLY `all.cy.ts` file in order to launch all the tests in one time and get the global code coverage (run another test file will erase the previous code coverage data).

Then execute `npm run e2e:coverage` to calculate the code coverage.

Tests report with code coverage is available at `coverage/lcov-report/index.html`.

# Run

To run the app in dev mode, execute `npm run start`.

In a browser go to `http://localhost:4200` to use the app (start the back-end before !).

# Folder structure

```bash
├── cypress
│   ├── downloads
│   ├── e2e
│   ├── plugins
│   ├── screenshots
│   └── support
└── src
    ├── app
    │   ├── components
    │   │   ├── me
    │   │   └── not-found
    │   ├── features
    │   │   ├── auth
    │   │   │   ├── components
    │   │   │   │   ├── login
    │   │   │   │   └── register
    │   │   │   ├── interfaces
    │   │   │   └── services
    │   │   └── sessions
    │   │       ├── components
    │   │       │   ├── detail
    │   │       │   ├── form
    │   │       │   └── list
    │   │       ├── interfaces
    │   │       └── services
    │   ├── guards
    │   ├── interceptors
    │   ├── interfaces
    │   └── services
    ├── assets
    └── environments

```

# Author

Gilles BERNARD (@tipikae)
