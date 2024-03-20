/* eslint-disable no-console */
// jest-dom adds custom jest matchers for asserting on DOM nodes.
// allows you to do things like:
// expect(element).toHaveTextContent(/react/i)
// learn more: https://github.com/testing-library/jest-dom
import "@testing-library/jest-dom";

import { beforeAll, afterAll, afterEach } from "@jest/globals";
import { QueryCache } from "@tanstack/react-query";
import { server } from "./mockServer";

const queryCache = new QueryCache();
// Establish API mocking before all tests.
beforeAll(() => server.listen());

/*
Reset any request handlers that we may add during the tests,
so they don't affect other tests. Also reset the react query cache */
afterEach(() => {
  server.resetHandlers();
  queryCache.clear();
});

// Clean up after the tests are finished.
afterAll(() => {
  queryCache.clear();
  server.close();
});
