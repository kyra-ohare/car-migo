import * as Yup from 'yup';
import { validation } from '../../constants';

// const minAge = dayjs().subtract(appConstants.minAge, 'year'); // TODO

export const signUpValidationSchema = Yup.object().shape({
  firstName: Yup.string().required('First name must not be empty.'),
  lastName: Yup.string().required('Last name must not be empty.'),
  // dob: Yup.date().min(minAge).required('Minimum age is ' + minAge), // TODO
  phoneNumber: Yup.string().required('Phone number must not be empty.'),
  email: Yup.string()
    .email(validation.INVALID_EMAIL)
    .min(validation.EMAIL_MIN_SIZE)
    .max(validation.EMAIL_MAX_SIZE)
    .required(validation.NO_EMPTY_EMAIL),
  password: Yup.string()
    .matches(validation.PASSWORD_RULE, validation.VALID_PASSWORD_MESSAGE)
    .required(validation.NO_EMPTY_PASSWORD),
  confirmPassword: Yup.string()
    .oneOf([Yup.ref('password')], 'Passwords must match.')
    .required('Confirm your password.'),
});
