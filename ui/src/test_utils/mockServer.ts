import { setupServer } from "msw/node";
import { requestHandlers } from "./mockHandler";

export const server = setupServer(...requestHandlers);
