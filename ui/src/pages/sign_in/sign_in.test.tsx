import "@testing-library/jest-dom";
import { screen } from "@testing-library/react";
import TestUtils from "../../test_utils";
import SignIn from ".";
import { vi } from "vitest";
vi.mock("react-router-dom", async (importOriginal) => {
  const actual: Record<string, unknown> = await importOriginal();
  return {
    ...actual,
    useNavigate: () => vi.fn(),
  };
});

describe("Sign In Unit Tests", () => {
  test("renders sign_in page", () => {
    TestUtils.render(<SignIn />);

    expect(screen.getByTestId("outer-box")).toBeInTheDocument();
    expect(screen.getByTestId("inner-box")).toBeInTheDocument();
    expect(screen.getByTestId("sign-in-with-email")).toBeInTheDocument();
    expect(screen.getByTestId("sign-in-with-password")).toBeInTheDocument();
    expect(screen.getByTestId("form-control-label")).toBeInTheDocument();
    expect(screen.getByTestId("submit-button")).toBeInTheDocument();
    expect(screen.getByTestId("links")).toBeInTheDocument();
    // TODO: see attempt in confirm_email.test.tsx
    // expect(screen.getByTestId('alert-pop-up')).toBeInTheDocument();
  });
});
