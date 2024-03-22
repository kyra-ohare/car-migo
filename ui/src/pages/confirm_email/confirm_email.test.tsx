import "@testing-library/jest-dom";
// import userEvent from '@testing-library/user-event'
import { screen } from "@testing-library/react";
import TestUtils from "../../test_utils";
import ConfirmEmail from ".";
import { vi } from "vitest";
vi.mock("react-router-dom", async (importOriginal) => {
  const actual: Record<string, unknown> = await importOriginal();
  return {
    ...actual,
    useNavigate: () => vi.fn(),
  };
});
describe("Confirm Email Unit Tests", () => {
  test("renders confirm_email page", () => {
    TestUtils.render(<ConfirmEmail />);

    expect(screen.getByTestId("box")).toBeInTheDocument();
    expect(screen.getByTestId("welcome-message")).toBeInTheDocument();
    expect(screen.getByTestId("catchy-message")).toBeInTheDocument();
    // screen.debug(undefined, Infinity);
    expect(screen.getByTestId("confirm-email-address")).toBeInTheDocument();
    expect(screen.getByTestId("submit-button")).toBeInTheDocument();
  });

  // TODO:
  test("renders AlertPopUp when user clicks on the button", async () => {
    // const user = userEvent.setup()
    // await user.click(screen.getByRole('button', {name: /click me!/i}))
    // expect(screen.getByTestId('alert-pop-up')).toBeInTheDocument();
  });
});
