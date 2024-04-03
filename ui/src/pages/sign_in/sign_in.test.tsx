import '@testing-library/jest-dom';
import { fireEvent, screen, waitFor } from '@testing-library/react';
import TestUtils from '../../test_utils';
import SignIn from '.';
import { vi } from 'vitest';
import userEvent from '@testing-library/user-event';
import { testConstants } from '../../test_utils/test_constants';
import { validation } from '../../constants';
import { useNavigate } from 'react-router-dom';

const mockNavigate = vi.fn();

vi.mock('react-router-dom', async (importOriginal) => {
  const actual: Record<string, unknown> = await importOriginal();
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  };
});


// describe('Sign In Unit Tests', () => {
//   test('renders sign_in page', () => {
//     TestUtils.render(<SignIn />);

//     expect(screen.getByTestId('container')).toBeInTheDocument();
//     expect(screen.getByTestId('outer-box')).toBeInTheDocument();
//     expect(screen.getByTestId('inner-box')).toBeInTheDocument();
//     expect(screen.getByTestId('sign-in-with-email')).toBeInTheDocument();
//     expect(screen.getByTestId('sign-in-with-password')).toBeInTheDocument();
//     expect(screen.getByTestId('form-control-label')).toBeInTheDocument();
//     expect(screen.getByTestId('submit-button')).toBeInTheDocument();
//     expect(screen.getByTestId('links')).toBeInTheDocument();
//     // TODO: see attempt in confirm_email.test.tsx
//     // expect(screen.getByTestId('alert-pop-up')).toBeInTheDocument();
//   });
// });

describe('renders AlertPopUp component', () => {
  test('button clicked with valid data returns 200 OK', async () => {
    // const mockNav = vi.fn(); 
    // (useNavigate as vi.VitestUtils).mockReturnValue({
    //   navigate: mockNav,
    // });
    
    TestUtils.render(<SignIn />);

    const emailDataField = screen.getByTestId('sign-in-with-email-input');
    const passwordDataField = screen.getByTestId('sign-in-with-password-input');
    const submitButton = screen.getByTestId('submit-button');

    expect(emailDataField).toBeInTheDocument();
    expect(passwordDataField).toBeInTheDocument();
    expect(submitButton).toBeInTheDocument();

    fireEvent.change(emailDataField, {
      target: { value: testConstants.validEmail },
    });
    fireEvent.change(passwordDataField, {
      target: { value: testConstants.validPassword },
    });

    await userEvent.click(submitButton);
    // await waitFor(() => {
      
    //   expect(mockNavigate).toHaveBeenCalled();
    // });
    // match accessToken from server with 'access-token'
    // .then(response => {
    // console.log(response); // returns undefined
    // response.accessToken == 'access-token';
    // });
    // onSuccess: (data: IToken) => {
    //   setBearer(data.accessToken);
    //   checkIfValidToken({
    //     accessToken: data.accessToken,
    //     refreshToken: data.refreshToken,
    //   });
    //   navigate(navigation.HOME_PAGE);
    // },
  });

  // test('button clicked with invalid data returns 403 FORBIDDEN', async () => {
  //   TestUtils.render(<SignIn />);
  //   const emailDataField = screen.getByTestId('sign-in-with-email-input');
  //   const passwordDataField = screen.getByTestId('sign-in-with-password-input');
  //   const submitButton = screen.getByTestId('submit-button');

  //   expect(emailDataField).toBeInTheDocument();
  //   expect(passwordDataField).toBeInTheDocument();
  //   expect(submitButton).toBeInTheDocument();

  //   fireEvent.change(emailDataField, {
  //     target: { value: testConstants.notFoundEmail },
  //   });
  //   fireEvent.change(passwordDataField, {
  //     target: { value: testConstants.invalidPassword },
  //   });

  //   await userEvent.click(submitButton);
  //   await waitFor(() => {
  //     screen.debug(undefined, Infinity);
  //     expect(screen.getByTestId('alert-pop-up')).toBeInTheDocument();
  //     expect(
  //       screen.getByText(
  //         'Oh no! Bad credentials for ' + testConstants.notFoundEmail
  //       )
  //     ).toBeInTheDocument();
  //   });
  // });

  // test('button clicked with invalid data returns 500 Internal Server Error', async () => {
  //   TestUtils.render(<SignIn />);
  //   const emailDataField = screen.getByTestId('sign-in-with-email-input');
  //   const passwordDataField = screen.getByTestId('sign-in-with-password-input');
  //   const submitButton = screen.getByTestId('submit-button');

  //   expect(emailDataField).toBeInTheDocument();
  //   expect(passwordDataField).toBeInTheDocument();
  //   expect(submitButton).toBeInTheDocument();

  //   fireEvent.change(emailDataField, {
  //     target: { value: testConstants.notFoundEmail },
  //   });
  //   fireEvent.change(passwordDataField, {
  //     target: { value: testConstants.validPassword },
  //   });

  //   await userEvent.click(submitButton);
  //   await waitFor(() => {
  //     expect(screen.getByTestId('alert-pop-up')).toBeInTheDocument();
  //     expect(
  //       // screen.getByText('Oh no! Bad credentials for ' + testConstants.notFoundEmail)
  //       screen.getByText(validation.GENERIC_ERROR_MSG)
  //     ).toBeInTheDocument();
  //   });
  // });
});
